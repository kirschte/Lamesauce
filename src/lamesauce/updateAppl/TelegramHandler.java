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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import lamesauce.tasks.Instructions;
import lamesauce.Lamesauce;
import lamesauce.tasks.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * @author captnmo
 */
public class TelegramHandler extends TelegramLongPollingBot {

    private final String BOT_TOKEN = "450391506:AAHQnvWolEvu7iHv-TTy8tu39-rv0you_68";
    private final String BOT_USERNAME = "Lamesauce2_bot";

    private final List<Long> SQUAD //for logging purpose only
            = new ArrayList<>(Collections.singletonList(-135634788L)); //the telegram Chat ID
    private Helper hlp;

    /**
     * initialize. Only needed for BOT (have to be static)
     *
     * @param lm
     */
    public static TelegramHandler initTelegramHandler(Lamesauce lm) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        TelegramHandler th = new TelegramHandler(lm);
        try {
            botsApi.registerBot(th);
            //System.out.println("Bot successfully started");
            //System.out.println("...with the following authorized users:");
            //for (String i : user.getAuthedUsers()) {
            //    System.out.print(i + ", ");
            //}
            //System.out.println("");
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
        return th;
    }

    private TelegramHandler(Lamesauce lm) {
        this.hlp = new Helper(lm);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * where the real shit happens
     *
     * @param update UpdateInformation from TelegramAPI
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
            if (SQUAD.contains(chat)) {
                sendLogMessage(new Message(userID, messageRecieved));
            }

            if (!(messageRecieved.trim().isEmpty())) {
                identify(chat, username, userFirstName, messageRecieved);
                //houston, we have a tasks
            }

        }
    }

    private void identify(long chat, String username, String userFirstName, String message) {
        boolean foo = Instructions.getIdentifiers().stream().anyMatch("!hel"::contains);
        List<String> droppedWords =
                Arrays.stream(message.trim().split("\\s+"))
                        .dropWhile(s -> !Instructions.getIdentifiers().stream().anyMatch(s::contains))
                        .collect(Collectors.toList());
        assert droppedWords.size() > 0; //per definition. Empty message are filtered out previously
        String removed = droppedWords.remove(0);

        StringBuilder words = new StringBuilder();
        droppedWords.forEach(words::append);

        Instructions.containsIdent(removed)
                .ifPresent(instructions -> hlp.notify(
                        new TelegramMessage(
                                chat,
                                removed.replace(instructions.getIdentifier(), "")
                                        + words.toString(),
                                username,
                                userFirstName,
                                instructions)
                )); //else: a non-valid identifier --> nothing should happen
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    /**
     * sends a tasks
     *
     * @param msg Message. Contains ChatID (where to send) and msg (what to send)
     */
    @SuppressWarnings("deprecation")
    public void sendMessage(Message msg) {
        if (!msg.getText().isEmpty()) {
            SendMessage message = new SendMessage()
                    .setChatId(msg.getID())
                    .setText(msg.getText());
            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                System.err.println(e);
            }
        }
    }

    private void sendLogMessage(Message msg) {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm");
        Date date = new Date();

        //format: [00:02] <Jenny> Hello world
        System.out.println("[" +
                dateFormat.format(date) +
                "]" +
                " <" +
                msg.getID() +
                "> " +
                msg.getText());
    }

    @SuppressWarnings("deprecation")
    private class Helper extends Observable {

        private Helper(Lamesauce lm) {
            addObserver(lm);
        }

        private void notify(TelegramMessage tm) {
            setChanged();
            notifyObservers(tm);
        }
    }
}
