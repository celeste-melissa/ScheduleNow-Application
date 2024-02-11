package com.software.schedulenow.DAO;

import com.software.schedulenow.model.Customer;
import com.software.schedulenow.helper.JDBC;

import java.sql.*;
import java.util.*;
/** CustomersDAO: Provides methods to interact with the customers data in the database
 *
 */

public class CustomersDAO {
    private static long customerId;

    /**
     * Retrieves a list of all customers from the database
     *
     * @return
     * @throws SQLException
     */
    public static List<Customer> getCustomers() throws SQLException {
        // Open database connection and prepare SQL query to fetch all customers

        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.customers";
        //Execute query and process the ResultSet to create a list of customer objects
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Customer> customers = new ArrayList<>(); // returns the list of customers
        try {
            while (resultSet.next()) {
                long customerId = resultSet.getLong("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String phoneNumber = resultSet.getString("Phone");
                String postalCode = resultSet.getString("Postal_Code");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long divisionId = resultSet.getLong("Division_ID");

                String state = FirstLevelDivisionsDAO.getDivisionNameById(divisionId);

                Customer customer = new Customer(customerId, customerName, address, phoneNumber, postalCode, createDate.toLocalDateTime(), lastUpdate.toLocalDateTime(), createdBy, lastUpdatedBy, divisionId);
                customer.setState(state); // Assuming there's a setter method in Customer class

                customers.add(customer);



            }
        }

        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return customers;
    }

    /** Adds a new customer to the database
     *
     * @param customer
     * @throws SQLException
     */

    public static void createCustomer(Customer customer) throws SQLException{
        // Open database connections and prepare INSERT INTO statement
        JDBC.openConnection();
        String sqlStatement = "INSERT INTO client_schedule.customers" +  "(Customer_Name, Address, Phone, Postal_Code, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Execute update to insert the new customer data
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getAddress());
        preparedStatement.setString(3, customer.getPhoneNumber());
        preparedStatement.setString(4, customer.getPostalCode());
        preparedStatement.setTimestamp(5, (Timestamp.valueOf(customer.getCreateDate())));
        preparedStatement.setString(6, customer.getCreatedBy());
        preparedStatement.setTimestamp(7, (Timestamp.valueOf(customer.getLastUpdate())));
        preparedStatement.setString(8, customer.getLastUpdatedBy());
        preparedStatement.setLong(9, customer.getDivisionId());
        preparedStatement.executeUpdate();
    }

    /** Updates an existing customer's details in the database
     *
     * @param customer
     * @throws SQLException
     */

    public static void updateCustomer(Customer customer) throws SQLException {
        // Open database connection
        JDBC.openConnection();
        // Prepare SQL update statement
        String sqlStatement = "UPDATE client_schedule.customers " +
                "SET Customer_Name = ?, " +
                "Address = ?, "+
                "Phone = ?, " +
                "Postal_Code = ?, " +
                "Create_Date = ?, " +
                "Created_By = ?, " +
                "Last_Update = ?, " +
                "Last_Updated_By = ?, " +
                "Division_ID = ? " +
                "WHERE Customer_ID = ?;";
        // Execute update to modify customer record
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getAddress());
        preparedStatement.setString(3, customer.getPhoneNumber());
        preparedStatement.setString(4, customer.getPostalCode());
        preparedStatement.setTimestamp(5, (Timestamp.valueOf(customer.getCreateDate())));
        preparedStatement.setString(6, customer.getCreatedBy());
        preparedStatement.setTimestamp(7, (Timestamp.valueOf(customer.getLastUpdate())));
        preparedStatement.setString(8, customer.getLastUpdatedBy());
        preparedStatement.setLong(9, customer.getDivisionId());
        preparedStatement.setLong(10, customer.getId());
        preparedStatement.executeUpdate();

        JDBC.closeConnection();
    }

    /** Deletes a customer from the database based on customer ID
     *
     * @param customerId
     * @return
     * @throws SQLException
     */

    public static int deleteCustomer(long customerId) throws SQLException{
        // Opens database connection and prepare SQL statement
        JDBC.openConnection();
        String sqlStatement = "DELETE FROM client_schedule.customers WHERE Customer_ID =?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, customerId);
        int action = preparedStatement.executeUpdate();
        JDBC.closeConnection();
        return action;
    }

    /** Finds a customer in the database using the customer Id
     *
     * @param customerId
     * @return
     * @throws SQLException
     */



    public static Customer searchCustomer(long customerId) throws SQLException {
        // Opens database connection and Prepares SQL query
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.customers WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        // Set the parameter value
        preparedStatement.setLong(1, customerId);

        ResultSet resultSet = preparedStatement.executeQuery();
        Customer customer = null;
        if (resultSet.next()) {
            long id = resultSet.getLong("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");
            String address = resultSet.getString("Address");
            String phoneNumber = resultSet.getString("Phone");
            String postalCode = resultSet.getString("Postal_Code");
            Timestamp createDate = resultSet.getTimestamp("Create_Date");
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            long divisionId = resultSet.getLong("Division_ID");

            customer = new Customer(id, customerName, address, phoneNumber, postalCode, createDate.toLocalDateTime(),
                    lastUpdate.toLocalDateTime(), createdBy, lastUpdatedBy, divisionId);
        }
        JDBC.closeConnection();
        return customer; //returns the customer object or null if not found
    }


}




