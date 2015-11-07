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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

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
        boolean refresh = false;
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
                    Intent i = new Intent("zhou.allen.bruinmenu.UPDATEDB");
                    boolean alarmUp = (PendingIntent.getBroadcast(LoadingScreenActivity.this, 0, i, PendingIntent.FLAG_NO_CREATE) != null);

                    if (!alarmUp) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        long updateStartHour = prefs.getLong("update_start_hour", 6);
                        long updateStartMinute = prefs.getLong("update_start_minute", 0);
                        int INTERVAL_HOUR = 3600000;
                        int INTERVAL_MINUTE = 60000;

                        PendingIntent pi = PendingIntent.getBroadcast(LoadingScreenActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) LoadingScreenActivity.this.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, updateStartHour*INTERVAL_HOUR + updateStartMinute*INTERVAL_MINUTE, AlarmManager.INTERVAL_DAY, pi);
                        refresh = true;
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
            if (refresh == false) {
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