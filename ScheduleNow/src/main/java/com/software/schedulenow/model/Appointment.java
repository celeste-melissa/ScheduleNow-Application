package com.software.schedulenow.model;

import java.time.LocalDateTime;

/** Appointment: a model class representing an appointment in a scheduling application
 *
 */
public class Appointment {
    // Fields to store appointment data
    private long id;
    private String title;
    private String type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String location;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private String createdBy;
    private String lastUpdatedBy;
    private long customerId;
    private long userId;
    private long contactId;

    /** Constructor with all fields
     *
     * @param id
     * @param title
     * @param type
     * @param description
     * @param start
     * @param end
     * @param location
     * @param createDate
     * @param lastUpdate
     * @param createdBy
     * @param lastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointment(long id, String title, String type, String description, LocalDateTime start, LocalDateTime end, String location, LocalDateTime createDate, LocalDateTime lastUpdate, String createdBy, String lastUpdatedBy, long customerId, long userId, long contactId) {
        // Initialization of all fields
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.location = location;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /** Constructor without the 'id' field for creating new appointments
     *
     * @param title
     * @param type
     * @param description
     * @param start
     * @param end
     * @param location
     * @param createDate
     * @param lastUpdate
     * @param createdBy
     * @param lastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointment(String title, String type, String description,LocalDateTime start,
                       LocalDateTime end, String location, LocalDateTime createDate, LocalDateTime lastUpdate, String createdBy, String lastUpdatedBy, long customerId, long userId, long contactId) {
    // Initialization of all fields
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.location = location;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /** Getters and setters for each field
     * These methods allow other parts of the application to access and modify the appointment data
     * @return
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreateDate(){return createDate;}
    public void setCreateDate(LocalDateTime createDate){this.createDate = createDate;}
    public LocalDateTime getLastUpdate(){return lastUpdate;}
    public void setLastUpdate(LocalDateTime lastUpdate){this.lastUpdate = lastUpdate;}
    public String getCreatedBy(){return createdBy;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
