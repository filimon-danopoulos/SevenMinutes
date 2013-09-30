package se.filimon.sevenminutes;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class SevenMinutesApplication extends Application {

    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
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
}

