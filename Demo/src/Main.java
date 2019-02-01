package src;
import java.io.File;
import java.util.ArrayList;

import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args)
    {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final String dir = System.getProperty("user.dir");

                //Start Derby Driver
                new DerbyDriver().go(args);
                System.out.println("DerbyDriver finished");
        
                System.out.println("current dir = " + dir);
                TestSelectWindow testWindow = new TestSelectWindow();
                testWindow.setVisible(true);
            }
        });

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


    
    
}