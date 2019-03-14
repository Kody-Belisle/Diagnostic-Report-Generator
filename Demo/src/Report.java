package src;

import java.lang.reflect.Array;
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
        private String animalType;

        int testType;
        int animalCount;
        
        String logID;
        static ArrayList<Float> testResults;
        private static ArrayList<String> calculatedResult = new ArrayList<String>();
        
        public Report(Client client, String animalType, String dateTested, int animalCount, int testType, String logID) {
            this.singleClient = client;
            this.animalType = animalType;
            this.dateTested = dateTested;
            this.animalCount = animalCount;
            this.testType = testType;
            this.logID = logID;
            this.dateReceived = parseLogID();
            testResults = new ArrayList<Float>();
        }

        /*
         *  Method to calculate text results: Positive, Negative, Neutral
         *  After calculated, it adds animals to the database tied to the report and the client name
         */
        public int addFinalAnimals(ArrayList<String> animalIds){
            ArrayList<String> animals = animalIds;
            //DerbyDao dao = new DerbyDao();

            for (int i = 0; i < animalCount; i++) {
                calculateResults(i);
                System.out.println("Added: " + calculatedResult.get(i));
                //TODO: animal List includes test values and blanks. Possible idea: Add method
                // in InputWindow that would parse only the populated animal names and pass that
                // through. Animal count should be enough to track this accurately
                //dao.addAnimal(animals.get(i), animalType, (double)testResults.get(i), calculatedResult.get(i), singleClient.getCompanyName(), logID);
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

        public String getAnimalType() {
            return animalType;
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
            StringBuilder outputString = new StringBuilder();
            char[] log =  logID.toCharArray();

            if (log.length == 9) {
                //one digit month
                for (int i = 1; i <= 5; i++) {
                    if(i%2 == 0) {
                        outputString.append("/");
                        outputString.append(log[i]);
                    } else {
                        outputString.append(log[i]);
                    }
                }

            } else if (log.length == 10) {
                //two digit month
                for (int i = 1; i <= 6; i++) {
                    if( i % 2 == 0) {
                        outputString.append(log[i]);
                        outputString.append("/");
                    } else {
                        outputString.append(log[i]);
                    }
                }
            } else {
                System.out.println("Unable to parse LOGID");
            }
            return outputString.toString();
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
    
            Float testResult = testResults.get(index);

            Float marginalCutoff = getMargins();
            //test negative
            if (testResult < marginalCutoff) {
    
                calculatedResult.add("NEGATIVE");
    
            } else if (testResult > marginalCutoff && testResult < (marginalCutoff + 0.3) ) {
    
                calculatedResult.add("MARGINAL");
            } else if (testResult > marginalCutoff) {
    
                calculatedResult.add("POSITVE");
            }
    
        }

        public static Float getMargins() {
            Float negativeControlOne = controlValues.get(0);
            Float negativeControlTwo = controlValues.get(1);

            Float positiveControlOne = controlValues.get(2);
            Float positiveControlTwo = controlValues.get(3);

            Float negAvg = (negativeControlOne + negativeControlTwo)/2;
            Float posAvg = (positiveControlTwo + positiveControlOne)/2;

            Float marginalCutoff = ((posAvg - negAvg)/4) + negAvg;
            return marginalCutoff;
        }

        public void setControlValues(ArrayList<Float> controlValues) {
            this.controlValues = controlValues;
        }

}
