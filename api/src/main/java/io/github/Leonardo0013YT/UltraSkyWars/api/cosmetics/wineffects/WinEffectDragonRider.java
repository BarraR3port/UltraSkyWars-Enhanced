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
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class WinEffectDragonRider implements WinEffect {
    
    private EnderDragon dragon;
    
    @Override
    public void start(Player p, Game game){
        dragon = p.getWorld().spawn(p.getLocation(), EnderDragon.class);
        dragon.setPassenger(p);
        dragon.setMetadata("NO_TARGET", new FixedMetadataValue(UltraSkyWarsApi.get(), ""));
        p.getWorld().playSound(p.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 1.0f, 1.0f);
    }
    
    @Override
    public void stop(){
        if (dragon != null){
            dragon.remove();
        }
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectDragonRider();
    }
    
}