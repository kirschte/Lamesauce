/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamesauce;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 *
 * @author paul
 */
public class Lamesauce extends TelegramLongPollingBot {

    private final String BOT_TOKEN = "";
    private final String BOT_USERNAME = "Lamesauce";

    User user;

    public Lamesauce() throws IOException {
        this.user = new User();
    }
    
    /**
     * Gets bot Token
     *
     * @return bots token
     */
    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * sends a message
     *
     * @param chat chatid, ie where to send
     * @param text what to send
     */
    private void sendMessage(long chat, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chat)
                .setText(text);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a quote to the hall of shame
     *
     * @param chat where to send notification
     * @param user username to check for auth
     * @param userFirstName users first name for the reply
     * @param quote text for the site (already formated)
     */
    private void addQuote(long chat, String user, String userFirstName, String quote) {
        if (this.user.isAuthed(user)) {
            sendMessage(chat, "Adding message to the hall of shame >:)");

            DateFormat dateFormat = new SimpleDateFormat("dd.MM");
            Date date = new Date();

            try {
                Process p = new ProcessBuilder("./addBot.sh", ""
                        + dateFormat.format(date).toString() + ":"
                        + quote + "").start();

            } catch (IOException ex) {
                Logger.getLogger(Lamesauce.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sendMessage(chat, "I'm sorry " + userFirstName
                    + ", I'm afraid I can't let you do that."
                    + " However, I've added it to the "
                    + "list for future approval.");

            DateFormat dateFormat = new SimpleDateFormat("dd.MM");
            Date date = new Date();

            try {
                Process p = new ProcessBuilder("./addBot.sh", "#"
                        + dateFormat.format(date).toString() + ":"
                        + quote + "").start();

            } catch (IOException ex) {
                Logger.getLogger(Lamesauce.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * authorizes a username for changes
     *
     * @param chat for response
     * @param user username which is authorized
     * @param userFirstName for reply purposes
     * @param args the supplied username
     */
    private void authUser(long chat, String user, String userFirstName, String args) {
        if (this.user.isAuthed(user)) {
            this.user.addAuthedUser(args);
            sendMessage(chat, "User " + args
                    + " has been authorized for changes!");

        } else {
            sendMessage(chat, "I'm sorry " + userFirstName
                    + ", I'm afraid I can't let you do that");

        }
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

    private void deauthUser(long chat, String user, String args) {
        if (this.user.isAuthed(user)) {
            if (this.user.removeAuthedUser(args)) {
                sendMessage(chat, "User " + args + " has been deauthorized!");
            } else {
                sendMessage(chat, "User is not authorized!");
            }

        } else {
            sendMessage(chat, "I really hope that you're not trying to deauthorize someone when you're not allowed to!");
        }
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
     * @param number
     * @return result
     */
    private static boolean isNumeric(String number) {
        return number.matches("\\d+");
    }
    
    private static void log(long userID, String text ){
    
        DateFormat dateFormat = new SimpleDateFormat("kk:mm");
            Date date = new Date();
            
        //format: [00:02] <Jenny> Hello world
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(dateFormat.format(date));
        sb.append("]");
        sb.append(" <");
        sb.append(userID);
        sb.append("> ");
        sb.append(text);
        System.out.println(sb.toString());
    }
    
    /**
     * where the real shit happens
     *
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        //real shit happes here
        if (update.hasMessage() && update.getMessage().hasText()) {
            //extract data
            long userID = update.getMessage().getFrom().getId();
            
            String username = update.getMessage().getFrom().getUserName();
            String userFirstName = update.getMessage().getFrom().getFirstName();
            String messageRecieved = update.getMessage().getText();
            long chat = update.getMessage().getChatId();
            if (chat == -135634788){
                log(userID, messageRecieved);
            }
            
            
            
            if (!(messageRecieved.trim().isEmpty())) {
                String[] command = messageRecieved.split(" ", 2);
                //houston, we have a message

                switch (command[0]) {
                    case "!add":
                        addQuote(chat, username, userFirstName, command[1]);
                        break;

                    case "!auth":
                        authUser(chat, username, userFirstName, command[1]);
                        break;

                    case "!areyoualive":
                        sendMessage(chat, "Thank you for asking " + userFirstName
                                + ". Yes, I'm alive and well.");

                        break;
                    case "!whoisadmin":
                        listAdmins(chat);
                        break;

                    case "!deauth":
                        deauthUser(chat, username, command[1]);
                        break;
                    case "!help":
                        helpDialog(chat);
                        break;
                    case "!bringbeer":
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

                        break;

                    default:
                        break;
                }
            }

        }
    }

    /**
     * Gets bot Username
     *
     * @return bots Username
     */
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        User user = new User();
        try {
            botsApi.registerBot(new Lamesauce());
            //System.out.println("Bot successfully started");
            //System.out.println("...with the following authorized users:");
            //for (String i : user.getAuthedUsers()) {
            //    System.out.print(i + ", ");
            //}
            //System.out.println("");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
