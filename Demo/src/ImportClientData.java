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

                //Client data setup
                String companyName = null;
                String clientName = null;
                String address = null;
                String city = null;
                String state = null;
                String zip = null;
                String email = null;
                String phone = null;

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
                        Client client = new Client(companyName, clientName, address, city, state, zip, email, phone, null);
                        DerbyDao dao = new DerbyDao();
                        dao.addClient(client);
                        //reset client data
                        companyName = null;
                        clientName = null;
                        address = null;
                        city = null;
                        state = null;
                        zip = null;
                        email = null;
                        phone = null;
                    }
                    switch (i) {

                        case 0:
                            System.out.println("Company Name: \t" + token);
                            companyName = token;
                            break;
                        case 1:
                            System.out.println("Name: \t" + token);
                            clientName = token;
                            break;
                        case 2:
                            System.out.println("Address: \t" + token);
                            address = token;
                            break;
                        case 3:
                            System.out.println("City: \t" + token);
                            city = token;
                            break;
                        case 4:
                            System.out.println("State: \t" + token);
                            state = token;
                            break;
                        case 5:
                            System.out.println("Zip: \t" + token);
                            zip = token;
                            break;
                        case 6:
                            System.out.println("Email address: \t" + token);
                            email = token;
                            break;
                        case 7:
                            System.out.println("Phone: \t" + token);
                            phone = token;
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