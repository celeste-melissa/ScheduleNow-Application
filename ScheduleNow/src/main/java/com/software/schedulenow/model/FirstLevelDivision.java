package com.software.schedulenow.model;

import java.time.LocalDateTime;

/** FirstLevelDivision: a model class representing first level administrative divisions
 *
 */
public class FirstLevelDivision {
    // Fields representing division-specific details
    private long id;
    private String divisionName;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdate;
    private String createdBy;
    private String lastUpdateBy;
    private long countryId;

    /** Constructor to initialize a FirstLevelDivision object with specified values
     *
     * @param id
     * @param divisionName
     * @param dateCreated
     * @param lastUpdate
     * @param createdBy
     * @param lastUpdateBy
     * @param countryId
     */
    public FirstLevelDivision(long id, String divisionName, LocalDateTime dateCreated, LocalDateTime lastUpdate, String createdBy, String lastUpdateBy, long countryId){
        this.id = id;
        this.divisionName = divisionName;
        this.dateCreated = dateCreated;
        this.lastUpdate = lastUpdate;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.countryId = countryId;
    }


    //getters and setters for accessing and modifying the properties
    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getDivisionName(){return divisionName;}
    public void setDivisionName(String name){this.divisionName = divisionName;}
    public LocalDateTime getDateCreated(){return dateCreated;}
    public void setDateCreated(LocalDateTime dateCreated){this.dateCreated = dateCreated;}
    public LocalDateTime getLastUpdate(){return lastUpdate;}
    public void setLastUpdate(LocalDateTime lastUpdate){this.lastUpdate = lastUpdate;}
    public String getCreatedBy(){return createdBy;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public String getLastUpdateBy(){return lastUpdateBy;}
    public void setLastUpdateBy(String lastUpdateBy){this.lastUpdateBy = lastUpdateBy;}
    public long getCountryId(){return countryId;}
    public void setCountryId(long countryId){this.countryId = countryId;}

    /** Overrides the toString method for returning the division's name
     *
     * @return
     */
    @Override
    public String toString(){return this.getDivisionName();}
}
