package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ImportClientData {



    public static void main(String args[]) {
        parseExistingCSV();
    }


    public static void parseExistingCSV() {

        File file = new File("Customer List.csv");
        int i = 0;

        try {
            //get rid of all the headers
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");
            while (scanner.hasNext() && i < 8) {
                //System.out.println(scanner.next());
                i++;
                /*get rid of table headers:
                Company Name
                Name
                Address
                City
                State
                Zip
                Email address
                Phone number
                */
            }

            int j = 0;
            while (scanner.hasNextLine() && j < 3) {

                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                i = 0;
                j++;
                //populate database
                // prints all info for one customer
                System.out.println("Line: " + line + " J: " + j + "\n");
                Boolean flag = false;
                while (lineScanner.hasNext() && !flag) {
                    String token = "";
                    try {
                        token = scanner.next();
                    } catch (NoSuchElementException e) {
                        System.out.println("EOF");
                        flag = true;
                        continue;
                    }
                    //System.out.print("Token: " + token + "\n");
                    if (i == 8) {
                        i = 0;
                    }
                    switch (i) {

                        case 0:
                            System.out.println("Company Name: \t" + token);
                            break;
                        case 1:
                            System.out.println("Name: \t" + token);
                            break;
                        case 2:
                            System.out.println("Address: \t" + token);
                            break;
                        case 3:
                            System.out.println("City: \t" + token);
                            break;
                        case 4:
                            System.out.println("State: \t" + token);
                            break;
                        case 5:
                            System.out.println("Zip: \t" + token);
                            break;
                        case 6:
                            System.out.println("Email address: \t" + token);
                            break;
                        case 7:
                            System.out.println("Phone: \t" + token);
                            break;
                        default:
                            break;

                    }
                    i++;
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }


    }

}