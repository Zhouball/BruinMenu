package zhou.allen.bruinmenu;

/*
Custom ExpandableListAdapter class
(Mostly) Written by Ravi Tamada in the AndroidHive tutorial at http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
10/19/2015
(Slightly) Modified by Preetham Reddy Narayanareddy
*/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import zhou.allen.bruinmenu.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Hall> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MenuItem>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Hall> listDataHeader, HashMap<String, List<MenuItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    public MenuItem getChildData(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getChildData(groupPosition, childPosition).getItem();
    }

    public boolean getChildIsKitchen(int groupPosition, int childPosition) {
        return (getChildData(groupPosition, childPosition).getVeg() == -78);
    }
    public Integer getChildVeg(int groupPosition, int childPosition) {
        return getChildData(groupPosition, childPosition).getVeg();
    }
    public boolean getChildIsFav(int groupPosition, int childPosition) {
        return getChildData(groupPosition, childPosition).isFavorite();
    }
    public String getChildNurtiUrl(int groupPosition, int childPosition) {
        return getChildData(groupPosition, childPosition).getNutriurl();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(getChildIsKitchen(groupPosition, childPosition)) {
            convertView = infalInflater.inflate(R.layout.list_kitchen, null);
        } else {
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        if(!getChildIsKitchen(groupPosition, childPosition)) {
            ImageView vegIcon = (ImageView) convertView.findViewById(R.id.vegetarian);
            if (getChildVeg(groupPosition, childPosition) == 1) {
                vegIcon.setVisibility(View.VISIBLE);
            } else if (getChildVeg(groupPosition, childPosition) == 2) {
                vegIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.vegan));
                vegIcon.setVisibility(View.VISIBLE);
            }

            final ImageButton favIcon = (ImageButton) convertView.findViewById(R.id.favorite);
            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuDBHelper dbHelper = new MenuDBHelper(_context);

                    if(!getChildIsFav(groupPosition, childPosition)) {
                        favIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.star_pressed));
                        getChildData(groupPosition, childPosition).setFavorite(true);
                        dbHelper.addFavorite((String) (getChild(groupPosition, childPosition)));
                    } else {
                        favIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.star_unpressed));
                        getChildData(groupPosition, childPosition).setFavorite(false);
                        dbHelper.deleteFavorite((String) (getChild(groupPosition, childPosition)));
                    }
                    dbHelper.close();
                }
            });

            if(getChildIsFav(groupPosition, childPosition)) {
                favIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.star_pressed));
            }

            txtListChild.setOnClickListener(new View.OnClickListener() {
                private String nutriURLprefix = "http://menu.ha.ucla.edu/foodpro/";

                @Override
                public void onClick(View v) {
                    String nutriURL = nutriURLprefix + getChildNurtiUrl(groupPosition, childPosition);
                    Intent intent = new Intent(_context, LoadNutriDataActivity.class);
                    intent.putExtra("nutriURL", nutriURL);
                    _context.startActivity(intent);
                }
            });

        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) { return this._listDataHeader.get(groupPosition).getItem(); }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView hallHours = (TextView) convertView.findViewById(R.id.hallHours);
        Hall hall = (Hall) getGroup(groupPosition);
        if(hall.getStartTime() != -3600000) {
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            Calendar st = Calendar.getInstance();
            st.setTimeInMillis(hall.getStartTime());
            String sts = formatter.format(st);
            Calendar et = Calendar.getInstance();
            et.setTimeInMillis(hall.getEndTime());
            String ets = formatter.format(st);
            hallHours.setText(sts + " - " + ets);
            hallHours.setTextColor(getTimeColor(hall));
        } else {
            hallHours.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private int getTimeColor(Hall hall) {
        int INTERVAL_HOUR = 3600000;
        Calendar cal = Calendar.getInstance();
        // get the difference between now and hall's next time. that's the color of the hall
        if(hall.getEndTime() - cal.getTimeInMillis() < 0.5*INTERVAL_HOUR) return Color.YELLOW;
        if(cal.getTimeInMillis() < hall.getStartTime() || cal.getTimeInMillis() > hall.getEndTime()) return Color.RED;
        else return Color.WHITE;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}