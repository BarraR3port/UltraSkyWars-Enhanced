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
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;

public class NoneEvent extends GameEvent {
    
    public NoneEvent(UltraSkyWarsApi plugin, int time){
        this.time = time;
        this.reset = time;
        this.type = "final";
        this.name = "none";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }
    
    public NoneEvent(NoneEvent e){
        this.time = e.getReset();
        this.reset = e.getReset();
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }
    
    @Override
    public void start(Game game){
    }
    
    @Override
    public void stop(Game game){
    }
    
    @Override
    public void reset(){
        this.time = this.reset;
        this.type = "final";
        this.name = "none";
    }
    
    @Override
    public NoneEvent clone(){
        return new NoneEvent(this);
    }
    
}