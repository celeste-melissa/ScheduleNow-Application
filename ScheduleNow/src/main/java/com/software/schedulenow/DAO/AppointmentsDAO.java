package com.software.schedulenow.DAO;

import com.software.schedulenow.model.Appointment;
import com.software.schedulenow.helper.JDBC;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

/** AppointmentsDAO: Provides methods to interact with the appointments data in the database.
 *
 */
public class AppointmentsDAO {
    /** getAppointments: Retrieves a list of appointments for a specific user from the database
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<Appointment>getAppointments(long userId) throws SQLException {
        // Database connection and SQL query preparation
        JDBC.openConnection();
        // Fetches appointments where the User_ID matches the specified userId
        String sqlStatement = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, userId);
        /** Processes the ResultSet to create a list of Appointment objects
         *
         */
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>(); // Returns the list of appointments for the user
        try {
            while (resultSet.next()) {
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long customerId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long contactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, type, description, start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, customerId, userIdCol, contactId);
                appointmentList.add(appointment);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        JDBC.closeConnection();
        return appointmentList;
    }

    /** Retrieves a list of all appointments in the database
     * Fetches all appointments without filtering by user
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAllAppointments() throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.appointments";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>();

        try{
            while (resultSet.next()){
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long customerId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long contactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, type, description,  start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, customerId, userIdCol, contactId);
                appointmentList.add(appointment);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return appointmentList;
}

    /** Retrieves appointments for a user within the next 30 days
     * Filters appointments to those within the next 30 days
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAllAppointmentsByMonth(long userId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.appointments WHERE User_ID = ? AND Start >= ? AND Start < ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, userId);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime thirtyDaysFromToday = today.plusDays(30);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(today));
        preparedStatement.setTimestamp(3, Timestamp.valueOf(thirtyDaysFromToday));

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long customerId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long contactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, type, title, description, start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, customerId, userIdCol, contactId);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return appointmentList;
    }

    /** Retrieves appointments for a user within the next 7 days
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAllAppointmentsByWeek(long userId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.appointments WHERE User_ID = ? AND Start >= ? AND Start < ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, userId);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime sevenDaysFromToday = today.plusDays(7);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(today));
        preparedStatement.setTimestamp(3, Timestamp.valueOf(sevenDaysFromToday));

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long customerId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long contactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, type, description, start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, customerId, userIdCol, contactId);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return appointmentList;
    }

    /** Deletes an appointment by its ID from the database
     * Executes SQL DELETE operation for a specific appointment based on its ID
     * @param appointmentId
     * @throws SQLException
     */
    public static void deleteAppointment(long appointmentId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "DELETE FROM client_schedule.appointments WHERE Appointment_ID=?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, appointmentId);
        preparedStatement.executeUpdate();
        JDBC.closeConnection();
    }


    public static void deleteAppointmentByCustomerId(long customerId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "DELETE * FROM client_schedule.appointments WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, customerId);
        preparedStatement.executeUpdate();
        JDBC.closeConnection();
    }

    /** Adds a new appointment to the database
     * Inserts a new appointment record into the database
     * Uses the appointment object to set values in the SQL INSERT statement
     * @param appointment
     * @throws SQLException
     */
    public static void createAppointment(Appointment appointment) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "INSERT INTO client_schedule.appointments" +
                "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By," +
                " Customer_ID, User_ID, Contact_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, appointment.getTitle());
        preparedStatement.setString(2, appointment.getDescription());
        preparedStatement.setString(3, appointment.getLocation());
        preparedStatement.setString(4, appointment.getType());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        preparedStatement.setTimestamp(7, Timestamp.valueOf(appointment.getCreateDate()));
        preparedStatement.setString(8, appointment.getCreatedBy());
        preparedStatement.setTimestamp(9, Timestamp.valueOf(appointment.getLastUpdate()));
        preparedStatement.setString(10, appointment.getLastUpdatedBy());
        preparedStatement.setLong(11, appointment.getCustomerId());
        preparedStatement.setLong(12, appointment.getUserId());
        preparedStatement.setLong(13, appointment.getContactId());
        preparedStatement.executeUpdate();
        JDBC.closeConnection();
    }

    /** Updates an existing appointment to the database
     * Executes an SQL UPDATE operation to modify an existing appointment
     * The appointment object provides the new values for the update
     * @param appointment
     * @throws SQLException
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "UPDATE client_schedule.appointments " +
                "set Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?," +
                "Last_Update = ?, " +
                "Last_Updated_By = ?, " +
                "Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ? " +
                "where Appointment_ID = ?;";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, appointment.getTitle());
        preparedStatement.setString(2, appointment.getDescription());
        preparedStatement.setString(3, appointment.getLocation());
        preparedStatement.setString(4, appointment.getType());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        preparedStatement.setTimestamp(7, Timestamp.valueOf(appointment.getLastUpdate()));
        preparedStatement.setString(8, appointment.getLastUpdatedBy());
        preparedStatement.setLong(9, appointment.getCustomerId());
        preparedStatement.setLong(10, appointment.getUserId());
        preparedStatement.setLong(11, appointment.getContactId());
        preparedStatement.setLong(12, appointment.getId());
        preparedStatement.executeUpdate();
        JDBC.closeConnection();
    }

    /** Retrieves appointments associated with a specific contact ID
     * Filters by contact ID
     * @param contactId
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAppointmentByContactId(long contactId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.appointments WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, contactId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long customerId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long apptContactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, type, description,start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, customerId, userIdCol, apptContactId);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return appointmentList;
    }

    /** Retrieves appointments associated with a specific customer ID
     * Filers by customer ID
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAppointmentsByCustomerID(long customerId) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setLong(1, customerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Appointment> appointmentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long appointmentId = resultSet.getLong("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimeStamp = resultSet.getTimestamp("End");
                Timestamp createTimeStamp = resultSet.getTimestamp("Create_Date");
                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime end = endTimeStamp.toLocalDateTime();
                LocalDateTime createDate = createTimeStamp.toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateTimeStamp = resultSet.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = lastUpdateTimeStamp.toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                long custId = resultSet.getLong("Customer_ID");
                long userIdCol = resultSet.getLong("User_ID");
                long apptContactId = resultSet.getLong("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, type, description, start, end, location,
                        createDate, lastUpdate, createdBy, lastUpdatedBy, custId, userIdCol, apptContactId);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBC.closeConnection();
        return appointmentList;
    }

}
