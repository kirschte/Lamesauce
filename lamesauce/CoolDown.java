/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
