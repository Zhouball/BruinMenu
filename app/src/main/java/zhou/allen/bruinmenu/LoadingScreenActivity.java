package zhou.allen.bruinmenu;

/**
 * Created by Owner on 10/18/2015.
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class LoadingScreenActivity extends Activity
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;
    private LoadingScreenActivity thisActivity = this;

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
        String html;
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoadingScreenActivity.this, "Connecting...",
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

                    Response response = client.newCall(request).execute();
                    html = response.body().string();
                    //((AppVariables) getApplicationContext()).setBruinMenu(html);
                    //System.out.print("I got the page!");
                } catch (IOException e) {
                    //TODO fails to connect
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
            Intent i = new Intent(thisActivity, MainActivity.class);
            i.putExtra("html",html);
            startActivity(i);
            setContentView(R.layout.activity_main);
        }
    }
}