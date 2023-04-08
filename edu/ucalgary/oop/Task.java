package edu.ucalgary.oop;

//TODO: maybe make this abstract to match the uml (if it doesn't break stuff).
/**
* The Task class represents any activity for volunteers to complete. Subclasses
* include: Cleaning, Feeding and Treatment.
*
* @author  Sudarshan Naicker
* @version 1.1
* @since   2023-01-01
*/
public class Task {
    private String description;
    private int duration;
    private int maxWindow;
    private int startTime;

    /**
    * Task constructor; used as a way to construct objects of the subclasses
    * through the super constructor.
    * @param description description of task.
    * @param duration length of time needed to complete task.
    * @param maxWindow flexibility of task given in hours.
    * @param startTime the first hour in window of hours the task can be done.
    */
    public Task(String description, int duration, int maxWindow, int startTIme){
        this.description = description;
        this.duration = duration;
        this.maxWindow = maxWindow;
    }

    /**
    * Setter method used to set a new start time for the task.
    * @param sTime the new start time for the task.
    */
    public void setStartTime(int sTime) { 
        this.startTime = sTime; 
    }

    /**
    * Getter method that returns the start time of the task.
    * @return start time.
    */
    public int getStartTime() { 
        return this.startTime; 
    }

    /**
    * Getter method that returns the description of the task.
    * @return description.
    */
    public String getDescription() { 
        return this.description; 
    }

    /**
    * Getter method that returns the duration of the task.
    * @return duration.
    */
    public int getDuration() { 
        return this.duration; 
    }

    /**
    * Getter method that returns the max window of the task.
    * @return max window.
    */
    public int getMaxWindow() { 
        return this.maxWindow; 
    }
}
