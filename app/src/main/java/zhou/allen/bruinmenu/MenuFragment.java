package zhou.allen.bruinmenu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.expandablelistview.ExpandableListAdapter;


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
    TextView textView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

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
            Log.d("TOD", timeOfDay); ///TODO: REMOVE LOG
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_menu, container, false);

        //get the listview and textview
        menu = (ExpandableListView) layout.findViewById(R.id.expandableListView);
        textView = (TextView) layout.findViewById(R.id.titleTextView);
        //setting list adapter
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        menu.setAdapter(listAdapter);
        //preparing list data
        prepareListData();

        return layout;
    }

    private void prepareListData() {

        listDataHeader.add("Covel");
        listDataHeader.add("De Neve");
        listDataHeader.add("FEAST at Rieber");
        listDataHeader.add("Bruin Plate");

        MenuDBHelper dbHelper = new MenuDBHelper(getContext());

        List<String> covel = dbHelper.getEntryByLocAndMealTime("covel", timeOfDay);
        List<String> deNeve = dbHelper.getEntryByLocAndMealTime("deNeve", timeOfDay);
        List<String> feast = dbHelper.getEntryByLocAndMealTime("feast", timeOfDay);
        List<String> bPlate = dbHelper.getEntryByLocAndMealTime("bPlate", timeOfDay);

        listDataChild.put(listDataHeader.get(0), covel); // Header, Child data
        listDataChild.put(listDataHeader.get(1), deNeve);
        listDataChild.put(listDataHeader.get(2), feast);
        listDataChild.put(listDataHeader.get(3), bPlate);
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
