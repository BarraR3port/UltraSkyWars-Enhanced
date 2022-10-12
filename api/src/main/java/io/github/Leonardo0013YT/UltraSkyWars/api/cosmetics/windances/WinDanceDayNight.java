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
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinDanceDayNight implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    private static int perTickTime, taskTick;
    private BukkitTask task;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            perTickTime = plugin.getWindance().getIntOrDefault(path + ".perTickTime", 700);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 1);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        task = new BukkitRunnable() {
            public void run(){
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                p.getWorld().setTime(p.getWorld().getTime() + perTickTime);
                p.playSound(p.getLocation(), XSound.BLOCK_NOTE_BLOCK_HAT.parseSound(), 0.01f, 0.01f);
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
        return new WinDanceDayNight();
    }
}