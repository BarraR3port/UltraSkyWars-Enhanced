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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.listeners.EloListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.managers.EloRankManager;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.managers.SeasonManager;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.menus.RankedMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.placeholders.Placeholders;
import org.bukkit.Bukkit;

public class InjectionEloRank implements Injection {
    
    private Injection instance;
    private EloRankManager erm;
    private SeasonManager sm;
    private Settings eloRank, rankeds;
    private RankedMenu rem;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        instance = this;
        eloRank = new Settings("modules/eloRank", false, false);
        rankeds = new Settings("modules/rankeds", false, false);
        erm = new EloRankManager(main, this);
        sm = new SeasonManager(this);
        rem = new RankedMenu(this);
        Bukkit.getServer().getPluginManager().registerEvents(new EloListener(main, this), main);
        Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> sm.reduce(), 20 * 5, 20 * 5);
    }
    
    @Override
    public void reload(){
        eloRank.reload();
        erm.reload();
    }
    
    @Override
    public void disable(){
    
    }
    
    public Settings getRankeds(){
        return rankeds;
    }
    
    public Settings getEloRank(){
        return eloRank;
    }
    
    public RankedMenu getRem(){
        return rem;
    }
    
    public SeasonManager getSm(){
        return sm;
    }
    
    public EloRankManager getErm(){
        return erm;
    }
    
    public Injection getInstance(){
        return instance;
    }
    
    public void loadPlaceholders(){
        new Placeholders(this).register();
    }
    
    /*
    REMOVED DUE TO REPO SERVER DOWN
    public void loadMVdWPlaceholders(){
        new MVdWPlaceholders(UltraSkyWarsApi.get(), this).register();
    }*/
    
}