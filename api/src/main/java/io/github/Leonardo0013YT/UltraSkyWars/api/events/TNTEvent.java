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
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class TNTEvent extends GameEvent {
    
    private BukkitTask task;
    
    public TNTEvent(UltraSkyWarsApi plugin, int time){
        this.time = time;
        this.reset = time;
        this.task = null;
        this.type = "final";
        this.name = "tnt";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }
    
    public TNTEvent(TNTEvent e){
        this.time = e.getReset();
        this.reset = e.getReset();
        this.task = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }
    
    @Override
    public void start(Game game){
        this.task = new BukkitRunnable() {
            @Override
            public void run(){
                for ( Player on : game.getPlayers() ){
                    TNTPrimed tntPrimed = on.getWorld().spawn(on.getLocation().clone().add(0, 10, 0), TNTPrimed.class);
                    tntPrimed.setVelocity(new Vector(0, -2, 0));
                }
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 40, 40);
        for ( Player on : game.getCached() ){
            CustomSound.EVENTS_TNT.reproduce(on);
        }
    }
    
    @Override
    public void stop(Game game){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public void reset(){
        this.time = this.reset;
        this.task = null;
        this.type = "final";
        this.name = "tnt";
    }
    
    @Override
    public TNTEvent clone(){
        return new TNTEvent(this);
    }
    
}