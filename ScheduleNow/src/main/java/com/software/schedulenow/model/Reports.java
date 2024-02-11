package com.software.schedulenow.model;

import java.time.Month;

/** Reports: model class implies its use in generating report-related data
 *
 */
public class Reports {
    // Field attributes for this class
    private Month month;
    private String appointmentType;
    private int appointmentCount;
    private Customer customer;
    private int customerCount;
    private FirstLevelDivision division;
    private String divisionName;

    /** Constructor that gather division names and their corresponding customer count
     *
     * @param divisionName
     * @param customerCount
     */
    public Reports(String divisionName, int customerCount) {
        this.customerCount = customerCount;
        this.divisionName = divisionName;
    }

    /** Constructor that gathers the customer and customer count
     *
     * @param customer
     * @param customerCount
     */

    public Reports(Customer customer, int customerCount){
        this.customer = customer;
        this.customerCount = customerCount;
    }

    /** Constructor that initializes a report based ona division and the count of customers in that division
     *
     * @param division
     * @param customerCount
     */

    public Reports(FirstLevelDivision division, int customerCount){
        this.customerCount = customerCount;
        this.division = division;
    }

    /** Constructs a report for a specific month, appointment type, and count
     * Useful for monthly activity reports
     * @param month
     * @param appointmentType
     * @param appointmentCount
     */
    public Reports(Month month, String appointmentType, int appointmentCount){
        this.appointmentType = appointmentType;
        this.month = month;
        this.appointmentCount = appointmentCount;
    }

    /** Creates a report from a single appointment
     *
     * @param appointment
     */
    public Reports(Appointment appointment) {
        this.month = appointment.getStart().getMonth();
        this.appointmentType = appointment.getType();
        this.appointmentCount = 1;
    }
    //getters and setters
    public FirstLevelDivision getDivision() {
        return this.division;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(FirstLevelDivision division) {
        this.division = division;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {this.appointmentType = appointmentType;}
}
