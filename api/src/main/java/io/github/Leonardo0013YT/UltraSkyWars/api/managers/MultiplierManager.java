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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Multiplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiplierManager {
    
    private final Map<String, ArrayList<Multiplier>> multipliers = new HashMap<>();
    
    public void addMultiplier(int id, String type, String name, double amount, long remaining){
        if (!multipliers.containsKey(type)){
            multipliers.put(type, new ArrayList<>());
        }
        multipliers.get(type).add(new Multiplier(id, type, name, remaining, amount));
    }
    
    public Multiplier getServerMultiplier(String type){
        if (multipliers.containsKey(type)){
            Multiplier m = multipliers.get(type).get(0);
            if (m.getRemaining() < System.currentTimeMillis()){
                UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
                boolean removed = plugin.getDb().removeMultiplier(m.getId());
                if (removed){
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(plugin.getLang().get(p, "messages.multiplierFinish").replace("<name>", m.getName()).replace("<type>", m.getType())));
                }
                return null;
            }
            return m;
        }
        return null;
    }
    
    public double getPlayerMultiplier(Player p, String type){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (p.isOp() || p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".*")){
            return plugin.getCm().getMaxMultiplier();
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".4")){
            return 4.0;
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".3")){
            return 3.0;
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".2")){
            return 2.0;
        }
        return 1.0;
    }
    
    public double getPlayerMultiplier(Player p, String type, double amount){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (p.isOp() || p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".*")){
            return (plugin.getCm().getMaxMultiplier() * amount) - amount;
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".4")){
            return (4.0 * amount) - amount;
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".3")){
            return (3.0 * amount) - amount;
        }
        if (p.hasPermission("ultraskywars.multiplier." + type.toLowerCase() + ".2")){
            return (2.0 * amount) - amount;
        }
        return 0;
    }
    
    public void clear(){
        multipliers.clear();
    }
    
}