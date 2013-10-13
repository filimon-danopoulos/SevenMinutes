package se.filimon.sevenminutes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingsActivity extends Activity {
    private SevenMinutesApplication app;
    private CheckBox soundEnabledCheckBox;
    private CheckBox readNextExerciseCheckBox;
    private CheckBox readTwentySecondsCheckBox;
    private CheckBox readTenSecondsCheckBox;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);

        this.setContentView(R.layout.settings);

        this.app = (SevenMinutesApplication) SettingsActivity.this.getApplication();

        Button cancelButton = (Button) this.findViewById(R.id.cancel_settings_button);
        cancelButton.setOnClickListener(this.cancelButtonClickedListener);

        Button saveButton = (Button) this.findViewById(R.id.save_settings_button);
        saveButton.setOnClickListener(this.saveButtonClickedListener);

        this.soundEnabledCheckBox = (CheckBox) this.findViewById(R.id.sound_enabled_checkbox);
        this.soundEnabledCheckBox.setOnCheckedChangeListener(this.soundEnabledCheckChangeListener);

        this.readNextExerciseCheckBox = (CheckBox) this.findViewById(R.id.read_next_exercise_checkbox);
        this.readNextExerciseCheckBox.setOnCheckedChangeListener(this.readNextCheckChangeListener);

        this.readTwentySecondsCheckBox = (CheckBox) this.findViewById(R.id.read_twenty_second_mark_checkbox);
        this.readTwentySecondsCheckBox.setOnCheckedChangeListener(this.readTwentySecondsCheckChangeListener);

        this.readTenSecondsCheckBox = (CheckBox) this.findViewById(R.id.read_ten_second_mark_checkbox);
        this.readTenSecondsCheckBox.setOnCheckedChangeListener(this.readTenSecondsCheckChangeListener);

        this.setCheckBoxValues();

    }

    private CheckBox.OnCheckedChangeListener readNextCheckChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsActivity.this.app.settings.isNextExerciseEnabled = isChecked;
        }
    };

    private CheckBox.OnCheckedChangeListener readTwentySecondsCheckChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsActivity.this.app.settings.isTwentySecondMarkEnabled = isChecked;
        }
    };

    private CheckBox.OnCheckedChangeListener readTenSecondsCheckChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsActivity.this.app.settings.isTenSecondMarkEnabled = isChecked;
        }
    };

    private CheckBox.OnCheckedChangeListener soundEnabledCheckChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsActivity.this.app.settings.isSoundEnabled = isChecked;

            SettingsActivity.this.readTwentySecondsCheckBox.setEnabled(isChecked);
            SettingsActivity.this.readTenSecondsCheckBox.setEnabled(isChecked);

            SettingsActivity.this.readNextExerciseCheckBox.setEnabled(isChecked);
        }
    };

    private Button.OnClickListener cancelButtonClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            SettingsActivity.this.app.resetSettings();
            SettingsActivity.this.finish();
            SettingsActivity.this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);
        }
    };

    private Button.OnClickListener saveButtonClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (SettingsActivity.this.app.saveSettings()) {
                SettingsActivity.this.finish();
                SettingsActivity.this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);
            }
        }
    };

    private void setCheckBoxValues() {
        this.soundEnabledCheckBox.setChecked(this.app.settings.isSoundEnabled);

        this.readTwentySecondsCheckBox.setEnabled(this.app.settings.isSoundEnabled);
        this.readTwentySecondsCheckBox.setChecked(this.app.settings.isTwentySecondMarkEnabled);

        this.readTenSecondsCheckBox.setEnabled(this.app.settings.isSoundEnabled);
        this.readTenSecondsCheckBox.setChecked(this.app.settings.isTenSecondMarkEnabled);

        this.readNextExerciseCheckBox.setEnabled(this.app.settings.isSoundEnabled);
        this.readNextExerciseCheckBox.setChecked(this.app.settings.isNextExerciseEnabled);
    }
}