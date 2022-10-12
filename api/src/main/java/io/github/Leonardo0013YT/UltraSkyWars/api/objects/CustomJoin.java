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
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;

import java.util.List;

public class CustomJoin {
    
    private final String name;
    private final List<String> maps;
    private final UltraSkyWarsApi plugin;
    
    public CustomJoin(UltraSkyWarsApi plugin, String path){
        this.plugin = plugin;
        this.name = plugin.getJoin().get(null, path + ".name");
        this.maps = plugin.getJoin().getList(path + ".maps");
    }
    
    public String getName(){
        return name;
    }
    
    public GameData getRandomGame(){
        int alto = 0;
        GameData g = null;
        for ( String map : maps ){
            GameData game = plugin.getGm().getGameData().get(map);
            if (game == null || game.isState(State.GAME) || game.isState(State.RESTARTING) || game.isState(State.FINISH) || game.isState(State.PREGAME)){
                continue;
            }
            if (g == null || (alto < game.getPlayers())){
                g = game;
                alto = game.getPlayers();
            }
        }
        return g;
    }
    
    public int getGameSize(){
        int count = 0;
        for ( String map : maps ){
            Game game = plugin.getGm().getGameByName(map);
            if (game == null){
                continue;
            }
            count += game.getPlayers().size();
        }
        return count;
    }
    
}