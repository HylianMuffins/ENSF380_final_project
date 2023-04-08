package edu.ucalgary.oop;

import java.util.ArrayList;

/**
* The Feeding class represents the type of task used in the schedule to feed 
* a certain number of animals of any given species.
*
* @author  Sudarshan Naicker
* @version 1.3
* @since   2023-04-01
*/
public class Feeding extends Task {
    private String species;
    private ArrayList<Animal> hungryAnimals;

    /**
    * Feeding constructor that derives all necessary values from the species
    * and number of animals that are being fed.
    * @param species Specifies the species of the animals being fed.
    * @param hungryAnimals Contains references to all the animals being fed.
    */
    public Feeding(String species, ArrayList<Animal> hungryAnimals) {
        super(createDescription(species, hungryAnimals), 
            createDuration(species, hungryAnimals), 
            3, 
            createStartTime(species)
        );
        this.species = species;
        this.hungryAnimals = hungryAnimals;
    }
    
    // Getters
    
    /**
    * Getter method that returns the number of animals being fed.
    * @return int representing # of animals.
    */
    public int getQuantity() {
        return hungryAnimals.size();
    }

    /**
    * Getter method that returns the species of the animals being fed.
    * @return species.
    */
    public String getSpecies() {
        return this.species;
    }

    /**
    * Getter method that returns the list of animals being fed.
    * @return list of hungry animals.
    */
    public ArrayList<Animal> getHungryAnimals() {
        return this.hungryAnimals;
    }

    /**
    * Private method used to generate a description of the task to send to
    * the Task super constructor.
    * @return description of task.
    */
    private static String createDescription(String species, ArrayList<Animal> hungryAnimals) {
        StringBuilder tmp = new StringBuilder();
        int i = 0;
        for (Animal animal : hungryAnimals) {
            if (i == 0) {
                tmp.append(animal.getNickname());
            } else {
                tmp.append(", " + animal.getNickname());
            }
            i++;
        }
        String animalString = tmp.toString();
        return String.format("Feeding - %s (%d: %s)", 
            species, hungryAnimals.size(), animalString
        );
    }

    /**
    * Private method used to generate the duration of the task to send to
    * the Task super constructor.
    * @return duration of task.
    */
    private static int createDuration(String species, ArrayList<Animal> hungryAnimals) {
        int duration = 0;
        duration += AnimalTypes.valueOf(species.toUpperCase()).getTime()[0];
        for (int i = 0; i < hungryAnimals.size(); i++) {
            duration += AnimalTypes.valueOf(species.toUpperCase()).getTime()[1];
        }
        return duration;
    }

    /**
    * Private method used to generate the start time of the task to send to
    * the Task super constructor.
    * @return start time of task.
    */
    private static int createStartTime(String species) {
        return AnimalTypes.valueOf(species.toUpperCase()).getTime()[3];
    }
}
