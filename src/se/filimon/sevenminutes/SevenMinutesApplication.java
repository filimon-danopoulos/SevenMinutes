package se.filimon.sevenminutes;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class SevenMinutesApplication extends Application {

    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    private int current = 0;

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public Exercise getNextExercise() {
        Exercise exercise = this.exercises.get(this.current);
        this.current++;
        return exercise;
    }

    public String getNextExerciseName() {
        if (this.current < this.exercises.size()) {
            return this.exercises.get(this.current + 1).getName();
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
        this.addExercise(new Exercise("Jumping Jacks", 3), true, false);
        this.addExercise(new Exercise("Wall Sit", 3), false, false);
        this.addExercise(new Exercise("Push ups", 3), false, false);
        this.addExercise(new Exercise("Crunches", 3), false, false);
        this.addExercise(new Exercise("Step ups", 3), false, false);
        this.addExercise(new Exercise("Squats", 3), false, false);
        this.addExercise(new Exercise("Triceps Dips", 3), false, false);
        this.addExercise(new Exercise("Plank", 3), false, false);
        this.addExercise(new Exercise("High Knees", 3), false, false);
        this.addExercise(new Exercise("Lunges", 3), false, false);
        this.addExercise(new Exercise("Push up with twist", 3), false, false);
        this.addExercise(new Exercise("Side plank", 3, "Change side!", 15), false, true);
    }


    private void addExercise(Exercise exercise, boolean isFirst, boolean isLast) {
        if (!isFirst) {
            this.addExercise(new Exercise("Rest", 10, "Now rest, the next exercise is " + exercise.getName(), "Go"));
        } else {
            this.addExercise(new Exercise("Start ", 10, "The first exercise is " + exercise.getName(), "Go"));
        }
        this.addExercise(exercise);
        if (isLast) {
            this.addExercise(new Exercise("Stop", 10, "Good job! You are done.", ""));
        }

    }
}

