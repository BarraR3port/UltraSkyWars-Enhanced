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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.InjectionSigns;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.signs.signs.GameSign;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignsManager {
    
    private final HashMap<Integer, GameSign> signs = new HashMap<>();
    private final HashMap<Location, Integer> signsLoc = new HashMap<>();
    private final ArrayList<String> showing = new ArrayList<>();
    private final ArrayList<String> waiting = new ArrayList<>();
    private final UltraSkyWarsApi plugin;
    private final InjectionSigns signsInjection;
    
    public SignsManager(UltraSkyWarsApi plugin, InjectionSigns signsInjection){
        this.plugin = plugin;
        this.signsInjection = signsInjection;
        reload();
    }
    
    public void reload(){
        showing.clear();
        waiting.clear();
        signs.clear();
        signsLoc.clear();
        if (signsInjection.getSigns().isSet("signs")){
            ConfigurationSection cs = signsInjection.getSigns().getConfig().getConfigurationSection("signs");
            if (cs == null) return;
            for ( String s : cs.getKeys(false) ){
                if (!signsInjection.getSigns().isSet("signs." + s.toUpperCase())) return;
                List<String> locs = signsInjection.getSigns().getList("signs." + s.toUpperCase());
                for ( String l : locs ){
                    Location loc = Utils.getStringLocation(l);
                    if (loc == null) continue;
                    if (loc.getBlock().getState() instanceof Sign){
                        int id = signs.size();
                        signs.put(id, new GameSign(s, loc));
                        signsLoc.put(loc, id);
                    }
                }
            }
        }
        loadSigns();
    }
    
    public void loadSigns(){
        for ( GameData data : UltraSkyWarsApi.getGameData().values() ){
            update(data.getServer(), data.getMap(), data.getState(), data.getPlayers(), data.getMax());
        }
        for ( GameSign gs : signs.values() ){
            if (!gs.isOccupied()){
                lines(gs);
            }
        }
    }
    
    private void lines(GameSign gs){
        String l1 = signsInjection.getSigns().get("texts.lines.empty.1");
        String l2 = signsInjection.getSigns().get("texts.lines.empty.2");
        String l3 = signsInjection.getSigns().get("texts.lines.empty.3");
        String l4 = signsInjection.getSigns().get("texts.lines.empty.4");
        gs.setState("EMPTY");
        gs.setLines(l1, l2, l3, l4);
    }
    
    public void update(String type2, String server2, String map2, String state2, int players2, int max2){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (plugin.getCm().isSignsRotation()){
                if (isStarted(state2)){
                    if (showing.contains(server2)){
                        GameSign gs = getServerSign(server2);
                        if (gs != null){
                            gs.setData(null);
                            gs.setState("EMPTY");
                            gs.setOccupied(false);
                            String l1 = signsInjection.getSigns().get("texts.lines.empty.1");
                            String l2 = signsInjection.getSigns().get("texts.lines.empty.2");
                            String l3 = signsInjection.getSigns().get("texts.lines.empty.3");
                            String l4 = signsInjection.getSigns().get("texts.lines.empty.4");
                            gs.setLines(l1, l2, l3, l4);
                            showing.remove(server2);
                            waiting.remove(server2);
                        }
                    }
                    fillEmpty(type2);
                    return;
                }
            }
            if (showing.contains(server2)){
                GameSign gs = getServerSign(server2);
                if (gs == null){
                    showing.remove(server2);
                    waiting.remove(server2);
                    update(type2, server2, map2, state2, players2, max2);
                    return;
                }
                removeWaiting(server2, map2, state2, players2, max2, gs);
                return;
            } else {
                if (added(type2, server2, map2, state2, players2, max2)) return;
            }
            fillEmpty(type2);
        });
    }
    
    private boolean added(String type2, String server2, String map2, String state2, int players2, int max2){
        GameSign gs = getEmptySign(type2);
        if (gs != null){
            GameData gameData = UltraSkyWarsApi.getGameData().get(server2);
            if (gameData == null){
                return true;
            }
            gs.setData(gameData);
            setLines(map2, state2, players2, max2, gs, gameData);
            showing.add(server2);
            waiting.remove(server2);
            return true;
        } else {
            if (!waiting.contains(server2)){
                waiting.add(server2);
            }
        }
        return false;
    }
    
    private void removeWaiting(String server2, String map2, String state2, int players2, int max2, GameSign gs){
        GameData gameData = gs.getData();
        setLines(map2, state2, players2, max2, gs, gameData);
        waiting.remove(server2);
    }
    
    private void setLines(String map2, String state2, int players2, int max2, GameSign gs, GameData gameData){
        gs.setState(state2);
        gs.setOccupied(true);
        String l1 = signsInjection.getSigns().get("texts.lines." + state2.toLowerCase() + ".1").replaceAll("<max>", String.valueOf(max2)).replaceAll("<players>", String.valueOf(players2)).replaceAll("<map>", map2).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + gameData.getState().toLowerCase())).replaceAll("<color>", gameData.getColor());
        String l2 = signsInjection.getSigns().get("texts.lines." + state2.toLowerCase() + ".2").replaceAll("<max>", String.valueOf(max2)).replaceAll("<players>", String.valueOf(players2)).replaceAll("<map>", map2).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + gameData.getState().toLowerCase())).replaceAll("<color>", gameData.getColor());
        String l3 = signsInjection.getSigns().get("texts.lines." + state2.toLowerCase() + ".3").replaceAll("<max>", String.valueOf(max2)).replaceAll("<players>", String.valueOf(players2)).replaceAll("<map>", map2).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + gameData.getState().toLowerCase())).replaceAll("<color>", gameData.getColor());
        String l4 = signsInjection.getSigns().get("texts.lines." + state2.toLowerCase() + ".4").replaceAll("<max>", String.valueOf(max2)).replaceAll("<players>", String.valueOf(players2)).replaceAll("<map>", map2).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + gameData.getState().toLowerCase())).replaceAll("<color>", gameData.getColor());
        gs.setLines(l1, l2, l3, l4);
    }
    
    public void update(String type, String map, String state, int players, int max){
        if (plugin.getCm().isSignsRotation()){
            if (isStarted(state)){
                if (showing.contains(map)){
                    GameSign gs = getMapSign(map);
                    if (gs != null){
                        gs.setData(null);
                        gs.setOccupied(false);
                        lines(gs);
                        showing.remove(map);
                        waiting.remove(map);
                    }
                }
                fillEmpty(type);
                return;
            }
        }
        if (showing.contains(map)){
            GameSign gs = getMapSign(map);
            if (gs == null){
                waiting.remove(map);
                showing.remove(map);
                update(type, map, state, players, max);
                return;
            }
            removeWaiting(map, map, state, players, max, gs);
        } else {
            if (added(type, map, map, state, players, max)) return;
        }
        fillEmpty(type);
    }
    
    private void fillEmpty(String type){
        if (waiting.size() > 0){
            String sv = waiting.get(0);
            GameSign gs = getEmptySign(type);
            if (gs != null){
                GameData gameData = UltraSkyWarsApi.getGameData().get(sv);
                if (gameData == null){
                    return;
                }
                String newState = gameData.getState();
                int newPlayers = gameData.getPlayers();
                int newMax = gameData.getMax();
                String newName = gameData.getMap();
                gs.setData(gameData);
                gs.setState(newState);
                gs.setOccupied(true);
                String l1 = signsInjection.getSigns().get("texts.lines." + newState.toLowerCase() + ".1").replaceAll("<max>", String.valueOf(newMax)).replaceAll("<players>", String.valueOf(newPlayers)).replaceAll("<map>", newName).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + newState.toLowerCase())).replaceAll("<color>", gameData.getColor());
                String l2 = signsInjection.getSigns().get("texts.lines." + newState.toLowerCase() + ".2").replaceAll("<max>", String.valueOf(newMax)).replaceAll("<players>", String.valueOf(newPlayers)).replaceAll("<map>", newName).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + newState.toLowerCase())).replaceAll("<color>", gameData.getColor());
                String l3 = signsInjection.getSigns().get("texts.lines." + newState.toLowerCase() + ".3").replaceAll("<max>", String.valueOf(newMax)).replaceAll("<players>", String.valueOf(newPlayers)).replaceAll("<map>", newName).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + newState.toLowerCase())).replaceAll("<color>", gameData.getColor());
                String l4 = signsInjection.getSigns().get("texts.lines." + newState.toLowerCase() + ".4").replaceAll("<max>", String.valueOf(newMax)).replaceAll("<players>", String.valueOf(newPlayers)).replaceAll("<map>", newName).replaceAll("<state>", signsInjection.getSigns().get("texts.states." + newState.toLowerCase())).replaceAll("<color>", gameData.getColor());
                gs.setLines(l1, l2, l3, l4);
                waiting.remove(sv);
                showing.add(sv);
            }
        }
    }
    
    private boolean isStarted(String state){
        return state.equalsIgnoreCase("EMPTY") || state.equalsIgnoreCase("PREGAME") || state.equalsIgnoreCase("GAME") || state.equalsIgnoreCase("FINISH") || state.equalsIgnoreCase("RESTARTING");
    }
    
    public GameSign getGameSignByLoc(Location loc){
        if (signsLoc.containsKey(loc)){
            return signs.get(signsLoc.get(loc));
        }
        return null;
    }
    
    private GameSign getEmptySign(String type){
        for ( GameSign g : signs.values() ){
            if (!g.isOccupied() && g.getType().equalsIgnoreCase(type)){
                return g;
            }
        }
        return null;
    }
    
    private GameSign getServerSign(String server){
        for ( GameSign g : signs.values() ){
            if (g.isOccupied() && g.getData().getServer().equals(server)){
                return g;
            }
        }
        return null;
    }
    
    private GameSign getMapSign(String map){
        for ( GameSign g : signs.values() ){
            if (g.isOccupied() && g.getData().getMap().equals(map)){
                return g;
            }
        }
        return null;
    }
    
    public HashMap<Integer, GameSign> getSigns(){
        return signs;
    }
}