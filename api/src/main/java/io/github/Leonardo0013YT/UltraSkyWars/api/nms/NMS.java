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

package io.github.Leonardo0013YT.UltraSkyWars.api.nms;

import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.nms.NametagVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;

public abstract class NMS {
    
    protected final static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    
    protected static NametagVersion nametagVersion;
    protected static Class<?> packet;
    protected Object enumTimes, enumTitle, enumSubtitle;
    protected Constructor<?> packetPlayOutTitle, packetPlayOutTimes, packetPlayOutChat;
    protected Method a, position, sendTitle, getHandle, getHandleEntity;
    protected DamageCauses causes;
    protected boolean isNewAction;
    
    
    public static NametagVersion getNametagVersion(){
        return NametagVersion.valueOf(version);
    }
    
    public static Class<?> getNMSClass(String name){
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }
    
    public static Class<?> getOBClass(String name){
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }
    
    public abstract Vehicle spawnHorse(Location loc, Player p);
    
    public abstract Collection<Integer> spawn(Player p, Location loc, ItemStack head);
    
    public abstract void destroy(Player p, Collection<Integer> id);
    
    public abstract void followPlayer(Player player, LivingEntity entity, double d);
    
    public abstract void displayParticle(Player p, Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount);
    
    public abstract void broadcastParticle(Location location, float offsetX, float offsetY, float offsetZ, int speed, String enumParticle, int amount, double range);
    
    public abstract boolean isParticle(String particle);
    
    public abstract void freezeMob(LivingEntity mob);
    
    public abstract void sendPacket(Player player, Object object);
    
    public abstract void setCollidesWithEntities(Player p, boolean bol);
    
    public abstract DamageCauses getCauses();
    
    
    public abstract void sendActionBar(String msg, Player... players);
    
    public abstract void sendActionBar(String msg, Collection<Player> players);
    
    public abstract void moveDragon(Entity ent, double x, double y, double z, float yaw, float pitch);
    
    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players);
    
    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players);
    
}