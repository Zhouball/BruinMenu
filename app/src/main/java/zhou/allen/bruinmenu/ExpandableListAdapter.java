package zhou.allen.bruinmenu;

/*
Custom ExpandableListAdapter class
(Mostly) Written by Ravi Tamada in the AndroidHive tutorial at http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
10/19/2015
(Slightly) Modified by Preetham Reddy Narayanareddy
*/

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import zhou.allen.bruinmenu.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MenuItem>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<MenuItem>> listChildData) {
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

            ImageButton favIcon = (ImageButton) convertView.findViewById(R.id.favorite);
            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///TODO: if not favorite: add to favorites, set button to enabled
                    ///TODO: else: remove from favorites, set button to disabled
                }
            });

            if(getChildIsFav(groupPosition, childPosition)) {
                favIcon.setSelected(true);
            }

            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nutriURL = getChildNurtiUrl(groupPosition, childPosition);
                    Intent intent = new Intent(_context, NutriDataWebView.class);
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
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

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

        return convertView;
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