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
package lamesauce.updateAppl;

import lamesauce.tasks.Instructions;
import lamesauce.tasks.Message;

/**
 * @author captnmo
 */
public class TelegramMessage extends Message {

    private final String username; //Absender
    private final String firstName; //Absender
    private final Instructions inst;

    /**
     * @param ID        chat: where to send notification
     * @param text      text for the site (already formated)
     * @param username  username to check for auth
     * @param firstName users first name for the reply
     * @param inst      instructions what to do
     */
    public TelegramMessage(long ID, String text, String username, String firstName, Instructions inst) {
        super(ID, text);
        this.username = username;
        this.firstName = firstName;
        this.inst = inst;
    }

    public final Instructions getInst() {
        return inst;
    }


    public final String getUsername() {
        return username;
    }

    public final String getFirstName() {
        return firstName;
    }


}
