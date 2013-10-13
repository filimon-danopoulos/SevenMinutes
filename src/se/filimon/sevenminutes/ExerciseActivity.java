package se.filimon.sevenminutes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ExerciseActivity extends Activity implements TextToSpeech.OnInitListener{
    private static int COUNTDOWN_MESSAGE = 1337; // Message used for async countdown
    private int timeRemaining; // Handles the remaining time
    private Exercise exercise; // The current exercise
    private SevenMinutesApplication app; // Application object, stores global state
    private TextToSpeech tts; // Used to speak the countdown and any messages
    private TextView countDown; // countdown TextView needs to be a field so that we can increment it from the count down method
    private Handler handler; // The message handler used to listen for the countdown messages.


    /**
     * Creates an instance of this activity. Instantiates all local state and sets the text of the TextViews appropriately.
     * Starts countdown.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate state
        this.app = (SevenMinutesApplication) this.getApplication();
        this.exercise = this.app.getNextExercise();
        this.tts = new TextToSpeech(this, this);
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == COUNTDOWN_MESSAGE) {
                    ExerciseActivity.this.handleCountDownTick();
                }
            }
        };

        this.timeRemaining = this.exercise.getDuration();

        this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);
        this.setContentView(R.layout.exercise);

        TextView title = (TextView) this.findViewById(R.id.exercise_name);
        title.setText(this.exercise.getName());
        this.countDown = (TextView) this.findViewById(R.id.exercise_countdown);

        if (this.app.getNextExerciseName().isEmpty()) {
            this.countDown.setVisibility(View.GONE);

            TextView nextLabel = (TextView) this.findViewById(R.id.next_exercise_label);
            nextLabel.setVisibility(View.GONE);

            TextView nextText = (TextView) this.findViewById(R.id.exercise_next);
            nextText.setVisibility(View.GONE);

            Button cancelButton = (Button) this.findViewById(R.id.cancel_exercise_button);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExerciseActivity.this.finish();
                    ExerciseActivity.this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);
                }
            });

            TextView endText = (TextView) this.findViewById(R.id.exercise_end_text);
            endText.setVisibility(View.VISIBLE);
        } else {
            TextView nextExercise = (TextView) this.findViewById(R.id.exercise_next);
            nextExercise.setText(this.app.getNextExerciseName());

            this.startCountDown();
        }
    }

    /***
     * First thing that will happen the activity is no longer visible. Cancels the countdown and kill tts.
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.handler.removeMessages(COUNTDOWN_MESSAGE); // Remove all messages
        if (this.tts != null) { // Terminate tts.
            this.tts.stop();
            this.tts.shutdown();
        }
        this.finish();
        this.overridePendingTransition(R.anim.expand_from_right, R.anim.shrink_to_left);
    }

    /***
     * Handles instantiation of the text to speech engine. Sets language to english. Shows a Toast if any error occurs.
     * Also says any opening phrase
     * @param status TTS engine status.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            this.tts.setLanguage(Locale.ENGLISH);
            // We only want to say the start message if the TTS engine is initialized.
            if (!this.exercise.getStartMessage().isEmpty() && this.app.settings.isNextExerciseEnabled) {
                this.say(this.exercise.getStartMessage());
            }
        } else {
            Toast t = Toast.makeText(ExerciseActivity.this, "Could not init TTS", Toast.LENGTH_LONG);
            t.show();
        }
    }

    /***
     * Says opening phrase if any and starts countdown
     */
    private void startCountDown() {
        handleCountDownTick();
    }

    /***
     * Handles each decrement in the countdown. Will set the countdown TextView and speak the remaining time in all
     * cases it should. Starts a new activity when the we have no time remaining.
     */
    private void handleCountDownTick() {
        if(this.timeRemaining == 0) {
            // We open the next activity if countdown is done.
            Intent intent = new Intent(this, ExerciseActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            // Otherwise we set the TextView and conditionally say a message
            this.countDown.setText(Integer.toString(this.timeRemaining));
            if (this.exercise.hasSwitch() && this.timeRemaining == this.exercise.getSwitchTime()) {
                this.say(this.exercise.getSwitchMessage());
            } else if ((this.timeRemaining == 20 && this.app.settings.isTwentySecondMarkEnabled) ||
                    (this.timeRemaining == 10 && this.app.settings.isTenSecondMarkEnabled)) {
                this.say(Integer.toString(this.timeRemaining)+ " seconds left.");
            } else if (this.timeRemaining <= 5) {
                this.say(Integer.toString(this.timeRemaining));
            }
            this.handler.sendEmptyMessageDelayed(COUNTDOWN_MESSAGE, 1000); // Trigger next message in 1 sec
        }
        this.timeRemaining--; // Decrement time left.
    }

    /***
     * Wraps TextToSpeak#speak method for cleaner code. No params and queue mode flush set.
     * @param message The message to speak with TTS.
     */
    private void say(String message) {
        if (!this.tts.isSpeaking() && this.app.settings.isSoundEnabled) { // We don't speak if we are already speaking.
            this.tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
