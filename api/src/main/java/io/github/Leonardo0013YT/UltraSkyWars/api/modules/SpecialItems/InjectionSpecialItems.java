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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.cmds.SpecialItemsCMD;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.listeners.SpecialItemsListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.managers.ItemManager;
import lombok.Getter;

@Getter
public class InjectionSpecialItems implements Injection {
    
    private Settings special_items;
    private ItemManager im;
    
    @Override
    public void loadInjection(UltraSkyWarsApi plugin){
        this.special_items = new Settings("modules/special_items", true, false);
        this.im = new ItemManager(plugin, this);
        plugin.getCommand("swi").setExecutor(new SpecialItemsCMD(plugin, this));
        plugin.getServer().getPluginManager().registerEvents(new SpecialItemsListener(plugin, this), plugin);
    }
    
    @Override
    public void reload(){
    
    }
    
    @Override
    public void disable(){
    
    }
    
}