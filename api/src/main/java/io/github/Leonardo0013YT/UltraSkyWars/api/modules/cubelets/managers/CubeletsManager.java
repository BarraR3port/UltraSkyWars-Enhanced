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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.cubelets.Cubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CubeletsManager {
    
    private final HashMap<Location, Cubelets> cubelets = new HashMap<>();
    private final UltraSkyWarsApi plugin;
    private final InjectionCubelets injectionCubelets;
    
    public CubeletsManager(UltraSkyWarsApi plugin, InjectionCubelets injectionCubelets){
        this.plugin = plugin;
        this.injectionCubelets = injectionCubelets;
        loadCubelets();
    }
    
    public void loadCubelets(){
        cubelets.clear();
        if (plugin.getConfig().isSet("cubelets")){
            ConfigurationSection soul = plugin.getConfig().getConfigurationSection("cubelets");
            for ( String s : soul.getKeys(false) ){
                Location loc = Utils.getStringLocation(plugin.getConfig().getString("cubelets." + s + ".loc"));
                if (loc == null) continue;
                cubelets.put(loc, new Cubelets(plugin, injectionCubelets, loc));
            }
        }
    }
    
    public void executeCubelet(Player p){
        int random = ThreadLocalRandom.current().nextInt(0, 101);
        if (random < plugin.getCm().getCubeletChance()){
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            sw.addCubelets(1);
            p.sendMessage(plugin.getLang().get(p, "winCubelet"));
        }
    }
    
    public HashMap<Location, Cubelets> getCubelets(){
        return cubelets;
    }
    
    public void reload(){
        cubelets.values().forEach(Cubelets::reload);
    }
    
}