//Treatment
public class Treatment {
    private final int TREATMENTID;
    private final int animalID;

    /*
     Constructor to set treatmentId, id to their respective data fields.
    */
    public Treatment(int treatmentID, int id) {
        this.TREATMENTID = treatmentid;
        this.animalID = id;   
    }

    // Getters
    public String getTreatmentID() { return this.TREATMENTID; }
    public int getAnimalID() { return this.animalID;}
}
