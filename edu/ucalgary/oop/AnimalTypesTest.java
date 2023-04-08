/**
* @author  Rutvi
* @since   2023-04-07
*/
package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class AnimalTypesTest {
    /**
    * The Following test is to test whether Coyote's feedingPrepTime, feedingTime, cleaningTime, starttime is correctly written.
    */

    @Test
    public void testCoyoteTime() {
        AnimalTypes animalType = AnimalTypes.COYOTE;
        int[] expectedTime = {10, 5, 5, 19};
        assertArrayEquals(expectedTime, animalType.getTime());
    }

    /**
    * The Following test is to test whether Fox's feedingPrepTime, feedingTime, cleaningTime, starttime is correctly written.
    */

    @Test
    public void testFoxTime() {
        AnimalTypes animalType = AnimalTypes.FOX;
        int[] expectedTime = {5, 5, 5, 0};
        assertArrayEquals(expectedTime, animalType.getTime());
    }

    /**
    * The Following test is to test whether Porcupine's feedingPrepTime, feedingTime, cleaningTime, starttime is correctly written.
    */

    @Test
    public void testPorcupineTime() {
        AnimalTypes animalType = AnimalTypes.PORCUPINE;
        int[] expectedTime = {0, 5, 10, 19};
        assertArrayEquals(expectedTime, animalType.getTime());
    }

    /**
    * The Following test is to test whether Beaver's feedingPrepTime, feedingTime, cleaningTime, starttime is correctly written.
    */

    @Test
    public void testBeaverTime() {
        AnimalTypes animalType = AnimalTypes.BEAVER;
        int[] expectedTime = {0, 5, 5, 8};
        assertArrayEquals(expectedTime, animalType.getTime());
    }

    /**
    * The Following test is to test whether Raccoon's feedingPrepTime, feedingTime, cleaningTime, starttime is correctly written.
    */

    @Test
    public void testRaccoonTime() {
        AnimalTypes animalType = AnimalTypes.RACCOON;
        int[] expectedTime = {0, 5, 5, 0};
        assertArrayEquals(expectedTime, animalType.getTime());
    }

    
    
}
