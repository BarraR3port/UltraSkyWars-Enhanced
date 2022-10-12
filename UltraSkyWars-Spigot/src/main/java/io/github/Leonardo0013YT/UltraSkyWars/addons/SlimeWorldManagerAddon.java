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

package io.github.Leonardo0013YT.UltraSkyWars.addons;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.managers.ISlimeWorldManagerAddon;
import org.bukkit.Bukkit;

import java.io.IOException;

public class SlimeWorldManagerAddon implements ISlimeWorldManagerAddon {
    
    private final UltraSkyWarsApi plugin;
    private final SlimePlugin slime;
    private final SlimeLoader loader;
    
    public SlimeWorldManagerAddon(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        this.slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = slime.getLoader(plugin.getCm().getSlimeworldmanagerLoader());
    }
    
    public void createWorld(String name){
        SlimePropertyMap slimeProperties = new SlimePropertyMap();
        slimeProperties.setInt(SlimeProperties.SPAWN_X, 0);
        slimeProperties.setInt(SlimeProperties.SPAWN_Y, 75);
        slimeProperties.setInt(SlimeProperties.SPAWN_Z, 0);
        slimeProperties.setBoolean(SlimeProperties.PVP, true);
        slimeProperties.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        slimeProperties.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        slimeProperties.setString(SlimeProperties.DIFFICULTY, "normal");
        slimeProperties.setString(SlimeProperties.ENVIRONMENT, "normal");
        try {
            slime.createEmptyWorld(loader, name, false, slimeProperties);
        } catch (WorldAlreadyExistsException | IOException ignored) {
        }
    }
    
}