package com.rhombus.bruinmenu;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Set;

public class SettingsActivity extends PreferenceActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //getFragmentManager().beginTransaction().add(android.R.id.content, new PrefsFragment()).commit();
        addPreferencesFromResource(R.xml.preference);

        Button button = (Button) findViewById(R.id.pickerbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Have another frequency setter (set update every 5 hours, eg.)

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                int currHour = (int) prefs.getLong("update_start_hour", 6);
                int currMinute = (int) prefs.getLong("update_start_minute", 0);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

                        long sethour = view.getCurrentHour();
                        long setminute = view.getCurrentMinute();
                        editor.putLong("update_start_hour", sethour).commit();
                        editor.putLong("update_start_minute", setminute).commit();
                        view.setVisibility(View.INVISIBLE);
                        //String updateTimeString = (sethour == 0 || sethour == 12 ? 12 : sethour % 12) + ":" + setminute + " " + ((sethour < 12) ? "AM" : "PM");
                        //Toast.makeText(SettingsActivity.this, "Menus will update at " + updateTimeString, Toast.LENGTH_LONG).show();
                        Intent i = new Intent("com.rhombus.action.RESET_ALARM");
                        sendBroadcast(i);
                    }
                };
                final TimePickerDialog picker = new TimePickerDialog(SettingsActivity.this, timeSetListener, currHour, ((int) currMinute / 30) * 30, false);

                picker.show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.updateSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long prevF = prefs.getLong("update_frequency", AlarmManager.INTERVAL_DAY);
        if (prevF == AlarmManager.INTERVAL_DAY)
            spinner.setSelection(0);
        else if (prevF == AlarmManager.INTERVAL_HALF_DAY)
            spinner.setSelection(1);
        else if (prevF == AlarmManager.INTERVAL_HOUR * 6)
            spinner.setSelection(2);
        else if (prevF == AlarmManager.INTERVAL_HOUR * 3)
            spinner.setSelection(3);
        else if (prevF == AlarmManager.INTERVAL_HOUR * 2)
            spinner.setSelection(4);
        else if (prevF == AlarmManager.INTERVAL_HOUR)
            spinner.setSelection(5);
        else if (prevF == AlarmManager.INTERVAL_HALF_HOUR)
            spinner.setSelection(6);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        switch (pos) {
            case 0:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_DAY).commit();
                break;
            case 1:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HALF_DAY).commit();
                break;
            case 2:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HOUR * 6).commit();
                break;
            case 3:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HOUR * 3).commit();
                break;
            case 4:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HOUR * 2).commit();
                break;
            case 5:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HOUR).commit();
                break;
            case 6:
                editor.putLong("update_frequency", AlarmManager.INTERVAL_HALF_HOUR).commit();
                break;
        }
        Intent i = new Intent("com.rhombus.action.RESET_ALARM");
        sendBroadcast(i);
    }

    public void onNothingSelected(AdapterView<?> parent) {
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
