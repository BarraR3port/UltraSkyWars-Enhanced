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
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGameWinEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Game game;
    private final Team winner;
    private boolean isCancelled = false;
    
    public USWGameWinEvent(Game game, Team winner){
        this.game = game;
        this.winner = winner;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }
    
    public Game getGame(){
        return game;
    }
    
    public Team getWinner(){
        return winner;
    }
    
    @Override
    public boolean isCancelled(){
        return isCancelled;
    }
    
    @Override
    public void setCancelled(boolean isCancelled){
        this.isCancelled = isCancelled;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS_LIST;
    }
    
}