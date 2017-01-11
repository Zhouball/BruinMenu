package com.rhombus.bruinmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

public class QuickDiningMenusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_dining_menus);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //setContentView(R.layout.activity_main);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
