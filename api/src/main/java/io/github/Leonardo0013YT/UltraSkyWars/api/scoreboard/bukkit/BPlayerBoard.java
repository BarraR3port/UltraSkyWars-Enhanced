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

package io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.bukkit;

import io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.Netherboard;
import io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.api.PlayerBoard;
import io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.bukkit.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BPlayerBoard implements PlayerBoard<String, Integer, String> {
    
    private final Player player;
    private Scoreboard scoreboard;
    
    private String name;
    
    private Objective objective;
    private Objective buffer;
    
    private ConcurrentHashMap<Integer, String> lines = new ConcurrentHashMap<>();
    
    private boolean deleted = false;
    
    public BPlayerBoard(Player player, String name){
        this(player, null, name);
    }
    
    public BPlayerBoard(Player player, Scoreboard scoreboard, String name){
        this.player = player;
        this.scoreboard = scoreboard;
        
        if (this.scoreboard == null){
            Scoreboard sb = player.getScoreboard();
            
            if (sb == null || sb == Bukkit.getScoreboardManager().getMainScoreboard())
                sb = Bukkit.getScoreboardManager().getNewScoreboard();
            
            this.scoreboard = sb;
        }
        
        this.name = name;
        
        String subName = player.getName().length() <= 14
                ? player.getName()
                : player.getName().substring(0, 14);
        
        this.objective = this.scoreboard.getObjective("sb" + subName);
        this.buffer = this.scoreboard.getObjective("bf" + subName);
        
        if (this.objective == null)
            this.objective = this.scoreboard.registerNewObjective("sb" + subName, "dummy");
        if (this.buffer == null)
            this.buffer = this.scoreboard.registerNewObjective("bf" + subName, "dummy");
        
        this.objective.setDisplayName(name);
        sendObjective(this.objective, ObjectiveMode.CREATE);
        sendObjectiveDisplay(this.objective);
        
        this.buffer.setDisplayName(name);
        sendObjective(this.buffer, ObjectiveMode.CREATE);
        
        this.player.setScoreboard(this.scoreboard);
    }
    
    @Override
    public String get(Integer score){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        
        return this.lines.get(score);
    }
    
    @Override
    public void set(String show, Integer score){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        
        String line = getNoDup(score) + show;
        String name = (line.length() > 34) ? line.substring(0, 34) : line;
        String oldName = this.lines.get(score);
        
        if (name.equals(oldName))
            return;
        
        this.lines.entrySet().removeIf(entry -> entry.getValue().equals(name));
        
        if (oldName != null){
            if (NMS.getVersion().getMajor().equals("1.7")){
                sendScore(this.objective, oldName, score, true);
                sendScore(this.objective, name, score, false);
            } else {
                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);
                swapBuffers();
                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);
            }
        } else {
            sendScore(this.objective, name, score, false);
            sendScore(this.buffer, name, score, false);
        }
        this.lines.put(score, name);
    }
    
    @Override
    public void setAll(String... lines){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        
        for ( int i = 0; i < lines.length; i++ ){
            String line = lines[i];
            set(line, lines.length - i);
        }
        
        Set<Integer> scores = this.lines.keySet();
        for ( int score : scores ){
            if (score <= 0 || score > lines.length){
                remove(score);
            }
        }
    }
    
    @Override
    public void clear(){
        this.lines.keySet().forEach(this::remove);
        this.lines.clear();
    }
    
    private void swapBuffers(){
        sendObjectiveDisplay(this.buffer);
        Objective temp = this.buffer;
        this.buffer = this.objective;
        this.objective = temp;
    }
    
    private void sendObjective(Objective obj, ObjectiveMode mode){
        try {
            Object objHandle = NMS.getHandle(obj);
            Object packetObj = NMS.PACKET_OBJ.newInstance(objHandle, mode.ordinal());
            NMS.sendPacket(packetObj, player);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException ignored) {
        }
    }
    
    private void sendObjectiveDisplay(Objective obj){
        try {
            Object objHandle = NMS.getHandle(obj);
            Object packet = NMS.PACKET_DISPLAY.newInstance(1, objHandle);
            NMS.sendPacket(packet, player);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException ignored) {
            
        }
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void sendScore(Objective obj, String name, int score, boolean remove){
        try {
            Object sbHandle = NMS.getHandle(scoreboard);
            Object objHandle = NMS.getHandle(obj);
            
            Object sbScore = NMS.SB_SCORE.newInstance(sbHandle, objHandle, name);
            
            NMS.SB_SCORE_SET.invoke(sbScore, score);
            Map scores = (Map) NMS.PLAYER_SCORES.get(sbHandle);
            
            if (remove){
                if (scores.containsKey(name))
                    ((Map) scores.get(name)).remove(objHandle);
            } else {
                if (!scores.containsKey(name))
                    scores.put(name, new HashMap());
                ((Map) scores.get(name)).put(objHandle, sbScore);
            }
            
            switch(NMS.getVersion().getMajor()){
                case "1.7":{
                    Object packet = NMS.PACKET_SCORE.newInstance(sbScore, remove ? 1 : 0);
                    NMS.sendPacket(packet, player);
                    break;
                }
                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12":{
                    Object packet;
                    if (remove){
                        packet = NMS.PACKET_SCORE_REMOVE.newInstance(name, objHandle);
                    } else {
                        packet = NMS.PACKET_SCORE.newInstance(sbScore);
                    }
                    NMS.sendPacket(packet, player);
                    break;
                }
                default:{
                    Object packet = NMS.PACKET_SCORE.newInstance(remove ? NMS.ENUM_SCORE_ACTION_REMOVE : NMS.ENUM_SCORE_ACTION_CHANGE, obj.getName(), name, score);
                    NMS.sendPacket(packet, player);
                    break;
                }
            }
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException ignored) {
        }
    }
    
    @Override
    public void remove(Integer score){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        String name = this.lines.get(score);
        if (name == null)
            return;
        this.scoreboard.resetScores(name);
        this.lines.remove(score);
    }
    
    @Override
    public void delete(){
        if (this.deleted)
            return;
        Netherboard.instance().removeBoard(player);
        sendObjective(this.objective, ObjectiveMode.REMOVE);
        sendObjective(this.buffer, ObjectiveMode.REMOVE);
        this.objective.unregister();
        this.objective = null;
        this.buffer.unregister();
        this.buffer = null;
        this.lines = null;
        this.deleted = true;
    }
    
    @Override
    public Map<Integer, String> getLines(){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        
        return new HashMap<>(lines);
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setName(String name){
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");
        
        this.name = name;
        this.objective.setDisplayName(name);
        this.buffer.setDisplayName(name);
        sendObjective(this.objective, ObjectiveMode.UPDATE);
        sendObjective(this.buffer, ObjectiveMode.UPDATE);
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public Scoreboard getScoreboard(){
        return scoreboard;
    }
    
    public String getNoDup(int line){
        if (line == 0){
            return "§a";
        }
        if (line == 1){
            return "§d";
        }
        if (line == 2){
            return "§b";
        }
        if (line == 3){
            return "§c";
        }
        if (line == 4){
            return "§0";
        }
        if (line == 5){
            return "§1";
        }
        if (line == 6){
            return "§2";
        }
        if (line == 7){
            return "§3";
        }
        if (line == 8){
            return "§4";
        }
        if (line == 9){
            return "§5";
        }
        if (line == 10){
            return "§6";
        }
        if (line == 11){
            return "§7";
        }
        if (line == 12){
            return "§8";
        }
        if (line == 13){
            return "§9";
        }
        if (line == 14){
            return "§e";
        }
        return "§f";
    }
    
    private enum ObjectiveMode {CREATE, REMOVE, UPDATE}
    
}
