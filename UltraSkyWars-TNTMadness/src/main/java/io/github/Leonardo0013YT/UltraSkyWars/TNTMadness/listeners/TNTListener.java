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

package io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.UltraSkyWarsTNTM;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestType;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWGamePlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TNTListener implements Listener {
    
    private final UltraSkyWarsTNTM plugin;
    private final HashMap<UUID, Long> noFall = new HashMap<>();
    
    public TNTListener(UltraSkyWarsTNTM plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        noFall.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        noFall.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void onJoin(USWGamePlayerLoadEvent e){
        Player p = e.getPlayer();
        Game game = e.getGame();
        if (game.getGameType().equals("TNT_MADNESS")){
            p.getInventory().setItem(0, plugin.getIm().getTntMadnessBook());
        }
    }
    
    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        Game game = UltraSkyWarsApi.get().getGm().getGameByName(e.getLocation().getWorld().getName());
        if (game != null){
            ChestType sw = UltraSkyWarsApi.get().getCtm().getChests().get(game.getChestType());
            if (sw != null){
                for ( Block b : e.blockList() ){
                    if (ThreadLocalRandom.current().nextBoolean()){
                        ChestItem ci = sw.getChest().getRandomItem(false, "TNT_MADNESS");
                        b.getWorld().dropItem(e.getLocation().clone().add(0.5, 0.5, 0.5), ci.getItem());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (!UltraSkyWarsApi.isPlayerGame(p)){
                return;
            }
            EntityDamageEvent.DamageCause c = e.getCause();
            if (c.equals(EntityDamageEvent.DamageCause.FALL)){
                if (noFall.containsKey(p.getUniqueId())){
                    long finish = noFall.get(p.getUniqueId());
                    if (finish >= System.currentTimeMillis()){
                        e.setDamage(0);
                    }
                    noFall.remove(p.getUniqueId());
                }
                return;
            }
            if (!c.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getDamager() instanceof TNTPrimed){
            TNTPrimed tnt = (TNTPrimed) e.getDamager();
            if (tnt.hasMetadata("DAMAGE")){
                boolean fall = tnt.getMetadata("DAMAGE").get(0).asBoolean();
                if (fall){
                    Player p = (Player) e.getEntity();
                    noFall.put(p.getUniqueId(), System.currentTimeMillis() + 5000);
                }
                e.setDamage(0);
            }
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (!UltraSkyWarsApi.isPlayerGame(p)){
                return;
            }
            ItemStack c = p.getItemInHand();
            if (c == null || c.getType().equals(Material.AIR)) return;
            Block b = e.getClickedBlock();
            ItemStack i = c.clone();
            i.setAmount(1);
            World w = b.getWorld();
            Location loc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation().add(0.5, 0, 0.5);
            if (plugin.getIm().getTntLaunchPad().equals(i)){
                removeItemInHand(p);
                spawnTNT(w, loc.clone().add(0, 0.5, 1.0), true);
                spawnTNT(w, loc.clone().add(0, 0.5, -1.0), true);
                spawnTNT(w, loc.clone().add(1.0, 0.5, 0), true);
                spawnTNT(w, loc.clone().add(-1.0, 0.5, 0), true);
            }
            if (plugin.getIm().getInstantBoom().equals(i)){
                e.setCancelled(true);
                removeItemInHand(p);
                knockBack(p, loc);
                w.getNearbyEntities(loc, 3, 3, 3).forEach(on -> {
                    if (on instanceof Player){
                        Player d = (Player) on;
                        if (d.getUniqueId().equals(p.getUniqueId())) return;
                        knockBack(d, loc);
                    }
                });
            }
            if (plugin.getIm().getNormalTNT().equals(i)){
                e.setCancelled(true);
                removeItemInHand(p);
                spawnTNT(w, loc.clone().add(0, 0.5, 0), false);
            }
        }
    }
    
    public void knockBack(Player d, Location l){
        UltraSkyWarsApi.get().getVc().getNMS().displayParticle(d, l, 0, 0, 0, 0, "EXPLOSION_LARGE", 1);
        d.playSound(d.getLocation(), Sound.EXPLODE, 1.0f, 10.0f);
        Vector v = d.getEyeLocation().getDirection();
        d.setVelocity(v.multiply((v.getY() > 0 ? 1.8 : -1.8)));
        noFall.put(d.getUniqueId(), System.currentTimeMillis() + 5000);
    }
    
    public void spawnTNT(World w, Location l1, boolean noFall){
        TNTPrimed t = w.spawn(l1, TNTPrimed.class);
        t.setMetadata("DAMAGE", new FixedMetadataValue(plugin, noFall));
        t.setFuseTicks(40);
    }
    
    public void removeItemInHand(Player p){
        ItemStack i = p.getItemInHand();
        if (i.getAmount() > 1){
            i.setAmount(i.getAmount() - 1);
            p.setItemInHand(i);
        } else {
            p.setItemInHand(null);
        }
    }
    
}