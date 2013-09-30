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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate state
        this.app = (SevenMinutesApplication) this.getApplication();
        this.exercise = this.app.getNextExercise();
        this.tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                }
            }
        );
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == COUNTDOWN_MESSAGE) {
                    ExerciseActivity.this.handleCountDownTick();
                }
            }
        };
        this.tts.setLanguage(Locale.ENGLISH);

        this.timeRemaining = this.exercise.getDuration();

        this.overridePendingTransition(R.anim.shrink_to_left, R.anim.expand_from_right);
        this.setContentView(R.layout.exercise);

        TextView title = (TextView) this.findViewById(R.id.exercise_name);
        title.setText(this.exercise.getName());

        TextView nextExercise = (TextView) this.findViewById(R.id.exercise_next);
        nextExercise.setText(app.getNextExerciseName());

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        handleCountDownTick();
    }


    private void handleCountDownTick() {
        if(this.timeRemaining == 0) {
            Intent intent = new Intent(this, ExerciseActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            this.countDown.setText(Integer.toString(this.timeRemaining));
            if (!this.tts.isSpeaking()) {
                if (this.timeRemaining == this.exercise.getDuration() - 1 && !this.exercise.getStartMessage().isEmpty()) {
                    this.tts.speak(this.exercise.getStartMessage(), TextToSpeech.QUEUE_FLUSH, null);
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
