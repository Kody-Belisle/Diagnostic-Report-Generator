import java.io.File;
import java.util.ArrayList;

import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args)
    {
        final String dir = System.getProperty("user.dir");

        //Start Derby Driver
        new DerbyDriver().go(args);
        System.out.println("DerbyDriver finished");

        System.out.println("current dir = " + dir);
    	File file = new File("ExampleReport.csv");
        ParsePlateReaderData parser = new ParsePlateReaderData(file);
        ArrayList <String> animalIDList = new ArrayList <String>();

        animalIDList = getAnimalIDList();
        GenerateReport report = new GenerateReport(generateResults(parser.parseValues(), animalIDList));
        parser.printValues();
        report.printToFile();

        InputWindow animalGUI = new InputWindow();
        DrawGUI gui = new DrawGUI();
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

    public static ArrayList <String> getAnimalIDList() {

        Scanner scanner;
        ArrayList <String> animalIDList = new ArrayList <String>();

        try {
            scanner = new Scanner(new File("FarmManifest.csv"));

            //Get all tokens and store them in the arrayList
            while (scanner.hasNextLine()) {
                String value = scanner.nextLine();
                animalIDList.add(value);
                //System.out.println("Added: " + value);
            }
        
            scanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }

        return animalIDList;

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