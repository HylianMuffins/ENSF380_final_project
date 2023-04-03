//Feeding

package edu.ucalgary.oop;

import java.util.ArrayList;

public class Feeding extends Task {
    private String species;
    private ArrayList<Animal> hungryAnimals;

    /*
    Constructor to set species and hungry animals to their respective data fields.
    */
    public Feeding(String species, ArrayList<Animal> hungryAnimals) {
        super(createDescription(species, hungryAnimals), createDuration(species, hungryAnimals), 3, createStartTime(species));
        this.species = species;
        this.hungryAnimals = hungryAnimals;

    }
    
    // Getters
    
    public int getQuantity() {
        return hungryAnimals.size();
    }

    private static String createDescription(String species, ArrayList<Animal> hungryAnimals) {
        StringBuilder tmp = new StringBuilder();
        int i = 0;
        for (Animal animal : hungryAnimals) {
            if (i == 0) {
                tmp.append(animal.getNickname());
            } else {
                tmp.append(", " + animal.getNickname());
            }
        }
        String animalString = tmp.toString();
        return String.format("Feeding - %s (%d: %s)", species, hungryAnimals.size(), animalString);
    }

    private static int createDuration(String species, ArrayList<Animal> hungryAnimals) {
        int duration = 0;
        duration += AnimalTypes.valueOf(species.toUpperCase()).getTime()[0];
        for (Animal animal : hungryAnimals) {
            duration += AnimalTypes.valueOf(species.toUpperCase()).getTime()[1];
        }
        return duration;
    }
    
    // private static int createMaxWindow(String species) {
    //     return AnimalTypes.valueOf(species.toUpperCase()).getTime()[3];
    // }

    private static int createStartTime(String species) {
        return AnimalTypes.valueOf(species.toUpperCase()).getTime()[3];
    }
}
