package com.software.schedulenow.model;

/** User: a model class that defines the 'user' representing the user entity
 *
 */
public class User {
    // Declares private variables
    //id for unique identification
    // username and password for authentication
    private long id;
    private String userName;
    private String password;

    /** Full constructor initializing all the class fields, typically used when creating a new User object with all its
     * details
     * @param id
     * @param userName
     * @param password
     */
    public User(long id, String userName, String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    /** Partial constructor for creating a user object without specifying the 'id'
     *
     * @param userName
     * @param password
     */
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
    //Getters and setters
    public long  getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getUserName(){return userName;}
    public void setUserName(String userName){this.userName = userName;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    /** Overrides toString method to return the user's name
     * Useful for printing or logging the User object in a human-readable format
     * @return
     */

    @Override
    public String toString(){return this.getUserName();}
}
