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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.lprotection.InjectionLProtection;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionLProtection injection;
    private String lobbyWorld;
    private boolean noBreakArmorStand, noInteractArmorStand, noBreakFarm, igniteProtect, hideJoinMessage, hideQuitMessage, opBypass, noBreak, noPlace, noDamage, noHunger, noWeather, noExplosion, noDayCycle, noMobSpawn, noDrop, joinTeleport, voidTeleport;
    private List<String> interactBlocked;
    
    public ConfigManager(UltraSkyWarsApi plugin, InjectionLProtection injection){
        this.plugin = plugin;
        this.injection = injection;
        reload();
    }
    
    public void reload(){
        lobbyWorld = injection.getLobbyOptions().get("options.lobbyWorld");
        noBreakArmorStand = injection.getLobbyOptions().getBooleanOrDefault("options.noBreakArmorStand", true);
        noInteractArmorStand = injection.getLobbyOptions().getBooleanOrDefault("options.noInteractArmorStand", true);
        noBreakFarm = injection.getLobbyOptions().getBooleanOrDefault("options.noBreakFarm", true);
        igniteProtect = injection.getLobbyOptions().getBooleanOrDefault("options.igniteProtect", true);
        hideJoinMessage = injection.getLobbyOptions().getBooleanOrDefault("options.hideJoinMessage", true);
        hideQuitMessage = injection.getLobbyOptions().getBooleanOrDefault("options.hideQuitMessage", true);
        joinTeleport = injection.getLobbyOptions().getBoolean("options.joinTeleport");
        opBypass = injection.getLobbyOptions().getBoolean("options.opBypass");
        voidTeleport = injection.getLobbyOptions().getBoolean("options.voidTeleport");
        noBreak = injection.getLobbyOptions().getBoolean("options.noBreak");
        noPlace = injection.getLobbyOptions().getBoolean("options.noPlace");
        noDamage = injection.getLobbyOptions().getBoolean("options.noDamage");
        noHunger = injection.getLobbyOptions().getBoolean("options.noHunger");
        noWeather = injection.getLobbyOptions().getBoolean("options.noWeather");
        noExplosion = injection.getLobbyOptions().getBoolean("options.noExplosion");
        noDayCycle = injection.getLobbyOptions().getBoolean("options.noDayCycle");
        noMobSpawn = injection.getLobbyOptions().getBoolean("options.noMobSpawn");
        noDrop = injection.getLobbyOptions().getBoolean("options.noDrop");
        interactBlocked = injection.getLobbyOptions().getListOrDefault("options.interactBlocked", new ArrayList<>());
    }
    
    public boolean isNoBreakArmorStand(){
        return noBreakArmorStand;
    }
    
    public boolean isNoInteractArmorStand(){
        return noInteractArmorStand;
    }
    
    public boolean isNoBreakFarm(){
        return noBreakFarm;
    }
    
    public boolean isIgniteProtect(){
        return igniteProtect;
    }
    
    public boolean isHideJoinMessage(){
        return hideJoinMessage;
    }
    
    public boolean isHideQuitMessage(){
        return hideQuitMessage;
    }
    
    public String getLobbyWorld(){
        return lobbyWorld;
    }
    
    public List<String> getInteractBlocked(){
        return interactBlocked;
    }
    
    public boolean isOpBypass(){
        return opBypass;
    }
    
    public boolean isJoinTeleport(){
        return joinTeleport;
    }
    
    public boolean isVoidTeleport(){
        return voidTeleport;
    }
    
    public boolean isNoDrop(){
        return noDrop;
    }
    
    public boolean isNoBreak(){
        return noBreak;
    }
    
    public boolean isNoPlace(){
        return noPlace;
    }
    
    public boolean isNoDamage(){
        return noDamage;
    }
    
    public boolean isNoHunger(){
        return noHunger;
    }
    
    public boolean isNoWeather(){
        return noWeather;
    }
    
    public boolean isNoExplosion(){
        return noExplosion;
    }
    
    public boolean isNoDayCycle(){
        return noDayCycle;
    }
    
    public boolean isNoMobSpawn(){
        return noMobSpawn;
    }
}