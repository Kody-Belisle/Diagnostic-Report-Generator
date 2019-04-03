package src;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateSerializer {
    private final String filename = "state.ser";
    private ArrayList<Report> reports;
    private javax.swing.JTable currentMap;

    public static void main(String[] args) {
        Client testClient = new Client("Serialize Test", "John Doe", "123 Main St", "Boise", "ID", "83705", "", "", "");
        Report object = new Report(testClient, "Sheep", "2019-04-03", 3, 2, "O40319003");

        String[] names = {"Bob", "Sally", "Dolly", "Jane", "Chuck"};
        List<String> animalList = Arrays.asList(names);

        ArrayList<String> animalIDs = new ArrayList<String>(animalList);
    }

    public void deserialize() {

        javax.swing.JTable table = null;
        ArrayList<Report> readList = null;
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            readList = (ArrayList<Report>) in.readObject();
            table = (javax.swing.JTable) in.readObject();

            in.close();
            file.close();

            for (Report report: readList) {
                System.out.println("Object has been deserialized.");
                System.out.println("Client = " + report.getSingleClient().getCompanyName());
                System.out.println("Test Type = " + report.getTestType());
                System.out.println("Date Received = " + report.getReceived());
                System.out.println("Date Tested = " + report.getTested());
                System.out.println("Animal Type = " + report.getAnimalType());
                System.out.println("Log ID = " + report.getLogID());
            }
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    public void serialize() {
        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(reports);
            out.writeObject(currentMap);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    public void setReports(ArrayList<Report> reportList) {
        this.reports = reportList;
    }

    public void setCurrentMap(javax.swing.JTable wellMap) {
        this.currentMap = wellMap;
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public javax.swing.JTable getCurrentMap() {
        return currentMap;
    }
}