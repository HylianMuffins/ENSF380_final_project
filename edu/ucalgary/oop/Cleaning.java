package edu.ucalgary.oop;

/**
* The cleaning class represents the type of task used in the schedule to clean
* a certain animal's cage.
*
* @author  Rutvi Brahmbhatt
* @version 1.4
* @since   2023-03-31
*/
public class Cleaning extends Task {
    private String animalNickname;
    private String species;

    /**
    * Cleaning constructor that derives all necessary values from the nickname
    * and species of the animal who's cage is being cleaned.
    * @param nickname Name of the animal who's cage is being cleaned.
    * @param species species of the animal who's cage is being cleaned.
    */
    public Cleaning (String nickname, String species) {
        super(createDescription(nickname, species), 
            createDuration(species), 24, 0);
        this.animalNickname = nickname;
        this.species = species;
    }
    
    /**
    * Getter method that returns the species of the animal who's cage is being
    * cleaned.
    * @return species.
    */
    public String getSpecies() { 
        return this.species;
    }

    /**
    * Getter method that returns the nickname of the animal who's cage is being
    * cleaned.
    * @return nickname.
    */
    public String animalNickname() { 
        return this.animalNickname;
    }

    /**
    * Private method used to generate a description of the task to send to
    * the Task super constructor.
    * @return description of task.
    */
    private static String createDescription(String nickname, String species) {
        return String.format("Cleaing - %s (%s)", species, nickname);
    }

    /**
    * Private method used to generate a duration of the task to send to
    * the Task super constructor.
    * @return duration of task.
    */
    private static int createDuration(String species) {
        return AnimalTypes.valueOf(species.toUpperCase()).getTime()[2];
    }

}