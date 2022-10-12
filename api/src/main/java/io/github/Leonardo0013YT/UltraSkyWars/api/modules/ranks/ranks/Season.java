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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.ranks.InjectionEloRank;

import java.util.HashMap;

public class Season {
    
    private final int season;
    private final HashMap<Integer, SeasonDivision> divisions = new HashMap<>();
    
    public Season(InjectionEloRank ier, String path, int season){
        this.season = season;
        int actSeason = ier.getRankeds().getInt("data.season");
        if (ier.getRankeds().isSet(path + "divisions")){
            for ( String d : ier.getRankeds().getConfig().getConfigurationSection(path + "divisions").getKeys(false) ){
                int order = ier.getRankeds().getInt("divisions." + d + ".order");
                divisions.put(order, new SeasonDivision(ier, path + "divisions." + d, season, actSeason > season));
            }
        }
    }
    
    public void execute(){
    
    }
    
    public int getSeason(){
        return season;
    }
    
    public HashMap<Integer, SeasonDivision> getDivisions(){
        return divisions;
    }
}