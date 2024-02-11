package com.software.schedulenow.controller;

import com.software.schedulenow.Main;
import com.software.schedulenow.DAO.UserDAO;
import com.software.schedulenow.model.User;
import com.software.schedulenow.helper.UserLog;
import com.software.schedulenow.helper.SessionLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/** This class is intended to handle the login scene
 *
 */
public class loginController implements Initializable {
    // Observable list for language options in the combo box
    private final ObservableList<String> languageInEnglish = FXCollections.observableArrayList("English", "French");

    @FXML
    private Label welcomeHeader;
    @FXML private Label signinLabel;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameText;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordText;

    @FXML
    private Button loginButton;

    @FXML
    private Label languageLabelSetting;
    @FXML
    private Label timeZoneLabel;

    private ResourceBundle rb;

    /** Method handling the login button click event referenced in - login.fxml
     *
     * @param mouseEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onClickLoginButton(MouseEvent mouseEvent) throws SQLException, IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        // Validate user credentials
        User validateUser = UserDAO.validateUser(username, password);
    // Handles invalid login credentials
        if (validateUser == null) {
            // Alert displayed for invalid login
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("invalidUserTitle"));
            alert.setContentText((rb.getString("invalidUserContent")));
            alert.setHeaderText(rb.getString("invalidUserHeader"));
            alert.showAndWait();
            UserLog.stampInvalidUser(username, password);
            return;
        }
        // Set user session and log user login activity

        SessionLog.getInstance(validateUser.getId(), validateUser.getUserName());
        UserLog.stampUserLogin(username);
        // Load the main application screen after successful login
        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("selection-menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        selectionMenuController SectionMenuController = fxmlLoader.getController();

        stage.setTitle("ScheduleNow - Welcome");
        stage.setScene(scene);
        stage.show();
    }

    /** Method to update language based on user selection
     *
     * @param language
     */
    public void updateLanguage(String language) {
        // Resource bundle for internationalization
        rb = ResourceBundle.getBundle("i18n/login", new Locale(language.equals("French") ? "fr" : "en"));
        // Update UI labels based on language selection
        usernameLabel.setText(rb.getString("usernameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        languageLabelSetting.setText(rb.getString("languageLabelSetting"));
        loginButton.setText(rb.getString("loginButtonLabel"));
        ZoneId zoneId = ZoneId.systemDefault();
        timeZoneLabel.setText(rb.getString("timeZoneLabel") + " : " + zoneId);
        welcomeHeader.setText(rb.getString("welcomeHeader"));
        signinLabel.setText(rb.getString("signinLabel"));
    }

    /** Initialize the login controller
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        languageComboBox.setItems(languageInEnglish);
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.getLanguage().equals("fr")){
        languageComboBox.setValue("Français");
        updateLanguage("French");

    }
    else{
        languageComboBox.setValue("English");
        updateLanguage("English");
    }
    // Listener for language selection changes
    languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateLanguage(newValue);

});
    }
}
