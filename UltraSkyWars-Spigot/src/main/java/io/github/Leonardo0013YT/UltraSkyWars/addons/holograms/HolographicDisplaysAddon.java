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

package io.github.Leonardo0013YT.UltraSkyWars.addons.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.HologramAddon;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class HolographicDisplaysAddon implements HologramAddon {
    
    private final HashMap<Location, Hologram> holograms = new HashMap<>();
    
    @Override
    public void createHologram(Location spawn, List<String> lines){
        Location loc = spawn.clone();
        Hologram h = HologramsAPI.createHologram(UltraSkyWarsApi.get(), loc.clone().add(0, 1.3 + (lines.size() * 0.3), 0));
        for ( String l : lines ){
            h.appendTextLine(l.replaceAll("&", "§"));
        }
        holograms.put(spawn, h);
    }
    
    @Override
    public void createHologram(Location spawn, List<String> lines, ItemStack item){
        Location loc = spawn.clone();
        Hologram h = HologramsAPI.createHologram(UltraSkyWarsApi.get(), loc.clone().add(0, 1.3 + (lines.size() * 0.3), 0));
        for ( String l : lines ){
            if (l.equals("<item>")){
                h.appendItemLine(item);
            } else {
                h.appendTextLine(l.replaceAll("&", "§"));
            }
        }
        holograms.put(spawn, h);
    }
    
    @Override
    public void deleteHologram(Location spawn){
        holograms.get(spawn).delete();
        holograms.remove(spawn);
    }
    
    @Override
    public boolean hasHologram(Location spawn){
        return holograms.containsKey(spawn);
    }
    
    @Override
    public void remove(){
        for ( Hologram h : holograms.values() ){
            h.delete();
        }
        holograms.clear();
    }
    
}