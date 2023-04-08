/**
* @author  Rutvi
* @since   2023-04-07
*/
package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class TreatmentTest {
    /**
     * The following tests check the getter methods of Treatment class.
     * These tests check if the getters return correct values for the
     * TreatmentId and AnimalId.
     * The constructor is not tested as the getters passing these tests indicates
     * that the constructor is functioning as expected.
     */
    @Test
    public void testTreatmentGetters() { // Treatment Id and AnimalId
        Treatment treatment1 = new Treatment ("Flush neck wound",25,1,6,5,8);

        int actualAnimalId = treatment1.getAnimalID();
        int expectedAnimalId = 8;
        assertEquals("getAnimalID for Class 'Treatment' does not work", expectedAnimalId, actualAnimalId);

        int actualTreatmentId = treatment1.getTreatmentID();
        int expectedTreatmentId = 5;
        assertEquals("getNickname for Class 'Treatment' does not work", expectedTreatmentId, actualTreatmentId);
    }

    
    
}
