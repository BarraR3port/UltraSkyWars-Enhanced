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

public class EloRank {
    
    private final int min;
    private final int max;
    private final int order;
    private final String color;
    private final String prefix;
    private final String corto;
    private final String name;
    
    public EloRank(String color, String corto, String prefix){
        this.color = color;
        this.corto = corto;
        this.prefix = prefix;
        this.name = "Default";
        this.max = 0;
        this.min = 0;
        this.order = 0;
    }
    
    public EloRank(InjectionEloRank plugin, String path, String name){
        this.name = name;
        this.min = plugin.getEloRank().getInt(path + ".min");
        this.max = plugin.getEloRank().getInt(path + ".max");
        this.order = plugin.getEloRank().getInt(path + ".order");
        this.color = plugin.getEloRank().get(null, path + ".color");
        this.prefix = plugin.getEloRank().get(null, path + ".prefix");
        this.corto = plugin.getEloRank().get(null, path + ".short");
    }
    
    public String getPrefix(){
        return prefix;
    }
    
    public String getCorto(){
        return corto;
    }
    
    public String getColor(){
        return color;
    }
    
    public int getOrder(){
        return order;
    }
    
    public int getMax(){
        return max;
    }
    
    public int getMin(){
        return min;
    }
    
    public String getName(){
        return name;
    }
}