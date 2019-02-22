package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * takes a plate reader file, parses, then arranges it properly for array storage
 */
public class ParsePlateReaderData {

    private Scanner scanner;
    private int valueCount = 0;
    private int garbageValueCount = 13;
    private static ArrayList <Float> testValues;

    public ParsePlateReaderData(File report) {

        try {
            scanner = new Scanner(report);
            scanner.useDelimiter(",");
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }

        testValues = new ArrayList<Float>();

    }



    /**
     * 
     * @param testID
     * @return arrayList containing parsed values
     * grabs all of the floats out of the file we're parsing
     */
    public ArrayList <Float> parseValues(int testID) {

        //Get all tokens and store them in the arrayList
        while (scanner.hasNext()) {

            valueCount++;

            if (valueCount <= garbageValueCount) {
                scanner.next();
                continue;
            }

            String value = scanner.next();

            try{
                Float.parseFloat(value);
                testValues.add(Float.parseFloat(value));
            }catch(NumberFormatException e){
                //not float so do nothing
            }

        }
    
        scanner.close();

        arrangeValues();

        return testValues;

    }
    
    /**
     * properly arranges values from the input file (initially they are parsed along a row then down a column
     *  and we need them to be parsed down a column then across a row)
     */
    private void arrangeValues() {


        ArrayList <Float> arrangedValues = new ArrayList<Float>();

        /*      
        
                EXAMPLE DATA NOTES

            Element 0 is labeled as BLANK

            Element 3 is negative control one

            Element 12 is negative control two

            Element 15 is positive control one

            Element 24 is positive control two

        */

        //positions hardcoded for now
        int blank = 0;
        int neg1 = 3;
        int neg2 = 12;
        int pos1 = 15;
        int pos2 = 24;
        
        //get the rest of the values in the correct order
        //36 should be first test
        //48 should be second test
        int rowLength = 12;
        int colHeight = 8;
        
        getControlValues(arrangedValues);
        //list elements 0-3 are now: 
        //neg1, neg2, pos1, pos2

        for (int j = 0; j < rowLength; j++) {

                for (int i = 0; i < colHeight; i++ ) {

                    int element = (rowLength * i) + j;
                    //don't wanna add the test elements
                    if (element != blank && element != neg1 && element != neg2 && element != pos1 && element != pos2) {
                        arrangedValues.add(testValues.get(element));
                    }
                }
                

        }

        testValues = arrangedValues;
    }

    /**
     * grabs the control values from our parsed list and puts them at the beginning of our new, ordered list
     */
    private void getControlValues(ArrayList <Float> arrangedValues) {

            /*      
        
                EXAMPLE DATA NOTES

            Element 0 is labeled as BLANK

            Element 3 is negative control one

            Element 12 is negative control two

            Element 15 is positive control one

            Element 24 is positive control two

        */

        //not adding BLANK

        arrangedValues.add(testValues.get(3));
        System.out.println("negative control one: " + arrangedValues.get(0));
        arrangedValues.add(testValues.get(12));
        System.out.println("negative control two " + arrangedValues.get(1));
        arrangedValues.add(testValues.get(15));
        System.out.println("positive control one: " + arrangedValues.get(2));
        arrangedValues.add(testValues.get(24));
        System.out.println("positive control two: " + arrangedValues.get(3));

        //list elements 0-3 are now: 
        //neg1, neg2, pos1, pos2


    }

}
