import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
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

    ArrayList <String> animalIDList;

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
                filePath = tf.getText();


                //get the file from the field
                File file = new File(filePath);

                //parse data then generate report
                ParsePlateReaderData parser = new ParsePlateReaderData(file);

                GenerateReport report = new GenerateReport(parser.parseValues(), animalIDList);
                
                parser.printValues();
                report.printToFile();
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
}