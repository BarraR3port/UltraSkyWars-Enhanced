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

package io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NMS {
    
    public static final Field PLAYER_SCORES;
    public static final Constructor<?> PACKET_SCORE_REMOVE;
    public static final Constructor<?> PACKET_SCORE;
    public static final Object ENUM_SCORE_ACTION_CHANGE;
    public static final Object ENUM_SCORE_ACTION_REMOVE;
    public static final Constructor<?> SB_SCORE;
    public static final Method SB_SCORE_SET;
    public static final Constructor<?> PACKET_OBJ;
    public static final Constructor<?> PACKET_DISPLAY;
    public static final Field PLAYER_CONNECTION;
    public static final Method SEND_PACKET;
    private static final String packageName;
    private static final Version version;
    private static final Map<Class<?>, Method> handles = new HashMap<>();
    
    static{
        String name = Bukkit.getServer().getClass().getPackage().getName();
        
        String ver = name.substring(name.lastIndexOf('.') + 1);
        version = new Version(ver);
        
        packageName = "net.minecraft.server." + ver;
        
        Field playerScores = null;
        
        Constructor<?> packetScoreRemove = null;
        Constructor<?> packetScore = null;
        
        Object enumScoreActionChange = null;
        Object enumScoreActionRemove = null;
        
        Constructor<?> sbScore = null;
        Method sbScoreSet = null;
        
        Constructor<?> packetObj = null;
        Constructor<?> packetDisplay = null;
        
        Field playerConnection = null;
        Method sendPacket = null;
        
        try {
            Class<?> packetScoreClass = getClass("PacketPlayOutScoreboardScore");
            Class<?> packetDisplayClass = getClass("PacketPlayOutScoreboardDisplayObjective");
            Class<?> packetObjClass = getClass("PacketPlayOutScoreboardObjective");
            
            Class<?> scoreClass = getClass("ScoreboardScore");
            Class<?> scoreActionClass = getClass("ScoreboardServer$Action");
            
            Class<?> sbClass = getClass("Scoreboard");
            Class<?> objClass = getClass("ScoreboardObjective");
            
            Class<?> playerClass = getClass("EntityPlayer");
            Class<?> playerConnectionClass = getClass("PlayerConnection");
            Class<?> packetClass = getClass("Packet");
            
            playerScores = sbClass.getDeclaredField("playerScores");
            playerScores.setAccessible(true);
            
            sbScore = scoreClass.getConstructor(sbClass, objClass, String.class);
            sbScoreSet = scoreClass.getMethod("setScore", int.class);
            
            packetObj = packetObjClass.getConstructor(objClass, int.class);
            
            switch(version.getMajor()){
                case "1.7":
                    packetScore = packetScoreClass.getConstructor(scoreClass, int.class);
                    break;
                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12":
                    packetScore = packetScoreClass.getConstructor(scoreClass);
                    packetScoreRemove = packetScoreClass.getConstructor(String.class, objClass);
                    break;
                default:
                    packetScore = packetScoreClass.getConstructor(scoreActionClass,
                            String.class, String.class, int.class);
                    
                    enumScoreActionChange = scoreActionClass.getEnumConstants()[0];
                    enumScoreActionRemove = scoreActionClass.getEnumConstants()[1];
                    break;
            }
            
            packetDisplay = packetDisplayClass.getConstructor(int.class, objClass);
            
            playerConnection = playerClass.getField("playerConnection");
            sendPacket = playerConnectionClass.getMethod("sendPacket", packetClass);
        } catch (Exception ignored) {
        }
        
        PLAYER_SCORES = playerScores;
        
        PACKET_SCORE_REMOVE = packetScoreRemove;
        PACKET_SCORE = packetScore;
        
        ENUM_SCORE_ACTION_CHANGE = enumScoreActionChange;
        ENUM_SCORE_ACTION_REMOVE = enumScoreActionRemove;
        
        SB_SCORE = sbScore;
        SB_SCORE_SET = sbScoreSet;
        
        PACKET_OBJ = packetObj;
        PACKET_DISPLAY = packetDisplay;
        
        PLAYER_CONNECTION = playerConnection;
        SEND_PACKET = sendPacket;
    }
    
    public static Version getVersion(){
        return version;
    }
    
    public static Class<?> getClass(String name){
        try {
            return Class.forName(packageName + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    public static Object getHandle(Object obj) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException{
        
        Class<?> clazz = obj.getClass();
        
        if (!handles.containsKey(clazz)){
            Method method = clazz.getDeclaredMethod("getHandle");
            
            if (!method.isAccessible())
                method.setAccessible(true);
            
            handles.put(clazz, method);
        }
        
        return handles.get(clazz).invoke(obj);
    }
    
    public static void sendPacket(Object packet, Player... players){
        for ( Player p : players ){
            try {
                Object playerConnection = PLAYER_CONNECTION.get(getHandle(p));
                SEND_PACKET.invoke(playerConnection, packet);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            }
        }
    }
    
    public static class Version {
        
        private final String name;
        
        private final String major;
        private final String minor;
        
        Version(String name){
            this.name = name;
            
            String[] splitted = name.split("_");
            
            this.major = splitted[0].substring(1) + "." + splitted[1];
            this.minor = splitted[2].substring(1);
        }
        
        public String getName(){
            return name;
        }
        
        public String getMajor(){
            return major;
        }
        
        public String getMinor(){
            return minor;
        }
        
    }
    
}
