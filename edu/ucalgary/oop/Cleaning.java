package edu.ucalgary.oop;

//Cleaning
public class Cleaning {
    private String animalNickname;
    private String species;
    // private ArrayList<Animal> animal;


    /*
    Constructor to set nicknameand species to their respective data fields.
    */
    public Cleaning (String nickname, String species) {
        this.animalNickname = nickname;
        this.species = species;
    }

    // Getters
    public int getAnimalID() {
        return this.id;
    }

    public String getSpecies() { 
        return this.species;
    }

}