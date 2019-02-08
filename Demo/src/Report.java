package src;

import java.util.ArrayList;

/**
 * stores all necessary report data and performs calculations on it
 */
public class Report {
        ArrayList<PlateTest> plateTests;
        String name;
        String address;
        String city;
        String state;
        String dateReceived;
        String dateTested;
        int animalCount;
        
        String logID;
        ArrayList<Float> testResults;
        private static  ArrayList<String> calculatedResult = new ArrayList<String>();
        
        public Report(String name, String address, String city, String state, String dateReceived, String dateTested, int animalCount) {

            this.name = name;
            this.address = address;
            this.city = city;
            this.state = state;
            this.dateReceived = dateReceived;
            this.dateTested = dateTested;
            this.animalCount = animalCount;

            testResults = new ArrayList<Float>();
        }
        
        /**
         * prints out all the plate tests
         */
        public void printToConsole() {
        
            for (PlateTest e : plateTests) {

                System.out.println(e.toString() + "\n");
            }

        }

        public void addTestResult(Float testResult) {

            testResults.add(testResult);
        }

        public int getAnimalCount() {

            return animalCount;
        }

        public String getName() {

            return name;
        }

        public String getAddress() {

            return address;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String calculateLog() {
            return "";
        }

        public String getReceived(){
            return dateReceived;
        }

        public String getTested(){
            return dateTested;
        }


        /**
         * 
         * @param index
         * @param testValues
         * uses calculations from provided excel sheet and puts results in array (should be a map at some point)
         */
        public static void calculateResults(int index, ArrayList <Float> testValues) {

            Float negativeControlOne = testValues.get(0);
            Float negativeControlTwo = testValues.get(1);
    
            Float positiveControlOne = testValues.get(2);
            Float positiveControlTwo = testValues.get(3);
    
            Float testResult = testValues.get(index);
    
            Float negAvg = (negativeControlOne + negativeControlTwo)/2;
            Float posAvg = (positiveControlTwo + positiveControlOne)/2;
    
            //test negative
            if (testResult < (((posAvg - negAvg) / 4)) + negAvg) {
    
                calculatedResult.add("NEGATIVE");
    
            } else if (testResult > (((posAvg - negAvg) / 4)) + negAvg && testResult < ((((posAvg - negAvg) / 4)) + negAvg) + 0.3 ) {
    
                calculatedResult.add("MARGINAL");
            } else if (testResult > (((posAvg - negAvg) / 4)) + negAvg) {
    
                calculatedResult.add("POSITVE");
            }
    
        }
}
