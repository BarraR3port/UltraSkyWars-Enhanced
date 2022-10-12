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
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class WinEffectVulcanFire implements WinEffect, Cloneable {
    
    private final Collection<FallingBlock> fires = new ArrayList<>();
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
                CustomSound.WINEFFECTS_VULCANFIRE.reproduce(p);
                FallingBlock fallingBlock = spawnFire(p.getLocation(), random(-0.5, 0.5), random(-0.5, 0.5));
                fallingBlock.setDropItem(false);
                fires.add(fallingBlock);
            }
        }.runTaskTimer(plugin, 0, 2);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
        for ( FallingBlock fb : fires ){
            if (fb == null) continue;
            if (!fb.isDead()){
                fb.remove();
            } else if (fb.isOnGround()){
                fb.getLocation().getBlock().setType(Material.AIR);
            }
        }
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectVulcanFire();
    }
    
    protected double random(double d, double d2){
        return d + ThreadLocalRandom.current().nextDouble() * (d2 - d);
    }
    
    private FallingBlock spawnFire(Location location, double d, double d3){
        @SuppressWarnings("deprecation")
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Material.FIRE, (byte) ThreadLocalRandom.current().nextInt(15));
        fallingBlock.setVelocity(new Vector(d, 0.75, d3));
        return fallingBlock;
    }
    
}
