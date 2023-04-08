/**
* @author  Rutvi
* @since   2023-04-07
*/
package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

public class HourTest {
    /**
     * Checking Hour constructor, if 0<= hour >=23.
     */
    @Test 
    public void testHour() { 
        Hour actualHour = new Hour(2);
        assertEquals("Hour value is incorrect", 2, actualHour.getHour());
    }

    /**
     * Checking Hour constructor, if the hour<0 or hour>23 it should throw IllegalArgumentException.
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
     * The following test checks the whether the updateTimeAvailable method is working properly in Hour.java
    */
    @Test
    public void testupdateTimeAvailable() {

        int timetaken = 50; // change this to anything between 0-60 to test updateTimeAvailable() function.
        Hour testHour = new Hour(2);

        int expectedtimeavailable = testHour.getTimeAvailable() - timetaken; // finding the expected time available
        
        testHour.updateTimeAvailable(timetaken); // sets the new value for timeavailable 
        int testtimeAvailable = testHour.getTimeAvailable(); // gets the new timeavailable after the updatetimeavailable has been passed
        

        assertEquals("The updateTimeAvailable() is not working as intended", expectedtimeavailable, testtimeAvailable);
    }




}
