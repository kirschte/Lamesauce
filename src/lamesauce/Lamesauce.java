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

import lamesauce.tasks.Message;
import lamesauce.updateAppl.TelegramMessage;
import lamesauce.updateAppl.TelegramHandler;
import lamesauce.user.User;
import lamesauce.user.ValueAndOutput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author paul
 * @author captnmo
 */
@SuppressWarnings("deprecation")
public class Lamesauce implements Observer {

    /**
     * maximal amount of beerprinting
     */
    private final int BEER_CONSTANT = 4096;
    /**
     * A list of Authenticated or Not-Authenticated user
     */
    private List<User> user;
    /**
     * The Telegram Handler. Used to send TelegramMessages.
     * Informs this about new messages with fitting keywords with an observer.
     */
    private TelegramHandler bot;

    public Lamesauce() {
        this.user = LoadStoreArchitecture.loadFromFile();
        this.bot = TelegramHandler.initTelegramHandler(this);

    }

    /**
     * returns User or no User, if a User in the List could be identified.
     *
     * @param username the username to identify user in given List
     * @return optional user or no User
     */
    private Optional<User> getUserFromName(String username) {
        return user.stream()
                .filter(s -> s.getUsername().equals(username))
                .findFirst();
    }

    /**
     * authenticate all Users specified in any constellation the tasks.
     * If authenticating user doesn't exist, create a new one.
     * If the authenticating user is not authenticated, do nothing.
     *
     * @param tm the TelegramMessage
     */
    private void authUser(TelegramMessage tm) {
        getUserFromName(tm.getUsername())
                .ifPresentOrElse(u -> user = user.stream() //geht auch ohne user=?!
                                .map(s -> tm.getText().contains(s.getUsername()) ? u.auth(s) : s.nothing())
                                .peek(s -> bot.sendMessage(new Message(tm.getID(), s.getOutput())))
                                .map(ValueAndOutput::getValue)
                                .collect(Collectors.toList())
                        , () -> user.add(new User(tm.getUsername(), tm.getFirstName())));
    }

    /**
     * deauthenticate all Users specified in any constellation the tasks.
     * If authenticating user doesn't exist, create a new one.
     * If the authenticating user is not authenticated, do nothing.
     *
     * @param tm the TelegramMessage
     */
    private void deauthUser(TelegramMessage tm) {
        getUserFromName(tm.getUsername())
                .ifPresentOrElse(u -> user = user.stream() //geht auch ohne user=?!
                                .map(s -> tm.getText().contains(s.getUsername()) ? u.deauth(s) : s.nothing())
                                .peek(s -> bot.sendMessage(new Message(tm.getID(), s.getOutput())))
                                .map(ValueAndOutput::getValue)
                                .collect(Collectors.toList())
                        , () -> user.add(new User(tm.getUsername(), tm.getFirstName())));
    }

    /**
     * determines if and how many beer should be send.
     * This functions gets the first number in the Message
     * after the keyword for bringing beer.
     * If no number is available or it is out of range
     * the user will be signalized his stupid attend.
     *
     * @param tm the TelegramMessage
     */
    private void bringBeer(TelegramMessage tm) {
        if (tm.getText().trim().isEmpty()) {
            bringBeer(tm, 1);
        } else {
            Arrays.stream(tm.getText().split("\\D+"))
                    .filter(n -> n.trim().length() > 0)
                    .map(Integer::parseInt)
                    .filter(s -> 0 < s && s < BEER_CONSTANT)
                    .findFirst()
                    .ifPresentOrElse(n -> bringBeer(tm, n)
                            , () -> bot.sendMessage(new Message(tm.getID(), "YOU DUCKFACE.")));
        }
    }

    /**
     * brings a beer to the chat
     *
     * @param tm for the response
     */
    private void bringBeer(TelegramMessage tm, int amount) {
        StringBuilder sb = new StringBuilder(amount);
        for (int i = 0; i < amount; i++) {
            sb.append(Character.toChars(127866));
        }

        bot.sendMessage(new Message(tm.getID(), sb.toString()));
    }

    private void addQuote(TelegramMessage tm) {
        if (invMoSQL(tm.getText())) {
            getUserFromName(tm.getUsername())
                    .ifPresentOrElse(u -> u.addQuote(tm.getText())
                            , () -> user.add(new User(tm.getUsername(), tm.getFirstName())));
        } else {
            bot.sendMessage(new Message(
                    tm.getID()
                    , "You are even so stupid, that you can't use MoSQL properly…"));
        }
    }

    private boolean invMoSQL(String str) {
        return Arrays.stream(str.split("(?<!:):(?!:)"))
                .filter(s -> s.trim().length() != 0)
                .count() == 3;
    }

    private void alive(TelegramMessage tm) {
        bot.sendMessage(new Message(tm.getID(), goodMorning(tm)
                + "Yes I'm alive and well."));
    }

    private String goodMorning(TelegramMessage tm) {
        return "Hello "
                + tm.getFirstName() + " "
                + new String(Character.toChars(128520), 0, 2)
                + ",\nThanks for asking your question.\nI'm feeling glad that "
                + "I'm beeing able to answer your unique request:\n\n";
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
     * @param tm for help
     */
    private void helpDialog(TelegramMessage tm) {
        String sb = "The following commands are available:" +
                " \n\n" +
                "!auth [username] - gives user rights, to add quotes without verfication" +
                " \n\n" +
                "!deauth [username]- removes a users right to add quotes" +
                " \n\n" +
                "!add MoSQL@[name:quote:context] - adds a quote to the hall of shame. When not authenticated, " +
                "the quote will undergo a review process and not appear on the site" +
                " \n\n" +
                "!whoisadmin - lists the current authenticated users" +
                " \n\n" +
                "!bringbeer {x ∈ ℕ | 0 < x < 4096} - brings a beer" +
                " \n\n";

        bot.sendMessage(new Message(tm.getID(), sb));
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
                case ALIVE:
                    alive(tm);
                    break;
                case AUTH:
                    authUser(tm);
                    break;
                case DEAUTH:
                    deauthUser(tm);
                    break;
                case BEER:
                    bringBeer(tm);
                    break;
                case ADD:
                    addQuote(tm);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Lamesauce l = new Lamesauce();
    }

}
