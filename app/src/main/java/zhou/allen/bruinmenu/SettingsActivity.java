package zhou.allen.bruinmenu;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
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

                //TODO: Time in milliseconds not set properly.
                //TODO: Have another frequency setter (set update every 5 hours, eg.)

                int INTERVAL_HOUR = 3600000;
                int INTERVAL_MINUTE = 60000;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //prefs.edit().putLong("update_start", 6*INTERVAL_HOUR).commit();


                long timeInMillis = prefs.getLong("update_start", 6*INTERVAL_HOUR);
                int currHour = (int) (timeInMillis/INTERVAL_HOUR);
                int currMinute = (int) ((timeInMillis-currHour*INTERVAL_HOUR)/INTERVAL_MINUTE);

                Toast.makeText(SettingsActivity.this, timeInMillis+" "+currHour+" "+currMinute, Toast.LENGTH_LONG).show();

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);

                        Calendar offset = Calendar.getInstance();
                        offset.set(Calendar.MILLISECOND, 0);
                        long offsetTime = offset.getTimeInMillis();

                        Toast.makeText(SettingsActivity.this, Long.toString(cal.getTimeInMillis()-offsetTime), Toast.LENGTH_LONG).show();

                        editor.putLong("update_start", cal.getTimeInMillis() - offsetTime).commit();

                        view.setVisibility(View.INVISIBLE);
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
