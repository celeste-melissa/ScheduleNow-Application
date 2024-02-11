package com.software.schedulenow;

import javafx.fxml.FXMLLoader;
import com.software.schedulenow.helper.JDBC;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class Main extends Application {
    /**This method is the main entry point for ScheduleNow application
     * @author Celeste Catala - ScheduleNow Application C195
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        //Create a new scene with the loaded FXML and sets the dimensions
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        // Set the title of the primary stage (window)
        stage.setTitle("ScheduleNow Application");
        //Set the scene for the primary stage
        stage.setScene(scene);
        //Display the primary stage
        stage.show();
    }

    /** The main method of ScheduleNow application
     *
     * @param args
     */
    public static void main(String [] args){
        //Launch the JDBC connection when the application starts
        JDBC.openConnection();
        //Launch the JavaFX application.
        launch(args);
        //Close the JDBC connection when the application exits
        JDBC.closeConnection();
    }
}
