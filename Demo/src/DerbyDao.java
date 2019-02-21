package src;

import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.DriverConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/*
 * DerbyDao is the data access object for the embedded database.
 * Any connection to the database will be done through here
 *
 */
public class DerbyDao {
    private static String framework = "embedded";
    private String protocol = "jdbc:derby:C:\\Users\\Hjohn\\IdeaProjects\\Diagnostic-Report-Generator\\lib\\";
    private static Connection conn;
    private static Statement s;
    private static ArrayList<Statement> statements;
    private static ResultSet rs;

    public DerbyDao() {

    }

    private Connection getConnection() {
        System.out.println("DerbyDriver starting in " + framework + " mode");
        conn = null;

        try {
            Properties props = new Properties(); // connection properties
            // providing a user name and password is optional in the embedded
            // and derbyclient frameworks
            props.put("user", "user1");
            props.put("password", "");

            /* By default, the schema APP will be used when no username is
             * provided.
             * Otherwise, the schema name is the same as the user name (in this
             * case "user1" or USER1.)
             *
             * Note that user authentication is off by default, meaning that any
             * user can connect to your database using any password. To enable
             * authentication, see the Derby Developer's Guide.
             */

            String dbName = "demoDB"; // the name of the database

            /*
             * This connection specifies create=true in the connection URL to
             * cause the database to be created when connecting for the first
             * time. To remove the database, remove the directory derbyDB (the
             * same as the database name) and its contents.
             *
             * The directory derbyDB will be created under the directory that
             * the system property derby.system.home points to, or the current
             * directory (user.dir) if derby.system.home is not set.
             */
            conn = DriverManager.getConnection(protocol + dbName
                    + ";create=true", props);

            System.out.println("Connected to and created database " + dbName);


            // We want to control transactions manually. Autocommit is on by
            // default in JDBC.
            conn.setAutoCommit(false);

            
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);

                // We create a table...
                s.execute("create table client(client_name varchar(50) NOT NULL, name varchar(50) NOT NULL," +
                        "phone varchar(20) NOT NULL, email varchar(50) NOT NULL," +
                        "address varchar(100) NOT NULL, zip char(5) NOT NULL, city varchar(30) NOT NULL," +
                        "state char(2) NOT NULL, primary key (client_name))");
                System.out.println("Created table client");

            rs = s.executeQuery(
                        "SELECT * FROM client");


            conn.commit();
            System.out.println("Committed the transaction");


            /*
             * In embedded mode, an application should shut down the database.
             * If the application fails to shut down the database,
             * Derby will not perform a checkpoint when the JVM shuts down.
             * This means that it will take longer to boot (connect to) the
             * database the next time, because Derby needs to perform a recovery
             * operation.
             *
             * It is also possible to shut down the Derby system/engine, which
             * automatically shuts down all booted databases.
             *
             * Explicitly shutting down the database or the Derby engine with
             * the connection URL is preferred. This style of shutdown will
             * always throw an SQLException.
             *
             * Not shutting down when in a client environment, see method
             * Javadoc.
             */


        } catch (SQLException sqle) {
            printSQLException(sqle);
        } finally {
            shutDown();
        }

        return conn;
    }

    /*
     * DIFFERENT FROM GETCONNECTION()
     * Is called from ReportGenerator in order to set up the data factory
     * This method only set up the DriverConnectionProvider
     */
    public DriverConnectionProvider setUpConnProvider() {
        //Get the current directory so that
        //the path is not hardcoded
        String protocol = System.getProperty("user.dir");
        String dbName = "demoDB";

        final DriverConnectionProvider singleDriverConnectionProvider = new DriverConnectionProvider();
        singleDriverConnectionProvider.setDriver("org.apache.derby.jdbc.EmbeddedDriver");
        //singleDriverConnectionProvider.setUrl("jdbc:derby:./sql/sampledata");
        //System.out.println("Current Directory: " + System.getProperty("user.dir"));

        System.out.println("Full URL:" + "jdbc:derby:" + protocol + "\\lib\\" + dbName + ";create=true");
        singleDriverConnectionProvider.setUrl("jdbc:derby:" + protocol + "\\lib\\" + dbName + ";create=true");
        singleDriverConnectionProvider.setProperty("user", "user1");
        singleDriverConnectionProvider.setProperty("password", "");

        return singleDriverConnectionProvider;
    }

    private ResultSet getClients(String clientName) {

        return null;
    }

    private static void shutDown() {

        try {
            // the shutdown=true attribute shuts down Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

            // To shut down a specific database only, but keep the
            // engine running (for example for connecting to other
            // databases), specify a database in the connection URL:
            //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
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

