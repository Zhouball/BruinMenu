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
}
