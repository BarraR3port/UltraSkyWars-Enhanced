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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners.GameListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners.MenuListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners.PlayerListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.managers.SoulWellManager;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.menus.SoulWellMenu;
import lombok.Getter;

@Getter
public class InjectionSoulWell implements Injection {
    
    private UltraSkyWarsApi plugin;
    private Settings soulwell;
    private SoulWellManager swm;
    private SoulWellMenu wel;
    
    @Override
    public void loadInjection(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        this.soulwell = new Settings("modules/soulwell", true, false);
        this.swm = new SoulWellManager(plugin, this);
        this.wel = new SoulWellMenu(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(plugin, this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(plugin, this), plugin);
        if (!plugin.getCm().isBungeeModeLobby()){
            plugin.getServer().getPluginManager().registerEvents(new GameListener(plugin, this), plugin);
        }
    }
    
    @Override
    public void reload(){
    
    }
    
    @Override
    public void disable(){
    }
    
}