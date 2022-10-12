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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WinEffectNotes implements WinEffect {
    
    private final ArrayList<Item> items = new ArrayList<>();
    private final Material[] discs = new Material[]{Material.GOLD_RECORD, Material.GREEN_RECORD, Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8, Material.RECORD_9, Material.RECORD_10, Material.RECORD_11, Material.RECORD_12};
    private BukkitTask task;
    
    @Override
    public void start(Player p, Game game){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        task = new BukkitRunnable() {
            final String name = game.getSpectator().getWorld().getName();
            
            @Override
            public void run(){
                if (p == null || !p.isOnline() || !name.equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                Item item = spawnDisc(p.getLocation(), random(-0.25, 0.25), random(-0.25, 0.25));
                CustomSound.WINEFFECTS_NOTES.reproduce(p);
                plugin.getVc().getNMS().broadcastParticle(p.getLocation(), ThreadLocalRandom.current().nextInt(0, 24), 0, 0, 1, "NOTE", 5, 10);
                items.add(item);
                for ( Item c : new ArrayList<>(items) ){
                    if (c.getTicksLived() > 30){
                        c.remove();
                        plugin.getVc().getNMS().broadcastParticle(item.getLocation(), ThreadLocalRandom.current().nextInt(0, 24), 0, 0, 1, "NOTE", 5, 10);
                        items.remove(c);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 6);
    }
    
    @Override
    public void stop(){
        items.clear();
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectNotes();
    }
    
    protected double random(double d, double d2){
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }
    
    private Item spawnDisc(Location location, double d, double d3){
        ItemStack itemStack = new ItemStack(this.discs[ThreadLocalRandom.current().nextInt(this.discs.length)]);
        Item item = location.getWorld().dropItem(location, itemStack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(new Vector(d, 0.8, d3));
        return item;
    }
    
}