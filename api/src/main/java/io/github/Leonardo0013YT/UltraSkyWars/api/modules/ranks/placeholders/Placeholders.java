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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.placeholders;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks.EloRank;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
    
    private final InjectionEloRank plugin;
    
    public Placeholders(InjectionEloRank plugin){
        this.plugin = plugin;
    }
    
    public String getIdentifier(){
        return "uswranked";
    }
    
    public String getAuthor(){
        return "Leonardo0013YT";
    }
    
    public String getVersion(){
        return "1.0.0";
    }
    
    @Override
    public boolean persist(){
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player p, String id){
        SWPlayer sw = UltraSkyWarsApi.get().getDb().getSWPlayer(p);
        if (sw == null){
            return "";
        }
        if (id.equals("progress")){
            EloRank er = plugin.getErm().getEloRank(p);
            int elo = sw.getElo() - er.getMin();
            int max = er.getMax() - er.getMin();
            return Utils.getProgressBar(elo, max, UltraSkyWarsApi.get().getCm().getProgressBarAmount());
        }
        if (id.equals("rank")){
            return plugin.getErm().getEloRankChat(p);
        }
        return null;
    }
    
}