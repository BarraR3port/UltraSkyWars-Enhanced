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

package io.github.Leonardo0013YT.UltraSkyWars.TNTMadness;

import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.cmds.TNTMadnessCMD;
import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.listeners.TNTListener;
import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.managers.ItemManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class UltraSkyWarsTNTM extends JavaPlugin {
    
    private static UltraSkyWarsTNTM instance;
    private Settings tntLang;
    private ItemManager im;
    
    @Override
    public void onEnable(){
        instance = this;
        this.tntLang = new Settings(this, "tntLang", true, false);
        this.im = new ItemManager(this);
        getCommand("uswt").setExecutor(new TNTMadnessCMD(this));
        getServer().getPluginManager().registerEvents(new TNTListener(this), this);
    }
    
    @Override
    public void onDisable(){
        
    }
}