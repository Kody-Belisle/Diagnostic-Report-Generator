package src;

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

    private Connection getConnection() {
        System.out.println("DerbyDriver starting in " + framework + " mode");
        conn = null;

        try {
            Properties props = new Properties(); // connection properties
            // providing a user name and password is optional in the embedded
            // and derbyclient frameworks
            props.put("user", "user1");
            props.put("password", "user1");

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

                // and add a few rows...

                /* It is recommended to use PreparedStatements when you are
                 * repeating execution of an SQL statement. PreparedStatements also
                 * allows you to parameterize variables. By using PreparedStatements
                 * you may increase performance (because the Derby engine does not
                 * have to recompile the SQL statement each time it is executed) and
                 * improve security (because of Java type checking).
                 */
                // parameter 1 is num (int), parameter 2 is addr (varchar)
                /*psInsert = conn.prepareStatement(
                        "insert into client values (?, ?, ?, ?, ?, ?, ?, ?)");
                statements.add(psInsert);

                psInsert.setString(1, "Test Company");
                psInsert.setString(2, "Bob Jones");
                psInsert.setString(3, "(208) 111-1111");
                psInsert.setString(4, "test@gmail.com");
                psInsert.setString(5, "123 Main St.");
                psInsert.setString(6, "83706");
                psInsert.setString(7, "Boise");
                psInsert.setString(8, "ID");
                psInsert.executeUpdate();
                System.out.println("Inserted Test company, bob jones, at 123 Main st.");*/

                // Let's update some rows as well...

                // parameter 1 and 3 are num (int), parameter 2 is addr (varchar)
            /*psUpdate = conn.prepareStatement(
                    "update location set num=?, addr=? where num=?");
            statements.add(psUpdate);

            psUpdate.setInt(1, 180);
            psUpdate.setString(2, "Grand Ave.");
            psUpdate.setInt(3, 1956);
            psUpdate.executeUpdate();
            System.out.println("Updated 1956 Webster to 180 Grand");

            psUpdate.setInt(1, 300);
            psUpdate.setString(2, "Lakeshore Ave.");
            psUpdate.setInt(3, 180);
            psUpdate.executeUpdate();
            System.out.println("Updated 180 Grand to 300 Lakeshore");*/


            /*
               We select the rows and verify the results.
             */
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

