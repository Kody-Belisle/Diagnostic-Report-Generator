package src;

import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.DriverConnectionProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

/*
 * DerbyDao is the data access object for the embedded database.
 * Any connection to the database will be done through here
 *
 */
public class DerbyDao {
    private static String framework = "embedded";
    //private String protocol = "jdbc:derby:C:\\Users\\Hjohn\\IdeaProjects\\Diagnostic-Report-Generator\\lib\\";
    private static Connection conn;
    private static Statement s;
    private static ArrayList<Statement> statements = new ArrayList<Statement>();
    private static ResultSet rs;
    private static boolean made = false;

    public DerbyDao() {
        getConnection();
    }

    public void startUp() {

            String protocol = "";
            System.out.println("Derby starting in " + framework + " mode");
            conn = null;
            //Get current user directory

            protocol = "jdbc:derby:" + System.getProperty("user.dir");

            //String protocol = "jdbc:derby:jar:" + System.getProperty("user.dir");

            //This code here loads the driver for the executable jar
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            try {
                Class.forName(driver);
            } catch(java.lang.ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Properties props = new Properties(); // connection properties
                // providing a user name and password is optional in the embedded
                // and derbyclient frameworks
                //Because user is specified, SCHEMA IS USER1

                props.put("user", "app");
                //props.put("user", "user1");
                props.put("password", "");


                String dbName = "reportDB"; // the name of the database
                //String dbName = "demoDB";

                /*
                 * This connection specifies create=true in the connection URL to
                 * cause the database to be created when connecting for the first
                 * time. To remove the database, remove the directory derbyDB (the
                 * same as the database name) and its contents.
                 *
                 * The directory derbyDB will is created at user.dir which is where
                 * jar will be plus lib folder
                 */


                    conn = DriverManager.getConnection(protocol + "\\lib\\" + dbName
                            + ";create=true", props);
                    /*
                    conn = DriverManager.getConnection(protocol + dbName
                            + ";create=true", props);
                    */
                //conn = DriverManager.getConnection("jdbc:derby:" + dbName, props);

                System.out.println("Connected to and created database " + dbName);

                // We want to control transactions manually. Autocommit is on by
                // default in JDBC.
                conn.setAutoCommit(false);
                made = true;


            } catch (SQLException sqle) {
                printSQLException(sqle);
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
            }

    }

    /*
     *   Only call startUp if it's the first time the DAO is being called.
     *   Otherwise, just return existing connection
     */
    public void getConnection() {
        if(!made) {
            startUp();
        }
    }

    public int addClient(Client clientToAdd) {
        int results = 0;
        try {
            PreparedStatement psInsert = conn.prepareStatement(
                    "insert into client values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statements.add(psInsert);

            psInsert.setString(1, clientToAdd.getCompanyName());
            psInsert.setString(2, clientToAdd.getName());
            psInsert.setString(3, clientToAdd.getPhoneOne());
            psInsert.setString(4, clientToAdd.getPhoneTwo());
            psInsert.setString(5, clientToAdd.getEmail());
            psInsert.setString(6, clientToAdd.getAddress());
            psInsert.setString(7, clientToAdd.getZip());
            psInsert.setString(8, clientToAdd.getCity());
            psInsert.setString(9, clientToAdd.getState());
            results = psInsert.executeUpdate();

            if (results <= 0) {
                System.out.println("New Client not added to database");
            } else {
                System.out.println("Inserted New Client: " + clientToAdd.getCompanyName());
            }

            conn.commit();
            System.out.println("Committed the change");

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        return results;
    }

    public void addAnimal(String animalID, String type, Double result, String textResult, String companyName, String logID) throws SQLException{
        try {
            conn.setAutoCommit(false);
            PreparedStatement psInsert = conn.prepareStatement(
                    "insert into animals values (?, ?, ?, ?, ?, ?)");
            statements.add(psInsert);

            psInsert.setString(1, type);
            psInsert.setDouble(2, result);
            psInsert.setString(3, animalID);
            psInsert.setString(4, textResult);
            psInsert.setString(5, logID);
            psInsert.setString(6, companyName);
            int results = psInsert.executeUpdate();

            if (results <= 0) {
                System.out.println("New Animal not added to database");
            } else {
                System.out.println("Inserted New Animal: " + animalID + " Owned by: " + companyName);
            }

            conn.commit();
            System.out.println("Committed the change");
        } catch (SQLException sqle) {
            conn.rollback();
            printSQLException(sqle);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void removeAnimals(String clientName, String logID, String animalType) {
        try {
            PreparedStatement psInsert = conn.prepareStatement(
                    "delete from animals where client_name = ? and log_id = ? and type = ?");
            statements.add(psInsert);

            psInsert.setString(1, clientName);
            psInsert.setString(2, logID);
            psInsert.setString(3, animalType);

            int results = psInsert.executeUpdate();

            if (results <= 0) {
                System.out.println("Animals were not deleted from database");
            } else {
                System.out.println("Deleted " + results + " Animals");
            }

            conn.commit();
            System.out.println("Committed the change");
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void deleteAllAnimals() throws SQLException{
        try{
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("delete from ANIMALS");
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            printSQLException(e);
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public void addReport(String logID, String type, String dateReceived, String dateTested, String resultFile, String clientName) {
        try {
            PreparedStatement psInsert = conn.prepareStatement(
                    "insert into report (log_id, type, client_name, date_received, date_tested, results_file) values (?, ?, ?, ?, ?, ?)");
            statements.add(psInsert);

            psInsert.setString(1, logID);
            psInsert.setString(2, type);
            psInsert.setString(3, clientName);
            psInsert.setString(4, dateReceived);
            psInsert.setString(5, dateTested);
            psInsert.setString(6, resultFile);
            int results = psInsert.executeUpdate();

            if (results <= 0) {
                System.out.println("New Report not added to database");
            } else {
                System.out.println("Inserted New Report: " + logID + " Generated For: " + clientName);
            }

            conn.commit();
            System.out.println("Committed the change");
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void getClients(ArrayList<String> names) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select client_name from client");
            statements.add(stmt);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String name = rs.getString("client_name");
                names.add(name);
            }

            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

    }

    public Client getOneClient(String name) {
        Client foundClient = null;
        try {

            PreparedStatement stmt = conn.prepareStatement("select * from client where client_name = ?");
            statements.add(stmt);

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs != null) {
                while(rs.next()) {
                    foundClient = new Client(
                            rs.getString("client_name"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("state"),
                            rs.getString("zip"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("second_phone")
                    );
                    return foundClient;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        return foundClient;
    }

    public ArrayList<String[]> backupReport(String logID, String clientName, String animalType) {
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("select DISTINCT REPORT.CLIENT_NAME, ANIMALS.ANIMAL_ID as animalID, ANIMALS.RESULT as OD, ANIMALS.TEXT_RESULT as Result " +
                    "from report join animals on REPORT.LOG_ID = ANIMALS.LOG_ID where REPORT.CLIENT_NAME = ? " +
                    "AND ANIMALS.CLIENT_NAME = ? AND ANIMALS.type = ? AND ANIMALS.LOG_ID = ?");
            stmt.setString(1, clientName);
            stmt.setString(2, clientName);
            stmt.setString(3, animalType);
            stmt.setString(4, logID);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String[] line = new String[3];
                String animalName = rs.getString("animalID");
                float animalOD = rs.getFloat("OD");
                String animalResult = rs.getString("Result");

                lines.add(new String[] {animalName, Float.toString(animalOD), animalResult});
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        return lines;
    }

    public void updateSingleClient(Client client) {
        try {

            PreparedStatement stmt = conn.prepareStatement("update client set client_name = ?, name = ?, " +
                    "phone = ?, second_phone = ?, email = ?, address = ?, zip = ?, city = ?, state = ?" +
                    " where client_name = ?");
            statements.add(stmt);

            stmt.setString(1, client.getCompanyName());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getPhoneOne());
            stmt.setString(4, client.getPhoneTwo());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getAddress());
            stmt.setString(7, client.getZip());
            stmt.setString(8, client.getCity());
            stmt.setString(9, client.getState());
            stmt.setString(10, client.getCompanyName());
            int results = stmt.executeUpdate();

            if (results <= 0) {
                System.out.println("New Client not added to database");
            } else {
                System.out.println("Updated Client: " + client.getCompanyName());
                conn.commit();
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    /*
     * DIFFERENT FROM startUp()
     * Is called from ReportGenerator in order to set up the data factory
     * This method only set up the DriverConnectionProvider
     */
    public DriverConnectionProvider setUpConnProvider() {
        //Get the current directory so that
        //the path is not hardcoded
        String protocol = System.getProperty("user.dir");
        //String dbName = "demoDB";
        String dbName = "reportDB";

        final DriverConnectionProvider singleDriverConnectionProvider = new DriverConnectionProvider();
        singleDriverConnectionProvider.setDriver("org.apache.derby.jdbc.EmbeddedDriver");

        //Set up driver connection specifically for Pentaho
        System.out.println("Full URL:" + "jdbc:derby:" + protocol + "\\lib\\" + dbName + ";create=true");
        singleDriverConnectionProvider.setUrl("jdbc:derby:" + protocol + "\\lib\\" + dbName + ";create=true");
        //singleDriverConnectionProvider.setProperty("user", "user1");
        singleDriverConnectionProvider.setProperty("user", "app");
        singleDriverConnectionProvider.setProperty("password", "");

        return singleDriverConnectionProvider;
    }

    public void exportClients() {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy.HH.mm");

            PreparedStatement ps = conn.prepareStatement(
                    "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            //ps.setString(1, "USER1");
            ps.setString(1, "APP");
            ps.setString(2, "CLIENT");
            final String outFile = "out/clients" + sdf.format((time)) + ".txt";
            ps.setString(3, outFile);
            //ps.setString(4, '%');
            ps.setString(4, ",");
            ps.setString(5, null);
            ps.setString(6, null);
            ps.execute();

        } catch (SQLException sqle) {
            printSQLException(sqle);

        }

    }


    public static void shutDown() {

        try {
            // the shutdown=true attribute shuts down Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException se) {
            if (((se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState())))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        } finally {

            // ResultSet
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                // PreparedStatement extend Statement
                Statement st = (Statement) statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    printSQLException(sqle);
                }
            }

            //Connection
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }

    }

    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param e the SQLException from which to print details.
     */
    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
}

