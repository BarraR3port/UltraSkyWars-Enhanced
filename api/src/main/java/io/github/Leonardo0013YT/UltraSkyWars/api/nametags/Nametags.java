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

package io.github.Leonardo0013YT.UltraSkyWars.api.nametags;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.nms.NametagVersion;
import io.github.Leonardo0013YT.UltraSkyWars.api.nms.NMS;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class Nametags {
    
    static Constructor<?> PacketPlayOutScoreboardTeam, ChatComponentText;
    
    static{
        try {
            PacketPlayOutScoreboardTeam = NMS.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
            ChatComponentText = NMS.getNMSClass("ChatComponentText").getConstructor(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    Object packet;
    
    public Nametags(String teamName, String displayName, String prefix){
        try {
            NametagVersion version = NMS.getNametagVersion();
            packet = PacketPlayOutScoreboardTeam.newInstance();
            Object displayObject = ChatComponentText.newInstance(displayName);
            Object prefixObject = ChatComponentText.newInstance(prefix);
            this.setField(version.getH(), 0);
            this.setField(version.getB(), XMaterial.isNewVersion() ? displayObject : displayName);
            this.setField(version.getA(), teamName);
            this.setField(version.getC(), XMaterial.isNewVersion() ? prefixObject : prefix);
            this.setField(version.getE(), "always");
            this.setField(version.getI(), 1);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteTeam(String teamName){
        for ( Player pl : Bukkit.getOnlinePlayers() ){
            remove(teamName, pl);
        }
    }
    
    public void deleteTeam(Player p, String teamName){
        remove(teamName, p);
    }
    
    private void remove(String teamName, Player p){
        try {
            Object packet = PacketPlayOutScoreboardTeam.newInstance();
            NametagVersion version = NMS.getNametagVersion();
            Field f = packet.getClass().getDeclaredField(version.getA());
            f.setAccessible(true);
            f.set(packet, teamName);
            f.setAccessible(false);
            Field f2 = packet.getClass().getDeclaredField(version.getH());
            f2.setAccessible(true);
            f2.set(packet, 1);
            f2.setAccessible(false);
            UltraSkyWarsApi.get().getVc().getNMS().sendPacket(p, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void addPlayer(Player pl){
        try {
            this.add(pl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendToPlayer(Player pl){
        UltraSkyWarsApi.get().getVc().getNMS().sendPacket(pl, packet);
    }
    
    public void setField(String field, Object value){
        try {
            Field f = this.packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(this.packet, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void add(Player pl) throws NoSuchFieldException, IllegalAccessException{
        NametagVersion version = NMS.getNametagVersion();
        Field f = this.packet.getClass().getDeclaredField(version.getG());
        f.setAccessible(true);
        ((Collection<String>) f.get(this.packet)).add(pl.getName());
    }
    
    
}