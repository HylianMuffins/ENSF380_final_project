//Feeding

package edu.ucalgary.oop;

import java.util.ArrayList;

public class Feeding {
    private String species;
    private ArrayList<Animal> hungryAnimals;

    /*
    Constructor to set species and hungry animals to their respective data fields.
    */
    public Feeding(String species, ArrayList<Animal> hungryAnimals) {
        this.species = species;
        this.hungryAnimals = hungryAnimals;

    }
    
    // Getters
    
    public int getQuantity() {
        return this.quantity;
    }
    
}
