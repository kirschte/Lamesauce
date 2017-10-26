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
package lamesauce.user;


/**
 * @author captnmo
 */
public class AuthedUser extends User {

    public AuthedUser(String username, String userFirstName) {
        super(username, userFirstName);
    }

    @Override
    public ValueAndOutput<User, String> auth(User user) {
        return new ValueAndOutput<>(new AuthedUser(
                user.getUsername(), user.getUserFirstName())
                , "User " + user.getUserFirstName()
                + " has been authorized for changes!"
        );
    }

    @Override
    public ValueAndOutput<User, String> deauth(User user) {
        return user.isAuthed() ?
                new ValueAndOutput<>(
                        new User(user.getUsername(), user.getUserFirstName())
                        , "User " + user.getUserFirstName() + " has been deauthorized!"
                ) :
                new ValueAndOutput<>(
                        user
                        , "User " + user.getUserFirstName() + " is not authorized!"
                );
    }

    @Override
    public boolean isAuthed() {
        return true;
    }

    @Override
    public String addQuote(String quote) {
        addSH("", quote);
        return "Adding tasks to the hall of shame >:)";
    }

}
