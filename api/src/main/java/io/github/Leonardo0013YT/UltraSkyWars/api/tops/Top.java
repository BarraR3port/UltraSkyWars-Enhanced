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

package io.github.Leonardo0013YT.UltraSkyWars.api.tops;

import io.github.Leonardo0013YT.UltraSkyWars.api.enums.TopType;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Top {
    
    private final Map<Integer, TopPlayer> tops = new HashMap<>();
    private final TopType type;
    
    public Top(TopType type, List<String> tops){
        this.type = type;
        for ( String top : tops ){
            String[] t = top.split(":");
            String uuid = t[0];
            String name = t[1];
            int position = Integer.parseInt(t[2]);
            int amount = Integer.parseInt(t[3]);
            this.tops.put(position, new TopPlayer(type.name(), uuid, name, position, amount));
        }
    }
    
}