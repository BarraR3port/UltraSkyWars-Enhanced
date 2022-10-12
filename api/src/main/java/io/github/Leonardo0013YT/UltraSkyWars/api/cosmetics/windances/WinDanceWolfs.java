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
import org.bukkit.entity.Wolf;

import java.util.concurrent.ThreadLocalRandom;

public class WinDanceWolfs implements WinDance, Cloneable {
    
    private static boolean loaded = false;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        for ( int i = 0; i < 20; i++ ){
            int x = ThreadLocalRandom.current().nextInt(0, 4);
            int z = ThreadLocalRandom.current().nextInt(0, 4);
            Location center = p.getLocation().clone().add(x, 1, z);
            Wolf wolf = world.spawn(center, Wolf.class);
            wolf.setOwner(p);
            wolf.setSitting(ThreadLocalRandom.current().nextBoolean());
        }
    }
    
    @Override
    public void stop(){
    
    }
    
    @Override
    public WinDance clone(){
        return new WinDanceWolfs();
    }
    
}