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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lamesauce.message.SendObserver;
import lamesauce.message.SendTelegramMessage;
import org.telegram.telegrambots.api.objects.Update;

/**
 *
 * @author paul
 */
public class Lamesauce extends SendTelegramMessage {

    private final String BOT_TOKEN = "LamesauceAPI";
    private final String BOT_USERNAME = "Lamesauce";

    public final List<Long> SQUAD
            = new ArrayList<>(Arrays.asList(-135634788L)); //the telegram Chat ID
    private List<SendObserver> so; //same lenth as SQUAD

    private List<User> user;

    public Lamesauce() {
        this.so = SQUAD.stream()
                .map(SendObserver::new)
                .collect(Collectors.toList());
        this.user = LoadStoreArchitecture.loadFromFile();

    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
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
            Optional<SendObserver> chatFirst = so.stream()
                    .filter(s -> s.getChat() == chat)
                    .findFirst();
            if (!chatFirst.isPresent()) {
                so.add(new SendObserver(chat, false));
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
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
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
