package com.software.schedulenow.DAO;

import com.software.schedulenow.helper.JDBC;
import com.software.schedulenow.helper.SessionLog;
import com.software.schedulenow.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/** UserDAO: Provides methods to interact with the users data in the database.
 *
 */
public class UserDAO {
    /** Contructors for UserDAO
     *
     * @param id
     * @param userName
     * @param password
     */
    public UserDAO(Long id, String userName, String password) {

    }

    public UserDAO() {

    }

    /** Validates if a user exists in the database with the given username and password.
     *
     * @param userNameInput
     * @param passwordInput
     * @return
     * @throws SQLException
     */
    public static User validateUser(String userNameInput, String passwordInput) throws SQLException {
        // Open the database connection and prepare SQL query to validate user
        Connection connection = JDBC.getConnection();
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.users WHERE user_name = ? AND password = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, userNameInput);
        preparedStatement.setString(2, passwordInput);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("Not Found");
            JDBC.closeConnection();
            return null;
        } else {
            long id = resultSet.getLong("User_ID");
            String userName = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            User newUser = new User(id, userName, password);
            JDBC.closeConnection();
            return newUser;
        }
    }

    /** Retrieves all users from the database
     *
     * @return
     * @throws SQLException
     */

    public static List<User> getUsers() throws SQLException {
        // Open the database connection and prepare SQL query to fetch all users
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.users";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Long userId = resultSet.getLong("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                User user = new User(userId, userName, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return users;
    }

    public static ObservableList<Long> getUserIds() throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT User_ID FROM client_schedule.users";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Long> userIds = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                long userId = resultSet.getLong("User_Id");
                userIds.add(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return userIds;
    }

    /** Retrieves the username of the currently logged-in user
     *
     * @return
     * @throws SQLException
     */
    public static String getLoggedInUserName() throws SQLException {
        //Open database connection and prepare SQL query to fetch the username of the logged-in user
        JDBC.openConnection();
        String sqlStatement = "SELECT User_Name FROM client_schedule.users WHERE User_Id = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, SessionLog.getInstance().getUserId());
        ResultSet resultSet = preparedStatement.executeQuery();
        String userName = " ";
        try {
            if (resultSet.next()) {
                userName = resultSet.getString("User_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName; // returns the username of the logged-in user
    }


}
