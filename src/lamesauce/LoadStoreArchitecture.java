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
package lamesauce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lamesauce.message.SendObserver;

/**
 *
 * @author captnmo
 */
public class LoadStoreArchitecture {

    public static List<User> loadFromFile() {
        String savedUsers = null;
        try {
            savedUsers = new String(Files.readAllBytes(Paths.get("/binaries/authedUsers.txt")));
        } catch (IOException ex) {
            System.err.println(ex);
        }
        List<String> savedUsersList = savedUsers == null
                ? new ArrayList<>()
                : Arrays.asList(savedUsers.split("\\r?\\n"));
        Map<Boolean, List<String[]>> collect = savedUsersList.stream()
                .map(name -> name.split(";"))
                .collect(Collectors.partitioningBy(i -> "1".equals(i[1])));
        List<User> users = collect.entrySet().stream()
                .filter(Entry::getKey)
                .map((entry) -> entry.getValue().get(0))
                .map(all -> new AuthedUser(
                (List<SendObserver>) new SendObserver(Long.parseLong(all[2])),
                all[3],
                all[0])).collect(Collectors.toList());
        users.addAll((Collection<? extends User>) collect.entrySet().stream()
                .filter(entry -> !entry.getKey())
                .map((entry) -> entry.getValue().get(0))
                .map(all -> new User(
                        (List<SendObserver>) new SendObserver(Long.parseLong(all[2])),
                        all[3],
                        all[0])));

        return users;
    }

    public static void saveToFile(List<User> l) {
        System.err.println("Undefined");
    }
}
