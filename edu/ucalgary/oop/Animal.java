package edu.ucalgary.oop;

public class Animal {
    private String nickname;
    private final int ANIMALID;
    private final String ANIMALSPECIES;

    /*
    * Constructor to set nickname, id and species to their respective
    * data fields.
    */
    public Animal(String nickname, int id, String species) {
        this.nickname = nickname;
        this.ANIMALID = id;
        this.ANIMALSPECIES = species;
    }

    // Getters
    public String getNickname() { return this.nickname; }
    public int getAnimalID() { return this.ANIMALID;}
    public String getSpecies() {return this.ANIMALSPECIES; }
}