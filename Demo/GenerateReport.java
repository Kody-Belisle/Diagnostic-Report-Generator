import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GenerateReport {
        ArrayList<PlateTest> plateTests;

        public GenerateReport(ArrayList <Float> testValues, ArrayList <String> animalIDList) {

            plateTests = generateResults(testValues, animalIDList);

        }


        public void printToConsole() {
        
            for (PlateTest e : plateTests) {

                System.out.println(e.toString() + "\n");
            }

        }

        public void printToFile() {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("Generated_Report.txt"));
                for (PlateTest e : plateTests) {
    
                   writer.write(e.toString() + "\r\n");
                }
                System.out.println("Wrote to file");
                writer.close();
            } catch (Exception e) {

                System.out.println("Write error");
                System.exit(0);
            }

        }

        public static ArrayList <PlateTest> generateResults(ArrayList <Float> testValues, ArrayList <String> animalIDList) {

            int NONTESTVALUES = 4;
            int iterateCount = testValues.size() - NONTESTVALUES;
            //minus 4 is for the non test values: pos1, pos2, neg1, neg2
            ArrayList <PlateTest> plateTests = new ArrayList <PlateTest>();
    
            //error handling
            if (testValues.size() - NONTESTVALUES != animalIDList.size()) {
    
                System.out.println("values table / animalIDList size mismatch");
                System.out.println("test count = " + (testValues.size() - NONTESTVALUES) );
                System.out.println("animal id count = " + animalIDList.size());
    
                //if animal list is smaller we need to make that many test objects
                //TODO: needs more thought on how to handle
                if (animalIDList.size() < testValues.size()) {
                    iterateCount = animalIDList.size();
                }
            }
    
            for (int i = 0; i < iterateCount; i++) {
    
                PlateTest test = new PlateTest(animalIDList.get(i), testValues.get(i + NONTESTVALUES), calculateResult(i + NONTESTVALUES, testValues));
                plateTests.add(test);
            }
            
            
            return plateTests;
        }


        public static int calculateResult(int index, ArrayList <Float> testValues) {

            Float negativeControlOne = testValues.get(0);
            Float negativeControlTwo = testValues.get(1);
    
            Float positiveControlOne = testValues.get(2);
            Float positiveControlTwo = testValues.get(3);
    
            Float testResult = testValues.get(index);
    
            Float negAvg = (negativeControlOne + negativeControlTwo)/2;
            Float posAvg = (positiveControlTwo + positiveControlOne)/2;
    
            //test negative
            if (testResult < (((posAvg - negAvg) / 4)) + negAvg) {
    
                return -1;
    
            } else if (testResult > (((posAvg - negAvg) / 4)) + negAvg && testResult < ((((posAvg - negAvg) / 4)) + negAvg) + 0.3 ) {
    
                return 0;
            } else if (testResult > (((posAvg - negAvg) / 4)) + negAvg) {
    
                return 1;
            }
            
            return -9;
    
        }
}
