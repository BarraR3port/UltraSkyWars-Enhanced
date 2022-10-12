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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameQuitEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.CompassItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.InstantTNTItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.SoupItem;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types.TNTLaunchItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.UUID;

public class SpecialItemsListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSpecialItems isi;
    private final HashMap<UUID, Long> noFall = new HashMap<>();
    private final HashMap<UUID, Long> countdown = new HashMap<>();
    private final HashMap<UUID, BukkitTask> enderPearls = new HashMap<>();
    
    public SpecialItemsListener(UltraSkyWarsApi plugin, InjectionSpecialItems isi){
        this.plugin = plugin;
        this.isi = isi;
    }
    
    @EventHandler
    public void onGameQuit(USWGameQuitEvent e){
        Player p = e.getPlayer();
        if (enderPearls.containsKey(p.getUniqueId())){
            BukkitTask task = enderPearls.remove(p.getUniqueId());
            if (task != null){
                task.cancel();
            }
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        countdown.remove(p.getUniqueId());
        noFall.remove(p.getUniqueId());
        BukkitTask task = enderPearls.remove(p.getUniqueId());
        if (task != null){
            task.cancel();
        }
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        countdown.remove(p.getUniqueId());
        noFall.remove(p.getUniqueId());
        if (enderPearls.containsKey(p.getUniqueId())){
            BukkitTask task = enderPearls.remove(p.getUniqueId());
            if (task != null){
                task.cancel();
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
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                if (noFall.containsKey(p.getUniqueId())){
                    long finish = noFall.get(p.getUniqueId());
                    if (finish >= System.currentTimeMillis()){
                        e.setDamage(0);
                    }
                    noFall.remove(p.getUniqueId());
                }
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
        if (e.getAction().equals(Action.PHYSICAL)) return;
        if (!plugin.getGm().isPlayerInGame(p)) return;
        if (isHand(p)) return;
        ItemStack item = p.getItemInHand();
        ItemStack i = item.clone();
        i.setAmount(1);
        Action action = e.getAction();
        if (isi.getIm().getCompass().equals(i)){
            CompassItem ci = (CompassItem) isi.getIm().getCompassItem();
            if (countdown.containsKey(p.getUniqueId())){
                int rest = (int) ((countdown.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L);
                if (rest > 0){
                    p.sendMessage(ci.getCountdown().replace("<time>", String.valueOf(rest)));
                    return;
                }
            }
            countdown.put(p.getUniqueId(), System.currentTimeMillis() + (ci.getTime() * 1000L));
            Player target = getNearest(p, ci.getRange());
            if (target == null){
                p.sendMessage(ci.getNoTarget());
                return;
            }
            p.setCompassTarget(target.getLocation());
            p.sendMessage(ci.getTargeted().replace("<player>", target.getName()));
            return;
        }
        if (isi.getIm().getSoup().equals(i)){
            e.setCancelled(true);
            SoupItem ci = (SoupItem) isi.getIm().getSoupItem();
            if (p.getHealth() >= p.getMaxHealth()){
                p.sendMessage(ci.getMaxHealth());
                return;
            }
            p.setHealth(p.getHealth() + ci.getHealth());
            removeItemInHand(p);
            CustomSound.SOUP_EAT.reproduce(p);
            return;
        }
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            if (isi.getIm().getEndBuff().equals(i)){
                if (p.getGameMode() == GameMode.SURVIVAL){
                    if (enderPearls.containsKey(p.getUniqueId())){
                        BukkitTask task = enderPearls.remove(p.getUniqueId());
                        if (task != null){
                            task.cancel();
                        }
                    }
                    e.setCancelled(true);
                    e.setUseItemInHand(Event.Result.DENY);
                    e.setUseInteractedBlock(Event.Result.DENY);
                    Item drop = p.getWorld().dropItem(p.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.ENDER_PEARL, 1));
                    drop.setPickupDelay(10000);
                    drop.setVelocity(p.getLocation().getDirection().normalize().multiply(1.5F));
                    drop.setPassenger(p);
                    enderPearls.put(p.getUniqueId(), spawnEnderPearl(drop));
                    removeItemInHand(p);
                }
                return;
            }
        }
        if (action.equals(Action.RIGHT_CLICK_BLOCK)){
            Block b = e.getClickedBlock();
            World w = b.getWorld();
            Location loc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation().add(0.5, 0, 0.5);
            if (isi.getIm().getTNTLaunch().equals(i)){
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
                return;
            }
            if (isi.getIm().getInstantTNT().equals(i)){
                e.setCancelled(true);
                removeItemInHand(p);
                spawnTNT(w, loc.clone().add(0, 0.5, 0), false);
            }
        }
    }
    
    @EventHandler
    public void onDismount(EntityDismountEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (!plugin.getGm().isPlayerInGame(p)) return;
            if (e.getDismounted() instanceof EnderPearl){
                if (enderPearls.containsKey(p.getUniqueId())){
                    BukkitTask task = enderPearls.remove(p.getUniqueId());
                    if (task != null){
                        task.cancel();
                    }
                }
            }
        }
    }
    
    public BukkitTask spawnEnderPearl(final Item item){
        return new BukkitRunnable() {
            public void run(){
                if (item.isDead()){
                    this.cancel();
                }
                if (item.getVelocity().getX() == 0.0D || item.getVelocity().getY() == 0.0D || item.getVelocity().getZ() == 0.0D){
                    Player p = (Player) item.getPassenger();
                    item.remove();
                    if (p != null){
                        p.teleport(p.getLocation().add(0.0D, 0.5D, 0.0D));
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 2L, 1L);
    }
    
    public boolean isHand(Player p){
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
            return true;
        }
        ItemStack item = p.getItemInHand();
        return !item.hasItemMeta() || !item.getItemMeta().hasDisplayName();
    }
    
    public void knockBack(Player d, Location l){
        TNTLaunchItem ci = (TNTLaunchItem) isi.getIm().getTNTLaunchItem();
        UltraSkyWarsApi.get().getVc().getNMS().displayParticle(d, l, 0, 0, 0, 0, "EXPLOSION_LARGE", 1);
        CustomSound.TNT_LAUNCH_EXPLODE.reproduce(d);
        Vector v = d.getEyeLocation().getDirection();
        d.setVelocity(v.multiply((v.getY() > 0 ? ci.getMultiply() : -ci.getMultiply())));
        noFall.put(d.getUniqueId(), System.currentTimeMillis() + (ci.getNoFall() + 1000L));
    }
    
    public void spawnTNT(World w, Location l1, boolean noFall){
        InstantTNTItem ci = (InstantTNTItem) isi.getIm().getInstantTNTItem();
        TNTPrimed t = w.spawn(l1, TNTPrimed.class);
        t.setMetadata("DAMAGE", new FixedMetadataValue(plugin, noFall));
        t.setFuseTicks(ci.getFuse_ticks());
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
    
    public Player getNearest(Player p, double range){
        double distance = 1000;
        Player target = null;
        for ( Entity e : p.getNearbyEntities(range, range, range) ){
            if (!(e instanceof Player))
                continue;
            if (e == p) continue;
            double distanceto = p.getLocation().distance(e.getLocation());
            if (distanceto > distance)
                continue;
            distance = distanceto;
            target = (Player) e;
        }
        return target;
    }
    
}