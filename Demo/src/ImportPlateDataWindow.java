package src;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
                    //All test results added to a single test, calculate results and add to database
                    n.addFinalAnimals();
                }

                for (Report r : reportList) {
                    printReport(r);
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


    protected void setAnimalIDList(ArrayList<String> animalIDList){

        this.animalIDList = animalIDList;

    }

    protected void setReportList(ArrayList<Report> reportList){

        this.reportList = reportList;
        for (Report report: reportList) {
            System.out.println("Client name: " + report.getSingleClient().getCompanyName());
        }
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    private void printReport(Report report) {
        String clientName = report.getSingleClient().getCompanyName();
        final File outputFilename = new File(clientName + "Report" + ".pdf");

        // Generate the report
        try {
            new ReportGenerator(report).generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReportProcessingException e) {
            e.printStackTrace();
        }

        // Output the location of the file
        System.err.println("Generated the report [" + outputFilename.getAbsolutePath() + "]");

    }
}