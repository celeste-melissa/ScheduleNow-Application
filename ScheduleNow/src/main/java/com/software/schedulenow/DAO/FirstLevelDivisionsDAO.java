package com.software.schedulenow.DAO;

import com.software.schedulenow.helper.JDBC;
import java.sql.*;
import java.util.ArrayList;

import com.software.schedulenow.model.FirstLevelDivision;

import javax.xml.transform.Result;
import java.util.*;
/** FirstLevelDivisionsDAO: Provides methods to interact with the first level divisions data in the database
 *

 */
public class FirstLevelDivisionsDAO {
    /** Retrieves all first level divisions from the database
     *
     * @return
     * @throws SQLException
     */
    public static List<FirstLevelDivision> getFirstLevelDivisions() throws SQLException{
        // Opens database connection and prepare SQL query to fetch all first level divisions
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.first_level_divisions";
        // Execute the query and process the ResultSet to create a list of FirstLevelDivision objects
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<FirstLevelDivision>firstLevelDivisionList = new ArrayList<>(); // returns the list of first level divisions
        try{
            while(resultSet.next()){
                long id = resultSet.getLong("Division_ID");
                String divisionName = resultSet.getString("Division");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long countryId = resultSet.getLong(("country_Id"));
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(id, divisionName,
                        createDate.toLocalDateTime(), lastUpdate.toLocalDateTime(), createdBy, lastUpdatedBy, countryId);
                firstLevelDivisionList.add(firstLevelDivision);

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return firstLevelDivisionList;
    }

    /** Finds the ID of a division based on its name
     *
     * @param divisionName
     * @return
     * @throws SQLException
     */
    public static long getDivisionIdByName(String divisionName) throws SQLException{
        // Opens the database connection and Prepares the SQL statement
        JDBC.openConnection();
        String sqlStatement = "SELECT Division_ID FROM client_schedule.first_level_divisions WHERE Division = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, divisionName);
        ResultSet resultSet = preparedStatement.executeQuery();
        long divisionId = -1;
        if(resultSet.next()){
            divisionId = resultSet.getLong("Division_ID");
        }
        JDBC.closeConnection();
        return divisionId;
    }

    /** Retrieves the name of a division based on its ID
     *
     * @param divisionId
     * @return
     * @throws SQLException
     */

    public static String getDivisionNameById(long divisionId) throws SQLException{
        // Opens the database connection and prepares the SQL statement
        JDBC.openConnection();
        String sqlStatement = "SELECT Division FROM client_schedule.first_level_divisions WHERE Division_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1,divisionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        String divisionName = " ";
        if(resultSet.next()){
            divisionName = resultSet.getString("Division");
        }
        JDBC.closeConnection();
        return divisionName;
    }

    /** Retrieves the list of first level divisions for a specific country ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public static List<FirstLevelDivision> divisionsSorted(long id) throws SQLException {
        // Opens the database connection and Prepares the SQL statment
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.first_level_divisions WHERE Country_ID = ? ";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<FirstLevelDivision> divisionsSorted = new ArrayList<>();
        try{
            while(resultSet.next()){
                long divisionId = resultSet.getLong("Division_ID");
                String divisionName = resultSet.getString("Division");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long countryId = resultSet.getLong("Country_Id");
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionId, divisionName,
                        createDate.toLocalDateTime(), lastUpdate.toLocalDateTime(), createdBy, lastUpdatedBy, countryId);
                divisionsSorted.add(firstLevelDivision);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
      }
        JDBC.closeConnection();
        return divisionsSorted;
    }
}
