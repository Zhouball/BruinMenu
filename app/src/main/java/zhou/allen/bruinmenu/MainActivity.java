package zhou.allen.bruinmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

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
        //mainTextView = (TextView) findViewById(R.id.main_textview);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            html = extras.getString("html");
        }

        //mainTextView.setText(html);

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

        listDataHeader.add("Covel");
        listDataHeader.add("De Neve");
        listDataHeader.add("FEAST at Rieber");
        listDataHeader.add("Bruin Plate");

        /*
        List<String> covel = new ArrayList<String>();
        List<String> deNeve = new ArrayList<String>();
        List<String> feast = new ArrayList<String>();
        List<String> bPlate = new ArrayList<String>();
        */

        MenuDBHelper dbHelper = new MenuDBHelper(this);

        List<String> covel = dbHelper.getEntryByLocAndMealTime("covel", "lunch");
        List<String> deNeve = dbHelper.getEntryByLocAndMealTime("deNeve", "lunch");
        List<String> feast = dbHelper.getEntryByLocAndMealTime("feast", "lunch");
        List<String> bPlate = dbHelper.getEntryByLocAndMealTime("bPlate", "lunch");



        listDataChild.put(listDataHeader.get(0), covel); // Header, Child data
        listDataChild.put(listDataHeader.get(1), deNeve);
        listDataChild.put(listDataHeader.get(2), feast);
        listDataChild.put(listDataHeader.get(3), bPlate);
    }
}
