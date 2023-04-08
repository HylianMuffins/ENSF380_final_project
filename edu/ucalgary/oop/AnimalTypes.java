package edu.ucalgary.oop;

/**
* The AnimalTypes enumeration; used to get info about timing for each species.
*
* @author  Sudarshan Naicker, Rutvi Brahmbhatt
* @version 1.3
* @since   2023-03-31
*/
public enum AnimalTypes{
    
    COYOTE{
        public int[] getTime() {
            //feedingPrepTime, feedingTime, cleaningTime, feedingStartTime
            int[] returnArray = {10, 5, 5, 19};
            return returnArray;
        }
    }, 
    FOX{
        public int[] getTime() {
        int[] returnArray = {5, 5, 5, 0};
        return returnArray;
    }}, 

    PORCUPINE{
        public int[] getTime() {
            int[] returnArray = {0, 5, 10, 19};
            return returnArray;
        }
    }, 

    BEAVER{
        public int[] getTime() {
            int[] returnArray = {0, 5, 5, 8};
            return returnArray;
        }
    },

    RACOON{
        public int[] getTime() {
            int[] returnArray = {0, 5, 5, 0};
            return returnArray;
        }
    };
    
    /**
    * Abstract Getter method that returns an array of integers representing 
    * feeding prep time, feeding time, cleaning time, and feeding start time, 
    * respectively. Return array is specific to each enumeration and in return,
    * to each species.
    * @return array of timing parameters.
    */
    public abstract int[] getTime();
    
}

