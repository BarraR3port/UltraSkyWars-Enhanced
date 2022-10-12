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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.listeners.PerksListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.managers.PerkManager;
import org.bukkit.Bukkit;

public class InjectionPerks implements Injection {
    
    private Injection instance;
    private PerkManager pem;
    private Settings perks;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        instance = this;
        perks = new Settings("modules/perks", true, false);
        pem = new PerkManager(this);
        if (!main.getCm().isBungeeModeLobby()){
            Bukkit.getServer().getPluginManager().registerEvents(new PerksListener(main, this), main);
        }
    }
    
    @Override
    public void reload(){
        perks.reload();
        pem.loadPerks();
    }
    
    @Override
    public void disable(){
    
    }
    
    public Settings getPerks(){
        return perks;
    }
    
    public PerkManager getPem(){
        return pem;
    }
    
    public Injection getInstance(){
        return instance;
    }
}