package com.hhsfbla.launch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Varun on 2/5/2017.
 */

public class UsernameAndPasswordStorage implements Serializable{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void storeCredentials(String saveLoc){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(saveLoc)));
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
