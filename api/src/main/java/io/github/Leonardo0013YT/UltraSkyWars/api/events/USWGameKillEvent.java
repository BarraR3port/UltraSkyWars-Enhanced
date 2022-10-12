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

import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGameKillEvent extends Event {
    
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Player death;
    private final Game game;
    
    public USWGameKillEvent(Player player, Player death, Game game){
        this.player = player;
        this.death = death;
        this.game = game;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }
    
    public Game getGame(){
        return game;
    }
    
    public Player getDeath(){
        return death;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS_LIST;
    }
}