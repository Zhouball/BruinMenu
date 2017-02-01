package com.rhombus.bruinmenu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwipesLeftFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwipesLeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipesLeftFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //premiums start from -4 because last 4 swipes (saturday and sunday of finals week) aren't counted.
    private int p14=-4, p19=-4, r14=0, r19=0;
    private String[] quarterEndDates = {
            "12112015",
            "03182016",
            "06102016",
            "12092016",
            "03242017"
    };

    public static SwipesLeftFragment newInstance() {
        SwipesLeftFragment fragment = new SwipesLeftFragment();
        return fragment;
    }

    public SwipesLeftFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get params if any exist
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_swipes_left, container, false);
        calcSwipes();
        //Log.d("Swipes Left", "(14P: " + p14 + "), (19P: " + p19 + "), (14: " + r14 + "), (19: " + r19 + ")");

        TextView p14Text = (TextView) layout.findViewById(R.id.p14Text);
        TextView r14Text = (TextView) layout.findViewById(R.id.r14Text);
        TextView p19Text = (TextView) layout.findViewById(R.id.p19Text);
        TextView r19Text = (TextView) layout.findViewById(R.id.r19Text);
        p14Text.setText(Integer.toString(p14));
        r14Text.setText(Integer.toString(r14));
        p19Text.setText(Integer.toString(p19));
        r19Text.setText(Integer.toString(r19));

        return layout;
    }

    private void calcSwipes() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MILLISECOND, 0);
        Calendar cal = findQuarter();
        //go backwards each week
        while(cal.get(Calendar.WEEK_OF_YEAR) != curr.get(Calendar.WEEK_OF_YEAR)) {
            p14+=14;
            p19+=19;
            cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)-1);
        }

        //adds the swipes that you'd have for next day
        switch(curr.get(Calendar.DAY_OF_WEEK)) {
            case 1: p14+=14; p19+=19; break;
            case 2: r14+=2; r19+=3;
            case 3: r14+=2; r19+=3;
            case 4: r14+=2; r19+=3;
            case 5: r14+=2; r19+=3;
            case 6: r14+=2; r19+=2;
            case 7: r14+=2; r19+=2;
            default:
        }
        //adds the swipes assuming you've used it at the start of the meal period
        int hour = curr.get(Calendar.HOUR_OF_DAY);
        if(hour >= 17) { //dinner started
            //daily swipes over
        } else if(hour >= 11) { //lunch started
            r14+=1; r19+=1;
        } else if(hour >= 7) { //breakfast started
            r14+=2; r19+=2;
        } else {
            r14+=2; r19+=3;
        }
        p14+=r14; p19+=r19;
    }

    private Calendar findQuarter() {
        for(String s : quarterEndDates) {
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(Calendar.getInstance().compareTo(cal) < 0) return cal;
        }
        return null;
    }

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
