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

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 *
 * @author captnmo
 */
public class SendTelegramMessage extends TelegramLongPollingBot implements SendMessages {

    /**
     * sends a message
     *
     * @param chat chatid, ie where to send
     * @param text what to send
     * @return
     */
    @Override
    public SendMessages sendMessage(long chat, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chat)
                .setText(text);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
        return this;
    }

    @Override
    public String getBotToken() {
        throw new AbstractMethodError("Not allowed");
    }

    @Override
    public void onUpdateReceived(Update update) {
        throw new AbstractMethodError("Not allowed");
    }

    @Override
    public String getBotUsername() {
        throw new AbstractMethodError("Not allowed");
    }

}
