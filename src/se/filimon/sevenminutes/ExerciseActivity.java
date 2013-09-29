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

import java.util.Locale;

public class ExerciseActivity extends Activity{
    private static int COUNTDOWN_MESSAGE = 1337;
    private int timeRemaining;
    private Exercise exercise;
    private SevenMinutesApplication app;
    private TextToSpeech tts;
    private TextView countDown;
    private Handler handler;

    private TextToSpeech.OnInitListener ttsOnInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
           if (status == TextToSpeech.SUCCESS) {
               tts.setLanguage(Locale.ENGLISH);
           }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate state
        this.app = (SevenMinutesApplication) this.getApplication();
        this.exercise = this.app.getNextExercise();
        this.tts = new TextToSpeech(this, this.ttsOnInitListener);
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == COUNTDOWN_MESSAGE) {
                    ExerciseActivity.this.continueCountDown();
                }
            }
        };

        this.timeRemaining = this.exercise.getDuration();

        this.setContentView(R.layout.exercise);

        TextView title = (TextView) this.findViewById(R.id.exercise_name);
        title.setText(this.exercise.getName());

        TextView nextExercise = (TextView) this.findViewById(R.id.exercise_next);
        nextExercise.setText(app.getNextExerciseName());


        this.countDown = (TextView) this.findViewById(R.id.exercise_countdown);
        this.startCountDown();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.handler.removeMessages(COUNTDOWN_MESSAGE);
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
    }

    private void startCountDown() {
        this.countDown.setText(Integer.toString(this.timeRemaining));
        if (this.exercise.getStartMessage() != "") {
            this.tts.speak(this.exercise.getStartMessage(), TextToSpeech.QUEUE_FLUSH, null);
        }
        this.handler.sendEmptyMessageDelayed(COUNTDOWN_MESSAGE, 1000);
    }


    private void continueCountDown() {
        this.timeRemaining--;
        this.countDown.setText(Integer.toString(this.timeRemaining));
        if(this.timeRemaining == 0) {
            Intent intent = new Intent(this, ExerciseActivity.class);
            if (exercise.getEndMessage() != "") {
                this.tts.speak(this.exercise.getEndMessage(), TextToSpeech.QUEUE_FLUSH, null);
            }
            this.startActivity(intent);
            this.finish();
        } else {
            this.tts.speak(Integer.toString(this.timeRemaining), TextToSpeech.QUEUE_ADD, null);
            this.handler.sendEmptyMessageDelayed(COUNTDOWN_MESSAGE, 1000);
        }
    }
}
