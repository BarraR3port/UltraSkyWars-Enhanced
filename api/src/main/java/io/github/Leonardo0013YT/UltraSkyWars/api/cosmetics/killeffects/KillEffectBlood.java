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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.KillEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillEffectBlood implements KillEffect, Cloneable {
    
    private static boolean loaded = false;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Player death, Location loc){
        if (death == null || !death.isOnline()){
            return;
        }
        death.getWorld().playEffect(loc.clone().add(0, 0.9, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        death.getWorld().playEffect(loc.clone().add(0, 0.5, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        death.getWorld().playEffect(loc.clone().add(0, 0.1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }
    
    @Override
    public void stop(){
    }
    
    @Override
    public KillEffect clone(){
        return new KillEffectBlood();
    }
    
}