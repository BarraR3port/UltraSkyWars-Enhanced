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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.UltraInventory;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSoulWell is;
    
    public PlayerListener(UltraSkyWarsApi plugin, InjectionSoulWell is){
        this.plugin = plugin;
        this.is = is;
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        if (plugin.getCm().isSoulwellEnabled()){
            if (plugin.getIjm().getSoulwell().getSwm().isSession(p)){
                SoulWellSession sws = plugin.getIjm().getSoulwell().getSwm().getSession(p);
                if (sws.isRolling()){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.openInventory(sws.getInv()), 1);
                }
            }
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            Block b = e.getClickedBlock();
            if (plugin.getCm().isSoulwellEnabled()){
                if (is.getSwm().getSoulWells().containsKey(b.getLocation())){
                    if (!plugin.getGm().isPlayerInGame(p) && !(is.getSwm().isSession(p) && is.getSwm().getSession(p).isConfirm())){
                        if (plugin.getLvl().isEmpty()){
                            p.sendMessage(plugin.getLang().get(p, "messages.noRewards"));
                            e.setCancelled(true);
                            return;
                        }
                        SoulWell sw = is.getSwm().getSoulWells().get(b.getLocation());
                        e.setCancelled(true);
                        is.getSwm().addSession(p, sw);
                        UltraInventory inventory = plugin.getUim().getMenus("soulwellmenu");
                        plugin.getUim().openContentInventory(p, inventory);
                    }
                }
            }
        }
    }
    
}