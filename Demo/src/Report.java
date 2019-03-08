package src;

import java.util.ArrayList;

/**
 * stores all necessary report data and performs calculations on it
 */
public class Report {
        ArrayList<PlateTest> plateTests;
        static ArrayList<Float> controlValues;
        private Client singleClient;
        private String dateReceived;
        private String dateTested;

        int testType;
        int animalCount;
        
        String logID;
        static ArrayList<Float> testResults;
        private static  ArrayList<String> calculatedResult = new ArrayList<String>();
        
        public Report(Client client, String dateReceived, String dateTested, int animalCount, int testType, String logID) {
            this.singleClient = client;
            this.dateTested = dateTested;
            this.animalCount = animalCount;
            this.testType = testType;
            this.logID = logID;
            dateReceived = parseLogID();
            testResults = new ArrayList<Float>();
        }

        /*
         *  Method to calculate text results: Positive, Negative, Neutral
         *  After calculated, it adds animals to the database tied to the report and the client name
         */
        public int addFinalAnimals(){
            for (int i = 0; i < animalCount; i++) {
                calculateResults(i);
                System.out.println("Added: " + calculatedResult.get(i));


            }

            //TODO: Actually calculate the results and add to database.
            return 0;
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

        public Client getSingleClient() {
            return singleClient;
        }

        public String getReceived(){
            return dateReceived;
        }

        public String getTested(){
            return dateTested;
        }

        public String getLogID() {
            return logID;
        }

        public String parseLogID() {
            //Log format: O10419004
            //is Date Received – 1/4/2019
            //O followed by M or MM (need two cases) followed by DD followed by YY followed by XXX TestNumber
            //OMDDYYXXX or OMMDDYYXXX

            char[] log =  logID.toCharArray();
            if (log.length == 9) {
                //one digit month
            }
            return "";
        }
        public String getTestType() {
            if (testType == 1) {
                return "CAE";
            }
            if (testType == 2) {
                return "CL";
            }
            if (testType == 3) {
                return "Johne's";
            }

            return null;
        }


        /**
         * 
         * @param index
         * uses calculations from provided excel sheet and puts results in array (should be a map at some point)
         */
        public static void calculateResults(int index) {
            System.out.println("Calculating testResult: " + testResults.get(index));

            //debug
            for (Float e: controlValues) {
                System.out.println(e);
            }


            Float negativeControlOne = controlValues.get(0);
            Float negativeControlTwo = controlValues.get(1);
    
            Float positiveControlOne = controlValues.get(2);
            Float positiveControlTwo = controlValues.get(3);
    
            Float testResult = testResults.get(index);
    
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

        public void setControlValues(ArrayList<Float> controlValues) {
            this.controlValues = controlValues;
        }
}
