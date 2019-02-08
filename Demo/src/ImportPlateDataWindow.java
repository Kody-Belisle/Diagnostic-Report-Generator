package src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImportPlateDataWindow extends JFrame {
    
    String filePath;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem openButton = new JMenuItem("Open");

    JPanel panel = new JPanel();
    JLabel label = new JLabel("Plate Data File: ");
    JTextField tf = new JTextField(50); // shows 50 characters before scrolling
    JButton send = new JButton("Generate Reports");

    public ArrayList<Report> reportList = new ArrayList<Report>();
    ArrayList <String> animalIDList;
    int testID;

    public ImportPlateDataWindow() {
        setJMenuBar(menuBar);
        setVisible(true);
        setSize(400, 200);
        menuBar.add(menu);
        menu.add(openButton);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory( new File (System.getProperty("user.dir")));
                chooser.showOpenDialog(null);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                String selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
                System.out.println("Selected file path: " + selectedFilePath);
                tf.setText(selectedFilePath);
                setVisible(true);
            }

        });

        
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Float> parsedData = new ArrayList<Float>();
                int dataIndex = 5;  //first 5 are test values
                filePath = tf.getText();
                //get the file from the field
                File file = new File(filePath);
                System.out.println("Generating Report");
                //parse data then generate report
                ParsePlateReaderData parser = new ParsePlateReaderData(file);
                parsedData = parser.parseValues(2);
                System.out.println("report count: "+ reportList.size());
                //give the report objects the amount of values they need
                for (Report n: reportList) {
                    for (int i = 0; i < n.getAnimalCount(); i++) {
                        n.addTestResult(parsedData.get(dataIndex));
                        System.out.println("Added: " + parsedData.get(dataIndex));
                    }
                }

                for (Report r : reportList) {
                    generateReport(r);
                    System.out.println("Generated Report");
                }

                setVisible(false);
            }

        });

        panel.add(label);
        panel.add(tf);
        panel.add(send);

        getContentPane().add(BorderLayout.CENTER, panel);
        setVisible(true);
    }


    public void setAnimalIDList(ArrayList<String> animalIDList){

        this.animalIDList = animalIDList;

    }

    public void setReportList(ArrayList<Report> reportList){

        this.reportList = reportList;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public void generateReport(Report report) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH-MM");
            Date date = new Date();
            String clientName = report.getName();
            BufferedWriter writer = new BufferedWriter(new FileWriter("Diagnostic-Report-Generator/" + "Generated Reports/" + clientName + " " + dateFormat.format(date) + ".txt"));


    
            switch (testID) {


                case 1:

                case 2:

                case 3:

            }

            
            
            writer.write("Submitted by: " + report.getName());




            System.out.println("Wrote to file");
            writer.close();
        } catch (IOException e) {

            System.out.println("IO exception");
            System.exit(0);
        }
    }
}