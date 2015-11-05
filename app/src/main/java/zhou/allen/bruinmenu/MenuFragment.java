package zhou.allen.bruinmenu;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "timeOfDay";

    private String timeOfDay;
    View layout;
    ExpandableListView menu;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<MenuItem>> listDataChild = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public static MenuFragment newInstance(String timeOfDay) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, timeOfDay);
        fragment.setArguments(args);
        return fragment;
    }

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timeOfDay = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_menu, container, false);

        //get the listview and textview
        menu = (ExpandableListView) layout.findViewById(R.id.expandableListView);
        //textView = (TextView) layout.findViewById(R.id.titleTextView);
        //setting list adapter
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        menu.setAdapter(listAdapter);


        final ExpandableListView elv = (ExpandableListView) layout.findViewById(R.id.expandableListView);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);
        elv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (elv != null && elv.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = elv.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = elv.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }
        });
        //preparing list data
        prepareListData();

        return layout;
    }

    private void prepareListData() {

        ArrayList<String> favoriteFood = new ArrayList<>();

        Context _context = getContext();
        MenuDBHelper dbHelper = new MenuDBHelper(_context, favoriteFood);
        ArrayList<List<MenuItem>> menuList = new ArrayList<>();
        ArrayList<Hall> halls = (ArrayList) dbHelper.getHallsByMealTime(timeOfDay);

        for(Hall hall : halls) {
            listDataHeader.add(hall.getItem()); //add the name of hall to listDataHeader
            ArrayList<Kitchen> kitchensList = (ArrayList) dbHelper.getKitchensByHall(hall); //get list of kitchens (Kitchens)
            ArrayList<MenuItem> listItems = new ArrayList<>(); //list of menuItems (name, url, veg, fav, id)
            for(Kitchen kitchen : kitchensList) {
                listItems.add(new MenuItem(kitchen.getItem(), "", -78, Boolean.FALSE, -78)); //-78 = magic number to show it's a kitchen
                listItems.addAll(dbHelper.getMenuItemsByKitchen(kitchen));
            }
            menuList.add(listItems);
        }

        if (menuList.size() == 0) {
            listDataHeader.add("Nothing to see here!");
            menuList.add(new ArrayList<MenuItem>());
        }
        for (int i = 0; i < menuList.size(); i++) {
            listDataChild.put(listDataHeader.get(i), menuList.get(i)); // Header, Child data
        }

        //displaying notification
        if(!favoriteFood.isEmpty()) {
            timeOfDay = timeOfDay.substring(0, 1).toUpperCase() + timeOfDay.substring(1);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(_context).
                    setSmallIcon(R.drawable.vegetarian).
                    setContentTitle(timeOfDay + " Favorites").
                    setContentText(favoriteFood.get(0) + "....");
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(timeOfDay + " Favorites");
            for(String foods : favoriteFood) {
                inboxStyle.addLine(foods);
            }
            builder.setStyle(inboxStyle);
            builder.setContentIntent(PendingIntent.getActivity(_context, 0, new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
            builder.setAutoCancel(true);
            NotificationManager notifManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationID = -1;
            if("Breakfast".equals(timeOfDay)) notificationID = 0;
            else if("Lunch".equals(timeOfDay)) notificationID = 1;
            else if("Dinner".equals(timeOfDay)) notificationID = 2;
            notifManager.notify(notificationID, builder.build());
        }
        dbHelper.close();
    }
/*
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
*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
