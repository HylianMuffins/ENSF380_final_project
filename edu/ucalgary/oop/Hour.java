package edu.ucalgary.oop;

import java.util.ArrayList;

/**
 * The Hour class represents an hour in the 24 hr schedule for a given day.
 *
 * @author Ken Liu
 * @version 1.4
 * @since 2023-03-29
 */
public class Hour implements Cloneable {
    private int timeAvailable = 60;
    private final int HOUR;
    private boolean backupBoolean = false;
    private ArrayList<Task> tasks = new ArrayList<Task>();

    /**
     * Hour constructor taking in hour and setting the HOUR variable.
     * Throws an exception if the hour is not between 0 and 23.
     * 
     * @param hour int from 0 to 23 in 24 hour time.
     * @throws IllegalArgumentException when hour is not from 0 to 23
     */
    public Hour(int hour) throws IllegalArgumentException {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour value is < 0 OR Hour value is > 23");
        }
        this.HOUR = hour;
    }

    /**
     * Getter method that returns the int representing the hour in 24 hour time.
     * 
     * @return hour.
     */
    public int getHour() {
        return this.HOUR;
    }

    /**
     * Getter method that returns the available time left in the hour for
     * scheduling tasks.
     * 
     * @return timeAvailable.
     */
    public int getTimeAvailable() {
        return this.timeAvailable;
    }

    /**
     * Getter method that returns an ArrayList of references to tasks that are
     * scheduled for the hour.
     * 
     * @return tasks.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Getter method that returns the boolean that denotes whether a backup
     * volunteer is scheduled for the hour or not.
     * 
     * @return backupBoolean.
     */
    public boolean getBackupBoolean() {
        return this.backupBoolean;
    }

    /**
     * Setter method that updates the boolean denoting if a backup volunteer is
     * scheduled or not.
     * 
     * @param backupBoolean true for scheduling a backup, false for canceling.
     */
    public void setBackupBoolean(boolean backupBoolean) {
        this.backupBoolean = backupBoolean;
    }

    /**
     * Setter method that updates the tasks ArrayList.
     * Designed for use with cloner.
     * 
     * @param newTasks ArrayList of new tasks.
     */
    public void setTasks(ArrayList<Task> newTasks) {
        this.tasks = newTasks;
    }

    /**
     * Calculates the remaining available time of the hour.
     * Throws an exception if there is not enough available time.
     * 
     * @param timeTaken Duration of task being scheduled.
     * @throws IllegalArgumentException when not enough time is available
     */
    public void updateTimeAvailable(int timeTaken) throws IllegalArgumentException {
        if (timeTaken > timeAvailable) {
            throw new IllegalArgumentException("Not enough time available");
        }

        this.timeAvailable = this.timeAvailable - timeTaken;
    }

    /**
     * Clones the hour object, but not by a complete deep copy as the tasks
     * are never changed, only the ArrayList of tasks is changed.
     * 
     * @throws CloneNotSupportedException when Hour is not clonable
     */
    public Object clone() throws CloneNotSupportedException {

        // create new object with new tasks reference
        // copies of tasks can be shallow because they are not used
        Hour copy = (Hour) super.clone();

        ArrayList<Task> newTasks = new ArrayList<Task>();
        tasks.forEach(task -> newTasks.add(task));
        copy.setTasks(newTasks);

        return copy;
    }
}