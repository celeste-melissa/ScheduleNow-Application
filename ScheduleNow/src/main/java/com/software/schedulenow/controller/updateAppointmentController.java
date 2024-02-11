package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.AppointmentsDAO;
import com.software.schedulenow.DAO.ContactsDAO;
import com.software.schedulenow.DAO.CustomersDAO;
import com.software.schedulenow.DAO.UserDAO;
import com.software.schedulenow.model.Appointment;
import com.software.schedulenow.model.Contact;
import com.software.schedulenow.model.Customer;
import com.software.schedulenow.model.User;
import com.software.schedulenow.helper.BusinessHours;
import com.software.schedulenow.helper.ValidateForms;
import com.software.schedulenow.helper.SessionLog;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** This class is intended to handle updating an existing appointment
 * Modifies appointments
 */
public class updateAppointmentController implements Initializable {
    // Observable lists to hold data for ComboBoxes
    private final ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML private TextField titleText;
    @FXML private ComboBox typeComboBox;
    @FXML private TextField descriptionText;
    @FXML private TextField locationText;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox startTimeComboBox;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox endDateComboBox;
    @FXML private ComboBox clientComboBox;
    @FXML private ComboBox userComboBox;
    @FXML private ComboBox contactComboBox;
    @FXML private Button saveUpdateAppointmentButton;
    @FXML private Button cancelUpdateAppointmentButton;
    /** Variable to hold the selected appointment
     *
     */
    private Appointment selectedAppointment;

    /** Initializes the update appointment controller
     *
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
        // Initialize ComboBoxes with data from the database
        try{
            contacts.addAll(ContactsDAO.getAllContacts());
            customers.addAll(CustomersDAO.getCustomers());
            users.addAll(UserDAO.getUsers());
            contactComboBox.setItems(contacts);
            userComboBox.setItems(users);
            clientComboBox.setItems(customers);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        // Setting time options and appointment types
        startTimeComboBox.setItems(BusinessHours.timeOptions());
        endDateComboBox.setItems(BusinessHours.timeOptions());
        appointmentTypes.setAll("Strategy Development",  "Reflection Session", "Feedback Improvement", "Milestone Assessment");
        typeComboBox.setItems(appointmentTypes);
    }

    /** Method to populate fields with data from the selected appointment
     *
     * @param appointment
     */
   public void setAppointmentData(Appointment appointment) {
       selectedAppointment = appointment;
       titleText.setText(appointment.getTitle());
       typeComboBox.setValue(appointment.getType());
       descriptionText.setText(appointment.getDescription());
       locationText.setText(appointment.getLocation());
       startDatePicker.setValue(appointment.getStart().toLocalDate());
       startTimeComboBox.setValue(appointment.getStart().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
       endDatePicker.setValue(appointment.getEnd().toLocalDate());
       endDateComboBox.setValue(appointment.getEnd().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));

       long customerId = appointment.getCustomerId();
       for(Customer customer : customers){
           if(customer.getId() == customerId){
               clientComboBox.setValue(customer);
               break;
           }
       }

       long userId = appointment.getUserId();
       for(User user : users){
           if(user.getId() == userId){
               userComboBox.setValue(user);
               break;
           }
       }

       long contactId = appointment.getContactId();
       for(Contact contact : contacts){
           if(contact.getId() == contactId){
               contactComboBox.setValue(contact);
               break;
           }
       }
   }

    /** Handler for the save button click referenced in the - update-appointment.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
   public void onSaveUpdateAppointmentButtonClick(ActionEvent actionEvent) throws SQLException, IOException{
        boolean fieldsChecker = ValidateForms.isAppointmentFieldEmpty(titleText, typeComboBox, locationText, descriptionText, startDatePicker, startTimeComboBox,endDatePicker, endDateComboBox,clientComboBox, contactComboBox, userComboBox);
        // Checks if any fields are empty and displays an error alert any empty fields are found
        if(fieldsChecker){
            ValidateForms.errorFoundAlert("Error: ", "Empty fields are not accepted!", "All fields must be filled out. Please try again.");
            return;
        }
        // Extracting the values entered in the form fields
        String title = titleText.getText();
        String type = (String) typeComboBox.getValue();
        String description = descriptionText.getText();
        String location = locationText.getText();
        LocalDateTime start = BusinessHours.gatherDateTime(startDatePicker, startTimeComboBox);
        LocalDateTime end = BusinessHours.gatherDateTime(endDatePicker, endDateComboBox);
        LocalDateTime createDate = selectedAppointment.getCreateDate();
        String createdBy = selectedAppointment.getCreatedBy();
        String lastUpdatedBy = UserDAO.getLoggedInUserName();
        Contact selectedContact = (Contact) contactComboBox.getValue();
        Customer selectedCustomer = (Customer) clientComboBox.getValue();
        User selectedUser = (User) userComboBox.getValue();
        long customerId = selectedCustomer.getId();
        long userId = selectedUser.getId();
        long contactId = selectedContact.getId();
        // Validating the start and end times of the appointment
        boolean startCheck = ValidateForms.startDateCheck(start);
        boolean endCheck = ValidateForms.endDateCheck(start, end);
        boolean overlappingCheck = ValidateForms.overlappedAppointment(customerId, userId, start, end, selectedAppointment.getId());
       /** Handling different validation scenarios
        * end, start, overlapping are checked
        */
       if(endCheck){
           ValidateForms.errorFoundAlert("Warning", "Invalid Selection", "Your selected end date is before your start date");
       }
       else if (startCheck) {
           ValidateForms.errorFoundAlert("Warning", "Invalid Selection", "Your selected appointment Start must begin after the current date and time");
       }
       else if (overlappingCheck) {
           ValidateForms.errorFoundAlert("Warning", "Overlapping Appointment Error", "Selected customer already has an appointment schedule for this date and time. Please select an alternate time and/or date!");
       }

       else{
           /** Creating a new appointment object with updated information
            *
            */
           Appointment appointment = new Appointment(title, type, description,  start, end, location, createDate, LocalDateTime.now(), createdBy, lastUpdatedBy, customerId, userId, contactId);
           appointment.setId(selectedAppointment.getId());
           // Updating the appointment in the database
           AppointmentsDAO.updateAppointment(appointment);
           // Redirecting to the dashboard after successful update
           Stage stage = (Stage) saveUpdateAppointmentButton.getScene().getWindow();
           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
           Parent root = fxmlLoader.load();
           Scene scene = new Scene(root, 1117, 731);

           dashboardController DashboardController = fxmlLoader.getController();
           List<Appointment> appointmentList = AppointmentsDAO.getAppointments(SessionLog.getInstance().getUserId());
           DashboardController.refreshAppointmentsTable(appointmentList);
           DashboardController.refreshClientsTable(CustomersDAO.getCustomers());
           stage.setTitle("ScheduleNow - Appointment");
           stage.setScene(scene);
           stage.show();
       }
   }

    /** Handler for the cancel button click referenced in - update-appointment.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
   public void onCancelUpdateAppointmentButton(ActionEvent actionEvent) throws SQLException, IOException{
        Stage stage = (Stage) cancelUpdateAppointmentButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        dashboardController DashboardController = fxmlLoader.getController();
        long userId = SessionLog.getInstance().getUserId();
        List<Appointment> userAppointments = AppointmentsDAO.getAppointments(userId);
        List<Customer>allCustomers = CustomersDAO.getCustomers();
        DashboardController.refreshClientsTable(allCustomers);
        DashboardController.refreshAppointmentsTable(userAppointments);
       stage.setTitle("ScheduleNow - Appointments");
       stage.setScene(scene);
       stage.show();
   }
}
