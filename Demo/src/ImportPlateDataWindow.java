package src;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ImportPlateDataWindow extends JFrame {
    
    String filePath;
    private String fileName;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem openButton = new JMenuItem("Open");

    JPanel panel = new JPanel();
    JLabel label = new JLabel("Plate Data File: ");
    JTextField tf = new JTextField(50); // shows 50 characters before scrolling
    JButton send = new JButton("Generate Reports");

    private DerbyDao dao = new DerbyDao();
    public ArrayList<Report> reportList = new ArrayList<Report>();
    ArrayList <String> animalIDList;
    static int animalListCount;
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
                fileName = chooser.getSelectedFile().getName();
                String selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
                System.out.println("Selected file path: " + selectedFilePath);
                tf.setText(selectedFilePath);
                setVisible(true);
            }

        });

        
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Float> parsedData = new ArrayList<Float>();
                int dataIndex = 4;
                filePath = tf.getText();
                //get the file from the field
                File file = new File(filePath);
                System.out.println("Generating Report");
                //parse data then generate report
                ParsePlateReaderData parser = new ParsePlateReaderData(file, testID);
                parsedData = parser.parseValues();
                System.out.println("report count: "+ reportList.size());
                //give the report objects the amount of values they need

                animalListCount = 0;
                for (Report n: reportList) {
                    addReport(n);
                    n.setControlValues(parser.getControlValues());
                    for (int i = 0; i < n.getAnimalCount(); i++) {
                        n.addTestResult(parsedData.get(dataIndex));
                        System.out.println("Added: " + parsedData.get(dataIndex));
                        dataIndex++;
                    }
                    //All test results added to a single test, calculate results and add to database
                    n.addFinalAnimals(animalListCount, animalIDList);
                    animalListCount = animalListCount + n.getAnimalCount();
                }

                for (Report r : reportList) {
                    boolean made = printReport(r);
                    //If the report was made successfully then delete the animals from the database
                    //Should remove confusion about which animals belong to which client and which logID
                    if (made) {
                        System.out.println("Generated Report For: " + r.getSingleClient().getCompanyName());
                        dao.removeAnimals(r.getSingleClient().getCompanyName(), r.getLogID(), r.getAnimalType());
                    } else {
                        System.err.println("Report not generated for " + r.getSingleClient().getCompanyName());
                    }
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

    private void addReport(Report report) {
        DerbyDao dao = new DerbyDao();

        //TODO: Might need to remove all primary key and give it it's own report id that auto increments
        dao.addReport(report.getLogID(), report.getAnimalType(), report.getReceived(), report.getTested(), fileName, report.getSingleClient().getCompanyName());
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


    private boolean printReport(Report report) {
        String clientName = report.getSingleClient().getCompanyName();
        final File outputFilename = new File(clientName + "Report" + ".pdf");
        boolean made = false;
        // Generate the report
        try {
            new ReportGenerator(report).generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);
            made = true;

        } catch (IOException e) {
            e.printStackTrace();
            made = false;
        } catch (ReportProcessingException e) {
            e.printStackTrace();
            made = false;
        }

        // Output the location of the file
        //JOptionPane.showMessageDialog(null, "Generated report for " + clientName);
        return made;
    }


}