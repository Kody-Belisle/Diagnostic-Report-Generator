import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class DrawGUI extends JFrame {
    
    String filePath;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem openButton = new JMenuItem("Open");

    JPanel panel = new JPanel();
    JLabel label = new JLabel("Plate Data File: ");
    JTextField tf = new JTextField(50); // shows 50 characters before scrolling
    JButton send = new JButton("Select");

    public DrawGUI() {
        setJMenuBar(menuBar);
        setVisible(true);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                //parse data and bring up animal input window
                filePath = tf.getText();
                InputWindow animalWindow = new InputWindow();
                animalWindow.setVisible(true);
                setVisible(false);
            }

        });

        panel.add(label);
        panel.add(tf);
        panel.add(send);

        getContentPane().add(BorderLayout.CENTER, panel);
        setVisible(true);
    }
}