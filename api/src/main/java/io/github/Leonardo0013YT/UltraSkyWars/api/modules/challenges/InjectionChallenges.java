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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.enums.ChallengeType;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.listeners.ArmorListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.listeners.ChallengeListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.challenges.menus.ChallengeMenu;
import org.bukkit.Material;

public class InjectionChallenges implements Injection {
    
    private ChallengeMenu chm;
    private Settings challenges;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        this.chm = new ChallengeMenu(main);
        this.challenges = new Settings("modules/challenges", true, false);
        main.getServer().getPluginManager().registerEvents(new ChallengeListener(main, this), main);
        main.getServer().getPluginManager().registerEvents(new ArmorListener(), main);
        for ( ChallengeType type : ChallengeType.values() ){
            String path = "challenges." + type.name();
            type.setMaterial(Material.valueOf(challenges.getOrDefault(path + ".material", type.getMaterial().name())));
            type.setIconSlot(challenges.getIntOrDefault(path + ".iconSlot", type.getIconSlot()));
            type.setStatusSlot(challenges.getIntOrDefault(path + ".statusSlot", type.getStatusSlot()));
            type.setEnabled(challenges.getBoolean(path + ".enabled"));
            type.setOnWinEnabled(challenges.getBoolean(path + ".onWin.enabled"));
            type.setOnWinCommands(challenges.getList(path + ".onWin.commands"));
        }
    }
    
    @Override
    public void reload(){
    
    }
    
    @Override
    public void disable(){
    
    }
    
    public Settings getChallenges(){
        return challenges;
    }
    
    public ChallengeMenu getChm(){
        return chm;
    }
}