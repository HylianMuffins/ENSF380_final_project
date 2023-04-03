package edu.ucalgary.oop;

import java.util.ArrayList;

public class Hour {
    private int timeAvailable = 60;
    private final int HOUR;
    private boolean backupBoolean = false;
    private ArrayList<Task> tasks;

    /*
     * Constructor taking in hour and setting the HOUR variable,
     * also, takes in an ArrayList<Task> and setting it to tasks
     * Throws an exception if the hour is not between 0 and 23
     */
    public Hour(int hour) throws IllegalArgumentException{
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException();
        }
        this.HOUR = hour;
    }

    // Getters and setters
    public int getHour() { return this.HOUR; } 
    public int getTimeAvailable() {return this.timeAvailable; }
    public ArrayList<Task> getTasks() {return this.tasks; }
    public boolean getBackupBoolean() { return this.backupBoolean; }
    public void setBackupBoolean(boolean backupBoolean) { this.backupBoolean = backupBoolean; }

    /*
     * Calculates the remaining available time of the hour
     * Throws an exception if there is not enough available time.
     */
    public void updateTimeAvailable(int timeTaken) throws IllegalArgumentException {
        if (timeTaken > timeAvailable) {
            throw new IllegalArgumentException();
        }

        this.timeAvailable = this.timeAvailable - timeTaken;
    }
}