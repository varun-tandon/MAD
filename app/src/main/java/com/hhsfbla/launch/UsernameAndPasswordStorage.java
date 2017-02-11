package com.hhsfbla.launch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A helper class that stores the username and password of the user onto the SD card of the device
 */

public class UsernameAndPasswordStorage implements Serializable{
    /**
     * the username and password to be stored
     */
    private String username;
    private String password;

    /**
     * A getter class
     * @return the String username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * A setter class
     * @param username the String username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * A getter class
     * @return the String password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * A setter class
     * @param password the String password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * A helper method that stores the user information onto the device's SD card
     */
    public void storeCredentials(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/saveUserData.bin"))); //creates a new stream that follows the supplied path and stores the information at that path
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
