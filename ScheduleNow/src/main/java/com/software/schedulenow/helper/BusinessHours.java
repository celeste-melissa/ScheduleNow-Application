package com.software.schedulenow.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

/**BusinessHours: a helper class providing utility functions for date and time operations.
 *
 */
public final class BusinessHours {
    /** DateTimeFormatter for formatting LocalDateTime objects in a specific pattern
     *
     */
 private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy - MM - dd HH:mm:00");
 public static String SQLFormat(LocalDateTime dateTime) {return TIMESTAMP.format(dateTime);}
 public static DateTimeFormatter getTimestamp(){return TIMESTAMP;}

    /** Combines a LocalDate from a DatePickers and a LocalTime from ComboBox into a LocalDateTime.
     *
     * @param datePicker
     * @param comboBox
     * @return
     */
 public static LocalDateTime gatherDateTime(DatePicker datePicker, ComboBox comboBox){
     LocalDate date = datePicker.getValue();
     LocalTime time = LocalTime.parse((CharSequence) comboBox.getValue());
     return LocalDateTime.of(date, time);
 }

    /** Generates a list of time strings in 15-minute intervals within business hours (8 AM to 10 PM)
     *
     * @return
     */
    public static ObservableList<String> timeOptions() {
        ObservableList<String> times = FXCollections.observableArrayList();
        for (int hour = 8; hour < 22; hour++) {
            for (int min = 0; min < 60; min += 15) {
                times.add(String.format("%02d:%02d", hour, min));
            }
        }
        return times;
    }

    public static void getDate(){

    }
}

