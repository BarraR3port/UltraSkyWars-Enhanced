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

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

public class PreviewSession {
    
    private final Location lastLocation;
    private final ItemStack[] inv;
    private final ItemStack[] armor;
    private final Collection<PotionEffect> effects;
    private final boolean allowFly;
    private final boolean flying;
    private final float walkSpeed;
    private final float flySpeed;
    private final int food;
    private final double maxHealth;
    private final double health;
    private final Collection<Integer> entityId = new ArrayList<>();
    private final GameMode mode;
    private final Player p;
    private int id;
    
    public PreviewSession(int id, Player p){
        this.id = id;
        this.p = p;
        this.inv = p.getInventory().getContents();
        this.armor = p.getInventory().getArmorContents();
        this.effects = p.getActivePotionEffects();
        this.allowFly = p.getAllowFlight();
        this.flying = p.isFlying();
        this.walkSpeed = p.getWalkSpeed();
        this.flySpeed = p.getFlySpeed();
        this.food = p.getFoodLevel();
        this.health = p.getHealth();
        this.maxHealth = p.getMaxHealth();
        this.mode = p.getGameMode();
        this.lastLocation = p.getLocation();
    }
    
    public void reset(){
        p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setContents(inv);
        p.getInventory().setArmorContents(armor);
        effects.forEach(e -> p.addPotionEffect(e));
        p.setAllowFlight(allowFly);
        p.setFlying(flying);
        p.setWalkSpeed(walkSpeed);
        p.setFlySpeed(flySpeed);
        p.setFoodLevel(food);
        p.setMaxHealth(maxHealth);
        p.setHealth(health);
        p.setGameMode(mode);
        p.teleport(lastLocation);
        p.updateInventory();
    }
    
    public Collection<Integer> getEntityId(){
        return entityId;
    }
    
    public void addEntityId(int id){
        entityId.add(id);
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
}