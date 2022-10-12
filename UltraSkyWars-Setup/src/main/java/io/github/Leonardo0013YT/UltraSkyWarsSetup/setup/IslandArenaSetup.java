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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IslandArenaSetup {
    
    private final Player p;
    private final int id;
    private final ArrayList<Location> chests = new ArrayList<>();
    private Location spawn, balloon, fence;
    
    public IslandArenaSetup(Player p, int id){
        this.p = p;
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public Player getP(){
        return p;
    }
    
    public Location getSpawn(){
        return spawn;
    }
    
    public void setSpawn(Location spawn){
        this.spawn = spawn;
    }
    
    public Location getFence(){
        return fence;
    }
    
    public void setFence(Location fence){
        this.fence = fence;
    }
    
    public Location getBalloon(){
        return balloon;
    }
    
    public void setBalloon(Location balloon){
        this.balloon = balloon;
    }
    
    public ArrayList<Location> getChests(){
        return chests;
    }
    
    public boolean isChest(Location l){
        return chests.contains(l);
    }
    
    public void addChest(Location l){
        chests.add(l);
    }
    
    public void removeChest(Location l){
        chests.remove(l);
    }
    
}