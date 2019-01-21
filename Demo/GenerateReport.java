import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GenerateReport {
        ArrayList<PlateTest> plateTests;

        public GenerateReport(ArrayList<PlateTest> plateTests) {

            this.plateTests = plateTests;

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

                writer.close();
            } catch (Exception e) {

                System.out.println("Write error");
                System.exit(0);
            }

        }
}