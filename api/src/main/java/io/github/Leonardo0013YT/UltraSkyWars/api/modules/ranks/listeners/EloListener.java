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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EloListener implements Listener {
    
    private final InjectionEloRank eloInjection;
    private final UltraSkyWarsApi plugin;
    
    public EloListener(UltraSkyWarsApi plugin, InjectionEloRank eloInjection){
        this.plugin = plugin;
        this.eloInjection = eloInjection;
    }
    
    @EventHandler
    public void onMenu(InventoryClickEvent e){
        if (e.getView().getTitle().equals(eloInjection.getRankeds().get("menus.title"))){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void loadPlayer(USWPlayerLoadEvent e){
        Player p = e.getPlayer();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            eloInjection.getErm().checkUpgrateOrDegrate(p);
        });
    }
    
}