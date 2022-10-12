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
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellPath;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellRow;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class SoulWellNormalAnimation implements SoulWellAnimation {
    
    private final Inventory inv;
    private final ItemStack black;
    private final InjectionSoulWell is;
    private final SoulWellSession sws;
    private final UltraSkyWarsApi plugin;
    private final SoulWellRow row;
    private final Player p;
    private final Location loc;
    private final ArrayList<BukkitTask> tasks = new ArrayList<>();
    private ItemStack orange;
    private int executes;
    
    public SoulWellNormalAnimation(UltraSkyWarsApi plugin, InjectionSoulWell is, SoulWellSession sws, Player p, SoulWellRow row, Location loc){
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, 45, plugin.getLang().get(null, "menus.soulwellmenu.title"));
        this.black = ItemBuilder.item(XMaterial.BLACK_STAINED_GLASS, 1, plugin.getLang().get(null, "soulwell.rolling"), "§7");
        this.is = is;
        this.sws = sws;
        this.row = row;
        this.p = p;
        this.loc = loc;
    }
    
    @Override
    public void execute(){
        for ( int i : row.getGlass() ){
            inv.setItem(i, black);
        }
        p.openInventory(inv);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run(){
                if (executes == 21){
                    executes = 0;
                    executePhase2();
                    cancel();
                    return;
                }
                for ( SoulWellPath path : row.getPaths() ){
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for ( int i = 0; i < inv.getSize(); i++ ){
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")){
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 3, 3);
        tasks.add(task);
    }
    
    private void executePhase2(){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run(){
                if (executes == 7){
                    executes = 0;
                    executePhase3();
                    cancel();
                    return;
                }
                for ( SoulWellPath path : row.getPaths() ){
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for ( int i = 0; i < inv.getSize(); i++ ){
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")){
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 6, 6);
        tasks.add(task);
    }
    
    private void executePhase3(){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run(){
                if (executes == 4){
                    executes = 0;
                    for ( SoulWellPath path : row.getPaths() ){
                        sws.executeReward(p, inv.getItem(path.getThree()));
                    }
                    sws.firework(loc.clone().add(0.5, -1, 0.5));
                    sws.setRolling(false);
                    new BukkitRunnable() {
                        @Override
                        public void run(){
                            is.getSwm().removeSession(p);
                            p.closeInventory();
                        }
                    }.runTaskLater(plugin, 20);
                    cancel();
                    return;
                }
                for ( SoulWellPath path : row.getPaths() ){
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for ( int i = 0; i < inv.getSize(); i++ ){
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")){
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 10, 10);
        tasks.add(task);
    }
    
    @Override
    public void cancel(Player p){
        for ( BukkitTask bt : tasks ){
            if (bt == null) continue;
            bt.cancel();
        }
    }
    
    @Override
    public Inventory getInv(){
        return inv;
    }
}