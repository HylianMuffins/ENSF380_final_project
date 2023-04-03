package edu.ucalgary.oop;

//Cleaning
public class Cleaning extends Task {
    private String animalNickname;
    private String species;
    // private ArrayList<Animal> animal;


    /*
    Constructor to set nicknameand species to their respective data fields.
    */
    public Cleaning (String nickname, String species) {
        super(createDescription(nickname, species), createDuration(species), 24, 0);
        this.animalNickname = nickname;
        this.species = species;
        
    }

    // Getters
    

    public String getSpecies() { 
        return this.species;
    }

    public String animalNickname() { 
        return this.animalNickname;
    }

    private static String createDescription(String nickname, String species) {
        return String.format("Cleaing - %s (%s)", species, nickname);
    }

    private static int createDuration(String species) {
        return AnimalTypes.valueOf(species.toUpperCase()).getTime()[2];
    }

}