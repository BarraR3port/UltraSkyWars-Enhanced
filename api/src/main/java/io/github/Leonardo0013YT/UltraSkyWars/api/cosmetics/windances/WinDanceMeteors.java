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
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class WinDanceMeteors implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    private static int firstUp, taskTick;
    private static double maxOfCenter;
    private BukkitTask task;
    
    public WinDanceMeteors(){
        this.task = null;
    }
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            maxOfCenter = plugin.getWindance().getDoubleOrDefault(path + ".maxOfCenter", 1);
            firstUp = plugin.getWindance().getIntOrDefault(path + ".firstUp", 110);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 2);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        Location center = new Location(world, game.getBorderX(), firstUp, game.getBorderZ());
        task = new BukkitRunnable() {
            public void run(){
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                Fireball fb = world.spawn(center, Fireball.class);
                fb.setVelocity(new Vector(ThreadLocalRandom.current().nextDouble(-maxOfCenter, maxOfCenter), -1.5, ThreadLocalRandom.current().nextDouble(-maxOfCenter, maxOfCenter)));
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), taskTick, taskTick);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinDance clone(){
        return new WinDanceMeteors();
    }
    
}