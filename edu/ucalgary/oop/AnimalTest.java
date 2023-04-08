/**
* @author  Rutvi
* @since   2023-04-07
*/
package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class AnimalTest {
    /**
     * The following tests check the getter methods of Animal class.
     * These tests check if the getters return correct values for the
     * Animal's: Nickname, Id and Species.
     * The constructor is not tested as the getters passing these tests indicates
     * that the constructor is functioning as expected.
     */
    @Test
    public void testAnimalGetters() { // Test Animal Id, Nickname and respected species
        Animal animal2 = new Animal ("Biter", 2, "coyote");

        int actualAnimalId = animal2.getAnimalID();
        int expectedAnimalId = 2;
        assertEquals("getAnimalID for Class 'Animal' does not work", expectedAnimalId, actualAnimalId);

        String actualNickname = animal2.getNickname();
        String expectedNickname = "Biter";
        assertEquals("getNickname for Class 'Animal' does not work", expectedNickname, actualNickname);

        String actualSpecies = animal2.getSpecies();
        String expectedSpecies = "coyote";
        assertEquals("getSpecies for Class 'Animal' does not work", expectedSpecies, actualSpecies);
    }

}