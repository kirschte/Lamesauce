/*
 * Copyright (C) 2017 paul
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
package lamesauce;

import lamesauce.user.User;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author paul
 */
public class Lamesauce implements Observer {

    private List<User> user;
    private TelegramHandler bot;

    public Lamesauce() {
        this.user = LoadStoreArchitecture.loadFromFile();
        this.bot = new TelegramHandler(this);

    }

    /**
     * lists all admins in chat
     *
     * @param chat for response
     */
    private void listAdmins(long chat) {

        StringBuilder sb = new StringBuilder();
        for (String i : user.getAuthedUsers()) {
            sb.append(i);
            sb.append(" \n");
        }
        sendMessage(chat, "The following users are authorized: \n" + sb.toString());

    }

    /**
     * sends a help dialog
     *
     * @param chat for help
     */
    private void helpDialog(long chat) {
        StringBuilder sb = new StringBuilder();
        sb.append("The following commands are available:");
        sb.append(" \n\n");
        sb.append("!auth [username] - gives user rights, to add quotes without verfication");
        sb.append(" \n\n");

        sb.append("!deauth [username]- removes a users right to add quotes");
        sb.append(" \n\n");
        sb.append("!add [name:quote:context] - adds a quote to the hall of shame. When not authenticated, "
                + "the quote will undergo a review process and not appear on the site");
        sb.append(" \n\n");
        sb.append("!whoisadmin - lists the current authenticated users");
        sb.append(" \n\n");
        sb.append("!bringbeer - brings a beer");
        sb.append(" \n\n");

        sendMessage(chat, sb.toString());
    }

    /**
     * brings a beer to the chat
     *
     * @param chat for the response
     */
    private void bringBeer(long chat, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(Character.toChars(127866));
        }

        sendMessage(chat, sb.toString());
    }

    /**
     * checks if a number is numeric
     *
     * @param number
     * @return result
     */
    private static boolean isNumeric(String number) {
        return number.matches("\\d+");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Object[]
                && ((Object[]) arg).length == 3
                && ((Object[]) arg)[0] instanceof Long
                && ((Object[]) arg)[1] instanceof Instructions
                && ((Object[]) arg)[2] instanceof String[]) {
            long chat = (Long) ((Object[]) arg)[0];
            Instructions inst = (Instructions) ((Object[]) arg)[1];
            String[] params = (String[]) ((Object[]) arg)[2];
            assert params.length == inst.getCountOfParameters();
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Lamesauce l = new Lamesauce();  
    }

}
