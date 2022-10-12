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
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinDanceFireworks implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    private static int minOfCenter, maxOfCenter, firstUp, taskTick;
    private BukkitTask task;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            minOfCenter = plugin.getWindance().getIntOrDefault(path + ".minOfCenter", 10);
            maxOfCenter = plugin.getWindance().getIntOrDefault(path + ".maxOfCenter", 10);
            firstUp = plugin.getWindance().getIntOrDefault(path + ".firstUp", 90);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 20);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        Location loc1 = new Location(world, minOfCenter, firstUp, minOfCenter);
        Location loc2 = new Location(world, -minOfCenter, firstUp, minOfCenter);
        Location loc3 = new Location(world, minOfCenter, firstUp, -minOfCenter);
        Location loc4 = new Location(world, -minOfCenter, firstUp, -minOfCenter);
        Location loc5 = new Location(world, maxOfCenter, firstUp, maxOfCenter);
        Location loc6 = new Location(world, -maxOfCenter, firstUp, maxOfCenter);
        Location loc7 = new Location(world, maxOfCenter, firstUp, -maxOfCenter);
        Location loc8 = new Location(world, -maxOfCenter, firstUp, -maxOfCenter);
        task = new BukkitRunnable() {
            public void run(){
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                Utils.firework(loc1);
                Utils.firework(loc2);
                Utils.firework(loc3);
                Utils.firework(loc4);
                Utils.firework(loc5);
                Utils.firework(loc6);
                Utils.firework(loc7);
                Utils.firework(loc8);
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, taskTick);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinDance clone(){
        return new WinDanceFireworks();
    }
    
}