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

package io.github.Leonardo0013YT.UltraSkyWars.addons.leaderheads;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.StatType;
import me.robin.leaderheads.datacollectors.OnlineDataCollector;
import me.robin.leaderheads.objects.BoardType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SkyWarsDeaths extends OnlineDataCollector {
    
    private final UltraSkyWarsApi plugin;
    
    public SkyWarsDeaths(UltraSkyWarsApi plugin){
        super("usw-deaths", "UltraSkyWars", BoardType.DEFAULT, plugin.getLang().get(null, "leaderheads.skywarsdeaths.title"), "skywarsdeaths", Arrays.asList(plugin.getLang().get(null, "leaderheads.skywarsdeaths.lines.0"), plugin.getLang().get(null, "leaderheads.skywarsdeaths.lines.1"), plugin.getLang().get(null, "leaderheads.skywarsdeaths.lines.2"), plugin.getLang().get(null, "leaderheads.skywarsdeaths.lines.3")));
        this.plugin = plugin;
    }
    
    @Override
    public Double getScore(Player p){
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null){
            return 0.0;
        }
        return (double) sw.getTotalStat(StatType.DEATHS);
    }
    
}