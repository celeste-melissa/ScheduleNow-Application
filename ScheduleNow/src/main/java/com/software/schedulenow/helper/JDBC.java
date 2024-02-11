package com.software.schedulenow.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** JDBC: a helper class for establishing and managing JDBC connections to a MYSQL database
 *
 */

public class JDBC {
    // Database connection details - protocol, vendor, location, databaseName, JDBCUrl, driver, userName, password
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; //LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; //Driver Reference
    private static final String userName = "sqlUser"; //Username
    private static final String password = "Passw0rd!"; //Password
    public static Connection connection; //Connection Interface
    // PreparedStatement for executing SQL queries
    private static PreparedStatement preparedStatement;
    //Getter for PreparedStatement
    public static PreparedStatement getPreparedStatement(){return preparedStatement;}
    //Setter for PreparedStatement
    public static void setPreparedStatement(PreparedStatement preparedStatement){
        JDBC.preparedStatement = preparedStatement;
    }

    /** Establishes a connection to the database using the provided details
     *
     * @return
     */
    public static Connection openConnection() {
        try {
            Class.forName(driver); //Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection Object
            System.out.println("Connection Successful");
        }
        catch(SQLException | ClassNotFoundException err) {
            err.printStackTrace();
        }
        return connection;
    }

    /** Returns the current database connection
     *
     * @return
     */
    public static Connection getConnection(){return connection;}

    /** Closes the database connection
     *
     */
    public static void closeConnection(){
        try{
            connection.close();
            System.out.println("Connection Closed!");
        }
        catch(Exception err){
            //Do nothing because application is closed
        }
    }

}
