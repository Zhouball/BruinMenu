package zhou.allen.bruinmenu;

//import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
//import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

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

/**
 * Created by Owner on 10/20/2015.
 */
public class UpdateDBService extends Service {

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        new UpdateDB().execute();
        stopSelf();
    }

    public int onStartCommand(Intent intent, int flags, int startID)
    {
        //super.onCreate(savedInstanceState);

        //new UpdateDB().execute();


        return 1;
    }

    private class UpdateDB extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://menu.ha.ucla.edu/foodpro/default.asp";
                    client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
                    client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    //Response response = client.newCall(request).execute();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    String html = response.body().string();

                    response.body().close();


                    //((AppVariables) getApplicationContext()).setBruinMenu(html);
                    //System.out.print("I got the page!");

                    Context _context = getApplicationContext();
                    MenuDBHelper dbHelper = new MenuDBHelper(_context);

                    ArrayList<String> favoriteFoodPresent = new ArrayList<>();
                    ArrayList<String> favoriteFood = (ArrayList<String>) dbHelper.getFavorites();
                    // Get the database. If it does not exist, this is where it will
                    // also be created.
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.delete(MenuDBContract.HallEntry.TABLE_NAME, null, null);
                    db.delete(MenuDBContract.KitchenEntry.TABLE_NAME, null, null);
                    db.delete(MenuDBContract.MenuEntry.TABLE_NAME, null, null);

                    Document doc = Jsoup.parse(html);
                    Elements menus = doc.getElementsByClass("menucontent");


                    for (int i = 0; i < menus.size(); i++) {
                        String mealTime;
                        Elements timeMenu = menus.get(i).select(".menumealheader");
                        if (timeMenu.first().text().toLowerCase().contains("breakfast"))
                            mealTime = "breakfast";
                        else if (timeMenu.first().text().toLowerCase().contains("lunch"))
                            mealTime = "lunch";
                        else
                            mealTime = "dinner";

                        Elements halls = menus.get(i).select(".menulocheader");
                        Elements leftCells = menus.get(i).select(".menugridcell, .menusplit");
                        Elements rightCells = menus.get(i).select(".menugridcell_last, .menusplit");

                        ArrayList<Long> hallsIds = new ArrayList<>();
                        for (Element hall : halls) {
                            ContentValues values = new ContentValues();
                            values.put(MenuDBContract.HallEntry.COLUMN_NAME_ITEM, hall.text().trim());
                            values.put(MenuDBContract.HallEntry.COLUMN_NAME_MEALTIME, mealTime);
                            hallsIds.add(db.insert(
                                    MenuDBContract.HallEntry.TABLE_NAME,
                                    null,
                                    values));
                        }

                        int ite = 0;
                        for (Element cell : leftCells) {
                            if (cell.hasClass("menusplit")) {
                                ite += 2;
                            }
                            else {
                                Elements kitchen = cell.select(".category5");
                                ContentValues kvalues = new ContentValues();
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_ITEM, kitchen.first().text().trim());
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_HALL, hallsIds.get(ite));
                                long id = db.insert(
                                        MenuDBContract.KitchenEntry.TABLE_NAME,
                                        null,
                                        kvalues);

                                Elements items = cell.select(".level5");
                                for (Element e : items) {
                                    ContentValues ivalues = new ContentValues();
                                    Element link = e.select("a").first();
                                    String menuItemName = e.text().trim();
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, menuItemName);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_KITCHEN, id);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_NUTRIURL, link.attr("href"));
                                    if(favoriteFood.contains(menuItemName)) favoriteFoodPresent.add(id + "-" + menuItemName);

                                    Element v = e.select("img").first();
                                    int veg = 0;
                                    if (v == null) {
                                        veg = 0;
                                    } else if (v.attr("alt").toLowerCase().contains("vegetarian")) {
                                        veg = 1;
                                    } else if (v.attr("alt").toLowerCase().contains("vegan")) {
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

                        ite = 1;
                        for (Element cell : rightCells) {
                            if (cell.hasClass("menusplit")) {
                                ite += 2;
                            }
                            else {
                                Elements kitchen = cell.select(".category5");
                                ContentValues kvalues = new ContentValues();
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_ITEM, kitchen.first().text().trim());
                                kvalues.put(MenuDBContract.KitchenEntry.COLUMN_NAME_HALL, hallsIds.get(ite));
                                long id = db.insert(
                                        MenuDBContract.KitchenEntry.TABLE_NAME,
                                        null,
                                        kvalues);

                                Elements items = cell.select(".level5");
                                for (Element e : items) {
                                    ContentValues ivalues = new ContentValues();
                                    Element link = e.select("a").first();
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, e.text().trim());
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_KITCHEN, id);
                                    ivalues.put(MenuDBContract.MenuEntry.COLUMN_NAME_NUTRIURL, link.attr("href"));
                                    Element v = e.select("img").first();
                                    int veg = 0;
                                    if (v == null) {
                                        veg = 0;
                                    } else if (v.attr("alt").toLowerCase().contains("vegetarian")) {
                                        veg = 1;
                                    } else if (v.attr("alt").toLowerCase().contains("vegan")) {
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

                    //displaying notification
                    if(!favoriteFoodPresent.isEmpty()) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context).
                                setSmallIcon(R.drawable.vegetarian).
                                setContentTitle("Today's Favorites").
                                setContentText(favoriteFoodPresent.get(0) + "....");
                        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                        inboxStyle.setBigContentTitle("Today's Favorites");
                        for(String foods : favoriteFoodPresent) {
                            inboxStyle.addLine(foods);
                        }
                        builder.setStyle(inboxStyle);
                        builder.setContentIntent(PendingIntent.getActivity(_context, 0, new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
                        builder.setAutoCancel(true);
                        NotificationManager notifManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

                        int notificationID = 1;
                        notifManager.notify(notificationID, builder.build());
                    }

                } catch (Exception e) {
                    //Do nothing if service fails.
                    //return "Unable to retrieve web page. http://menu.ha.ucla.edu/foodpro/default.asp may be down.";
                }

            }
            return null;
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
