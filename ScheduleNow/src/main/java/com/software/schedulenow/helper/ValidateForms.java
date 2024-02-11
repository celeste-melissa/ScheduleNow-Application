package com.software.schedulenow.helper;

import com.software.schedulenow.DAO.AppointmentsDAO;
import com.software.schedulenow.model.Appointment;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.sql.SQLException;
import java.util.stream.Stream;

/** ValidateForms: a utility class for form validations in JavaFx Application
 *
 */

public class ValidateForms {
    /** Checks if any appointment-related fields are empty of null
     *
     * @param titleText
     * @param typeComboBox
     * @param descriptionText
     * @param locationText
     * @param startDatePicker
     * @param startTimeComboBox
     * @param endDatePicker
     * @param endDateComboBox
     * @param customerComboBox
     * @param userComboBox
     * @param contactComboBox
     * @return
     */
    public static boolean isAppointmentFieldEmpty(TextField titleText, ComboBox typeComboBox, TextField descriptionText, TextField locationText, DatePicker startDatePicker, ComboBox startTimeComboBox, DatePicker endDatePicker, ComboBox endDateComboBox, ComboBox customerComboBox, ComboBox userComboBox,  ComboBox contactComboBox){
        //Uses Java 8 Streams to check if any of the provided fields are null or empty
        // Returns true if any condition is met
        return Stream.of(titleText.getText(), typeComboBox.getValue(), descriptionText.getText(), locationText.getText(), startDatePicker.getValue(),startTimeComboBox.getValue(), endDatePicker.getValue(), endDateComboBox.getValue(), customerComboBox.getValue(), userComboBox.getValue(), contactComboBox.getValue()).anyMatch(Objects::isNull) || Stream.of((titleText.getText()), descriptionText.getText(), locationText.getText()).anyMatch(String::isEmpty);
    }

    /** Checks if any customer-related fields are empty or null
     *
     * @param nameText
     * @param addressText
     * @param phoneNumberText
     * @param countryComboBox
     * @param stateComboBox
     * @param postalCodeText
     * @return
     */

    public static boolean isCustomerFieldEmpty(TextField nameText, TextField addressText, TextField phoneNumberText, ComboBox countryComboBox, ComboBox stateComboBox, TextField postalCodeText){
        return Stream.of(nameText.getText(), addressText.getText(), phoneNumberText.getText(), countryComboBox.getValue(), postalCodeText.getText(), stateComboBox.getValue()).anyMatch(Objects::isNull) || Stream.of(nameText.getText(), addressText.getText(), phoneNumberText.getText(), postalCodeText.getText()).anyMatch(String::isEmpty);
    }

    /** Checks if the selected start date is before the current date and time
     *
     * @param startDatePicker
     * @return
     */
    public static boolean startDateCheck(LocalDateTime startDatePicker){
        // returns true if the start date is before the current date and time
     return startDatePicker.isBefore(ChronoLocalDateTime.from(LocalDateTime.now()));
    }

    /** Checks if the selected end date is before the selected start date
     *
     * @param startDatePicker
     * @param endDatePicker
     * @return
     */
    public static boolean endDateCheck(LocalDateTime startDatePicker, LocalDateTime endDatePicker){
        // returns true if the end date is before the start date
        return endDatePicker.isBefore(startDatePicker);
    }

    /** Checks for overlapping appointments for a given customer and user
     *
     * @param customerId
     * @param userId
     * @param start
     * @param end
     * @param appointmentId
     * @return
     * @throws SQLException
     */
    public static boolean overlappedAppointment(long customerId, long userId, LocalDateTime start, LocalDateTime end,Long appointmentId) throws  SQLException {
        // Fetches existing appointments and checks if the provided appointment overlaps with any item
        // Excludes the appointment itself if an ID is provided (useful for updates).
        List<Appointment> appointmentExisting = AppointmentsDAO.getAppointments(userId);
        appointmentExisting.addAll(AppointmentsDAO.getAppointmentsByCustomerID(customerId));
        for(Appointment appointment : appointmentExisting){
            if(start.isBefore(appointment.getEnd()) && end.isAfter(appointment.getStart()) && appointment.getId()!= appointmentId ){return true;}
        }
        return false;
    }

    /** Overload of overlappedAppointment method for checking overlapping appointments for a customer
     *
     * @param customerId
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public static boolean overlappedAppointment(long customerId,  LocalDateTime start, LocalDateTime end) throws  SQLException {
        //Checks for customer appointments - similar to overlappedAppointment method
        List<Appointment> appointmentExisting = AppointmentsDAO.getAppointments(customerId);
        for(Appointment appointment : appointmentExisting){
            if(start.isBefore(appointment.getEnd()) && end.isAfter(appointment.getStart())) {return true;}
        }
        return false;
    }

    /** Displayed an error alert with the provided title, header, and content
     *
     * @param title
     * @param header
     * @param content
     */
    public static void errorFoundAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
