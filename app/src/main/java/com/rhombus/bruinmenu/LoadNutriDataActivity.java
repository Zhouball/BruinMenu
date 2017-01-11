package com.rhombus.bruinmenu;

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
import android.webkit.WebView;
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

public class LoadNutriDataActivity extends Activity {
    //A ProgressDialog object
    private ProgressDialog progressDialog;
    String url;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nutri_data_web_view);
        WebView view = (WebView) this.findViewById(R.id.nutri_web_view);
        Intent intent = getIntent();
        url = intent.getStringExtra("nutriURL");
        view.loadUrl(url);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setLoadWithOverviewMode(true);

    }
}