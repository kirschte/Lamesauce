/*
 * Copyright (C) 2017 captnmo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package lamesauce.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

/**
 *
 * @author captnmo
 */
public class User extends Observable {
    
    private final String userFirstName;
    private final String username;
    
    public User(String username, String firstName) {
        this.userFirstName = firstName;
        this.username = username;
    }

    /**
     * authorizes a username for changes
     *
     * @param user user which is authorized
     * @return
     */
    public User auth(User user) {
        setChanged();
        notifyObservers("I'm sorry " + userFirstName
                + ", I'm afraid I can't let you do that");
        return user;
    }
    
    public User deauth(User user) {
        setChanged();
        notifyObservers("I really hope that you're not trying to deauthorize someone when you're not allowed to!");
        return user;
    }

    /**
     * adds a quote to the hall of shame
     *
     * @param chat where to send notification
     * @param user username to check for auth
     * @param userFirstName users first name for the reply
     * @param quote text for the site (already formated)
     */
    public void addQuote(long chat, String user, String userFirstName, String quote) {
        setChanged();
        notifyObservers(new Object[]{
            chat, "I'm sorry " + userFirstName
            + ", I'm afraid I can't let you do that."
            + " However, I've added it to the "
            + "list for future approval."});
        addSH("#", quote);
    }
    
    protected final void addSH(String prefix, String quote) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM");
        Date date = new Date();
        try {
            Process p = new ProcessBuilder("/binaries/add.sh", prefix
                    + dateFormat.format(date) + ":"
                    + quote).start();
            
        } catch (IOException ex) {
            System.err.println("Can't find ADD.SH");
        }
    }
    
    public String getUserFirstName() {
        return userFirstName;
    }
    
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public boolean isAuthed() {
        return false;
    }
  
}
