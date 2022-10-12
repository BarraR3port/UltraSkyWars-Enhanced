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

package io.github.Leonardo0013YT.UltraSkyWars.api.config;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Settings {
    
    private final YamlConfiguration config;
    private final File file;
    private final boolean hexColor;
    
    public Settings(String s, boolean defaults){
        this(s, defaults, false);
    }
    
    public Settings(String s, boolean defaults, boolean hexColor){
        this.hexColor = hexColor;
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        this.file = new File(plugin.getDataFolder(), s + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
        Reader reader = new InputStreamReader(plugin.getResource(s + ".yml"), StandardCharsets.UTF_8);
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(reader);
        try {
            if (!this.file.exists()){
                this.config.addDefaults(loadConfiguration);
                this.config.options().copyDefaults(true);
                this.config.save(file);
            } else {
                if (defaults){
                    this.config.addDefaults(loadConfiguration);
                    this.config.options().copyDefaults(true);
                    this.config.save(file);
                }
                this.config.load(this.file);
            }
        } catch (IOException | InvalidConfigurationException ignored) {
        }
        if (config.getInt("version", 0) < 8 && s.equals("levels")){
            this.config.addDefaults(loadConfiguration);
            this.config.options().copyDefaults(true);
            try {
                this.config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reload(){
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void save(){
        try {
            this.config.save(file);
        } catch (IOException ignored) {
        }
    }
    
    public YamlConfiguration getConfig(){
        return this.config;
    }
    
    public ItemStack getItemStack(String s){
        if (config.getString(s) == null){
            return new ItemStack(Material.STONE);
        }
        return this.config.getItemStack(s);
    }
    
    public String get(String s){
        if (config.getString(s) == null){
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', this.config.getString(s));
    }
    
    public String get(Player p, String s){
        if (config.getString(s) == null){
            return "";
        }
        String string = ChatColor.translateAlternateColorCodes('&', this.config.getString(s));
        if (p != null){
            return UltraSkyWarsApi.get().getAdm().parsePlaceholders(p, string);
        }
        return string;
    }
    
    public String getOrDefault(String s, String def){
        if (config.isSet(s)){
            return get(null, s);
        }
        set(s, def);
        save();
        return def;
    }
    
    public int getInt(String s){
        return this.config.getInt(s);
    }
    
    public int getIntOrDefault(String s, int def){
        if (config.isSet(s)){
            return getInt(s);
        }
        set(s, def);
        save();
        return def;
    }
    
    public double getDouble(String s){
        return this.config.getDouble(s);
    }
    
    public double getDoubleOrDefault(String s, double def){
        if (config.isSet(s)){
            return getDouble(s);
        }
        set(s, def);
        save();
        return def;
    }
    
    public List<String> getList(String s){
        return this.config.getStringList(s);
    }
    
    public List<String> getListOrDefault(String s, List<String> def){
        if (config.isSet(s)){
            return getList(s);
        }
        set(s, def);
        save();
        return def;
    }
    
    public boolean isSet(String s){
        return this.config.isSet(s);
    }
    
    public void set(String s, Object o){
        this.config.set(s, o);
    }
    
    public boolean getBoolean(String s){
        return this.config.getBoolean(s);
    }
    
    public boolean getBooleanOrDefault(String s, boolean def){
        if (config.isSet(s)){
            return getBoolean(s);
        }
        set(s, def);
        save();
        return def;
    }
    
}
