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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup.cosmetics;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TauntTypeSetup {
    
    private final Player p;
    private final String damage;
    private final ArrayList<String> msg;
    
    public TauntTypeSetup(Player p, String damage, List<String> msg){
        this.p = p;
        this.damage = damage;
        this.msg = new ArrayList<>(msg);
    }
    
    public String getDamage(){
        return damage;
    }
    
    public ArrayList<String> getMsg(){
        return msg;
    }
}