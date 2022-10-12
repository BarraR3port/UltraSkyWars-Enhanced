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

package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e){
        remove(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent e){
        remove(e.getPlayer());
    }
    
    private void remove(Player p){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        plugin.getCos().remove(p);
        plugin.getLvl().remove(p);
        plugin.getSb().remove(p);
        plugin.getUim().removeInventory(p);
        if (plugin.getIjm().isSoulWellInjection()){
            plugin.getIjm().getSoulwell().getSwm().removeSession(p);
        }
        plugin.getGm().removePlayerAllGame(p);
        plugin.getDb().savePlayer(p);
    }
    
}