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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.listeners.LobbyListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class InjectionLProtection implements Injection {
    
    private Settings lobbyOptions;
    private ConfigManager cm;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        lobbyOptions = new Settings("modules/lobbyOptions", true, false);
        cm = new ConfigManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new LobbyListener(main, this), main);
        if (cm.isNoDayCycle()){
            World w = Bukkit.getWorld(cm.getLobbyWorld());
            if (w != null){
                w.setGameRuleValue("doDaylightCycle", "false");
            }
        }
    }
    
    @Override
    public void reload(){
        lobbyOptions.reload();
        cm.reload();
    }
    
    @Override
    public void disable(){
    
    }
    
    public Settings getLobbyOptions(){
        return lobbyOptions;
    }
    
    public ConfigManager getCm(){
        return cm;
    }
}