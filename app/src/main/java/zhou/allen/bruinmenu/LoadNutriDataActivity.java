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
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.TimeUnit;

//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

public class LoadNutriDataActivity extends Activity
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;
    String url;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Initialize a LoadViewTask object and call the execute() method
        Intent intent = getIntent();
        url = intent.getStringExtra("nutriURL");
        new LoadTask().execute();


    }

    //To use the AsyncTask, it must be subclassed
    private class LoadTask extends AsyncTask<Void, Integer, Void> {
        String html;
        //boolean refresh = false;
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoadNutriDataActivity.this, "Connecting...",
                    "Loading, please wait...", false, false);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            //Get the current thread's token
            synchronized (this) {
                try {

                    OkHttpClient client = new OkHttpClient();

                    //String url = "http://menu.ha.ucla.edu/foodpro/default.asp";
                    client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
                    client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    //Response response = client.newCall(request).execute();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    html = response.body().string();

                    response.body().close();

                    Document doc = Jsoup.parse(html);
                    doc.select(".rddisclaimer").remove();
                    doc.select(".rdimg").remove();
                    doc.select(".rdprintlink").remove();

                    html = doc.toString();

                } catch (Exception e) {
                    Toast.makeText(LoadNutriDataActivity.this, "We couldn't access the nutritional data.", Toast.LENGTH_SHORT).show();
                    finish();
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
            i = new Intent (LoadNutriDataActivity.this, NutriDataWebView.class);
            i.putExtra("html", html);
            //i.putExtra("html",html);
            startActivity(i);
            //setContentView(R.layout.activity_main);
            LoadNutriDataActivity.this.finish();
        }
    }
}