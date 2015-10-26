package zhou.allen.bruinmenu;

/**
 * Created by Owner on 10/18/2015.
 */

//import java.io.IOException;
//import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;

//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

public class LoadingScreenActivity extends Activity
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Initialize a LoadViewTask object and call the execute() method
        new LoadTask().execute();


    }

    //To use the AsyncTask, it must be subclassed
    private class LoadTask extends AsyncTask<Void, Integer, Void> {
        //String html;
        boolean refresh;
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoadingScreenActivity.this, "Connecting...",
                    "Loading, please wait...", false, false);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            //Get the current thread's token
            synchronized (this) {
                try {


                    boolean alarmUp = (PendingIntent.getBroadcast(LoadingScreenActivity.this, 0,
                            new Intent(getApplicationContext(), UpdateDBService.class),
                            PendingIntent.FLAG_NO_CREATE) != null);

                    if (!alarmUp) {
                        Intent i = new Intent(getApplicationContext(), UpdateDBService.class);
                        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                i, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2 * AlarmManager.INTERVAL_HOUR, 1000 * 60, pi);
                        //startService(i);
                        refresh = true;

                        /*
                        try {
                            Thread.sleep(3000); //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        */
                    }



                } catch (Exception e) {
                    //return "Unable to retrieve web page. http://menu.ha.ucla.edu/foodpro/default.asp may be down.";
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
            Intent i;
            if (refresh = false) {
                i = new Intent(LoadingScreenActivity.this, MainActivity.class);
            }
            else {
                i = new Intent (LoadingScreenActivity.this, RefreshScreenActivity.class);
            }
            //i.putExtra("html",html);
            startActivity(i);
            //setContentView(R.layout.activity_main);
        }
    }
}