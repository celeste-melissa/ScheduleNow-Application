package com.software.schedulenow.DAO;

import com.software.schedulenow.helper.JDBC;
import com.software.schedulenow.model.Contact;
import javafx.collections.FXCollections;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
/** ContactsDAO: Provides methods to interact with the contacts in the database.
 *
 */
public class ContactsDAO {
    /** Retrieves a list of contact names from the database
     *
     * @return
     * @throws SQLException
     */
    public static List<String>getContactNames() throws SQLException {
        // Open database connection and prepare SQL query to fetch contact names
        JDBC.openConnection();
        //Execute query and process the ResultSet to create a list of contact names
        String sqlStatement = "SELECT Contact_Name FROM client_schedule.contacts";
        PreparedStatement preparedStatement= JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String>contactNames = FXCollections.observableArrayList();// returns the list of contact names

        try{
            while(resultSet.next()){
                String contact = resultSet.getString("Contact_Name");
                contactNames.add(contact);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return contactNames;
    }

    public static long getContactIdName(String contactName) throws SQLException{
        long contact = -1;
        JDBC.openConnection();
        String sqlStatement = "SELECT Contact_ID FROM client_schedule.contacts WHERE Contact_Name = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, contactName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            contact = resultSet.getLong("Contact_ID");
        }
        JDBC.closeConnection();
        return contact;
    }

    /** Retrieves a list of all contacts in the database
     *
     * @return
     * @throws SQLException
     */
    public static List<Contact>getAllContacts() throws SQLException{
        // Open connection and prepare SQL query to fetch all contact details
        JDBC.openConnection();
        // Execute query and process the ResultSet to create a list of contact objects
        String sqlStatement = "SELECT * FROM client_schedule.contacts";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Contact> contacts = new ArrayList<>(); // returns the list of contacts
        try{
            while(resultSet.next()){
                long contactID = resultSet.getLong("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String contactEmail = resultSet.getString("Email");

                Contact contact = new Contact(contactID, contactName, contactEmail);
                contacts.add(contact);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return contacts;
    }
}
