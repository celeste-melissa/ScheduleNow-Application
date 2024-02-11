package com.software.schedulenow.helper;



/** SessionLog : a singleton class designed to maintain a single instance of a user's session
 * Keeps track of the user currently logged in
 */
public class SessionLog {
    /**Static instance to ensure only one instance of SessionLog exists
     *
     */
    private static SessionLog instance;
    /**User-specific fields: userId and userName
     *
     */
    private final long userId;
    private final String userName;

    /** Constructor of SessionLog
     *
     * @param userId
     * @param userName
     */
    public SessionLog(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    /** (overloaded): returns the existing instance or creates a new one with specified user details
     *
     * @param userId
     * @param userName
     * @return
     */
    public static SessionLog getInstance(long userId, String userName) {
        if (instance == null) {
            instance = new SessionLog(userId, userName);
        }
        return instance;
    }

    /** Returns the existing instance or throws an exception if no instance is initialized
     *
     * @return
     */
    public static SessionLog getInstance(){
        if(instance == null){
            throw new IllegalStateException("No Instance Initialized");
        }
        return instance;
    }

    /** Resets the SessionLog instance, effectively ending the session
     *
     */
    public static void resetInstance() {
        instance = null;
    }

    /** Returns the user ID of the current session
     *
     * @return
     */

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return userName;
    }
}

