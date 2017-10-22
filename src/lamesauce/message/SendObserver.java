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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import lamesauce.User;

/**
 *
 * @author captnmo
 */
public class SendObserver implements Observer {

    private final ArrayList<SendMessages> msg;
    private Long chat;

    public SendObserver(long chat) {
        this(chat, true);
    }

    public SendObserver(long chat, boolean detailed) {
        this.msg = new ArrayList<>();
        this.msg.add(new SendTelegramMessage());
        if (detailed) {
            this.msg.add(new SendLogMessage());
        }
        this.chat = chat;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String
                && o instanceof User
                && ((User) o).containsChat(chat)) {
            String text = (String) arg;
            msg.stream()
                    .forEach(send -> send.sendMessage(chat, text));
        }
    }

    public Long getChat() {
        return chat;
    }
}
