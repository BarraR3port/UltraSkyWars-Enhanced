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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.killeffects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.KillEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

public class KillEffectTNT implements KillEffect, Cloneable {
    
    private static boolean loaded = false;
    private static int fuseTicks;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            fuseTicks = plugin.getKilleffect().getIntOrDefault(path + ".fuseTicks", 4);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Player death, Location loc){
        if (p == null || !p.isOnline()){
            return;
        }
        if (death == null || !death.isOnline()){
            return;
        }
        TNTPrimed primed = loc.getWorld().spawn(loc, TNTPrimed.class);
        primed.setFuseTicks(fuseTicks);
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        new BukkitRunnable() {
            @Override
            public void run(){
                plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "EXPLOSION_LARGE", 1, 64);
                CustomSound.KILLEFFECTS_TNT.reproduce(p);
                primed.remove();
            }
        }.runTaskLater(plugin, fuseTicks);
    }
    
    @Override
    public void stop(){
    }
    
    @Override
    public KillEffect clone(){
        return new KillEffectSquid();
    }
    
}
