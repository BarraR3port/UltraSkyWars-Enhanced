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
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class KillEffectFirework implements KillEffect, Cloneable {
    
    private final Random random;
    
    public KillEffectFirework(){
        this.random = new Random();
    }
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
    }
    
    @Override
    public void start(Player p, Player death, Location loc){
        if (death == null || !death.isOnline()){
            return;
        }
        Utils.firework(death.getLocation());
    }
    
    @Override
    public void stop(){
    }
    
    @Override
    public KillEffect clone(){
        return new KillEffectFirework();
    }
    
}