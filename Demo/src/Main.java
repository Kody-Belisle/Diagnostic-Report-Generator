package src;

public class Main

{
    public static void main(String[] args)
    {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final String dir = System.getProperty("user.dir");

                //Start Derby Driver
                //new DerbyDriver().go(args);
                //System.out.println("DerbyDriver finished");

                System.out.println("current dir = " + dir);


                new InputWindow().setVisible(true);
            }
        });

    }
}