package se.filimon.sevenminutes;

// A data bearing class that is used as a model of an exercise.
public class Exercise {
    //
    public Exercise(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.startMessage = "";
        this.endMessage = "";

        this.switchMessage = "";
        this.hasSwitch = false;
        this.switchTime = -1;
    }

    // Construct an exercise without a switch
    public Exercise(String name, int duration, String startMessage, String endMessage) {
        this(name, duration);
        this.startMessage = startMessage;
        this.endMessage = endMessage;
    }


    public Exercise(String name, int duration, String switchMessage, int switchTime) {
        this(name, duration);

        this.switchMessage = switchMessage;
        this.switchTime = switchTime;
    }


    // Construct an exercise with a switch
    public Exercise(String name, int duration, String startMessage, String endMessage, String switchMessage, int switchTime) {
        this(name, duration, startMessage, endMessage);

        this.switchMessage = switchMessage;
        this.hasSwitch = true;
        this.switchTime = switchTime;
    }

    // The name of the exercise
    private String name;

    // Messages to play during execution
    private String startMessage;
    private String switchMessage;
    private String endMessage;

    // Duration, and time of switch
    private int duration;
    private int switchTime;

    // Indicates if the exercise has a switch
    private boolean hasSwitch;

    // Public getters
    public String getName() {
        return name;
    }
    public String getStartMessage() {
        return startMessage;
    }
    public String getSwitchMessage() {
        return switchMessage;
    }
    public String getEndMessage() {
        return endMessage;
    }
    public int getDuration() {
        return duration;
    }
    public int getSwitchTime() {
        return switchTime;
    }
    public boolean hasSwitch() {
        return hasSwitch;
    }

}
