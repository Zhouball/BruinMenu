package zhou.allen.bruinmenu;

//import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
//import android.os.Bundle;
import android.os.IBinder;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import java.io.IOException;
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
                    int fine = 2;
                    String html = response.body().string();

                    response.body().close();


                    //((AppVariables) getApplicationContext()).setBruinMenu(html);
                    //System.out.print("I got the page!");

                    MenuDBHelper dbHelper = new MenuDBHelper(getApplicationContext());
                    // Get the database. If it does not exist, this is where it will
                    // also be created.
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.delete(MenuDBContract.MenuEntry.TABLE_NAME, null, null);

                    Document doc = Jsoup.parse(html);
                    Elements menus = doc.getElementsByClass("menucontent");

                    boolean topbottom; //top is true
                    for (int i = 0; i < menus.size(); i++) {
                        topbottom = true;
                        String mealTime;

                        Elements cells = menus.get(i).select(".menugridcell, .menusplit");

                        Elements locs = menus.get(i).select(".menulocheader");
                        ArrayList<String> locsS = new ArrayList<String>();
                        for (Element e :locs) {
                            locsS.add(e.text().trim().toLowerCase());
                        }

                        Elements timeMenu = menus.get(i).select(".menumealheader");
                        if (timeMenu.get(0).text().toLowerCase().contains("breakfast"))
                            mealTime = "breakfast";
                        else if (timeMenu.get(0).text().toLowerCase().contains("lunch"))
                            mealTime = "lunch";
                        else
                            mealTime = "dinner";

                        int temp = 0;

                        for (Element e : cells) {

                            if (e.hasClass("menusplit")) {
                                temp += 2;
                            }
                            else {
                                ContentValues values = new ContentValues();
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, listText(e));
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_MEALTIME, mealTime);
                                if (locsS.get(temp).contains("covel"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "covel");
                                else if (locsS.get(temp).contains("de neve"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "deNeve");
                                else if (locsS.get(temp).contains("rieber"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "feast");
                                else
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "bPlate");
                                long newRowId;
                                newRowId = db.insert(
                                        MenuDBContract.MenuEntry.TABLE_NAME,
                                        null,
                                        values);
                            }
                        }

                        Elements cells2 = menus.get(i).select(".menugridcell_last, .menusplit");
                        temp = 1;

                        for (Element e : cells2) {
                            if (e.hasClass("menusplit")) {
                                temp += 2;
                            } else {
                                ContentValues values = new ContentValues();
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, listText(e));
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_MEALTIME, mealTime);
                                if (locsS.get(temp).contains("covel"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "covel");
                                else if (locsS.get(temp).contains("de neve"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "deNeve");
                                else if (locsS.get(temp).contains("rieber"))
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "feast");
                                else
                                    values.put(MenuDBContract.MenuEntry.COLUMN_NAME_LOC, "bPlate");
                                long newRowId;
                                newRowId = db.insert(
                                        MenuDBContract.MenuEntry.TABLE_NAME,
                                        null,
                                        values);
                            }
                        }
                    }

                    // android.os.Debug.waitForDebugger();
                    dbHelper.close();
                    db.close();

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
