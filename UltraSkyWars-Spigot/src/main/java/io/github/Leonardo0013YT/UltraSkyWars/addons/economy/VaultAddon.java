/*
 * Copyright (c) 2022.
 *
 *  This program/library is free software: you can redistribute it and/or modify
 * it under the terms of the New BSD License (3-clause license).
 *
 * This program/library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the New BSD License (3-clause license)
 * for more details.
 *
 * You should have received a copy of the New BSD License (3-clause license)
 * along with this program/library; If not, see http://directory.fsf.org/wiki/License:BSD_3Clause/
 * for the New BSD License (3-clause license).
 *
 */

package io.github.Leonardo0013YT.UltraSkyWars.addons.economy;

import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.EconomyAddon;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAddon implements EconomyAddon {
    
    private Economy econ;
    
    public VaultAddon(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null){
            econ = rsp.getProvider();
        }
    }
    
    public void setCoins(Player p, double amount){
        if (econ != null){
            double now = econ.getBalance(p);
            econ.withdrawPlayer(p, now);
            econ.depositPlayer(p, amount);
        }
    }
    
    public void addCoins(Player p, double amount){
        if (econ != null){
            econ.depositPlayer(p, amount);
        }
    }
    
    public void removeCoins(Player p, double amount){
        if (econ != null){
            econ.withdrawPlayer(p, amount);
        }
    }
    
    public double getCoins(Player p){
        if (econ != null){
            return econ.getBalance(p);
        }
        return 0;
    }
    
}