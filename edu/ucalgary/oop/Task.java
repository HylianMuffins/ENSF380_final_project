package edu.ucalgary.oop;

public class Task {
    private String description;
    private int duration;
    private int maxWindow;
    private int startTime;

    public Task(String description, int duration, int maxWindow, int startTIme){
        this.description = description;
        this.duration = duration;
        this.maxWindow = maxWindow;
        
        


    }

    public void setStartTime(int sTime) { 
        this.startTime = sTime; 
    }

    public int getStartTime() { 
        return this.startTime; 
    }

    public String getDescription() { 
        return this.description; 
    }

    public int getDuration() { 
        return this.duration; 
    }

    public int getMaxWindow() { 
        return this.maxWindow; 
    }

    
}
