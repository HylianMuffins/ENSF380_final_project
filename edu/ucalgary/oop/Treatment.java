//Treatment

package edu.ucalgary.oop;


public class Treatment extends Task{
    private final int TREATMENTID;
    private int animalID;

    /* 
     Constructor to set treatmentId, id to their respective data fields.
    */
    public Treatment(String description, int duration, int maxWindow, int startTime, int treatmentID, int animalID) {
        super(description, duration, maxWindow, startTime);
        this.TREATMENTID = treatmentID;
        this.animalID = animalID;
    }

    // Getters
    public int getTreatmentID() { 
        return this.TREATMENTID; 
    }
    public int getAnimalID() { 
        return this.animalID;
    }
}