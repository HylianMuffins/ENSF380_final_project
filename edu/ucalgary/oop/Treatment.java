package edu.ucalgary.oop;

/**
 * The Treatment class represents the type of task specifically used to
 * administer medical treatments to animals.
 *
 * @author Rutvi Brahmbhatt
 * @version 1.3
 * @since 2023-03-31
 */
public class Treatment extends Task {
    private final int TREATMENTID;
    private int animalID;

    /**
     * Treatment constructor; creates new Task object of type Treatment by
     * calling the Task super constructor.
     * 
     * @param description description of treatment.
     * @param duration    length of time needed to complete treatment.
     * @param maxWindow   flexibility of treatment given in hours.
     * @param startTime   the first hour in window of hours the task can be done.
     * @param treatmentID id of treatment.
     * @param animalID    id of the animal this treatment is being performed on.
     */
    public Treatment(String description, int duration, int maxWindow,
            int startTime, int treatmentID, int animalID) {
        super(description, duration, maxWindow, startTime);
        this.TREATMENTID = treatmentID;
        this.animalID = animalID;
    }

    /**
     * Getter method that returns the id of this treatment.
     * 
     * @return treatmentID.
     */
    public int getTreatmentID() {
        return this.TREATMENTID;
    }

    /**
     * Getter method that returns the id of the animal the treatment is for.
     * 
     * @return animalID.
     */
    public int getAnimalID() {
        return this.animalID;
    }
}