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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.windances;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WinDance;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinDanceThunder implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    private static int xOfCenter, zOfCenter, minOfCenter, maxOfCenter, taskTick;
    private BukkitTask task;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            xOfCenter = plugin.getWindance().getIntOrDefault(path + ".xOfCenter", 10);
            zOfCenter = plugin.getWindance().getIntOrDefault(path + ".zOfCenter", 10);
            minOfCenter = plugin.getWindance().getIntOrDefault(path + ".minOfCenter", 10);
            maxOfCenter = plugin.getWindance().getIntOrDefault(path + ".maxOfCenter", 15);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 20);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World w = game.getSpectator().getWorld();
        Location loc1 = new Location(w, minOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), minOfCenter);
        Location loc2 = new Location(w, -minOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), minOfCenter);
        Location loc3 = new Location(w, minOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), -minOfCenter);
        Location loc4 = new Location(w, -minOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), -10);
        Location loc5 = new Location(w, maxOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), maxOfCenter);
        Location loc6 = new Location(w, -maxOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), maxOfCenter);
        Location loc7 = new Location(w, maxOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), -maxOfCenter);
        Location loc8 = new Location(w, -maxOfCenter, w.getHighestBlockYAt(xOfCenter, zOfCenter), -maxOfCenter);
        task = new BukkitRunnable() {
            @Override
            public void run(){
                if (p == null || !p.isOnline() || !w.getName().equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                thunder(loc1);
                thunder(loc2);
                thunder(loc3);
                thunder(loc4);
                thunder(loc5);
                thunder(loc6);
                thunder(loc7);
                thunder(loc8);
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, taskTick);
    }
    
    private void thunder(Location loc){
        loc.getWorld().strikeLightningEffect(loc);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinDance clone(){
        return new WinDanceThunder();
    }
    
}