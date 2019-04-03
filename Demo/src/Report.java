package src;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

/**
 * stores all necessary report data and performs calculations on it
 */
public class Report {
        private ArrayList<PlateTest> plateTests;
        private Client singleClient;
        private String dateReceived;
        private String dateTested;
        private String animalType;

        private TestType testObject;
        private int testType;
        private int animalCount;
        
        private String logID;
        private ArrayList<Float> testResults;
        private ArrayList<String> calculatedResult = new ArrayList<String>();
        
        public Report(Client client, String animalType, String dateTested, int animalCount, int testType, String logID) {
            this.singleClient = client;
            this.animalType = animalType;
            this.dateTested = dateTested;
            this.animalCount = animalCount;
            this.testType = testType;
            this.logID = logID;
            this.dateReceived = parseLogID();
            testResults = new ArrayList<Float>();

            switch (testType) {
                case 1:
                    //create BLV object
                case 2:
                    testObject = new CLTest();
                    break;
                case 3:
                    //create Johne's object
            }
        }

        /*
         *  Method to calculate text results: Positive, Negative, Neutral
         *  After calculated, it adds animals to the database tied to the report and the client name
         */
        public int addFinalAnimals(Integer startAt, ArrayList<String> animalIds){
            ArrayList<String> animals = animalIds;
            DerbyDao dao = new DerbyDao();

            for (int i = 0; i < animalCount; i++) {
                calculateResults(i);
                System.out.println("Added: " + calculatedResult.get(i));
                dao.addAnimal(animals.get(startAt + i), animalType, (double)testResults.get(i), calculatedResult.get(i), singleClient.getCompanyName(), logID);
            }

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
            String finalDate = "";

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

            try {
                //Pass the built string into the date formatter to make it possible to add to database
                CustomDateFormatter formatter = new CustomDateFormatter();
                finalDate = formatter.logDate(outputString.toString());

            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            return finalDate;
        }

        public String getTestType() {
            if (testType == 1) {
                return "BLV";
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
        public void calculateResults(int index) {
            System.out.println("Calculating testResult: " + testResults.get(index));
            Float testResult = testResults.get(index);
            calculatedResult.add(testObject.getResult(testResult));
        }



        public void setControlValues(ArrayList<Float> controlValues) {
            testObject.setControlValues(controlValues);
        }

        public void setParameters(Map parameters) {

            testObject.setReportParameters(parameters);

        }

}
