package com.software.schedulenow.DAO;

import com.software.schedulenow.helper.JDBC;
import com.software.schedulenow.model.Country;
import java.sql.*;
import java.util.*;
/** CountriesDAO: Provides methods to interact with the countries in the database
 *
 */
public class CountriesDAO {
    /** Retrieves a list of all countries from the database
     *
     * @return
     * @throws SQLException
     */
    public static List<Country> getCountries() throws SQLException{
        //Open database connection and prepare SQL query to fetch all countries
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.countries";
        // Execute and process the ResultSet to create a list of country objects
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Country> countries = new ArrayList<>(); // returns the list of countries
        try{
            while(resultSet.next()){
                long countryId = resultSet.getLong("Country_ID");
                String countryName = resultSet.getString("Country");
                Timestamp dateCreated = resultSet.getTimestamp("Create_Date");
                Timestamp lastUpdated = resultSet.getTimestamp("Last_Update");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");

                Country country = new Country(countryId, countryName, dateCreated.toLocalDateTime(), lastUpdated.toLocalDateTime(), createdBy, lastUpdatedBy);
                countries.add(country);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return countries;
    }

    /** Retrieves the name of the country based on the name of a first level division
     *
     * @param divisionName
     * @return
     * @throws SQLException
     */

    public static String getCountryNameByDivisionName(String divisionName) throws SQLException {
        // Open database connection and prepare SQL query
        JDBC.openConnection();
        // Query joins countries and first_level_divisions tables to find the country name corresponding to given division name
        String countryName = " ";
        String sqlStatement = "SELECT countries.Country " +
                "FROM countries INNER JOIN " +
                "first_level_divisions ON " +
                "countries.Country_ID = first_level_divisions.Country_ID " +
                "WHERE first_level_divisions.Division = ?;";
        //Execute the query and get the country name from the ResultSet
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, divisionName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            countryName = resultSet.getString("Country");
        }
        JDBC.closeConnection();
        return countryName;
    }
}
