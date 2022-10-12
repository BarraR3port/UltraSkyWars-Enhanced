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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.listeners.CubeletsListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.managers.CubeletsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class InjectionCubelets implements Injection {
    
    private final ArrayList<Entity> entities = new ArrayList<>();
    private CubeletsManager cbm;
    private Settings cubelets;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        cubelets = new Settings("modules/cubelets", true, false);
        cbm = new CubeletsManager(main, this);
        Bukkit.getServer().getPluginManager().registerEvents(new CubeletsListener(main, this), main);
    }
    
    public void reload(){
        cubelets.reload();
        cbm.reload();
    }
    
    @Override
    public void disable(){
    
    }
    
    public ArrayList<Entity> getEntities(){
        return entities;
    }
    
    public Settings getCubelets(){
        return cubelets;
    }
    
    public CubeletsManager getCbm(){
        return cbm;
    }
}