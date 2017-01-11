package com.rhombus.bruinmenu;

import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    int stage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        final Button easterButton = (Button)findViewById(R.id.easterButton);
        final ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
        final ImageButton rightButton = (ImageButton)findViewById(R.id.rightButton);
        final ImageButton upButton = (ImageButton)findViewById(R.id.upButton);
        final ImageButton downButton = (ImageButton)findViewById(R.id.downButton);
        final ImageButton aButton = (ImageButton)findViewById(R.id.aButton);
        final ImageButton bButton = (ImageButton)findViewById(R.id.bButton);
        final TextView credits = (TextView)findViewById(R.id.credits);

        easterButton.setVisibility(View.VISIBLE);
        easterButton.setBackgroundColor(Color.TRANSPARENT);
        easterButton.setTextColor(Color.TRANSPARENT);

        easterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                upButton.setVisibility(View.VISIBLE);
                downButton.setVisibility(View.VISIBLE);
                aButton.setVisibility(View.VISIBLE);
                bButton.setVisibility(View.VISIBLE);
                easterButton.setVisibility(View.INVISIBLE);
            }
        });

        leftButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);
        upButton.setVisibility(View.INVISIBLE);
        downButton.setVisibility(View.INVISIBLE);
        aButton.setVisibility(View.INVISIBLE);
        bButton.setVisibility(View.INVISIBLE);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 5:
                    case 7:
                        stage++; break;
                    default:
                        stage = 1;
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 6:
                    case 8:
                        stage++; break;
                    default:
                        stage = 1;
                }
            }
        });
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 1:
                    case 2:
                        stage++; break;
                    default:
                        stage = 1;
                }
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 3:
                    case 4:
                        stage++; break;
                    default:
                        stage = 1;
                }
            }
        });
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 10:
                        leftButton.setVisibility(View.INVISIBLE);
                        rightButton.setVisibility(View.INVISIBLE);
                        upButton.setVisibility(View.INVISIBLE);
                        downButton.setVisibility(View.INVISIBLE);
                        aButton.setVisibility(View.INVISIBLE);
                        bButton.setVisibility(View.INVISIBLE);
                        credits.setText(getResources().getString(R.string.credits) + "And many thanks \n \t   to Jaron Mink, for beta testing \n \t   to Wanda He, for design ideas \n \t   to Danial Hosseinian, for being absolutely no help at all \n \t   to Michael Bareian, for something or another... \n \t   and to all our friends and family for their support!");
                    default:
                        stage = 1;
                }
            }
        });
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stage) {
                    case 9:
                        stage++; break;
                    default:
                        stage = 1;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //setContentView(R.layout.activity_main);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
