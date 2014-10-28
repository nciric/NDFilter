package org.kaziprst.android.ndfilter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;


public class ShutterSpeedActivity extends Activity {
    private static final String SHUTTER_SPEED_INDEX = "SHUTTER_INDEX";
    private static final String ND_VALUE_INDEX = "ND_INDEX";
    private static final String SHARED_PREFS = "Shared preferences";

    private int shutter_speed_index;
    private int nd_value_index;
    private TextView current_shutter_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO(cira): use saved state to restore the pickers to previous state. Also save to prefs for app restarts.
        // TODO(cira): make an icon for the app.
        setContentView(R.layout.activity_shutter_speed);

        current_shutter_speed = (TextView) findViewById(R.id.calculated_shutter_speed);

        Resources resources = getResources();

        if (savedInstanceState != null) {
            shutter_speed_index = savedInstanceState.getInt(SHUTTER_SPEED_INDEX);
            nd_value_index = savedInstanceState.getInt(ND_VALUE_INDEX);
        } else {
            // Try shared prefs.
            SharedPreferences shared_preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            shutter_speed_index = shared_preferences.getInt(SHUTTER_SPEED_INDEX, 0);
            nd_value_index = shared_preferences.getInt(ND_VALUE_INDEX, 0);
        }

        String[] shutter_speeds = resources.getStringArray(R.array.shutter_speeds);
        NumberPicker shutter_speed = (NumberPicker) findViewById(R.id.shutter_value);
        shutter_speed.setMinValue(0);
        shutter_speed.setMaxValue(shutter_speeds.length - 1);
        shutter_speed.setDisplayedValues(shutter_speeds);
        shutter_speed.setValue(shutter_speed_index);
        shutter_speed.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old_value, int new_value) {
                shutter_speed_index = new_value;
                updateShutterSpeed();
            }
        });

        String[] nd_values = resources.getStringArray(R.array.nd_values);
        NumberPicker nd_value = (NumberPicker) findViewById(R.id.nd_value);
        nd_value.setMinValue(0);
        nd_value.setMaxValue(nd_values.length - 1);
        nd_value.setDisplayedValues(nd_values);
        nd_value.setValue(nd_value_index);
        nd_value.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old_value, int new_value) {
                nd_value_index = new_value;
                updateShutterSpeed();
            }
        });

        updateShutterSpeed();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(SHUTTER_SPEED_INDEX, shutter_speed_index);
        savedInstanceState.putInt(ND_VALUE_INDEX, nd_value_index);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        ed.putInt(SHUTTER_SPEED_INDEX, shutter_speed_index);
        ed.putInt(ND_VALUE_INDEX, nd_value_index);
        ed.commit();
    }

    private void updateShutterSpeed() {
        String shutter_speed = ShutterSpeedCalculator.calculateShutterSpeed(
                getApplicationContext(), shutter_speed_index, nd_value_index);
        current_shutter_speed.setText(shutter_speed);
    }
}
