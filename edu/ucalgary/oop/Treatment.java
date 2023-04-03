//Treatment

package edu.ucalgary.oop;


public class Treatment{
    private final int TREATMENTID;
    private int animalID;

    /*
     Constructor to set treatmentId, id to their respective data fields.
    */
    public Treatment(int treatmentID, int animalid) {
        this.TREATMENTID = treatmentID;
        this.animalID = animalid;   
    }

    // Getters
    public int getTreatmentID() { 
        return this.TREATMENTID; 
    }
    public int getAnimalID() { 
        return this.animalID;
    }
}