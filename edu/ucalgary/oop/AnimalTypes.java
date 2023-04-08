package edu.ucalgary.oop;


public enum AnimalTypes{
    
    COYOTE{
        public int[] getTime() {
            int[] returnArray = {10, 5, 5, 19}; //feedingPrepTime, feedingTime, cleaningTime, starttime
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
    
    public abstract int[] getTime();
    
}

