package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

/**
 * The following test class contains tests that check the methods of
 * All the classes.
 * 
 * @author Rutvi Brahmbhatt UCID: 30141734
 * @author Sudarshan Naicker UCID: 30162797
 * @since 2023-04-07
 * Zachariah Blair, Ken Liu, Sudarshan Naicker, Rutvi Brahmbhatt
 */
public class UnitTesting {

    public UnitTesting() {
    };

    /**
     * Testing getters for task.java.
     * 
     * @author Sudarshan Naicker
     */
    @Test
    public void testTaskGetters() {
        Task testtask = new Cleaning("test nickname", "beaver");

        int trialstartTime = testtask.getStartTime();
        assertEquals("getStartTime for Task.java does not work", 0, trialstartTime);

        String trialgetDescription = testtask.getDescription();
        assertEquals("detDescription for Task.java does not work as Intended", "Cleaing - beaver (test nickname)", trialgetDescription);

        int trialgetDuration = testtask.getDuration();
        assertEquals("getDuration for Task.java does not work", 5, trialgetDuration);

        int trialgetMaxWindow = testtask.getMaxWindow();
        assertEquals("GetMaxWindow for Task.java does not work", 24, trialgetMaxWindow);

    }

    /**
     * Testing Setters for tasks.
     * 
     * @author Sudarshan Naicker
     */
    @Test
    public void testTaskSetters() {

        int startTime = 0;
        Task testtask2 = new Cleaning("test nickname", "beaver");
        int expectedStartTime = 0;
        testtask2.setStartTime(startTime); // though we pass an argument of 5 in the above line, setStartTime is called
                                           // here which changed the StartTime to 0.

        assertEquals("setStartTime() is not able to add a value to startTime ", expectedStartTime,
                testtask2.getStartTime());

    }

    /**
     * Testing the Cleaning.java's getters.
     * 
     * @author Sudarshan Naicker
     */
    @Test
    public void testCleaningGetters() {
        Cleaning clean1 = new Cleaning("Boots", "coyote");

        String trialgetspecies = clean1.getSpecies();
        assertEquals("getSpecies for Cleaning.java does not work as intended", "coyote", trialgetspecies);

        String trialgetanimalNickname = clean1.animalNickname();
        assertEquals("animalNickname for Cleaning.java does not work as intended", "Boots", trialgetanimalNickname);

    }

    /**
     * Tesing the getter methods for feeding.java
     * 
     * @author Sudarshan Naicker
     */
    @Test
    public void testFeedingGetters() {

        // Creates 3 animal Objects
        Animal animal1 = new Animal("Loner", 1, "coyote");
        Animal animal2 = new Animal("Slinky", 7, "fox");
        Animal animal3 = new Animal("Gatekeeper", 10, "porcupine");

        // Actual arraylist
        ArrayList<Animal> actualset = new ArrayList<Animal>();
        actualset.add(animal1);
        actualset.add(animal2);
        actualset.add(animal3);

        // Expected arraylist
        ArrayList<Animal> expectedset = new ArrayList<Animal>();
        expectedset.add(animal1);
        expectedset.add(animal2);
        expectedset.add(animal3);

        // Calling feeding class constructor
        Feeding feed1 = new Feeding("fox", actualset);

        // Test the ArrayList<Animal> getHungryAnimals() function in Feeding.java
        ArrayList<Animal> trialhungryanimals = feed1.getHungryAnimals();
        assertEquals(expectedset, trialhungryanimals);

        // Tests the getSpecies() function in Feeding.java
        String actualSpecies = feed1.getSpecies();
        assertEquals("getSpecies() is not working as intended", "fox", actualSpecies);

        // Tests the getQuantity() function in Feeding.java
        int trialGetQuantity = feed1.getQuantity();
        assertEquals("getSpecies() is not working as intended", 3, trialGetQuantity);

    }

    /**
     * Testing the getters in schedule class
     * 
     * @author Rutvi Brahmbhatt
     * @author Sudarshan Naicker
     * 
     * @throws CloneNotSupportedException when hour is not cloneable
     * @throws TimeConflictException      when tasks are impossible to schedule
     */
    @Test
    public void testSchedule() throws CloneNotSupportedException, TimeConflictException {

        // Creates 3 animal Objects
        Animal animal1 = new Animal("Loner", 1, "coyote");
        Animal animal2 = new Animal("Slinky", 7, "fox");
        Animal animal3 = new Animal("Gatekeeper", 10, "porcupine");

        // Actual arraylist animal
        ArrayList<Animal> actualset = new ArrayList<Animal>();
        actualset.add(animal1);
        actualset.add(animal2);
        actualset.add(animal3);

        // Expected arraylist for animal
        ArrayList<Animal> expectedset = new ArrayList<Animal>();
        expectedset.add(animal1);
        expectedset.add(animal2);
        expectedset.add(animal3);

        // Created 3 animal Objects
        Treatment treat1 = new Treatment("Kit feeding", 30, 2, 0, 1, 1, 6);
        Treatment treat2 = new Treatment("Rebandage leg wound", 20, 1, 0, 2, 2, 6);
        Treatment treat3 = new Treatment("Apply burn ointment back", 10, 3, 0, 3, 3, 6);

        // Actual arraylist for treatment
        ArrayList<Task> actualTreat = new ArrayList<Task>();
        actualTreat.add(treat1);
        actualTreat.add(treat2);
        actualTreat.add(treat3);

        // Calling schedule constructor
        Schedule rutvi = new Schedule(actualset, actualTreat);

        /*
         * Testing the getanimals for schedule.java
         * 
         * @author Rutvi Brahmbhatt
         */
        ArrayList<Animal> actualAnimal = rutvi.getAnimals();
        assertEquals("getAnimal() is not working properly", expectedset, actualAnimal);

        /*
         * Testing the gettasks for schedule.java
         * 
         * @author Rutvi Brahmbhatt
         */
        HashMap<String, ArrayList<Task>> actualtask = rutvi.getTasks();
        assertEquals(3, actualtask.get("treatment").size());
        /*
         * Testing the getDate for schedule.java
         * 
         * @author Sudarshan Naicker
         */
        // add the next day here to test getData function()
        LocalDate expectedDate = LocalDate.now().plusDays(1);
        LocalDate actualDate = rutvi.getDate();
        // Tests whether the getDate() function is working as intented
        assertEquals("getDate() is not able to give tommorow's date", expectedDate, actualDate);

        /*
         * Testing the getHourList for schedule.java
         * 
         * @author Sudarshan Naicker
         */
        ArrayList<Hour> actualList = rutvi.getHourList();
        assertEquals("gethourlist() is not working", 24, actualList.size());

    }

    /**
     * Checking Hour constructor, if hour is between 0 and 23 inclusive.
     * 
     * @author Rutvi Brahmbhatt
     */
    @Test
    public void testHour() {
        Hour actualHour = new Hour(2);
        assertEquals("Hour value is incorrect", 2, actualHour.getHour());
    }

    /**
     * Checking Hour constructor, if the houris outside the given range it
     * should throw an IllegalArgumentException.
     * 
     * @author Rutvi Brahmbhatt
     */
    @Test
    public void testIllegalArgumentException() {

        try {
            new Hour(24);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Hour value is < 0 OR Hour value is > 23", ex.getMessage());
        }
    }

    /**
     * The following test checks the whether the updateTimeAvailable method is
     * working properly in Hour.java
     *
     * @author Sudarshan Naicker
     */
    @Test
    public void testupdateTimeAvailable() {

        // change this to anything between 0-60 to test updateTimeAvailable() function.
        int timetaken = 50;
        Hour testHour = new Hour(2);

        // finding the expected time available
        int expectedtimeavailable = testHour.getTimeAvailable() - timetaken;

        // sets the new value for timeavailable
        testHour.updateTimeAvailable(timetaken);
        // gets the new timeavailable after the updatetimeavailable has been passed
        int testtimeAvailable = testHour.getTimeAvailable();

        assertEquals("The updateTimeAvailable() is not working as intended", expectedtimeavailable, testtimeAvailable);
    }

    /**
     * Testing the getter methods for Animal.java
     * 
     * @author Rutvi Brahmbhatt
     */
    // Test Animal Id, Nickname and respected species
    @Test
    public void testAnimalGetters() {
        Animal animal2 = new Animal("Biter", 2, "coyote");

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

    /**
     * Testing the getter methods for Treatment.java
     * 
     * @author Rutvi Brahmbhatt
     */
    // Treatment Id and AnimalId
    @Test
    public void testTreatmentGetters() {
        Treatment treatment1 = new Treatment("Flush neck wound", 25, 1, 6, 2, 5, 8);

        int actualAnimalId = treatment1.getAnimalID();
        int expectedAnimalId = 8;
        assertEquals("getAnimalID for Class 'Treatment' does not work", expectedAnimalId, actualAnimalId);

        int actualTreatmentId = treatment1.getTreatmentID();
        int expectedTreatmentId = 5;
        assertEquals("getNickname for Class 'Treatment' does not work", expectedTreatmentId, actualTreatmentId);
    }

    /**
     * Testing the enumeration, Animaltypes.java
     * 
     * @author Rutvi Brahmbhatt
     */
    @Test
    public void testAnimalTypes() {

        /**
         * The Following test is to test whether Coyote's feedingPrepTime, feedingTime,
         * cleaningTime, starttime is correctly written.
         */

        AnimalTypes animalType1 = AnimalTypes.COYOTE;
        int[] expectedTime1 = { 10, 5, 5, 19 };
        assertArrayEquals(expectedTime1, animalType1.getTime());

        /**
         * The Following test is to test whether Fox's feedingPrepTime, feedingTime,
         * cleaningTime, starttime is correctly written.
         */
        AnimalTypes animalType2 = AnimalTypes.FOX;
        int[] expectedTime2 = { 5, 5, 5, 0 };
        assertArrayEquals(expectedTime2, animalType2.getTime());

        /**
         * The Following test is to test whether Porcupine's feedingPrepTime,
         * feedingTime, cleaningTime, starttime is correctly written.
         */
        AnimalTypes animalType3 = AnimalTypes.PORCUPINE;
        int[] expectedTime3 = { 0, 5, 10, 19 };
        assertArrayEquals(expectedTime3, animalType3.getTime());

        /**
         * The Following test is to test whether Beaver's feedingPrepTime, feedingTime,
         * cleaningTime, starttime is correctly written.
         */
        AnimalTypes animalType4 = AnimalTypes.BEAVER;
        int[] expectedTime4 = { 0, 5, 5, 8 };
        assertArrayEquals(expectedTime4, animalType4.getTime());

        /**
         * The Following test is to test whether Raccoon's feedingPrepTime, feedingTime,
         * cleaningTime, starttime is correctly written.
         */
        AnimalTypes animalType5 = AnimalTypes.RACCOON;
        int[] expectedTime5 = { 0, 5, 5, 0 };
        assertArrayEquals(expectedTime5, animalType5.getTime());

    }

}
