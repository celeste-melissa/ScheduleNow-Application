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
import com.software.schedulenow.helper.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/** This class is intended to handle adding an appointment to the database
 * Adds a new appointment
 */
public class addAppointmentController implements Initializable {
    /** Observable list is created to hold different appointment types
     *
     */
    ObservableList appointmentType = FXCollections.observableArrayList();
    @FXML private Label titleLabel;
    @FXML private TextField titleText;
    @FXML private Label typeLabel;
    @FXML private ComboBox typeComboBox;
    @FXML private Label descriptionLabel;
    @FXML private TextField descriptionText;
    @FXML private Label locationLabel;
    @FXML private TextField locationText;
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox startTimeComboBox;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDatePicker;
    @FXML private Label endTimeLabel;
    @FXML private ComboBox endDateComboBox;
    @FXML private Label customerLabel;
    @FXML private ComboBox customerComboBox;
    @FXML private Label userLabel;
    @FXML private ComboBox userComboBox;
    @FXML private Label contactLabel;
    @FXML private ComboBox contactComboBox;
    @FXML private Button saveAddAppointmentButton;
    @FXML private Button cancelAddAppointmentButton;
    /** Lists to store data fetched from the database tables corresponding to - customers, contacts, users
     *
     */
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    // Handler for the save button - referenced in add-appointment.fxml
    public void onSaveAddAppointmentButton(ActionEvent actionEvent) throws SQLException, IOException {
        // Validation will check if there are any empty fields - if so an alert is displayed to the user
        boolean fieldsChecker = ValidateForms.isAppointmentFieldEmpty(titleText, typeComboBox, descriptionText, locationText, startDatePicker, startTimeComboBox, endDatePicker, endDateComboBox, customerComboBox, userComboBox, contactComboBox);
        if (fieldsChecker) {
            ValidateForms.errorFoundAlert("Warning", "Empty Fields!", "Attention: Please ensure that all fields are completed before proceeding to add an appointment.");
            return;
        }
        /**Data preparation for creating a new appointment
         * Fetches user inputs and current user details
         */

        String currentUser = UserDAO.getLoggedInUserName();
        LocalDateTime currentDate = LocalDateTime.now();
        Contact selectedContact = (Contact) contactComboBox.getValue();
        Customer selectedCustomer = (Customer) customerComboBox.getValue();
        User selectedUser = (User) userComboBox.getValue();
        String title = titleText.getText();
        String type = (String) typeComboBox.getValue();
        String description = descriptionText.getText();
        String location = locationText.getText();
        String createdBy = currentUser;
        String lastUpdatedBy = currentUser;
        LocalDateTime start = BusinessHours.gatherDateTime(startDatePicker, startTimeComboBox);
        LocalDateTime end = BusinessHours.gatherDateTime(endDatePicker, endDateComboBox);
        long userId = selectedUser.getId();
        long contactId = selectedContact.getId();
        long customerId = selectedCustomer.getId();

        boolean startCheck = ValidateForms.startDateCheck(start);
        boolean endCheck = ValidateForms.endDateCheck(start, end);
        boolean overlappingCheck = ValidateForms.overlappedAppointment(customerId, start, end);
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
            //Create a new appointment object and save it to the database
            Appointment appointment = new Appointment(title, type, description, start, end, location, currentDate, LocalDateTime.now(), createdBy, lastUpdatedBy, customerId, userId, contactId);
            AppointmentsDAO.createAppointment(appointment);
            //Switch to the dashboard view after saving the appointment
            Stage stage = (Stage) saveAddAppointmentButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader((Main.class.getResource("dashboard.fxml")));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1117, 731);
            dashboardController DashboardController = fxmlLoader.getController();
            List<Appointment>appointmentList = AppointmentsDAO.getAppointments(userId);
            DashboardController.refreshAppointmentsTable(appointmentList);
            DashboardController.refreshClientsTable(CustomersDAO.getCustomers());

            stage.setTitle("ScheduleNow - Appointment");
            stage.setScene(scene);
            stage.show();

        }
    }

    /** Handler for the cancel button - referenced in the add-appointment.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onCancelAddAppointmentButtonClick(ActionEvent actionEvent) throws SQLException, IOException{
        Stage stage = (Stage) cancelAddAppointmentButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        dashboardController DashboardController = fxmlLoader.getController();
        long userId = SessionLog.getInstance().getUserId();
        List<Appointment> appointmentList = AppointmentsDAO.getAppointments(SessionLog.getInstance().getUserId());
        List<Customer> customerList = CustomersDAO.getCustomers();
        DashboardController.refreshAppointmentsTable(appointmentList);
        DashboardController.refreshClientsTable(customerList);
        stage.setTitle("ScheduleNow - Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Initialize the controller
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
        try{
            // Populate combo boxes with data for contacts, customers, users, and appointment types
            contacts.setAll(ContactsDAO.getAllContacts());
            customers.setAll(CustomersDAO.getCustomers());
            users.setAll(UserDAO.getUsers());
            contactComboBox.setItems(contacts);
            customerComboBox.setItems(customers);
            startTimeComboBox.setItems(BusinessHours.timeOptions());
            endDateComboBox.setItems(BusinessHours.timeOptions());
            userComboBox.setItems(users);
            appointmentType.setAll("Strategy Development",  "Reflection Session", "Feedback Improvement", "Milestone Assessment");
            typeComboBox.setItems(appointmentType);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
