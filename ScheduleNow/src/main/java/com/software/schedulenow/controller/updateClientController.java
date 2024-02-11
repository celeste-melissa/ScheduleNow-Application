package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.*;
import com.software.schedulenow.model.*;
import com.software.schedulenow.helper.ValidateForms;
import com.software.schedulenow.helper.SessionLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.*;

/** This class is intended to handle updating a customer that exists in the database
 * Modifies client (customer) data
 */
public class updateClientController implements Initializable{
    /** Observable lists to hold data for country and state combo boxes
     *
     */
    private final ObservableList<String> countryNames = FXCollections.observableArrayList();
    private final ObservableList<String> firstLevelDivisions = FXCollections.observableArrayList();

    @FXML private TextField nameText;
    @FXML private TextField addressText;
    @FXML private TextField phoneNumberText;
    @FXML private ComboBox countryComboBox;
    @FXML private ComboBox stateComboBox;
    @FXML private TextField postalCodeText;
    @FXML private Button saveUpdateClientButton;
    @FXML private Button cancelUpdateClientButton;
    private Customer selectedCustomer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            /** Populating the country names in the ComboBox
             *
             */
            for(Country country : CountriesDAO.getCountries()){
                countryNames.add(String.valueOf(country.getCountry()));
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        countryComboBox.setItems(countryNames);
        // Listener for changes in the selected country
        countryComboBox.valueProperty().addListener((observableValue, oldDivisions, newDivisions) -> {
            try{
                firstLevelDivisionsHandle();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        });

    }

    /** Handles the save button click referenced in - update-client.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onSaveUpdateClientButtonClick(ActionEvent actionEvent) throws SQLException, IOException{
        // Checks if any fields are empty
        boolean fieldCheck = ValidateForms.isCustomerFieldEmpty(nameText, addressText, phoneNumberText,countryComboBox,stateComboBox,postalCodeText);
        if(fieldCheck){
            // Alerts user if any fields are found empty
            ValidateForms.errorFoundAlert("Warning", "Empty Fields!", "Attention: Please ensure that all fields are completed before proceeding to add an appointment.");
            return;
        }
        long userId = SessionLog.getInstance().getUserId();
        String currentUsername = UserDAO.getLoggedInUserName();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        /** Extracts customer information from form
         *
         */
        String name = nameText.getText();
        String address = addressText.getText();
        String phone = phoneNumberText.getText();
        String postalCode = postalCodeText.getText();

        String division = (String) stateComboBox.getSelectionModel().getSelectedItem();
        long divisionId = FirstLevelDivisionsDAO.getDivisionIdByName(division);
        /** Creating updated customer object
         *
         */
        Customer customer = new Customer(name, address, phone, postalCode, now.toLocalDateTime(), now.toLocalDateTime(), currentUsername, currentUsername, divisionId);
        customer.setId(selectedCustomer.getId());
        // Updating the customer in the database
        CustomersDAO.updateCustomer(customer);
        // Navigating back to the dashboard scene
        Stage stage = (Stage) saveUpdateClientButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1117, 731);

        dashboardController DashboardController = fxmlLoader.getController();
        List<Appointment> appointmentList = AppointmentsDAO.getAppointments(userId);
        DashboardController.refreshAppointmentsTable(appointmentList);
        DashboardController.refreshClientsTable(CustomersDAO.getCustomers());

        stage.setTitle("ScheduleNow - Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Sets customer data in the form for editing
     *
     * @param customer
     * @throws SQLException
     */
    public void setCustomerData(Customer customer) throws SQLException{
        selectedCustomer = customer;
        // Filling form fields with customer data
        nameText.setText(customer.getName());
        addressText.setText(customer.getAddress());
        phoneNumberText.setText(customer.getPhoneNumber());
        String divisionName = FirstLevelDivisionsDAO.getDivisionNameById(customer.getDivisionId());
        System.out.println(divisionName);
        String countryName = CountriesDAO.getCountryNameByDivisionName(divisionName);
        countryComboBox.setValue(countryName);
        stateComboBox.setValue(divisionName);
        postalCodeText.setText(customer.getPostalCode());
    }

    /** Handles the cancel button event that is referenced in - update-client.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onCancelUpdateClientButtonClick(ActionEvent actionEvent) throws  SQLException, IOException{
        Stage stage = (Stage) cancelUpdateClientButton.getScene().getWindow();
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

    /** This method handles all the first level division conversions when a country is selected the states/provinces are loaded
     *
     * @throws SQLException
     */
    public void firstLevelDivisionsHandle() throws SQLException{
        List<FirstLevelDivision> divisions = null;
        firstLevelDivisions.clear(); // Clears the existing items in the firstLevelDivisions list
        try{
            // Check if the selected country is "U.S" and populate stateComboBox accordingly
            if(countryComboBox.getSelectionModel().getSelectedItem().equals("U.S")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(1);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            // Check if the selected country is "U.K" and populate stateComboBox accordingly
            else if(countryComboBox.getValue().equals("UK")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(2);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            // Check if the selected country is "Canada" and populate stateComboBox accordingly
            else if(countryComboBox.getValue().equals("Canada")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(3);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            else {
                divisions = new ArrayList(); // Initializes an empty list if none of the above conditions are met
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
