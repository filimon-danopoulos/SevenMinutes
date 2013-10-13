package se.filimon.sevenminutes;

import android.app.Application;
import android.widget.Toast;

import java.io.*;
import java.util.*;

public class SevenMinutesApplication extends Application {

    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    public ApplicationSettings settings = new ApplicationSettings();

    private int current = 0;

    public void reset() {
        this.current = 0;
    }

    public Exercise getNextExercise() {
        Exercise exercise = this.exercises.get(this.current);
        this.current++;
        return exercise;
    }

    public String getNextExerciseName() {
        if (this.current < this.exercises.size()) {
            return this.exercises.get(this.current).getName();
        }
        return "";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.setExercises();
        this.loadSettings();


    }

    private void setExercises() {
        // This is probable not the best way to to do this.
        this.addExercise(new Exercise("Jumping Jacks", 30, "Begin Jumping Jacks."),  true, false);
        this.addExercise(new Exercise("Wall Sit", 30, "Begin Wall Sit."), false, false);
        this.addExercise(new Exercise("Push ups", 30, "Begin Push ups."), false, false);
        this.addExercise(new Exercise("Crunches", 30, "Begin Crunches."), false, false);
        this.addExercise(new Exercise("Step ups", 30, "Begin Step ups."), false, false);
        this.addExercise(new Exercise("Squats", 30, "Begin Squats."), false, false);
        this.addExercise(new Exercise("Triceps Dips", 30, "Begin Triceps Dips."), false, false);
        this.addExercise(new Exercise("Plank", 30, "Begin Plank."), false, false);
        this.addExercise(new Exercise("High Knees", 30, "Begin High Knees."), false, false);
        this.addExercise(new Exercise("Lunges", 30, "Begin Lunges."), false, false);
        this.addExercise(new Exercise("Push up with twist", 30, "Begin Push up with twist."), false, false);
        this.addExercise(new Exercise("Side plank", 30, "Begin Side plank.", "Change side!", 15), false, true);
    }


    private void addExercise(Exercise exercise, boolean isFirst, boolean isLast) {
        if (!isFirst) {
            this.exercises.add(new Exercise("Rest", 10, "Now rest. The next exercise is " + exercise.getName()));
        } else {
            this.exercises.add(new Exercise("Start ", 10, "The first exercise is " + exercise.getName()+". Get ready."));
        }
        this.exercises.add(exercise);
        if (isLast) {
            this.exercises.add(new Exercise("Stop", 10, "Good job! You are done."));
        }

    }

    private void loadSettings() {
        try {
            FileInputStream fileStream = this.openFileInput("settings");
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            this.settings = (ApplicationSettings) objectStream.readObject();
            objectStream.close();
        } catch (Exception ex) {
            Toast toast = Toast.makeText(this, "Could not load settings, reverting to defaults.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean saveSettings() {
        try {
            File file = new File(this.getFilesDir(), "settings");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileStream = this.openFileOutput("settings", MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(this.settings);
            objectStream.close();
            return true;
        } catch (Exception ex) {
            Toast toast = Toast.makeText(this, "Could not save settings. Please try again.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    public void resetSettings() {
        this.loadSettings();
    }
}

