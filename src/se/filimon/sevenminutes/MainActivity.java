package se.filimon.sevenminutes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);

        this.setContentView(R.layout.main);

        Button startButton = (Button) this.findViewById(R.id.start_button);
        startButton.setOnClickListener(startButtonClickedListener);

        Button settingsButton = (Button) this.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this.settingsButtonClickedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SevenMinutesApplication app = (SevenMinutesApplication) this.getApplication();
        app.reset();
    }

    private View.OnClickListener startButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener settingsButtonClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent =  new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(intent);
        }
    };
}
