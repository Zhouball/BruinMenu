package zhou.allen.bruinmenu;

import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ExpandableListView;
//import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import info.androidhive.expandablelistview.ExpandableListAdapter;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    //GestureDetector
    private GestureDetectorCompat mDetector;

    //TextView mainTextView;
    ExpandableListView menu;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String html;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScreenSize();
        mDetector = new GestureDetectorCompat(this,this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            html = extras.getString("html");
        }

        //

        // get the listview
        menu = (ExpandableListView) findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        menu.setAdapter(listAdapter);
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Document doc = Jsoup.parse(html);
        Elements menus = doc.getElementsByClass("menucontent");

        listDataHeader.add("Covel");
        listDataHeader.add("De Neve");
        listDataHeader.add("FEAST at Rieber");
        listDataHeader.add("Bruin Plate");

        List<String> covel = new ArrayList<String>();
        List<String> deNeve = new ArrayList<String>();
        List<String> feast = new ArrayList<String>();
        List<String> bPlate = new ArrayList<String>();

        boolean topbottom; //top is true
        for (int i = 0; i < menus.size(); i++) {
            topbottom = true;
            Elements cells = menus.get(i).select(".menugridcell, .menusplit");
            for (Element e : cells) {
                if (e.hasClass("menusplit")) {
                    topbottom = false;
                }
                else if (topbottom == true) {
                    covel.add(listText(e));
                }
                else if (topbottom == false) {
                    feast.add(listText(e));
                }
            }
            Elements cells2 = menus.get(i).select(".menugridcell_last, .menusplit");
            topbottom = true;
            for (Element e : cells2) {
                if (e.hasClass("menusplit")) {
                    topbottom = false;
                }
                else if (topbottom == true) {
                    deNeve.add(listText(e));
                }
                else if (topbottom == false) {
                    bPlate.add(listText(e));
                }
            }
            /*
                else if (leftright == false) {
                    if (topbottom == true) {
                        deNeve.add(e.text());
                    }
                    else if (topbottom == false) {
                        bPlate.add(e.text());
                    }
                    leftright = true;
                }
                */

        }

        listDataChild.put(listDataHeader.get(0), covel); // Header, Child data
        listDataChild.put(listDataHeader.get(1), deNeve);
        listDataChild.put(listDataHeader.get(2), feast);
        listDataChild.put(listDataHeader.get(3), bPlate);
    }

    private String listText(Element e) {
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

    //Gesture Detection (can implement functionality for other gestures as well later.
    private String DEBUG_TAG = "Gestures";
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        this.mDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDown: " + e.toString());
        return false;
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + e.toString());
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onShowPress: " + e.toString());
    }
    @Override
    public void onLongPress (MotionEvent e) {
        Log.d(DEBUG_TAG, "onLongPress: " + e.toString());
    }

    private int screenX, screenY;
    private float swipeMinDistance;
    private float swipeMinVelocity = 100;
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + e1.toString()+e2.toString()+velocityX);

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        velocityX = Math.abs(velocityX);
        boolean result = false;
        //LR swipe
        if(velocityX > this.swipeMinVelocity && xDistance > this.swipeMinDistance) {
            if(e1.getX() > e2.getX())
                Log.d(DEBUG_TAG, "Swipe to right screen");
            else
                Log.d(DEBUG_TAG, "Swipe to left screen");
            result = true;
        }
        return result;
    }

    private void initScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
        } else {
            screenX = display.getWidth();
            screenY = display.getHeight();
        }
        swipeMinDistance = screenX/4;

        Log.d(DEBUG_TAG, "("+screenX+", "+screenY+"), "+swipeMinDistance);
    }
}
