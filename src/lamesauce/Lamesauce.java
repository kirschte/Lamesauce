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

import lamesauce.message.Message;
import lamesauce.message.TelegramMessage;
import lamesauce.user.User;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author paul
 */
public class Lamesauce implements Observer {

    private List<User> user;
    private TelegramHandler bot;

    public Lamesauce() {
        this.user = LoadStoreArchitecture.loadFromFile();
        this.bot = new TelegramHandler(this);

    }

    private void authUser(TelegramMessage tm) {

    }

    private void deauthUser(TelegramMessage tm) {

    }

    private void bringBeer(TelegramMessage tm) {
        if (command.length == 1) {
            bringBeer(chat, 1);
        } else if (isNumeric(command[1].trim())
                && ((Integer.parseInt(command[1].trim())) > 0)
                && ((Integer.parseInt(command[1].trim())) < 4096)) {
            bringBeer(chat, Integer.parseInt(command[1].trim()));
        } else if (!isNumeric(command[1].trim())
                || ((Integer.parseInt(command[1].trim())) <= 0)
                || ((Integer.parseInt(command[1].trim())) > 4096)) {
            sendMessage(chat, "YOU DUCKFACE.");
        }
    }

    private void addQuote(TelegramMessage tm) {

    }

    private void alive() {

        sendMessage(chat, "Thank you for asking " + userFirstName
                + ". Yes, I'm alive and well.");
    }

    private StringBuilder goodMorning(TelegramMessage tm) {
        return (new StringBuilder()).append("Hello")
                .append(tm.getFirstName())
                .append(Character.toChars(128520))
                .append(",\nThanks for your question and I'm feeling glad that ")
                .append("I'm beeing able to answer your unique request:\n\n");
    }


    /**
     * lists all admins in chat
     *
     * @param tm TelegramMessage for response
     */
    private void listAdmins(TelegramMessage tm) {
        StringBuilder sb = new StringBuilder(goodMorning(tm))
                .append("The following users are authorized:\n");
        user.stream()
                .filter(User::isAuthed)
                .forEach(s -> sb.append(s.getUsername()).append("\n"));

        bot.sendMessage(new Message(tm.getID(), sb.toString()));

    }

    /**
     * sends a help dialog
     *
     * @param chat for help
     */
    private void helpDialog(TelegramMessage tm) {
        StringBuilder sb = new StringBuilder(goodMorning(tm));
        sb.append("The following commands are available:")
                .append(" \n\n")
                .append("!auth [username] - gives user rights, to add quotes without verfication")
                .append(" \n\n")

                .append("!deauth [username]- removes a users right to add quotes")
                .append(" \n\n")
                .append("!add MoSQL@[name:quote:context] - adds a quote to the hall of shame. When not authenticated, ")
                .append("the quote will undergo a review process and not appear on the site")
                .append(" \n\n")
                .append("!whoisadmin - lists the current authenticated users")
                .append(" \n\n")
                .append("!bringbeer {x ∈ ℕ | 0 < x < 4096} - brings a beer")
                .append(" \n\n");

        bot.sendMessage(new Message(tm.getID(), sb.toString()));
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
        if (arg instanceof TelegramMessage) {
            TelegramMessage tm = (TelegramMessage) arg;
            switch (tm.getInst()) {
                case ADMIN:
                    listAdmins(tm);
                    break;
                case HELP:
                    helpDialog(tm);
                    break;
            }
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
