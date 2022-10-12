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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.Location;

public class SoulWell {
    
    private final UltraSkyWarsApi plugin;
    private final Location loc;
    private final Location hologram;
    
    public SoulWell(UltraSkyWarsApi plugin, Location loc){
        this.plugin = plugin;
        this.loc = loc;
        this.hologram = loc.clone().add(0.5, 0, 0.5);
    }
    
    public void reload(){
        if (plugin.getAdm().hasHologramPlugin()){
            plugin.getAdm().createHologram(hologram, plugin.getLang().getList("holograms.soulwell"));
        }
    }
    
    public Location getHologram(){
        return hologram;
    }
    
    public Location getLoc(){
        return loc;
    }
    
}