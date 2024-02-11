package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.*;
import com.software.schedulenow.model.Appointment;
import com.software.schedulenow.model.Country;
import com.software.schedulenow.model.Customer;
import com.software.schedulenow.model.FirstLevelDivision;
import com.software.schedulenow.helper.ValidateForms;
import com.software.schedulenow.helper.SessionLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDateTime;

/** This class handles  is intended to add a customer to the database
 * Add a new client (customer)
 */
public class addClientController implements Initializable{
    // Observable lists for storing country names and first level divisions
    private final ObservableList<String> countryNames = FXCollections.observableArrayList();
    private final ObservableList<String> firstLevelDivisions = FXCollections.observableArrayList();


    @FXML private TextField nameText;
    @FXML private TextField addressText;
    @FXML private TextField phoneNumberText;
    @FXML private ComboBox countryComboBox;
    @FXML private ComboBox stateComboBox;
    @FXML private TextField postalCodeText;
    @FXML private Button saveAddClientButton;
    @FXML private Button cancelAddClientButton;

    /**Initializes the add client controller.
     * In this method, a lambda expression is employed to define a listener attached to the value property of the
     * country selector combo box. This approach of using a lambda expression streamlines the code by allowing the definition
     * of the listener's functionality directly within the valueProperty().addListener() call, enhancing both conciseness
     * and readability. Every time there is a change in the country selection, this listener automatically triggers the
     * firstLevelDivisionsHandler() method. This ensures that the first-level division selector remains current and in
     * sync with the chosen country.
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
            // Populating the countryNames list with data from the database
            for(Country country : CountriesDAO.getCountries()){
                countryNames.add(String.valueOf(country.getCountry()));
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        //Setting the country names in the countryComboBox and adding a listener
        countryComboBox.setItems(countryNames);
        countryComboBox.valueProperty().addListener((observableValue, oldDivisions, newDivisions)->{
            try{
                firstLevelDivisionsHandle();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        });
    }

    /** Handles changes in the countryComboBox
     *
     * @throws SQLException
     */
    public void firstLevelDivisionsHandle() throws SQLException{
        //Clearing and populating the firstLevelDivisions list based on the selected Country
        List<FirstLevelDivision> divisions = null;
        firstLevelDivisions.clear();

        /** Logic for populating the divisions based on country selection
         *
         */
        try{
            if(countryComboBox.getSelectionModel().getSelectedItem().equals("U.S")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(1);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            else if(countryComboBox.getValue().equals("UK")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(2);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            else if(countryComboBox.getValue().equals("Canada")){
                divisions = FirstLevelDivisionsDAO.divisionsSorted(3);
                for(FirstLevelDivision division : divisions){
                    firstLevelDivisions.add(division.getDivisionName());
                }
                stateComboBox.setItems(firstLevelDivisions);
            }
            else {
                divisions = new ArrayList();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**Handler for the save button - referenced in add-client.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onSaveAddClientButtonClick(ActionEvent actionEvent) throws SQLException, IOException{
        //Validation check for empty fields
        boolean fieldCheck = ValidateForms.isCustomerFieldEmpty(nameText, addressText,phoneNumberText, stateComboBox,countryComboBox, postalCodeText);
        if(fieldCheck){
            ValidateForms.errorFoundAlert("Error", "Empty Fields Indicated", "Ensure that all fields are filled. Please try again.");
            return;
        }
      // Obtains the data entered by the user and saves it to the corresponding required values
        long userId = SessionLog.getInstance().getUserId();
        String currentUsername = UserDAO.getLoggedInUserName();
        String name = nameText.getText();
        String address = addressText.getText();
        String phoneNumber = phoneNumberText.getText();
        String postalCode = postalCodeText.getText();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        String division = (String) stateComboBox.getSelectionModel().getSelectedItem();
        long divisionId = FirstLevelDivisionsDAO.getDivisionIdByName(division);
         // Saving a new customer to the database
        Customer customer = new Customer(name, address, phoneNumber, postalCode,now.toLocalDateTime(), now.toLocalDateTime(), currentUsername, currentUsername, divisionId);
        CustomersDAO.createCustomer(customer);

        Stage stage = (Stage) saveAddClientButton.getScene().getWindow();
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


    /** Handler for the cancel button that is referenced in the - add-client.fxml
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onCancelAddClientButtonClick(ActionEvent actionEvent) throws  SQLException, IOException{
        Stage stage = (Stage) cancelAddClientButton.getScene().getWindow();
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

}
