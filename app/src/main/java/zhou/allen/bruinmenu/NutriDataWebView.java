package zhou.allen.bruinmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class NutriDataWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_data_web_view);

        Intent intent = getIntent();
        String nutriURL = intent.getStringExtra("nutriURL");

        WebView webview = new WebView(this);
        setContentView(webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);

        webview.loadUrl(nutriURL);
    }
}
