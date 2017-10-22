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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author captnmo
 */
public class SendLogMessage implements SendMessages {
    @Override
    public SendMessages sendMessage(long userID, String text) {
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

        return this;
    }
}
