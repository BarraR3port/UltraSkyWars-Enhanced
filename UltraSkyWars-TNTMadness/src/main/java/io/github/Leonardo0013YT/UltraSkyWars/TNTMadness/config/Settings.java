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

package io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.config;

import io.github.Leonardo0013YT.UltraSkyWars.TNTMadness.UltraSkyWarsTNTM;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Settings {
    
    private final YamlConfiguration config;
    private final File file;
    private final UltraSkyWarsTNTM u;
    private final boolean comments;
    
    public Settings(UltraSkyWarsTNTM u, String s, boolean defaults, boolean comments){
        this.u = u;
        this.comments = comments;
        this.file = new File(u.getDataFolder(), s + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
        Reader reader;
        if (comments){
            reader = new InputStreamReader(getConfigContent(new InputStreamReader(u.getResource(s + ".yml"), StandardCharsets.UTF_8)));
        } else {
            reader = new InputStreamReader(u.getResource(s + ".yml"), StandardCharsets.UTF_8);
        }
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(reader);
        try {
            if (!this.file.exists()){
                this.config.addDefaults(loadConfiguration);
                this.config.options().copyDefaults(true);
                if (comments){
                    save();
                } else {
                    this.config.save(file);
                }
            } else {
                if (defaults){
                    this.config.addDefaults(loadConfiguration);
                    this.config.options().copyDefaults(true);
                    if (comments){
                        save();
                    } else {
                        this.config.save(file);
                    }
                }
                this.config.load(this.file);
            }
        } catch (IOException | InvalidConfigurationException ignored) {
        }
    }
    
    public void reload(){
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public InputStream getConfigContent(Reader reader){
        try {
            String addLine, currentLine, pluginName = u.getDescription().getName();
            int commentNum = 0;
            StringBuilder whole = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(reader);
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.startsWith("#")){
                    addLine = currentLine.replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ":");
                    whole.append(addLine).append("\n");
                    commentNum++;
                } else {
                    whole.append(currentLine).append("\n");
                }
            }
            String config = whole.toString();
            InputStream configStream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
            bufferedReader.close();
            return configStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    private String prepareConfigString(String configString){
        String[] lines = configString.split("\n");
        StringBuilder config = new StringBuilder();
        for ( String line : lines ){
            if (line.startsWith(u.getDescription().getName() + "_COMMENT")){
                String comment = "#" + line.trim().substring(line.indexOf(":") + 1);
                String normalComment;
                
                if (comment.startsWith("# ' ")){
                    normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
                } else {
                    normalComment = comment;
                }
                
                config.append(normalComment).append("\n");
            } else {
                config.append(line).append("\n");
            }
        }
        return config.toString();
    }
    
    
    public void save(){
        if (comments){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(prepareConfigString(config.saveToString()));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.config.save(file);
            } catch (IOException ignored) {
            }
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
        return this.config.getString(s).replaceAll("<l>", "¡").replaceAll("&", "§").replaceAll("-,-", "ñ");
    }
    
    public String getOrDefault(String s, String def){
        if (config.isSet(s)){
            return get(s);
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
