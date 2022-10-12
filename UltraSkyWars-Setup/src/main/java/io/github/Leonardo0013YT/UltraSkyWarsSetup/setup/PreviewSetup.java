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


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Location;

public class PreviewSetup {
    
    private final String type;
    private Location player, cosmetic;
    
    public PreviewSetup(String type){
        this.type = type;
    }
    
    public void save(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        plugin.getConfig().set("previews." + type.toLowerCase() + ".player", Utils.getLocationString(player));
        plugin.getConfig().set("previews." + type.toLowerCase() + ".cosmetic", Utils.getLocationString(cosmetic));
        plugin.saveConfig();
        plugin.reload();
    }
    
    public String getType(){
        return type;
    }
    
    public Location getCosmetic(){
        return cosmetic;
    }
    
    public void setCosmetic(Location cosmetic){
        this.cosmetic = cosmetic;
    }
    
    public Location getPlayer(){
        return player;
    }
    
    public void setPlayer(Location player){
        this.player = player;
    }
}