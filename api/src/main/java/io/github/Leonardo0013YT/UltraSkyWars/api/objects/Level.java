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

package io.github.Leonardo0013YT.UltraSkyWars.api.objects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Level {
    
    @Getter
    private final int id;
    @Getter
    private final int xp;
    @Getter
    private final int level;
    @Getter
    private final int levelUp;
    @Getter
    private final String prefix;
    private final List<String> rewards;
    
    public Level(UltraSkyWarsApi plugin, String path, int id){
        this.id = id;
        this.level = plugin.getLevels().getInt(path + ".level");
        this.xp = plugin.getLevels().getInt(path + ".xp");
        this.levelUp = plugin.getLevels().getInt(path + ".levelUp");
        this.prefix = plugin.getLevels().get(path + ".prefix");
        this.rewards = plugin.getLevels().getList(path + ".rewards");
    }
    
    public void execute(Player p){
        for ( String r : rewards ){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), r.replaceFirst("/", "").replaceAll("<player>", p.getName()));
        }
    }
    
}