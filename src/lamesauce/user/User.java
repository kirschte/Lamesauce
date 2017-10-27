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

/**
 * @author captnmo
 */
public class User {

    private final String userFirstName;
    private final String username;

    public User(String username, String firstName) {
        this.username = username;
        this.userFirstName = firstName;
    }

    /**
     * authorizes a username for changes, if this is authenticated
     *
     * @param user user to authorize
     * @return either an authorized user or the same user
     */
    public ValueAndOutput<User, String> auth(User user) {
        return new ValueAndOutput<>(
                user
                , "I'm sorry " + userFirstName
                + ", I'm afraid I can't let you do that"
        );
    }

    /**
     * does nothing, but returning this in ValueAndOutput-Format
     *
     * @return this and empty String in ValueAndOutput-Format
     */
    public final ValueAndOutput<User, String> nothing() {
        return new ValueAndOutput<>(
                this
                , ""
        );
    }

    /**
     * deauthorizes a username for changes, if this and user are authenticated
     *
     * @param user user to deauthorize
     * @return either an deauthorized user or the same user
     */
    public ValueAndOutput<User, String> deauth(User user) {
        return new ValueAndOutput<>(
                user
                , "I really hope that you're not trying to deauthorize someone"
                + " when you're not allowed to!"
        );
    }

    /**
     * adds a quote to the hall of shame
     *
     * @param text the quote
     */
    public String addQuote(String text) {
        addSH("#", text);
        return "I'm sorry " + getUserFirstName()
                + ", I'm afraid I can't let you do that."
                + " However, I've added it to the "
                + "list for future approval.";
    }

    final void addSH(String prefix, String quote) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM");
        try {
            Process p = new ProcessBuilder("binaries/add.sh", prefix
                    + dateFormat.format(new Date()) + ":"
                    + quote).start();

        } catch (IOException ex) {
            System.err.println("Can't find ADD.SH");
        }
    }

    final String getUserFirstName() {
        return userFirstName;
    }

    /**
     * gets the username
     *
     * @return username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * get authed status
     *
     * @return true if authenticated, otherwise false
     */
    public boolean isAuthed() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return (userFirstName != null ? userFirstName.equals(user.userFirstName) : user.userFirstName == null)
                && (username != null ? username.equals(user.username) : user.username == null);
    }

    @Override
    public int hashCode() {
        int result = userFirstName != null ? userFirstName.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "0;" + getUserFirstName() + ";" + getUsername();
    }
}
