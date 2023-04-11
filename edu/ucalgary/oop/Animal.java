package edu.ucalgary.oop;

/**
 * The Animal class represents an animal in the organization's care.
 *
 * @author Ken Liu
 * @version 1.1
 * @since 2023-03-29
 */
public class Animal {
    private String nickname;
    private final int ANIMALID;
    private final String ANIMALSPECIES;

    /**
     * Animal constructor creates an animal with a nickname, id and species type.
     * 
     * @param nickname Name of animal.
     * @param id       Animal id for animal used in database.
     * @param species  Species of animal.
     */
    public Animal(String nickname, int id, String species) {
        this.nickname = nickname;
        this.ANIMALID = id;
        this.ANIMALSPECIES = species;
    }

    /**
     * Getter method that returns the nickname of the animal.
     * 
     * @return nickname.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Getter method that returns the id of the animal.
     * 
     * @return animalID.
     */
    public int getAnimalID() {
        return this.ANIMALID;
    }

    /**
     * Getter method that returns the species of the animal.
     * 
     * @return species.
     */
    public String getSpecies() {
        return this.ANIMALSPECIES;
    }
}