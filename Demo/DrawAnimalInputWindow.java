import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DrawAnimalInputWindow extends JFrame {

    GridLayout layout = new GridLayout(0,2);
    GridLayout layoutPlates = new GridLayout(12,8);
    JPanel panel = new JPanel();

    
    public DrawAnimalInputWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        
        
        //draw north side buttons
        panel.setLayout(layout);
        panel.add(new JTextField("Company Name"));
        panel.add(new JTextField("LOGID"));
        panel.add(new JTextField("Address"));
        panel.add(new JTextField("Date"));
        
        //draw wells
        
        for (int i = 0; i < 12; i++) {
            JLabel wellImage = new JLabel();
            wellImage.setIcon(new ImageIcon("well.png"));
            panel.add(wellImage);
        }



        //draw screen

        getContentPane().add(BorderLayout.NORTH, panel);
        setVisible(true);

    }

}