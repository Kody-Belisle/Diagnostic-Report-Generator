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
    TestType test;

    public ParsePlateReaderData(File report, int testType) {
        this.testType = testType;
        try {
            scanner = new Scanner(report);
            scanner.useDelimiter(",|\\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }

        testValues = new ArrayList<Float>();

        switch (testType) {
            case 1:
                //BLV test
                test = new BLVTest();
                break;
            case 2:
                //cl test
                test = new CLTest();
                break;
            case 3:
                //Johne's test
                test = new JohnesTest();
                break;

            default:
                test = new CLTest();
        }

    }

    public ParsePlateReaderData(int testType) {
        this.testType = testType;

        switch (testType) {
            case 1:
                //BLV test
                test = new BLVTest();
                break;
            case 2:
                //cl test
                test = new CLTest();
                break;
            case 3:
                //Johne's test
                test = new JohnesTest();
                break;

            default:
                test = new CLTest();
        }
    }


    public ArrayList<Float> getControlValues() {
        ArrayList<Float> controlValues = new ArrayList<Float>();
        for (int i = 0; i < 4; i++) {
            controlValues.add(testValues.get(i));
        }

        return controlValues;
    }

    public ArrayList<Float> getUnorderedControlValues() {
        ArrayList<Float> controlValues = new ArrayList<>();
        setControlValues(controlValues);
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

            /*
            if (valueCount <= garbageValueCount) {
                scanner.next();
                continue;
            }*/

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

    public ArrayList <Float> parseValuesWithoutArranging() {

        String UTF8_BOM = "\uFEFF";

        //Get all tokens and store them in the arrayList
        while (scanner.hasNext()) {

            valueCount++;

            String value = scanner.next();
            //Handle BOM character
            //"Read: value" is caught and ignored
            try {
                if (value.startsWith(UTF8_BOM)) {
                    value = value.substring(1);
                    Float.parseFloat(value);
                    testValues.add(Float.parseFloat(value));
                } else if (value.startsWith("R")) {
                    continue;
                }else {
                    Float.parseFloat(value);
                    testValues.add(Float.parseFloat(value));
                }
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

        }

        scanner.close();

        return testValues;
    }
    
    /**
     * properly arranges values from the input file (initially they are parsed along a row then down a column
     *  and we need them to be parsed down a column then across a row while omitting test values from the final array)
     */
    public ArrayList<Float> arrangeValues() {


        ArrayList <Float> arrangedValues = new ArrayList<Float>();
        int blank = -1;
        int neg1 = -1;
        int neg2 = -1;
        int pos1 = -1;
        int pos2 = -1;


        pos1 = test.getPos1();
        pos2 = test.getPos2();
        neg1 = test.getNeg1();
        neg2 = test.getNeg2();
        blank = test.getBlank();
        //get the rest of the values in the correct order


        setControlValues(arrangedValues);
        //list elements 0-3 are now:
        //neg1, neg2, pos1, pos2

        int rowLength = 12;
        int colHeight = 8;
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
        return testValues;
    }


    /**
     * grabs the control values from our parsed list and puts them at the beginning of our new, ordered list
     */
    private void setControlValues(ArrayList <Float> arrangedValues) {

        arrangedValues.add(testValues.get(test.getNeg1()));
        System.out.println("negative control one: " + arrangedValues.get(0));

        arrangedValues.add(testValues.get(test.getNeg2()));
        System.out.println("negative control two " + arrangedValues.get(1));

        arrangedValues.add(testValues.get(test.getPos1()));
        System.out.println("positive control one: " + arrangedValues.get(2));

        arrangedValues.add(testValues.get(test.getPos2()));
        System.out.println("positive control two: " + arrangedValues.get(3));

        //list elements 0-3 are now: 
        //neg1, neg2, pos1, pos2

    }

}
