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

package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class DragonEvent extends GameEvent {
    
    EnderDragon dragon;
    
    public DragonEvent(UltraSkyWarsApi plugin, int time){
        this.time = time;
        this.reset = time;
        this.dragon = null;
        this.type = "final";
        this.name = "dragon";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }
    
    public DragonEvent(DragonEvent e){
        this.time = e.getReset();
        this.reset = e.getReset();
        this.dragon = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }
    
    @Override
    public void start(Game game){
        dragon = game.getSpectator().getWorld().spawn(game.getSpectator(), EnderDragon.class);
        dragon.setNoDamageTicks(999999999);
        dragon.setMetadata("CUSTOM", new FixedMetadataValue(UltraSkyWarsApi.get(), "CUSTOM"));
        for ( Player on : game.getCached() ){
            CustomSound.EVENTS_DRAGON.reproduce(on);
        }
    }
    
    @Override
    public void stop(Game game){
        if (dragon != null){
            dragon.remove();
        }
    }
    
    @Override
    public void reset(){
        this.time = this.reset;
        this.dragon = null;
        this.type = "final";
        this.name = "dragon";
    }
    
    @Override
    public DragonEvent clone(){
        return new DragonEvent(this);
    }
    
}