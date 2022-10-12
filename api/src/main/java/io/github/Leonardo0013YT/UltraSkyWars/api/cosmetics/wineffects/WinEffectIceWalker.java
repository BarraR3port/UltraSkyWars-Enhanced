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
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class WinEffectIceWalker implements WinEffect, Cloneable {
    
    private BukkitTask task;
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        task = new BukkitRunnable() {
            @Override
            public void run(){
                if (!p.getWorld().getName().equals(world.getName())){
                    cancel();
                    return;
                }
                if (!p.isOnline()){
                    cancel();
                    return;
                }
                for ( Block b : getNearbyBlocks(p.getLocation()) ){
                    b.setType(Material.ICE);
                }
                p.playSound(p.getLocation(), XSound.ENTITY_PLAYER_ATTACK_STRONG.parseSound(), 0.01f, 0.01f);
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, 5);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    private List<Block> getNearbyBlocks(Location location){
        List<Block> blocks = new ArrayList<>();
        for ( int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; x++ ){
            for ( int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; y++ ){
                for ( int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; z++ ){
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR || block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE){
                        continue;
                    }
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectIceWalker();
    }
}