package com.software.schedulenow.controller;

import com.software.schedulenow.DAO.FirstLevelDivisionsDAO;
import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.AppointmentsDAO;
import com.software.schedulenow.DAO.CustomersDAO;
import com.software.schedulenow.model.Appointment;
import com.software.schedulenow.model.Customer;
import com.software.schedulenow.helper.SessionLog;
import com.software.schedulenow.model.FirstLevelDivision;
import com.software.schedulenow.model.Reports;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.stream.Collectors;

/** This class is intended to control the handles that are necessary for the dashboard
 * Methods included are - add appointment, update appointment, delete appointment, add client, update client, and delete client
 */

public class dashboardController implements Initializable {
    /** Observables lists to hold and manage appointment and customer data
     *
     */
    private final ObservableList<Appointment>appointmentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Customer>customerObservableList = FXCollections.observableArrayList();


    @FXML private Button returnButton;
    @FXML private ToggleGroup appointmentToggle;
    @FXML private RadioButton allAppointmentsButton;
    @FXML private RadioButton currentMonthButton;
    @FXML private RadioButton currentWeekButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button updateAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button addClientButton;
    @FXML private Button updateClientButton;
    @FXML private Button deleteClientButton;
    @FXML private Button signOutButton;
    @FXML private TableView<Appointment>appointmentsTable;
    @FXML private TableColumn<Appointment, Long> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, LocalDateTime>startDateColumn;
    @FXML private TableColumn<Appointment, LocalDateTime>endDateColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, Long> customerIdColumn;
    @FXML private TableColumn<Appointment, Long> userIdColumn;
    @FXML private TableView<Customer> clientsTable;
    @FXML private TableColumn <Customer, Long>clientIdColumn;
    @FXML private TableColumn <Customer, String>clientNameColumn;
    @FXML private TableColumn <Customer, String>clientAddressColumn;
    @FXML private TableColumn <Customer, String>clientPhoneColumn;
    @FXML private TableColumn <Customer, String> clientStateColumn;
    @FXML private TableColumn <Customer,String> clientPostCodeColumn;

    public dashboardController() throws SQLException {
    }

    /** Refreshes the appointments table with a new list of appointments
     *
     * @param appointments
     */
    public void refreshAppointmentsTable(List<Appointment>appointments){
        appointmentObservableList.clear();
        appointmentObservableList.addAll(appointments);
        appointmentsTable.setItems(appointmentObservableList);
        appointmentsTable.refresh();
    }

    /** Refreshes the clients table with a new list of customers
     *
     * @param customer
     */

    public void refreshClientsTable(List<Customer>customer){
        customerObservableList.clear();
        customerObservableList.addAll(customer);
        clientsTable.setItems(customerObservableList);
        clientsTable.refresh();
    }

    /** Method to handle add appointment button that is referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws IOException
     */

    public void onAddAppointmentButtonClick(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) addAppointmentButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-appointment.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        addAppointmentController addAppointmentController = fxmlLoader.getController();
        stage.setTitle("ScheduleNow - Add an Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**Method to handle Update appointment button - referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateAppointmentButtonClick(ActionEvent actionEvent) throws IOException{
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        try{
            Stage stage = (Stage) updateAppointmentButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("update-appointment.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            updateAppointmentController updateAppointmentController = fxmlLoader.getController();
            updateAppointmentController.setAppointmentData(selectedAppointment);
            stage.setTitle("ScheduleNow - Update an Appointment");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to update appointment data. Please try again.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /** Method to hand delete appointment button referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteAppointmentButtonClick(ActionEvent actionEvent) throws SQLException{
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Waring: This action cannot be undone!");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                long selectedId = selectedAppointment.getId();
                System.out.println(selectedId);
                AppointmentsDAO.deleteAppointment(selectedId);
                appointmentsTable.getItems().remove(selectedAppointment);
                appointmentsTable.refresh();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has been found. Please try again!");
            alert.setContentText("error");
            alert.showAndWait();
        }
    }


    /** Method to handle add client button referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAddClientButtonClick(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) addClientButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-client.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        addClientController addClientController = fxmlLoader.getController();
        stage.setTitle("ScheduleNow - Add a Customer");
        stage.setScene(scene);
        stage.show();
    }

    /** Method to handle update client button referenced in - dashboard.fxml
     *
     * @param actionEvent
     */
    public void onUpdateClientButtonClient(ActionEvent actionEvent) {
        Customer selectedCustomer = clientsTable.getSelectionModel().getSelectedItem();
        try{
            Stage stage = (Stage) updateClientButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("update-client.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            updateClientController updateClientController = fxmlLoader.getController();
            updateClientController.setCustomerData(selectedCustomer);

            stage.setTitle("ScheduleNow - Update a Client");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error was found. Please try again!");
            // Create expandable Exception text area
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane
            alert.getDialogPane().setExpandableContent(expContent);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**Method to handle delete client button referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteClientButtonClick(ActionEvent actionEvent) throws SQLException{
        Customer selectedCustomer = clientsTable.getSelectionModel().getSelectedItem();
        try{
            long selectedId = selectedCustomer.getId();
            List<Appointment> customerAppointments = AppointmentsDAO.getAppointmentsByCustomerID(selectedId);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Waring: This action cannot be undone!");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                CustomersDAO.deleteCustomer(selectedId);
                clientsTable.getItems().remove(selectedCustomer);
                clientsTable.refresh();
                for(Appointment appointment : customerAppointments){
                    AppointmentsDAO.deleteAppointment(appointment.getId());
                }
                refreshAppointmentsTable(AppointmentsDAO.getAppointments(SessionLog.getInstance().getUserId()));
            }
        }
        catch(Exception e){
           // Alert alert = new Alert(Alert.AlertType.ERROR);
            //alert.setTitle("Error");
            //alert.setHeaderText("An error has been found. Please try again!");
           // alert.setContentText("error");
            //alert.showAndWait();

            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error was found. Please try again!");
            // Create expandable Exception text area
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane
            alert.getDialogPane().setExpandableContent(expContent);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }

    /** Method to handle the sign out button referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSignOutButtonClick(ActionEvent actionEvent) throws IOException{
        SessionLog.resetInstance();
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ScheduleNow - Login");
        stage.setScene(scene);
        stage.show();
    }

    /** Method that handles the toggle button and filters appointment data according to each method name
     * Methods below include - onAllAppointments radio button, onCurrentMonth radio button, onCurrentWeek radio button
     * These radio buttons are placed within a toggle group found in the dashboard.fxml
     * @param actionEvent
     */
    public void onAllAppointmentsSelect(ActionEvent actionEvent){}

    public void onCurrentMonthSelect(ActionEvent actionEvent) throws SQLException{
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);
        filterByMonth(firstDayOfMonth, firstDayOfNextMonth);
    }

    public void onCurrentWeekSelect(ActionEvent actionEvent){}

    public void filterByMonth(ObservableList<Appointment> appointments) throws SQLException{}
    private void filterByMonth(LocalDate start, LocalDate end) {
        List<Appointment> appointments = appointmentObservableList.stream()
                .filter(appointment -> !appointment.getStart().toLocalDate().isBefore(start) &&
                        !appointment.getStart().toLocalDate().isAfter(end.minusDays(1)))
                .collect(Collectors.toList());
        refreshAppointmentsTable(appointments);
    }

    /** This method is intended to filter the appointments depending on which radio button is selected
     *
     * @throws SQLException
     */
    public void filterAppointments() throws SQLException{
        long userId = SessionLog.getInstance().getUserId();
        if(currentMonthButton.isSelected()) {
            List<Appointment> monthsAppointments = AppointmentsDAO.getAllAppointmentsByMonth(userId);
            refreshAppointmentsTable(monthsAppointments);
        }
        else if(currentWeekButton.isSelected()){
            List<Appointment> weeksAppointments = AppointmentsDAO.getAllAppointmentsByWeek(userId);
            refreshAppointmentsTable(weeksAppointments);
        }
        else if(allAppointmentsButton.isSelected()){
            List<Appointment> allAppointments = AppointmentsDAO.getAppointments(userId);
            refreshAppointmentsTable(allAppointments);
        }
    }


    /** Initializes the controller and sets up the data bindings for the tables and their corresponding table columns
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
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("i18n/login", Locale.getDefault());

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        appointmentsTable.refresh();

        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        clientStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        clientPostCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        clientsTable.refresh();

        appointmentToggle.selectedToggleProperty().addListener((observableValue, o, t1) -> {
            try {
                filterAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /** Method to hande the return to menu button referenced in - dashboard.fxml
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onReturnButtonClick(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("selection-menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("ScheduleNow - Menu");
        stage.setScene(scene);
        stage.show();
    }

}
