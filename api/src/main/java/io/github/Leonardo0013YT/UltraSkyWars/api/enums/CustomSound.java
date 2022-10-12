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

package io.github.Leonardo0013YT.UltraSkyWars.api.enums;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum CustomSound {
    
    PERK_NO_COINS(null, 0.0f, 0.0f),
    PERK_LOCKED(null, 0.0f, 0.0f),
    PERK_MAXED(null, 0.0f, 0.0f),
    PERK_BUY(null, 0.0f, 0.0f),
    PERK_OFF(null, 0.0f, 0.0f),
    PERK_ON(null, 0.0f, 0.0f),
    TNT_LAUNCH_EXPLODE(null, 0.0f, 0.0f),
    SOUP_EAT(null, 0.0f, 0.0f),
    PRESTIGE_NO_HAS(null, 0.0f, 0.0f),
    PRESTIGE_SELECT(null, 0.0f, 0.0f),
    PRESTIGE_ALREADY_SELECTED(null, 0.0f, 0.0f),
    SOUL_ANIMATION_3D_1(null, 0.0f, 0.0f),
    SOUL_ANIMATION_3D_2(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_1(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_2(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_3(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_4(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_5(null, 0.0f, 0.0f),
    SOUL_ANIMATION_BLAZE_6(null, 0.0f, 0.0f),
    COLLECT_HEAD(null, 0.0f, 0.0f),
    KILLEFFECTS_HEAD(null, 0.0f, 0.0f),
    WINEFFECTS_VULCANWOOL(null, 0.0f, 0.0f),
    WINEFFECTS_VULCANFIRE(null, 0.0f, 0.0f),
    WINEFFECTS_NOTES(null, 0.0f, 0.0f),
    WINEFFECTS_CHICKEN(null, 0.0f, 0.0f),
    NOBUY(null, 0.0f, 0.0f),
    SOULWELL(null, 0.0f, 0.0f),
    STARTING(null, 0.0f, 0.0f),
    PREGAME(null, 0.0f, 0.0f),
    CANCELSTART(null, 0.0f, 0.0f),
    UPGRADE(null, 0.0f, 0.0f),
    DEGRADE(null, 0.0f, 0.0f),
    NOSOULS(null, 0.0f, 0.0f),
    CUBELETS_ANI1(null, 0.0f, 0.0f),
    CUBELETS_ANI3(null, 0.0f, 0.0f),
    CUBELETS_REWARD(null, 0.0f, 0.0f),
    REWARDS_COMMON(null, 0.0f, 0.0f),
    REWARDS_UNCOMMON(null, 0.0f, 0.0f),
    REWARDS_RARE(null, 0.0f, 0.0f),
    REWARDS_EPIC(null, 0.0f, 0.0f),
    REWARDS_LEGENDARY(null, 0.0f, 0.0f),
    KILLEFFECTS_TNT(null, 0.0f, 0.0f),
    KILLEFFECTS_SQUID(null, 0.0f, 0.0f),
    EVENTS_BORDER(null, 0.0f, 0.0f),
    EVENTS_DRAGON(null, 0.0f, 0.0f),
    EVENTS_REFILL(null, 0.0f, 0.0f),
    EVENTS_NONE(null, 0.0f, 0.0f),
    EVENTS_TNT(null, 0.0f, 0.0f),
    EVENTS_WITHER(null, 0.0f, 0.0f),
    EVENTS_ZOMBIE(null, 0.0f, 0.0f),
    QUIT_PLAYER(null, 0.0f, 0.0f),
    JOIN_PLAYER(null, 0.0f, 0.0f);
    
    private Sound sound;
    private float volume, pitch;
    
    CustomSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public Sound getSound(){
        return sound;
    }
    
    public void setSound(Sound sound){
        this.sound = sound;
    }
    
    public float getVolume(){
        return volume;
    }
    
    public void setVolume(float volume){
        this.volume = volume;
    }
    
    public float getPitch(){
        return pitch;
    }
    
    public void setPitch(float pitch){
        this.pitch = pitch;
    }
    
    public void reproduce(Player p){
        p.playSound(p.getLocation(), sound, volume, pitch);
    }
}