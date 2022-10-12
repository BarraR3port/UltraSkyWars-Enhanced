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

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks.EloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class EloRankManager {
    
    private final HashMap<String, EloRank> eloRankHashMap = new HashMap<>();
    private final HashMap<UUID, String> playerEloRankHashMap = new HashMap<>();
    private final UltraSkyWarsApi plugin;
    private final InjectionEloRank injectionEloRank;
    private String defaultRank;
    private boolean randomEnabled;
    private int randomMin, randomMax, exactlyKill, exactlyWin, exactlyDeath;
    
    public EloRankManager(UltraSkyWarsApi plugin, InjectionEloRank injectionEloRank){
        this.plugin = plugin;
        this.injectionEloRank = injectionEloRank;
        reload();
    }
    
    public void reload(){
        defaultRank = injectionEloRank.getEloRank().getOrDefault("defaultRank", "classify");
        randomEnabled = injectionEloRank.getRankeds().getBooleanOrDefault("elos.random.enabled", false);
        randomMin = injectionEloRank.getRankeds().getIntOrDefault("elos.random.min", 1);
        randomMax = injectionEloRank.getRankeds().getIntOrDefault("elos.random.max", 10);
        exactlyKill = injectionEloRank.getRankeds().getIntOrDefault("elos.exactly.kill", 3);
        exactlyWin = injectionEloRank.getRankeds().getIntOrDefault("elos.exactly.win", 5);
        exactlyDeath = injectionEloRank.getRankeds().getIntOrDefault("elos.exactly.death", -1);
        eloRankHashMap.clear();
        if (injectionEloRank.getEloRank().isSet("ranks")){
            ConfigurationSection conf = injectionEloRank.getEloRank().getConfig().getConfigurationSection("ranks");
            for ( String rank : conf.getKeys(false) ){
                eloRankHashMap.put(rank, new EloRank(injectionEloRank, "ranks." + rank, rank));
            }
        }
    }
    
    public EloRank getEloRank(Player p){
        if (playerEloRankHashMap.containsKey(p.getUniqueId())){
            return eloRankHashMap.get(playerEloRankHashMap.get(p.getUniqueId()));
        }
        if (plugin.getDb().getSWPlayer(p) == null) return eloRankHashMap.get(defaultRank);
        int elo = plugin.getDb().getSWPlayer(p).getElo();
        for ( EloRank rank : eloRankHashMap.values() ){
            if (elo >= rank.getMin() && elo < rank.getMax()){
                playerEloRankHashMap.put(p.getUniqueId(), rank.getName());
                return rank;
            }
        }
        EloRank rank = eloRankHashMap.get(defaultRank);
        if (rank != null){
            return rank;
        } else {
            return new EloRank("§7", "§7", "§7");
        }
    }
    
    public int getEloDiference(int eloKiller, int eloDeath){
        if (eloKiller >= eloDeath){
            return a(eloKiller, eloDeath);
        } else {
            return a(eloDeath, eloKiller);
        }
    }
    
    private int a(int eloKiller, int eloDeath){
        DecimalFormat f = new DecimalFormat("#");
        f.setRoundingMode(RoundingMode.HALF_EVEN);
        int eloD = eloKiller - eloDeath;
        double eloO = (randomMax * eloD) * 0.01;
        double elo;
        if (eloO > randomMax){
            elo = (randomMax * eloO) * 0.01;
        } else {
            elo = randomMin;
        }
        return Integer.parseInt(f.format(elo));
    }
    
    public void checkUpgrateOrDegrate(Player p){
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        String eloRank = sw.getEloRank();
        EloRank rank = getEloRank(p);
        if (rank.getName().equals(eloRank)){
            return;
        }
        //Demote
        if (!eloRankHashMap.containsKey(rank.getName()) || !eloRankHashMap.containsKey(eloRank)){
            sw.setEloRank(defaultRank);
            return;
        }
        if (eloRankHashMap.get(rank.getName()).getOrder() < eloRankHashMap.get(eloRank).getOrder()){
            sw.setEloRank(rank.getName());
            CustomSound.DEGRADE.reproduce(p);
            p.sendMessage(plugin.getLang().get(p, "messages.demote.msg").replaceAll("<eloRank>", rank.getColor() + rank.getPrefix()));
            if (plugin.getCm().isBroadcastRankLevelDown()){
                for ( Player on : Bukkit.getOnlinePlayers() ){
                    on.sendMessage(plugin.getLang().get(p, "messages.demote.broadcast").replaceAll("<player>", p.getName()).replaceAll("<eloRank>", rank.getColor() + rank.getPrefix()));
                }
            }
            Utils.updateSB(p);
        }
        //Promote
        if (eloRankHashMap.get(rank.getName()).getOrder() > eloRankHashMap.get(eloRank).getOrder()){
            sw.setEloRank(rank.getName());
            CustomSound.UPGRADE.reproduce(p);
            p.sendMessage(plugin.getLang().get(p, "messages.promote.msg").replaceAll("<eloRank>", rank.getColor() + rank.getPrefix()));
            if (plugin.getCm().isBroadcastRankLevelUp()){
                for ( Player on : Bukkit.getOnlinePlayers() ){
                    on.sendMessage(plugin.getLang().get(p, "messages.promote.broadcast").replaceAll("<player>", p.getName()).replaceAll("<eloRank>", rank.getColor() + rank.getPrefix()));
                }
            }
            Utils.updateSB(p);
        }
    }
    
    public String getEloRankBetween(int amount){
        for ( EloRank er : eloRankHashMap.values() ){
            if (er.getMin() < amount && amount < er.getMax()){
                return er.getColor() + ":" + er.getPrefix();
            }
        }
        EloRank er = eloRankHashMap.get(defaultRank);
        return er.getColor() + ":" + er.getPrefix();
    }
    
    public String getEloRankSB(Player p){
        return getEloRank(p).getColor() + getEloRank(p).getCorto();
    }
    
    public String getEloRankChat(Player p){
        return getEloRank(p).getColor() + getEloRank(p).getPrefix();
    }
    
}