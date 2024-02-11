package com.software.schedulenow.model;

/** Contact: a model class representing a contact entity
 *
 */
public class Contact {
    // Fields to store contact information
    private long id;
    private String name;
    private String email;

    /** Constructor to initialize the contact with given values
     *
     * @param id
     * @param name
     * @param email
     */
    public Contact(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /** Getters and setters for each field
     * These methods provide controlled access to and modification of the contact's data
     *
     * @return
     */
    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    /** Override the toString method to represent the contact by name when converted to a String
     *
     * @return
     */
    @Override
    public String toString(){return this.name;}
}
