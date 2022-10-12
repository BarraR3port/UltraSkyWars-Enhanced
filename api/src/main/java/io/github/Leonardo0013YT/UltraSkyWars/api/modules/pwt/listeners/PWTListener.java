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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.pwt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PWTListener implements Listener {
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        for ( Player on : Bukkit.getOnlinePlayers() ){
            if (p.getWorld().getName().equals(on.getWorld().getName())) continue;
            on.hidePlayer(p);
            p.hidePlayer(on);
        }
    }
    
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Location from = e.getFrom();
        Location to = e.getTo();
        if (to == null || from == null || to.getWorld() == null || from.getWorld() == null) return;
        if (to.getWorld().getName().equals(from.getWorld().getName())){
            return;
        }
        Player p = e.getPlayer();
        World wTo = to.getWorld();
        World wFrom = from.getWorld();
        wFrom.getPlayers().forEach(on -> on.hidePlayer(p));
        wFrom.getPlayers().forEach(p::hidePlayer);
        wTo.getPlayers().forEach(pl -> pl.showPlayer(p));
        wTo.getPlayers().forEach(p::showPlayer);
    }
    
}