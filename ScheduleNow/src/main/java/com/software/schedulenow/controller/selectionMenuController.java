package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.AppointmentsDAO;
import com.software.schedulenow.DAO.CustomersDAO;
import com.software.schedulenow.model.Appointment;
import com.software.schedulenow.model.Customer;
import com.software.schedulenow.helper.SessionLog;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.util.*;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

/** This class is intended to control the selection menu screen
 *
 */

public class selectionMenuController implements Initializable {
    @FXML private Text noAppointmentsLabel;
    @FXML private Button appointmentsClientButton;
    @FXML private Button reportsButton;
    @FXML private Button signOutButton;

    /** Handler for Appointments/Clients button click referenced in - selection-menu.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onAppointmentsClientButton(ActionEvent actionEvent) throws SQLException, IOException{
        // Switch to dashboard scene upon button click
        Stage stage = (Stage) appointmentsClientButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1117, 731);
        dashboardController DashboardController = fxmlLoader.getController();
        long userId = SessionLog.getInstance().getUserId();
        List<Appointment>userAppointments = AppointmentsDAO.getAppointments(userId);
        List<Customer> allCustomers = CustomersDAO.getCustomers();
        DashboardController.refreshAppointmentsTable(userAppointments);
        DashboardController.refreshClientsTable(allCustomers);
        stage.setTitle("ScheduleNow - Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /** Handler for reports button click referenced in - selection-menu.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onReportsButtonClick(ActionEvent actionEvent) throws SQLException, IOException{
        // Switch to reports scene upon button click
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("reports.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("ScheduleNow - Reports");
        stage.setScene(scene);
        stage.show();
    }

    /** Handler for sign out button click
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onSignOutButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        // Reset the session and switch to login scene
        SessionLog.resetInstance();
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ScheduleNow - Login");
        stage.setScene(scene);
        stage.show();
    }


    /** Initialization the selection menu controller for all upcoming appointments
     * In this method, a lambda expression plays a crucial role in the stream pipeline, specifically for filtering
     * the list of appointments. It sets the criteria for filtering: an appointment qualifies for inclusion in the
     * output list if it begins after the present moment but before the 15 minutes. The use of a lambda expression in this
     * context simplifies the process of integrating the condition right into the filter() call. This not only makes the
     * code more succinct but also enhances its readability.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic, including checking ofr upcoming appointments
        List<Appointment> userAppointments = null;
        try{
            userAppointments = AppointmentsDAO.getAppointments(SessionLog.getInstance().getUserId());
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        List<Appointment> futureAppointments = userAppointments.stream()
                .filter(appointment -> appointment.getStart().isAfter(LocalDateTime.now())
                        && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15))).toList();
        /** Display a notification iif there are any upcoming appointments within 15 minutes
         *
         */
        if(!futureAppointments.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminder: You have an upcoming appointment soon.");
            alert.setHeaderText("You have " +futureAppointments.size() + " appointment(s) within the next 15 minutes!");
            StringBuilder appointmentStart = new StringBuilder();
            for(Appointment appointment : futureAppointments){
                appointmentStart.append("\n Appointment ID: " + appointment.getId() + "\n Appointment Start Time: " + appointment.getStart().toString());
            }

            alert.setContentText(appointmentStart.toString());
            alert.showAndWait();
        }
        else {
            noAppointmentsLabel.setVisible(true);
        }
    }
}
