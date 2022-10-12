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
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinEffectFireworks implements WinEffect, Cloneable {
    
    private BukkitTask task;
    
    public WinEffectFireworks(){
        this.task = null;
    }
    
    @Override
    public void start(Player p, Game game){
        task = new BukkitRunnable() {
            final String name = game.getSpectator().getWorld().getName();
            
            @Override
            public void run(){
                if (p == null || !p.isOnline() || !name.equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                Utils.firework(p.getLocation());
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, 6);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectFireworks();
    }
}