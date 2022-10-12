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
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.metadata.FixedMetadataValue;

public class WitherEvent extends GameEvent {
    
    private Wither wither;
    
    public WitherEvent(UltraSkyWarsApi plugin, int time){
        this.time = time;
        this.reset = time;
        this.wither = null;
        this.type = "final";
        this.name = "wither";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }
    
    public WitherEvent(WitherEvent e){
        this.time = e.getReset();
        this.reset = e.getReset();
        this.wither = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }
    
    @Override
    public void start(Game game){
        wither = game.getSpectator().getWorld().spawn(game.getSpectator(), Wither.class);
        wither.setNoDamageTicks(999999999);
        wither.setMetadata("CUSTOM", new FixedMetadataValue(UltraSkyWarsApi.get(), "CUSTOM"));
        for ( Player on : game.getCached() ){
            CustomSound.EVENTS_WITHER.reproduce(on);
        }
    }
    
    @Override
    public void stop(Game game){
        if (wither != null){
            wither.remove();
        }
    }
    
    @Override
    public void reset(){
        this.time = this.reset;
        this.wither = null;
        this.type = "final";
        this.name = "wither";
    }
    
    @Override
    public WitherEvent clone(){
        return new WitherEvent(this);
    }
    
}