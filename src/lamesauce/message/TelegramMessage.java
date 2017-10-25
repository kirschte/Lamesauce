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
package lamesauce.message;

import lamesauce.Instructions;

/**
 *
 * @author captnmo
 */
public class TelegramMessage extends Message {
    
    private final String username; //Absender
    private final String firstName; //Absender
    private final Instructions inst;
    
    public TelegramMessage(long ID, String text, String username, String firstName, Instructions inst) {
        super(ID, text);
        this.username = username;
        this.firstName = firstName;
        this.inst = inst;
    }

    public Instructions getInst() {
        return inst;
    }
    
    

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }
    
    
    
}
