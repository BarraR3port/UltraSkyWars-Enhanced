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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends.listeners.MobFriendsListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends.listeners.TargetListener;
import org.bukkit.Bukkit;

public class InjectionMobFriends implements Injection {
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        Bukkit.getServer().getPluginManager().registerEvents(new MobFriendsListener(main), main);
        Bukkit.getServer().getPluginManager().registerEvents(new TargetListener(main), main);
    }
    
    @Override
    public void reload(){
    
    }
    
    @Override
    public void disable(){
    
    }
    
}