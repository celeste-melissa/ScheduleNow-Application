package com.software.schedulenow.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Customer: a model class representing a customer entity
 *
 */
public class Customer {
    // Fields representing customer-specific details
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String postalCode;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private String createdBy;
    private String lastUpdatedBy;
    private long divisionId;
    private String state;

    /**
     * Constructor to initialize a customer object with specified values
     *
     * @param id
     * @param name
     * @param address
     * @param phoneNumber
     * @param postalCode
     * @param createDate
     * @param lastUpdate
     * @param createdBy
     * @param lastUpdatedBy
     * @param divisionId
     */
    public Customer(long id, String name, String address, String phoneNumber, String postalCode, LocalDateTime createDate, LocalDateTime lastUpdate,
                    String createdBy, String lastUpdatedBy, long divisionId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;


    }

    /** Alternative constructor without the 'id' field, typically used for creating new customer records
     *
     * @param name
     * @param address
     * @param phoneNumber
     * @param postalCode
     * @param createDate
     * @param lastUpdate
     * @param createdBy
     * @param lastUpdatedBy
     * @param divisionId
     */
    public Customer(String name, String address, String phoneNumber, String postalCode, LocalDateTime createDate, LocalDateTime lastUpdate,String createdBy,String lastUpdatedBy, long divisionId){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public Customer(long id, String name, String address, String phoneNumber, String postalCode, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, long divisionId) {
    }

    //Getters and setters for accessing and modifying the properties
    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public  String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}

    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public String getPostalCode(){return postalCode;}
    public void setPostalCode(String postalCode){this.postalCode = postalCode;}
    public LocalDateTime getCreateDate(){return createDate;}
    public void setCreateDate(LocalDateTime createDate){this.createDate = createDate;}
    public LocalDateTime getLastUpdate(){return lastUpdate;}
    public void setLastUpdate(LocalDateTime lastUpdate){this.lastUpdate = lastUpdate;}

    public String getCreatedBy(){return createdBy;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}
    public long getDivisionId(){return divisionId;}
    public void setDivisionId(long divisionId){this.divisionId = divisionId;}

    public String getState(){
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    /** Overrides the toString method for returning the customer's name
     *
     * @return
     */
    @Override
    public String toString(){return this.getName();}

}