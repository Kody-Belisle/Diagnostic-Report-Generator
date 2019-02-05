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

                System.out.println("    urrent dir = " + dir);


                TestSelectWindow testWindow = new TestSelectWindow();
                testWindow.setVisible(true);
            }
        });

    }
}