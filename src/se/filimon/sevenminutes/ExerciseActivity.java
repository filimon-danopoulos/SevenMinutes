package se.filimon.sevenminutes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
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
        this.tts.speak("TEST TEST TEST", TextToSpeech.QUEUE_FLUSH, null);

        this.timeRemaining = this.exercise.getDuration();

        this.overridePendingTransition(R.anim.shrink_to_left, R.anim.expand_from_right);
        this.setContentView(R.layout.exercise);

        TextView title = (TextView) this.findViewById(R.id.exercise_name);
        title.setText(this.exercise.getName());

        TextView nextExercise = (TextView) this.findViewById(R.id.exercise_next);
        nextExercise.setText(app.getNextExerciseName());

    }

    /***
     * Gets called just before the user sees the view. Used to trigger the countdown.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.countDown = (TextView) this.findViewById(R.id.exercise_countdown);
        this.startCountDown();
    }

    /***
     * First thing that will happen the activity is no longer visible. Cancels the countdown and kill tts.
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.handler.removeMessages(COUNTDOWN_MESSAGE);
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
    }

    /***
     * Handles instantiation of the text to speech engine. Sets language to english. Showws a Toast if any error occurs.
     * @param status TTS engine status.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            this.tts.setLanguage(Locale.ENGLISH);
        } else {
            Toast t = Toast.makeText(ExerciseActivity.this, "Could not init TTS", Toast.LENGTH_LONG);
            t.show();
        }
    }

    /***
     * Says opening phrase if any and starts countdown
     */
    private void startCountDown() {
        if (!this.exercise.getStartMessage().isEmpty()) {
            this.tts.speak(this.exercise.getStartMessage(), TextToSpeech.QUEUE_FLUSH, null);
        }
        handleCountDownTick();
    }

    /***
     * Handles each decrement in the countdown. Will set the countdown TextView and speak the remaining time in all
     * cases it should. Starts a new activity when the we have no time remaining.
     */
    private void handleCountDownTick() {
        if(this.timeRemaining == 0) {
            Intent intent = new Intent(this, ExerciseActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            this.countDown.setText(Integer.toString(this.timeRemaining));
            if (!this.tts.isSpeaking()) {
                if (this.exercise.hasSwitch() && this.timeRemaining == this.exercise.getSwitchTime()) {
                    this.tts.speak(this.exercise.getSwitchMessage(), TextToSpeech.QUEUE_FLUSH, null);
                } else if (this.timeRemaining == 20 || this.timeRemaining == 10) {
                    this.tts.speak(Integer.toString(this.timeRemaining)+ " seconds left.", TextToSpeech.QUEUE_FLUSH, null);
                } else if (this.timeRemaining <= 5) {
                    this.tts.speak(Integer.toString(this.timeRemaining), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            this.handler.sendEmptyMessageDelayed(COUNTDOWN_MESSAGE, 1000);
        }
        this.timeRemaining--;
    }

}
