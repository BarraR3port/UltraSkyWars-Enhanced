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
import io.github.Leonardo0013YT.UltraSkyWars.api.vote.Vote;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class USWGameStartEvent extends Event {
    
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Game game;
    private final Collection<Player> players;
    private final Collection<Team> teams;
    private final Vote vote;
    private final boolean isCancelled = false;
    
    public USWGameStartEvent(Game game, Collection<Player> players, Collection<Team> teams, Vote vote){
        this.game = game;
        this.players = players;
        this.teams = teams;
        this.vote = vote;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }
    
    public Collection<Player> getPlayers(){
        return players;
    }
    
    public Collection<Team> getTeams(){
        return teams;
    }
    
    public Game getGame(){
        return game;
    }
    
    public Vote getVote(){
        return vote;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS_LIST;
    }
    
}