/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamesauce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author paul
 */
public class User {
    
    private String[] knownUsers = {};

    User() throws IOException {
        initAuthedUsers(true);
    }

    /**
     * Finds the index of a user
     * @param value
     * @return index of user, or when not found, -1
     */
    private int indexOf(String value) {
        int i = 0;
        while (i < knownUsers.length
                && !(knownUsers[i].equals(value))) {
            i++;
        }
        if (i < knownUsers.length) {
            return i;
        } else {
            return -1;
        }
    }

    /**
     * finds a user in the list
     * @param value
     * @return
     */
    private boolean find(String value) {
        return indexOf(value) != -1;
    }
    /**
     * Removes a user at array index x
     * @param index 
     */
    private void removeAtIndex(int index) {
        if (index < 0 || index >= knownUsers.length) {
            System.err.println("Fehler: Falsche Löschposition");
        } else {
            String[] newArr = new String[knownUsers.length - 1];
            System.arraycopy(knownUsers, 0, newArr, 0, index);
            System.arraycopy(knownUsers, index + 1, newArr, index,
                    newArr.length - index);
            knownUsers = newArr;
        }
    }

    /**
     * checks if a user is authed
     *
     * @param username
     * @return boolean
     */
    public boolean isAuthed(String username) {
        boolean auth = false;
        for (String user : knownUsers) {
            if (user.equals(username)) {
                auth = true;
            }
        }
        return auth;
    }

    /**
     * adds a user to the authorized users
     *
     * @param username
     */
    public void addAuthedUser(String username) {
        if (!find(username)) {
            int newLength = knownUsers.length;
            String[] newArr = new String[newLength + 1];
            System.arraycopy(knownUsers, 0, newArr, 0, (newLength));
            newArr[newLength] = username;
            knownUsers = newArr;
        } else {
        }
    }

    /**
     * gets authed users
     *
     * @return Array with all current authed users
     */
    public String[] getAuthedUsers() {
        return knownUsers;
    }

    /**
     * Loads saved authed users into the program
     * @param overwrite true, when all permissions need to be overwriten, false when not
     * @return 
     * @throws IOException
     */
    public String[] initAuthedUsers(boolean overwrite) throws IOException {
        String savedUsers = new String(Files.readAllBytes(Paths.get("authedUsers.txt")));
        String[] savedUsersArray = savedUsers.split("\\r?\\n");
        
        if (overwrite) {
            knownUsers = savedUsersArray;
        }
        
        return savedUsersArray;
    }

    /**
     * DEPRICATED  - use init instead or for reloading without overwriting
     * @throws IOException
     */
    public void loadAuthedUsers() throws IOException {
        String[] loadUsers = initAuthedUsers(false);
        for (String i : loadUsers) {
            addAuthedUser(i);
        }

    }

    /**
     * Deauthorizes a user
     * @param name which is to be removed
     * @return true, when removed, false, when not
     */
    public boolean removeAuthedUser(String name) {
        boolean success = false;

        if (find(name.trim())) {
            //wenn gefunden
            removeAtIndex(indexOf(name));
            success = true;
        } 
        return success;
    }

}
