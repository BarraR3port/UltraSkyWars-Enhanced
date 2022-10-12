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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.listeners.SignsListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.managers.SignsManager;
import org.bukkit.Bukkit;

public class InjectionSigns implements Injection {
    
    private SignsManager sim;
    private Settings signs;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        signs = new Settings("modules/signs", false, false);
        sim = new SignsManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new SignsListener(main, this), main);
    }
    
    @Override
    public void reload(){
        signs.reload();
        sim.reload();
    }
    
    @Override
    public void disable(){
    
    }
    
    public SignsManager getSim(){
        return sim;
    }
    
    public Settings getSigns(){
        return signs;
    }
}