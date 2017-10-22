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

/**
 *
 * @author paul
 */
public class CoolDown {
    private String[] Cooldown = {};
    
    public void setCooldown(String user, int secs){
        String[] tmp = new String[Cooldown.length +2];
        System.arraycopy(Cooldown, 0, tmp, 0, Cooldown.length);
        tmp[Cooldown.length +1] = user;
        tmp[Cooldown.length +2] = Integer.toString(secs);  
    }
     
   public boolean hasCooldown(String user){
       boolean tmp = false;
       
       for (int i = 0; i < Cooldown.length; i  += 2){
           if (Cooldown[i].equals(user)){
               tmp = true;
           }
       }
       return tmp;
    }
    
   public int getCooldownSecs(String user){
       return 0;
   }
            
}
