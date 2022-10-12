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
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsAddon implements EconomyAddon {
    
    private final PlayerPointsAPI pointsAPI;
    
    public PlayerPointsAddon(){
        pointsAPI = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
    }
    
    public void addCoins(Player p, double amount){
        if (pointsAPI != null){
            pointsAPI.give(p.getUniqueId(), (int) amount);
        }
    }
    
    public void setCoins(Player p, double amount){
        if (pointsAPI != null){
            pointsAPI.set(p.getUniqueId(), (int) amount);
        }
    }
    
    public void removeCoins(Player p, double amount){
        if (pointsAPI != null){
            pointsAPI.take(p.getUniqueId(), (int) amount);
        }
    }
    
    public double getCoins(Player p){
        return pointsAPI.look(p.getUniqueId());
    }
    
}