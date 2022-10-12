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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.signs;

import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XBlock;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class GameSign {
    
    private final Block retract;
    private final String type;
    private final Location loc;
    private GameData data;
    private boolean occupied;
    
    public GameSign(String type, Location loc){
        this.type = type;
        this.loc = loc;
        this.retract = Utils.getBlockFaced(loc.getBlock());
        this.occupied = false;
    }
    
    public synchronized void setLines(String... line){
        if (loc.getBlock().getState() instanceof Sign){
            Sign sign = (Sign) loc.getBlock().getState();
            sign.setLine(0, line[0]);
            sign.setLine(1, line[1]);
            sign.setLine(2, line[2]);
            sign.setLine(3, line[3]);
            sign.update();
            sign.update(true);
        }
    }
    
    public void setState(String state){
        switch(state){
            case "WAITING":
                XBlock.setColor(retract, DyeColor.LIME);
                break;
            case "STARTING":
                XBlock.setColor(retract, DyeColor.YELLOW);
                break;
            case "PREGAME":
                XBlock.setColor(retract, DyeColor.PINK);
                break;
            case "GAME":
                XBlock.setColor(retract, DyeColor.RED);
                break;
            case "FINISH":
                XBlock.setColor(retract, DyeColor.BLUE);
                break;
            case "RESTARTING":
                XBlock.setColor(retract, DyeColor.PURPLE);
                break;
            default:
                XBlock.setColor(retract, DyeColor.WHITE);
                break;
        }
    }
    
    public boolean isOccupied(){
        return occupied;
    }
    
    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }
    
    public String getType(){
        return type.toLowerCase();
    }
    
    public GameData getData(){
        return data;
    }
    
    public void setData(GameData data){
        this.data = data;
    }
    
}