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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.InjectionLProtection;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class LobbyListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionLProtection injection;
    private final HashMap<UUID, Long> countdown = new HashMap<>();
    
    public LobbyListener(UltraSkyWarsApi plugin, InjectionLProtection injection){
        this.plugin = plugin;
        this.injection = injection;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e){
        if (injection.getCm().isHideJoinMessage()){
            e.setJoinMessage(null);
        }
        if (!injection.getCm().isJoinTeleport()) return;
        Player p = e.getPlayer();
        if (plugin.getMainLobby() != null){
            p.teleport(plugin.getMainLobby());
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (injection.getCm().isHideQuitMessage()){
            e.setQuitMessage(null);
        }
        countdown.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        countdown.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void onIgnite(BlockIgniteEvent e){
        if (check(e.getPlayer()) && injection.getCm().isIgniteProtect()){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (!injection.getCm().isNoBreak()) return;
        Player p = e.getPlayer();
        if (injection.getCm().isOpBypass() && p.isOp()) return;
        if (check(p)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (!injection.getCm().isNoPlace()) return;
        Player p = e.getPlayer();
        if (injection.getCm().isOpBypass() && p.isOp()) return;
        if (check(p)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand){
            Player p = (Player) e.getDamager();
            if (!injection.getCm().isNoBreakArmorStand()) return;
            if (check(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() instanceof ArmorStand){
            Player p = e.getPlayer();
            if (!injection.getCm().isNoInteractArmorStand()) return;
            if (check(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (check(p) && injection.getCm().isVoidTeleport() && countdown.getOrDefault(p.getUniqueId(), 0L) < System.currentTimeMillis()){
                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                    countdown.put(p.getUniqueId(), System.currentTimeMillis() + 1000);
                    if (plugin.getMainLobby() != null){
                        e.setCancelled(true);
                        p.setFallDistance(0);
                        p.teleport(plugin.getMainLobby());
                    }
                }
            }
            if (injection.getCm().isNoDamage()){
                if (check(p)){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onFood(FoodLevelChangeEvent e){
        if (!injection.getCm().isNoHunger()) return;
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (check(p)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (!injection.getCm().isNoDrop()) return;
        World w = e.getPlayer().getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onWeather(WeatherChangeEvent e){
        if (!injection.getCm().isNoWeather()) return;
        World w = e.getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            e.setCancelled(e.toWeatherState());
        }
    }
    
    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if (!injection.getCm().isNoExplosion()) return;
        World w = e.getLocation().getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onExplode(BlockExplodeEvent e){
        if (!injection.getCm().isNoExplosion()) return;
        World w = e.getBlock().getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if (!injection.getCm().isNoMobSpawn()) return;
        World w = e.getLocation().getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            if (e.getEntity() instanceof Player) return;
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        World w = p.getWorld();
        if (injection.getCm().getLobbyWorld().equals(w.getName())){
            if (e.getAction().equals(Action.PHYSICAL) && injection.getCm().isNoBreakFarm()){
                Material material = Material.getMaterial("FARMLAND");
                if (material == null){
                    material = Material.getMaterial("SOIL");
                }
                if (e.getClickedBlock().getType().equals(material)){
                    e.setCancelled(true);
                }
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Block b = e.getClickedBlock();
                if (p.getItemInHand() != null && !p.getItemInHand().getType().equals(Material.AIR)){
                    ItemStack item = p.getItemInHand();
                    if (injection.getCm().getInteractBlocked().contains(item.getType().name())){
                        e.setCancelled(true);
                        e.setUseItemInHand(Event.Result.DENY);
                        e.setUseInteractedBlock(Event.Result.DENY);
                    }
                }
                if (injection.getCm().getInteractBlocked().contains(b.getType().name())){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    private boolean check(Player p){
        if (p == null) return true;
        World w = p.getWorld();
        if (injection.getCm().getLobbyWorld() == null || w == null) return false;
        return injection.getCm().getLobbyWorld().equals(w.getName());
    }
    
}