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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class WinDanceIceWalker implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    private static int rangePerRound, taskTick;
    private int round = 1;
    private BukkitTask task;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            rangePerRound = plugin.getWindance().getIntOrDefault(path + ".rangePerRound", 5);
            taskTick = plugin.getWindance().getIntOrDefault(path + ".taskTick", 20);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        task = new BukkitRunnable() {
            @Override
            public void run(){
                if (p == null || !p.isOnline() || !world.getName().equals(p.getWorld().getName())){
                    stop();
                    return;
                }
                for ( Block block : getNearbyBlocks(p.getLocation(), rangePerRound * round) ){
                    block.setType(Material.ICE);
                }
                round++;
                p.playSound(p.getLocation(), XSound.ENTITY_PLAYER_ATTACK_STRONG.parseSound(), 0.01f, 0.01f);
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, taskTick);
    }
    
    public BukkitTask getTask(){
        return task;
    }
    
    private List<Block> getNearbyBlocks(Location location, int radius){
        List<Block> blocks = new ArrayList<>();
        for ( int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++ ){
            for ( int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++ ){
                for ( int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++ ){
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
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinDance clone(){
        return new WinDanceIceWalker();
    }
    
}