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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map.Entry;

public class Kit {
    
    private final HashMap<Integer, KitLevel> levels = new HashMap<>();
    private final List<String> modes;
    private final int id;
    private final int slot;
    private final int page;
    private final String name;
    private final String permission;
    
    public Kit(UltraSkyWarsApi plugin, String path){
        this.id = plugin.getKits().getInt(path + ".id");
        this.slot = plugin.getKits().getInt(path + ".slot");
        this.page = plugin.getKits().getInt(path + ".page");
        this.name = plugin.getKits().get(path + ".name");
        this.permission = plugin.getKits().get(path + ".permission");
        this.modes = plugin.getKits().getListOrDefault(path + ".modes", Arrays.asList("SOLO", "TEAM", "RANKED"));
        if (plugin.getKits().getConfig().getConfigurationSection(path + ".levels") != null){
            for ( String level : plugin.getKits().getConfig().getConfigurationSection(path + ".levels").getKeys(false) ){
                int nivel = Integer.parseInt(level);
                levels.put(nivel, new KitLevel(plugin, path + ".levels." + level, this));
            }
        }
        plugin.getKm().setLastPage(page);
    }
    
    public int getId(){
        return id;
    }
    
    public KitLevel getKitLevelByItem(Player p, ItemStack item){
        for ( KitLevel l : getLevels().values() ){
            if (l.getIcon(p).getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())){
                return l;
            }
        }
        return null;
    }
    
    public HashMap<Integer, KitLevel> getLevels(){
        return levels;
    }
    
    public Entry<Integer, KitLevel> getFirstLevel(){
        NavigableMap<Integer, KitLevel> levels = new TreeMap<>(this.levels);
        return levels.firstEntry();
    }
    
    public List<String> getModes(){
        return modes;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public int getPage(){
        return page;
    }
    
    public String getName(){
        return name;
    }
    
    public String getPermission(){
        return permission;
    }
}