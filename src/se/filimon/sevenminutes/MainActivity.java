package se.filimon.sevenminutes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        Button startButton = (Button) this.findViewById(R.id.start_button);
        startButton.setOnClickListener(startButtonClickedListener);

    }

    private View.OnClickListener startButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
            startActivity(intent);
        }
    };

}
