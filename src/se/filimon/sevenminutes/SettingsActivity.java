package se.filimon.sevenminutes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);

        this.setContentView(R.layout.settings);

        Button cancelButton = (Button) this.findViewById(R.id.cancel_settings_button);
        cancelButton.setOnClickListener(this.cancelButtonClickedListener);
    }

    private Button.OnClickListener cancelButtonClickedListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            SettingsActivity.this.finish();
        }
    };

    private void setCheckBoxValues() {

    }
}