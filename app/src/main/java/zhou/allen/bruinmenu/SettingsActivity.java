package zhou.allen.bruinmenu;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Set;

public class SettingsActivity extends PreferenceActivity {

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
                        String updateTimeString = (sethour==0||sethour==12?12:sethour%12)+":"+setminute+" "+((sethour<12)?"AM":"PM");
                        Toast.makeText(SettingsActivity.this, "Menus will update at "+updateTimeString, Toast.LENGTH_LONG).show();
                    }
                };
                final TimePickerDialog picker = new TimePickerDialog(SettingsActivity.this, timeSetListener, currHour, ((int) currMinute/30)*30, false);

                picker.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
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
