/* REMOVED DUE TO REPO SERVER DOWN
package io.github.Leonardo0013YT.UltraSkyWars.addons.economy;

import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.EconomyAddon;
import net.nifheim.beelzebu.coins.CoinsAPI;
import org.bukkit.entity.Player;

public class CoinsAddon implements EconomyAddon {
    
    public void addCoins(Player p, double amount){
        CoinsAPI.addCoins(p.getUniqueId(), amount, false);
    }
    
    public void setCoins(Player p, double amount){
        CoinsAPI.setCoins(p.getUniqueId(), amount);
    }
    
    public void removeCoins(Player p, double amount){
        CoinsAPI.takeCoins(p.getUniqueId(), amount);
    }
    
    public double getCoins(Player p){
        return CoinsAPI.getCoins(p.getUniqueId());
    }
}*/
