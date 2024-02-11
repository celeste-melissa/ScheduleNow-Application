package com.software.schedulenow.helper;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** UserLog: A class for handling logging related to user activities
 *
 */

public class UserLog {
    /** A static logger instance to log user activities
     *
     */
    private static final Logger logger = Logger.getLogger("UserLog");

    /** A static block to configure the logger with a FileHandler and formatter
     *
     */
    static{
        FileHandler filerHandler = null;
        try{
            //Create a FileHandler to write log messages to a file named 'user_log.txt'
            //The 'true' parameter enables appending to the existing file
            filerHandler = new FileHandler("user_log.txt", true);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        /** Adding the FileHandler to the logger
         *
         */
        logger.addHandler(filerHandler);
        // Using SimpleFormatter to format log messages in a human-readable format
        SimpleFormatter formatter = new SimpleFormatter();
        filerHandler.setFormatter(formatter);
    }

    /** Method to log successful user login
     *
     * @param userName
     * @throws IOException
     */
    public static void stampUserLogin(String userName) throws IOException{
        // logs an info message when a user signs in
        logger.info( "Username: " + userName + " has signed into the application");
    }

    /** Method to log invalid user login attempts
     *
     * @param userName
     * @param password
     * @throws IOException
     */
    public static void stampInvalidUser(String userName, String password) throws IOException{
        // logs an info message with the username and password for failed login attempts
        logger.info("Invalid login attempt: " + "UserName: " + userName + "Password: " + password);
    }
}
