package src;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateSerializer {

    public static void main(String[] args) {
        Client testClient = new Client("Serialize Test", "John Doe", "123 Main St", "Boise", "ID", "83705", "", "", "");
        Report object = new Report(testClient, "Sheep", "2019-04-03", 3, 2, "O40319003");
        String filename = "file.ser";
        String[] names = {"Bob", "Sally", "Dolly", "Jane", "Chuck"};
        List<String> animalList = Arrays.asList(names);

        ArrayList<String> animalIDs = new ArrayList<String>(animalList);

        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);
            out.writeObject(animalIDs);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }


        Report object1 = null;
        ArrayList<String> readList = null;
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            object1 = (Report) in.readObject();
            readList = (ArrayList<String>) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            System.out.println("Client = " + object1.getSingleClient().getCompanyName());
            System.out.println("Test Type = " + object1.getTestType());
            System.out.println("Date Received = " + object1.getReceived());
            System.out.println("Date Tested = " + object1.getTested());
            System.out.println("Animal Type = " + object1.getAnimalType());
            System.out.println("Log ID = " + object1.getLogID());

            for (String name: readList) {
                System.out.println(name);
            }
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }
}