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

package io.github.Leonardo0013YT.UltraSkyWarsSetup;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.cmds.SetupCMD;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.listeners.SetupListener;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.managers.SetupManager;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.menus.SetupMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UltraSkyWarsSetup extends JavaPlugin {
    
    private static UltraSkyWarsSetup instance;
    private UltraSkyWarsApi ultraSW;
    private SetupManager sm;
    private SetupMenu sem;
    
    public static UltraSkyWarsSetup get(){
        return instance;
    }
    
    @Override
    public void onEnable(){
        instance = this;
        if (!Bukkit.getPluginManager().isPluginEnabled("UltraSkyWars")){
            Bukkit.getConsoleSender().sendMessage("§4You must have UltraSkyWars.jar before you can use Setup mode.");
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ultraSW = UltraSkyWarsApi.get();
        this.sm = new SetupManager();
        this.sem = new SetupMenu(ultraSW);
        getServer().getPluginManager().registerEvents(new SetupListener(ultraSW, this), this);
        getCommand("sws").setExecutor(new SetupCMD(ultraSW, this));
    }
    
    @Override
    public void onDisable(){
    
    }
    
    public UltraSkyWarsApi getUltraSW(){
        return ultraSW;
    }
    
    public SetupManager getSm(){
        return sm;
    }
    
    public SetupMenu getSem(){
        return sem;
    }
}