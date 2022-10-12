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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.events.specials.RankedSeasonChangeEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks.Season;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class SeasonManager {
    
    private final InjectionEloRank ier;
    private final TimeUnit timeUnit;
    private final LinkedHashMap<Integer, Season> lasted = new LinkedHashMap<>();
    private int season;
    private long restTime;
    
    public SeasonManager(InjectionEloRank ier){
        this.ier = ier;
        this.timeUnit = TimeUnit.valueOf(ier.getRankeds().get("timeUnit"));
        this.season = ier.getRankeds().getConfig().getInt("data.season");
        this.restTime = ier.getRankeds().getConfig().getLong("data.finish");
        if (restTime <= 0){
            backup();
            season++;
            restTime = timeUnit.toMillis(ier.getRankeds().getInt("duration"));
            ier.getRankeds().set("data.season", season);
            ier.getRankeds().set("data.finish", restTime);
            ier.getRankeds().save();
            Bukkit.getServer().getPluginManager().callEvent(new RankedSeasonChangeEvent(season));
        }
        reload();
    }
    
    public Season getActualSeason(){
        return lasted.get(season);
    }
    
    public void reload(){
        lasted.clear();
        if (ier.getRankeds().isSet("backup")){
            for ( String s : ier.getRankeds().getConfig().getConfigurationSection("backup").getKeys(false) ){
                int season = Integer.parseInt(s);
                lasted.put(season, new Season(ier, "backup." + season + ".", season));
            }
        }
        lasted.put(season, new Season(ier, "", season));
    }
    
    public void backup(){
        if (ier.getRankeds().isSet("divisions")){
            for ( String s : ier.getRankeds().getConfig().getConfigurationSection("divisions").getKeys(false) ){
                String path = "divisions." + s;
                String backupPath = "backup." + season + ".divisions." + s;
                ier.getRankeds().set(backupPath + ".icon.material", ier.getRankeds().get(path + ".icon.material"));
                ier.getRankeds().set(backupPath + ".icon.amount", ier.getRankeds().getInt(path + ".icon.amount"));
                ier.getRankeds().set(backupPath + ".icon.data", ier.getRankeds().getInt(path + ".icon.data"));
                ier.getRankeds().set(backupPath + ".top.min", ier.getRankeds().getInt(path + ".top.min"));
                ier.getRankeds().set(backupPath + ".top.max", ier.getRankeds().getInt(path + ".top.max"));
                ier.getRankeds().set(backupPath + ".name", ier.getRankeds().get(path + ".name"));
                ier.getRankeds().set(backupPath + ".lore", ier.getRankeds().getList(path + ".lore"));
                ier.getRankeds().set(backupPath + ".ranks", ier.getRankeds().getList(path + ".ranks"));
                ier.getRankeds().set(backupPath + ".rewards.commands", ier.getRankeds().getList(path + ".rewards.commands"));
            }
        }
        ier.getRankeds().save();
    }
    
    public void reduce(){
        restTime = restTime - 5000;
        ier.getRankeds().set("data.finish", restTime);
        if (restTime <= 0){
            backup();
            season++;
            restTime = timeUnit.toMillis(ier.getRankeds().getInt("duration"));
            ier.getRankeds().set("data.season", season);
            ier.getRankeds().set("data.finish", restTime);
            Bukkit.getServer().getPluginManager().callEvent(new RankedSeasonChangeEvent(season));
        }
        ier.getRankeds().save();
    }
    
    public int getSeason(){
        return season;
    }
    
    public long getRestTime(){
        return restTime;
    }
    
    public HashMap<Integer, Season> getLasted(){
        return lasted;
    }
}