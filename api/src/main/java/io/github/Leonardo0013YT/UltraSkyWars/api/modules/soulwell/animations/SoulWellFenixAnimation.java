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
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.InstantFirework;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.*;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class SoulWellFenixAnimation implements SoulWellAnimation {
    
    private final Inventory inv;
    private final InjectionSoulWell is;
    private final UltraSkyWarsApi plugin;
    private final ItemStack orange;
    private final SoulWellSession sws;
    private final Player p;
    private final SoulWellRow row;
    private final Location loc;
    private final HashMap<Location, Material> backup = new HashMap<>();
    private final Collection<BukkitTask> tasks = new ArrayList<>();
    private Blaze blaze;
    private float lastVel;
    
    public SoulWellFenixAnimation(UltraSkyWarsApi plugin, InjectionSoulWell is, SoulWellSession sws, Player p, SoulWellRow row, Location loc){
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, 45, plugin.getLang().get(null, "menus.soulwellmenu.title"));
        this.is = is;
        this.orange = ItemBuilder.item(XMaterial.ORANGE_STAINED_GLASS_PANE, 1, "§7", "§7");
        this.sws = sws;
        this.p = p;
        this.row = row;
        this.loc = loc;
    }
    
    @Override
    public void execute(){
        p.openInventory(inv);
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            
            @Override
            public void run(){
                for ( int r : row.getResult() ){
                    inv.setItem(r, plugin.getLvl().getRandomReward().getIcon());
                }
                if (i < row.getFenix().length){
                    int slot = row.getFenix()[i];
                    inv.setItem(slot, orange);
                    i++;
                    CustomSound.SOUL_ANIMATION_BLAZE_1.reproduce(p);
                } else {
                    CustomSound.SOUL_ANIMATION_BLAZE_2.reproduce(p);
                    executeFenixAnimation2();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 2, 2);
        tasks.add(task);
    }
    
    private void executeFenixAnimation2(){
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            
            @Override
            public void run(){
                if (i == 1 || i == 3 || i == 5 || i == 7){
                    for ( int slot : row.getFenix() ){
                        inv.setItem(slot, null);
                    }
                    CustomSound.SOUL_ANIMATION_BLAZE_3.reproduce(p);
                } else {
                    for ( int slot : row.getFenix() ){
                        inv.setItem(slot, orange);
                    }
                    CustomSound.SOUL_ANIMATION_BLAZE_4.reproduce(p);
                }
                i++;
                if (i > 7){
                    sws.setRolling(false);
                    lastVel = p.getWalkSpeed();
                    p.setWalkSpeed(0f);
                    executeFenixAnimation3();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 5, 5);
        tasks.add(task);
    }
    
    private void executeFenixAnimation3(){
        sws.deleteHologram();
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            
            @Override
            public void run(){
                p.closeInventory();
                Location l1 = loc.clone().add(1, -1, 0);
                Location l2 = loc.clone().add(0, -1, 1);
                Location l3 = loc.clone().add(-1, -1, 0);
                Location l4 = loc.clone().add(0, -1, -1);
                Location l5 = loc.clone().add(1, -1, 1);
                Location l6 = loc.clone().add(-1, -1, -1);
                Location l7 = loc.clone().add(-1, -1, 1);
                Location l8 = loc.clone().add(1, -1, -1);
                ArrayList<Location> locs = new ArrayList<>(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8));
                if (i < locs.size()){
                    Location l = locs.get(i);
                    backup.put(l, l.getBlock().getType());
                    l.getBlock().setType(Material.NETHERRACK);
                    CustomSound.SOUL_ANIMATION_BLAZE_5.reproduce(p);
                    i++;
                } else {
                    executeFenixAnimation4();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 5, 5);
        tasks.add(task);
    }
    
    private void executeFenixAnimation4(){
        plugin.getEntities().add(blaze = loc.getWorld().spawn(loc.clone().add(0.5, 1, 0.5), Blaze.class));
        blaze.setNoDamageTicks(Integer.MAX_VALUE);
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            
            @Override
            public void run(){
                if (i < 3){
                    new InstantFirework(FireworkEffect.builder().withColor(Color.RED).withFade(Color.ORANGE).with(FireworkEffect.Type.BURST).build(), loc.clone().add(0.5, 3.5, 0.5));
                }
                i++;
                if (i >= 5){
                    cancel();
                    executeFenixAnimation5();
                }
            }
        }.runTaskTimer(plugin, 10, 10);
        tasks.add(task);
    }
    
    private void executeFenixAnimation5(){
        sws.setRolling(true);
        p.openInventory(inv);
        for ( int r : row.getResult() ){
            inv.setItem(r, plugin.getLvl().getRandomReward().getIcon());
        }
        for ( int r : row.getResult() ){
            CustomSound.SOUL_ANIMATION_BLAZE_6.reproduce(p);
            sws.executeReward(p, inv.getItem(r));
        }
        new BukkitRunnable() {
            @Override
            public void run(){
                p.setWalkSpeed(lastVel);
                blaze.remove();
                for ( Location b : backup.keySet() ){
                    b.getBlock().setType(backup.get(b));
                }
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
        p.setWalkSpeed(lastVel);
        if (blaze != null && !blaze.isDead()){
            blaze.remove();
        }
        for ( Location b : backup.keySet() ){
            b.getBlock().setType(backup.get(b));
        }
    }
    
    @Override
    public Inventory getInv(){
        return inv;
    }
    
}