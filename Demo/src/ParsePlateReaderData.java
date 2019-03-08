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
    private int testType;

    public ParsePlateReaderData(File report, int testType) {
        this.testType = testType;
        try {
            scanner = new Scanner(report);
            scanner.useDelimiter(",");
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }

        testValues = new ArrayList<Float>();

    }

    public ArrayList<Float> getControlValues() {
        ArrayList<Float> controlValues = new ArrayList<Float>();
        for (int i = 0; i < 4; i++) {
            controlValues.add(testValues.get(i));
        }

        return controlValues;
    }



    /**
     *
     * @return arrayList containing parsed values
     * grabs all of the floats out of the file we're parsing
     */
    public ArrayList <Float> parseValues() {

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
     *  and we need them to be parsed down a column then across a row while omitting test values from the final array)
     */
    private void arrangeValues() {


        ArrayList <Float> arrangedValues = new ArrayList<Float>();
        int blank = -1;
        int neg1 = -1;
        int neg2 = -1;
        int pos1 = -1;
        int pos2 = -1;

        switch (testType) {
            case 1:
                //BLV test
                neg1 = 25;
                neg2 = 32;
                pos1 = 37;
                pos2 = 44;
                break;
            case 2:
                //cl test
                blank = 0;
                neg1 = 3;
                neg2 = 12;
                pos1 = 15;
                pos2 = 24;
                break;
            case 3:
                //Johne's test
                neg1 = 24;
                neg2 = 36;
                pos1 = 0;
                pos2 = 12;
                break;

        }
        
        //get the rest of the values in the correct order
        //36 should be first test
        //48 should be second test
        int rowLength = 12;
        int colHeight = 8;

        setControlValues(arrangedValues);
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
    private void setControlValues(ArrayList <Float> arrangedValues) {

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
