package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.*;
import com.software.schedulenow.model.*;
import com.software.schedulenow.helper.SessionLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.*;

/** This class is intended to handle all the report tables
 *
 */
public class reportsController implements Initializable{
    /** Observable lists for appointments, contacts, and reports
     *
     */
    private final ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Contact> contactObservableList = FXCollections.observableArrayList();
    private final ObservableList<Reports> yearOverviewObservableList = FXCollections.observableArrayList();
    private final ObservableList<Reports>  divisionsObservableList = FXCollections.observableArrayList();
    /** Map to track monthly and divisional reports
     *
     */
    private final HashMap<String, Reports> monthlyReportMap =  new HashMap<String, Reports>();
    private final HashMap<String, Reports> divisionReportMap = new HashMap<>();
    HashMap<String, Set<Long>> countedCustomersPerDivision = new HashMap<>();
    /** List to store first-level divisions
     *
     */
    List<FirstLevelDivision>divisionList = FirstLevelDivisionsDAO.getFirstLevelDivisions();
    @FXML private Button returnButton;
    @FXML private Label contactLabel;
    @FXML private ComboBox contactComboBox;
    @FXML private TableView appointmentsTable;
    @FXML private TableColumn appointmentIdColumn;
    @FXML private TableColumn titleColumn;
    @FXML private TableColumn typeColumn;
    @FXML private TableColumn descriptionColumn;
    @FXML private TableColumn startDateColumn;
    @FXML private TableColumn endDateColumn;
    @FXML private TableColumn locationColumn;
    @FXML private TableColumn customerIdColumn;
    @FXML private TableView monthsTable;
    @FXML private TableColumn monthColumn;
    @FXML private TableColumn appointmentTypeColumn;
    @FXML private TableColumn totalAppointmentsColumn;
    @FXML private TableView divisionTable;
    @FXML private TableColumn divisionColumn;
    @FXML private TableColumn totalCustomersColumn;
    @FXML private Button signOutButton;
    private Contact selectedContact;
    private List<Appointment>appointments;

    /** Constructor for reports controller
     *
     * @throws SQLException
     */
    public reportsController() throws SQLException {
    }

    /** Method for event handler of return button click referenced in - reports.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onReturnButtonClick(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("selection-menu.fxml"));
        Parent  root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("ScheduleNow - Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Method for event handler of sign out button click referenced in - reports.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSignOutButtonClick(ActionEvent actionEvent) throws IOException{
        SessionLog.resetInstance();
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ScheduleNow - Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /** Initialization method for the reports controller
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
        //Populate contact list and set up listeners
    try{
        contactObservableList.setAll(ContactsDAO.getAllContacts());
    }
    catch (SQLException e){
        throw new RuntimeException(e);
    }

    contactComboBox.setItems(contactObservableList);
    contactComboBox.valueProperty().addListener((observableValue, oldContact, newContact) -> {
        appointmentObservableList.clear();
        selectedContact = (Contact) contactComboBox.getSelectionModel().getSelectedItem();
        try{
            appointments = AppointmentsDAO.getAppointmentByContactId(selectedContact.getId());
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentObservableList.setAll(appointments);
        appointmentsTable.setItems(appointmentObservableList);
        appointmentsTable.refresh();
    });
        /** Process all appointments for report generation
         *
         */

    List<Appointment> allAppointments;
    try{
        allAppointments = AppointmentsDAO.getAllAppointments();
    }
    catch(SQLException e){
        throw new RuntimeException(e);
    }
    //Process each appointment for monthly reports
    for(Appointment appointment : allAppointments){
        Month month = appointment.getStart().getMonth();
        String type = appointment.getType();
        String mapKey = month + " " + type;

        Reports reports = monthlyReportMap.get(mapKey);
        if(reports ==null){
            reports = new Reports(month, type, 1);
            monthlyReportMap.put(mapKey, reports);
        }
    }
    // Populate year overview table
    List<Reports> reports = new ArrayList<>(monthlyReportMap.values());

    yearOverviewObservableList.setAll(reports);
    monthsTable.setItems(yearOverviewObservableList);
    monthsTable.refresh();


        // Initialize the division report map and counted customers map
        for (FirstLevelDivision division : divisionList) {
            String divisionName = division.getDivisionName();
            Reports report = new Reports(divisionName, 0);
            divisionsObservableList.add(report);
            divisionReportMap.put(divisionName, report);
            countedCustomersPerDivision.put(divisionName, new HashSet<>());
        }

        // Process appointments and increment customer count
        for (Appointment appointment : allAppointments) {
            try {
                // Get the customer for the appointment
                Customer customer = CustomersDAO.searchCustomer(appointment.getCustomerId());

                // Get the division name
                String divisionName = FirstLevelDivisionsDAO.getDivisionNameById(customer.getDivisionId());

                // Check if customer has already been counted for this division
                Set<Long> countedCustomers = countedCustomersPerDivision.get(divisionName);
                if (!countedCustomers.contains(customer.getId())) {
                    // Increment count for this division
                    Reports report = divisionReportMap.get(divisionName);
                    report.setCustomerCount(report.getCustomerCount() + 1);

                    // Mark this customer as counted for this division
                    countedCustomers.add(customer.getId());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    // Set the division data in the divisionTable and refresh it.
    divisionTable.setItems(divisionsObservableList);
    divisionTable.refresh();
    // Set up the cell value factories for each column in the appointments table
    appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
    endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    // Set up the cell value factories for each column in the months table
    monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
    appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
    totalAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCount"));
    monthsTable.refresh();

    // Set up the cell value factories for each column in the division table
    divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    totalCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));
    divisionTable.refresh();
    }
}

