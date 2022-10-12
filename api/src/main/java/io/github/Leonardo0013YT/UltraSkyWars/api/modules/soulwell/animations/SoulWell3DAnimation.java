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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.animations;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.interfaces.SoulWellAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellRow;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class SoulWell3DAnimation implements SoulWellAnimation {
    
    private final Inventory inv;
    private final UltraSkyWarsApi plugin;
    private final InjectionSoulWell is;
    private final SoulWellSession sws;
    private final Player p;
    private final SoulWellRow row;
    private final Location loc;
    private final Collection<BukkitTask> tasks = new ArrayList<>();
    private final Collection<ArmorStand> armors = new ArrayList<>();
    private final Collection<Entity> items = new ArrayList<>();
    private float lastVel;
    
    public SoulWell3DAnimation(UltraSkyWarsApi plugin, InjectionSoulWell is, SoulWellSession sws, Player p, SoulWellRow row, Location loc){
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, 45, plugin.getLang().get(null, "menus.soulwellmenu.title"));
        this.is = is;
        this.sws = sws;
        this.p = p;
        this.row = row;
        this.loc = loc;
    }
    
    @Override
    public void execute(){
        sws.deleteHologram();
        lastVel = p.getWalkSpeed();
        p.setWalkSpeed(0f);
        for ( Vector v : row.getArmors() ){
            ArmorStand as = loc.getWorld().spawn(loc.clone().add(v).add(0.5, 0, 0.5), ArmorStand.class);
            as.setGravity(false);
            as.setSmall(false);
            as.setVisible(false);
            Item item = loc.getWorld().dropItem(as.getLocation().clone().add(0, 1.5, 0), plugin.getLvl().getRandomReward().getIcon());
            item.setPickupDelay(Integer.MAX_VALUE);
            items.add(item);
            as.setPassenger(item);
            armors.add(as);
        }
        BukkitTask task = new BukkitRunnable() {
            final double re = 0.2;
            final Location m1 = loc.clone().add(0.5, 1.5, -3).clone();
            final Location m2 = loc.clone().add(0.5, 3.0, 3).clone();
            int i = 0;
            
            @Override
            public void run(){
                for ( Entity e : items ){
                    e.remove();
                }
                items.clear();
                CustomSound.SOUL_ANIMATION_3D_1.reproduce(p);
                plugin.getVc().getNMS().broadcastParticle(m1.add(0, 0, re), 0, 0, 0, 0, "FLAME", 5, 5);
                plugin.getVc().getNMS().broadcastParticle(m2.add(0, 0, -re), 0, 0, 0, 0, "FLAME", 5, 5);
                for ( ArmorStand as : armors ){
                    Item item = loc.getWorld().dropItem(as.getLocation().clone().add(0, 1.5, 0), plugin.getLvl().getRandomReward().getIcon());
                    item.setPickupDelay(Integer.MAX_VALUE);
                    as.setPassenger(item);
                    items.add(item);
                }
                i++;
                if (i >= 30){
                    for ( Entity e : items ){
                        e.remove();
                    }
                    items.clear();
                    for ( ArmorStand as : armors ){
                        as.remove();
                    }
                    execute3DAnimation2();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 3, 3);
        tasks.add(task);
    }
    
    private void execute3DAnimation2(){
        sws.setRolling(true);
        p.openInventory(inv);
        p.setWalkSpeed(lastVel);
        for ( int r : row.getResult() ){
            inv.setItem(r, plugin.getLvl().getRandomReward().getIcon());
        }
        for ( int r : row.getResult() ){
            CustomSound.SOUL_ANIMATION_3D_2.reproduce(p);
            sws.executeReward(p, inv.getItem(r));
        }
        new BukkitRunnable() {
            @Override
            public void run(){
                sws.setRolling(false);
                p.closeInventory();
                is.getSwm().removeSession(p);
                sws.recreateHologram();
            }
        }.runTaskLater(plugin, 20);
    }
    
    @Override
    public void cancel(Player p){
        if (sws.isDeleted()){
            sws.recreateHologram();
        }
        for ( BukkitTask bt : tasks ){
            if (bt == null) continue;
            bt.cancel();
        }
        for ( ArmorStand a : armors ){
            if (a == null || a.isDead()) continue;
            a.remove();
        }
        for ( Entity i : items ){
            if (i == null || i.isDead()) continue;
            i.remove();
        }
        p.setWalkSpeed(lastVel);
    }
    
    @Override
    public Inventory getInv(){
        return inv;
    }
    
}