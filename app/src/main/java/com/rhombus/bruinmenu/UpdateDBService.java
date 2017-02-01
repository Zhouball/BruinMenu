package com.rhombus.bruinmenu;

//import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Owner on 10/20/2015.
 */
public class UpdateDBService extends Service {

    private ArrayList<String> favoriteFoodPresent;
    private ArrayList<String> favoriteFood;

    public IBinder onBind(Intent arg0) {
        return null;
    }
    private static final String TAG = "UpdateDbService";
    private Handler mHandler;

    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Service is running");
        new UpdateDB().execute();

        /*mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Service is running!" + System.currentTimeMillis(), Toast.LENGTH_LONG).show();
            }
        });*/

        stopSelf();
    }

    public int onStartCommand(Intent intent, int flags, int startID)
    {
        //super.onCreate(savedInstanceState);
        return 1;
    }

    private class UpdateDB extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://menu.dining.ucla.edu/Menus";
                    client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
                    client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    //Response response = client.newCall(request).execute();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    if(!response.isSuccessful()) {
                        //TODO: Notify that refresh failed.
                        return null;
                    }

                    String html = response.body().string();

                    response.body().close();


                    //((AppVariables) getApplicationContext()).setBruinMenu(html);
                    //System.out.print("I got the page!");

                    MenuDBHelper dbHelper = new MenuDBHelper(getApplicationContext());

                    favoriteFoodPresent = new ArrayList<>();
                    favoriteFood = (ArrayList<String>) dbHelper.getFavorites();
                    // Get the database. If it does not exist, this is where it will
                    // also be created.
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.delete(MenuDBContract.HallEntry.TABLE_NAME, null, null);
                    db.delete(MenuDBContract.KitchenEntry.TABLE_NAME, null, null);
                    db.delete(MenuDBContract.MenuEntry.TABLE_NAME, null, null);

                    Document doc = Jsoup.parse(html);
                    Elements menus = doc.getElementById("main-content").children();

                    String mealTime = "";
                    for (int i = 0; i < menus.size(); i++) {
                        Element menu = menus.get(i);
                        if ("page-header".equals(menu.id())) {
                            String mealTimeString = menu.text().toLowerCase();
                            mealTime = mealTimeString.contains("breakfast") ? "breakfast" :
                                    mealTimeString.contains("lunch") ? "lunch" :
                                            mealTimeString.contains("brunch") ? "lunch" :
                                            mealTimeString.contains("dinner") ? "dinner" : "";
                        } else if (menu.hasClass("menu-block")) {
                            // Get dining hall name
                            Element hcell = menu.getElementsByClass("col-header").size() > 0 ? menu.getElementsByClass("col-header").get(0) : null;
                            ContentValues hvalues = new ContentValues();
                            hvalues.put(MenuDBContract.HallEntry.COLUMN_NAME_ITEM, hcell.text().trim());
                            hvalues.put(MenuDBContract.HallEntry.COLUMN_NAME_MEALTIME, mealTime);
                            long hallId = db.insert(
                                    MenuDBContract.HallEntry.TABLE_NAME,
                                    null,
                                    hvalues);

                            // Get kitchen information
                            Elements kcells = menu.getElementsByClass("sect-item");
                            for (Element kcell : kcells) {
                                if (kcell == null) continue;
                                ContentValues kvalues = new ContentValues();

                                String kitchenName = kcell.child(0).previousSibling().toString().trim();
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_ITEM, kitchenName);
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_HALL, hallId);
                                long id = db.insert(
                                        MenuDBContract.KitchenEntry.TABLE_NAME,
                                        null,
                                        kvalues);

                                Elements items = kcell.getElementsByClass("menu-item");
                                for (Element item : items) {
                                    if (item == null) continue;
                                    ContentValues ivalues = new ContentValues();
                                    String link = item.select("a").first().attr("href");

                                    String menuItemName = item.select("a").first().text().trim();
                                    //Log.v("menuItem", menuItemName + ": " + link);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, menuItemName);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_KITCHEN, id);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_NUTRIURL, link);
                                    if (favoriteFood.contains(menuItemName)) {
                                        favoriteFoodPresent.add(kitchenName + "-" + menuItemName);
                                    }

                                    Element v = item.select("img").first();
                                    int veg = 0;
                                    if (v == null) {
                                        veg = 0;
                                    } else if ("v".equals(v.attr("alt").toLowerCase())) {
                                        veg = 1;
                                    } else if ("vg".equals(v.attr("alt").toLowerCase())) {
                                        veg = 2;
                                    }
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_VEG, veg);
                                    long id2 = db.insert(
                                            MenuDBContract.MenuEntry.TABLE_NAME,
                                            null,
                                            ivalues);
                                }
                            }
                        }
                    }
                    dbHelper.close();
                    db.close();

                } catch (Exception e) {
                    //Do nothing if service fails.
                    //return "Unable to retrieve web page. http://menu.ha.ucla.edu/foodpro/default.asp may be down.";
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //checking if notifications should display
            Context _context = getApplicationContext();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
            boolean notification_switch = prefs.getBoolean("notification_switch", true);
            if(notification_switch) {
                //displaying notification
                if (favoriteFoodPresent != null && !favoriteFoodPresent.isEmpty()) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(_context).
                            setSmallIcon(R.drawable.notification).
                            setLargeIcon(BitmapFactory.decodeResource(_context.getResources(), R.drawable.notification)).
                            setContentTitle("Today's Favorites").
                            setContentText(favoriteFoodPresent.get(0) + (favoriteFoodPresent.size() == 1 ? "" : "...."));
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.setBigContentTitle("Today's Favorites");
                    for (String foods : favoriteFoodPresent) {
                        inboxStyle.addLine(foods);
                    }
                    builder.setStyle(inboxStyle);
                    builder.setContentIntent(PendingIntent.getActivity(_context, 0, new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
                    builder.setAutoCancel(true);
                    NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    int notificationID = 1;
                    notifManager.notify(notificationID, builder.build());
                }
            }
        }
    }

    public static String listText(Element e) {
        Elements listItems = e.select("li");
        StringBuffer s = new StringBuffer(listItems.get(0).text().trim() + ":\n");
        for (int i = 1; i < listItems.size() - 1; i++) {
            if (!listItems.get(i).text().isEmpty())
                s.append(listItems.get(i).text().trim() + "\n");
        }
        if (listItems.size() > 1) {
            s.append(listItems.get(listItems.size() - 1).text().trim());
        }
        return s.toString();
    }
}
