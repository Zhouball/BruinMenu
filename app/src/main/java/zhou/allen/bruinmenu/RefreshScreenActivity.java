package zhou.allen.bruinmenu;

/**
 * Created by Owner on 10/18/2015.
 */

//import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
//import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RefreshScreenActivity extends Activity
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Initialize a LoadViewTask object and call the execute() method
        new GetPageTask().execute();


    }

    //To use the AsyncTask, it must be subclassed
    private class GetPageTask extends AsyncTask<Void, Integer, Void> {
        //String html;
        boolean refresh;
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RefreshScreenActivity.this, "Connecting...",
                    "Connecting to Bruin Menu, please wait...", false, false);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            //Get the current thread's token
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
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, UpdateDBService.listText(e));
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
                                values.put(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM, UpdateDBService.listText(e));
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
                    progressDialog = ProgressDialog.show(RefreshScreenActivity.this, "Connection Failed",
                            "Unable to connect to BruinMenu", false, false);
                    try {
                        Thread.sleep(1000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return null;
        }


        /*
        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }
        */

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {
            //close the progress dialog
            progressDialog.dismiss();
            //initialize the View

            //String html = ((AppVariables) getApplicationContext()).getBruinMenu();
            Intent i = new Intent(RefreshScreenActivity.this, MainActivity.class);

            //i.putExtra("html",html);
            startActivity(i);
            //setContentView(R.layout.activity_main);
        }
    }
}