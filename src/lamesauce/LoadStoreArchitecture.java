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

import lamesauce.user.AuthedUser;
import lamesauce.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * \<Storage\> ::= \<authed\>;\<username\>;\<userFirstName\>
 * \<authed\> ::= 0 | 1 //=> 0 eq not-authed; 1 eq authed
 *
 * @author captnmo
 */
public class LoadStoreArchitecture {

    private static final Path path = Paths.get("binaries/authedUsers.txt");

    static List<User> loadFromFile() {
        String savedUsers = null;
        try {
            savedUsers = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.err.println(e);
        }

        return savedUsers == null ?
                new ArrayList<>() :
                Arrays.stream(savedUsers.split("\\r?\\n"))
                        .map(line -> line.split(";"))
                        .filter(line -> line.length == 3
                                && line[0].trim().matches("[0|1]")
                                && line[1].trim().length() > 0
                                && line[2].trim().length() > 0)
                        .map(line -> "1".equals(line[0].trim()) ?
                                new AuthedUser(line[2].trim(), line[1].trim()) :
                                new User(line[2].trim(), line[1].trim())
                        ).collect(Collectors.toList());
    }


    static void saveToFile(List<User> user) {
        try {
            Files.write(path, user.stream().map(User::toString).collect(Collectors.toList()));
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
