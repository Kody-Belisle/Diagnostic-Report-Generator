package src;

public class Main

{
    public static void main(String[] args)
    {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final String dir = System.getProperty("user.dir");

                System.out.println("current dir = " + dir);


                new InputWindow().setVisible(true);
            }
        });

    }
}