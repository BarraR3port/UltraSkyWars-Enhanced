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

package io.github.Leonardo0013YT.UltraSkyWars.api.game;

import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import lombok.Getter;
import lombok.Setter;

public class GameData {
    
    @Getter
    @Setter
    private String map, server, state, type;
    @Getter
    @Setter
    private int players, max;
    @Setter
    private String color;
    
    public GameData(String server, String map, String color, String state, String type, int players, int max){
        this.map = map;
        this.server = server;
        this.color = color;
        this.state = state;
        this.players = players;
        this.max = max;
        this.type = type.toLowerCase();
    }
    
    public GameData(String map, String color, String state, String type, int players, int max){
        this("", map, color, state, type, players, max);
    }
    
    public boolean isState(State state){
        return State.valueOf(this.state.toUpperCase()).equals(state);
    }
    
    public String getColor(){
        if (color.equals("none")){
            return "";
        }
        return color;
    }
    
}