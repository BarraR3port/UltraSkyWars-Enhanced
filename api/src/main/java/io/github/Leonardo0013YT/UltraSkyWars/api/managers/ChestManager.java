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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.chests.ChestType;
import lombok.Getter;

import java.util.HashMap;

public class ChestManager {
    
    @Getter
    private final HashMap<String, ChestType> chests = new HashMap<>();
    @Getter
    private String defaultChest;
    
    public ChestManager(){
        reload();
    }
    
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (!plugin.getChestType().isSet("types")){
            return;
        }
        defaultChest = plugin.getChestType().getOrDefault("defaultType", "NORMAL");
        for ( String s : plugin.getChestType().getConfig().getConfigurationSection("types").getKeys(false) ){
            chests.put(s.toUpperCase(), new ChestType(plugin, s));
        }
        if (!chests.containsKey(defaultChest)){
            defaultChest = chests.keySet().stream().findFirst().orElse("NORMAL");
        }
    }
    
}