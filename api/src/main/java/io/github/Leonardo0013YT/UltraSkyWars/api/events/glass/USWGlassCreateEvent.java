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

package io.github.Leonardo0013YT.UltraSkyWars.api.events.glass;

import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Glass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class USWGlassCreateEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Glass glass;
    private final Location location;
    private final boolean team;
    private boolean isCancelled = false;
    
    public USWGlassCreateEvent(Player player, Glass glass, Location location, boolean team){
        this.player = player;
        this.glass = glass;
        this.location = location;
        this.team = team;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public Glass getGlass(){
        return glass;
    }
    
    public Location getLocation(){
        return location;
    }
    
    public boolean isTeam(){
        return team;
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