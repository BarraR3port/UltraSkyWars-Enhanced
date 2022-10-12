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
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SpectatorListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    
    public SpectatorListener(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null){
            return;
        }
        if (game.getSpectators().contains(p)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null){
            return;
        }
        if (game.getSpectators().contains(p)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onChest(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null){
            if (game.getSpectators().contains(p)){
                e.setCancelled(true);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null){
            if (game.getSpectators().contains(p)){
                if (e.getClick().isShiftClick()){
                    Inventory clicked = e.getClickedInventory();
                    if (clicked == e.getWhoClicked().getInventory()){
                        ItemStack clickedOn = e.getCurrentItem();
                        if (clickedOn != null){
                            e.setCancelled(true);
                        }
                    }
                }
                Inventory clicked = e.getClickedInventory();
                if (clicked != e.getWhoClicked().getInventory()){
                    ItemStack onCursor = e.getCursor();
                    if (onCursor != null){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDrag(InventoryDragEvent e){
        Player p = (Player) e.getWhoClicked();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null){
            if (game.getSpectators().contains(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    protected void onEntityDamageEvent(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player d = (Player) e.getDamager();
            Game game = plugin.getGm().getGameByPlayer(d);
            if (game != null && game.getSpectators().contains(d)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    protected void onPotionSplash(PotionSplashEvent e){
        ThrownPotion potion = e.getPotion();
        ArrayList<Player> online = new ArrayList<>();
        for ( LivingEntity ent : e.getAffectedEntities() ){
            if (!(ent instanceof Player)) continue;
            Player p = (Player) ent;
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (game.getSpectators().contains(p)){
                    online.add(p);
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for ( Player p : online ){
                potion.getEffects().forEach(ef -> p.removePotionEffect(ef.getType()));
            }
        }, 1L);
    }
    
    @EventHandler
    protected void onPlayerPickupItem(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null){
            if (game.getSpectators().contains(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    protected void onEntityTarget(EntityTargetEvent e){
        if (e.getTarget() == null){
            return;
        }
        if (e.getTarget() instanceof Player){
            Player p = (Player) e.getTarget();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (!e.getTarget().hasMetadata("NPC") && game.getSpectators().contains(p)){
                    e.setCancelled(true);
                }
            }
        }
        if (e.getTarget() instanceof Player){
            Player p = (Player) e.getTarget();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (game.getSpectators().contains(p)){
                    if (e.getEntity() instanceof ExperienceOrb){
                        repellExpOrb((Player) e.getTarget(), (ExperienceOrb) e.getEntity());
                        e.setCancelled(true);
                        e.setTarget(null);
                    }
                }
            }
        }
    }
    
    @EventHandler
    protected void onBlockDamage(BlockDamageEvent e){
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null){
            if (game.getSpectators().contains(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    protected void onEntityDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(p)){
                    e.setCancelled(true);
                    e.getEntity().setFireTicks(0);
                }
            }
        }
    }
    
    @EventHandler
    protected void onFoodLevelChange(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (!e.getEntity().hasMetadata("NPC") && game.getSpectators().contains(p)){
                    e.setCancelled(true);
                    p.setFoodLevel(20);
                    p.setSaturation(20);
                }
            }
        }
    }
    
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e){
        if (e.getEntered() instanceof Player){
            Player p = (Player) e.getEntered();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game != null){
                if (game.getSpectators().contains(p)){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    void repellExpOrb(Player player, ExperienceOrb orb){
        Location pLoc = player.getLocation();
        Location oLoc = orb.getLocation();
        Vector dir = oLoc.toVector().subtract(pLoc.toVector());
        double dx = Math.abs(dir.getX());
        double dz = Math.abs(dir.getZ());
        if ((dx == 0.0) && (dz == 0.0)){
            dir.setX(0.001);
        }
        if ((dx < 3.0) && (dz < 3.0)){
            Vector nDir = dir.normalize();
            Vector newV = nDir.clone().multiply(0.3);
            newV.setY(0);
            orb.setVelocity(newV);
            if ((dx < 1.0) && (dz < 1.0)){
                orb.teleport(oLoc.clone().add(nDir.multiply(1.0)), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
            if ((dx < 0.5) && (dz < 0.5)){
                orb.remove();
            }
        }
    }
    
}