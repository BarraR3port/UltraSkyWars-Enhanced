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

package io.github.Leonardo0013YT.UltraSkyWars.api.superclass;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class UltraInventory {
    
    public String title;
    public Map<Integer, ItemStack> config = new HashMap<>();
    public Map<Integer, ItemStack> contents = new HashMap<>();
    public int rows = 6;
    public String name;
    
    public UltraInventory(String name){
        this.name = name;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public Map<Integer, ItemStack> getConfig(){
        return config;
    }
    
    public void setConfig(Map<Integer, ItemStack> config){
        this.config = config;
    }
    
    public Map<Integer, ItemStack> getContents(){
        return contents;
    }
    
    public void setContents(Map<Integer, ItemStack> contents){
        this.contents = contents;
    }
    
    public int getRows(){
        return rows;
    }
    
    public void setRows(int rows){
        this.rows = rows;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void save(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        plugin.getMenus().set("menus." + name + ".rows", rows);
        plugin.getMenus().set("menus." + name + ".items", null);
        for ( int i : config.keySet() ){
            plugin.getMenus().set("menus." + name + ".items." + i, config.get(i));
        }
        plugin.getMenus().save();
        reload();
    }
    
    public abstract void reload();
    
}