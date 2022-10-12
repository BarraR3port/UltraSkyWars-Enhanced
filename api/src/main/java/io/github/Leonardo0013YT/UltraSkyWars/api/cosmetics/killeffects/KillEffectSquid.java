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
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KillEffectSquid implements KillEffect, Cloneable {
    
    private static int lavaAmount, explosionAmount;
    private static boolean loaded = false;
    private BukkitTask task;
    private int pased = 0;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            lavaAmount = plugin.getKilleffect().getIntOrDefault(path + ".lavaAmount", 1);
            explosionAmount = plugin.getKilleffect().getIntOrDefault(path + ".explosionAmount", 1);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Player death, Location loc){
        Squid squid = loc.getWorld().spawn(loc, Squid.class);
        squid.setNoDamageTicks(Integer.MAX_VALUE);
        squid.setMetadata("KILLEFFECT", new FixedMetadataValue(UltraSkyWarsApi.get(), "KILLEFFECT"));
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        task = new BukkitRunnable() {
            @Override
            public void run(){
                if (death == null || !death.isOnline()){
                    squid.remove();
                    cancel();
                    return;
                }
                pased++;
                if (pased >= 20){
                    squid.remove();
                    cancel();
                    return;
                }
                Location loc = squid.getLocation().clone().add(0, 0.3 * pased, 0);
                squid.teleport(loc);
                if (plugin.getVc().is1_12() || plugin.getVc().is1_13to17()){
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "EXPLOSION_NORMAL", explosionAmount, 64);
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "DRIP_LAVA", lavaAmount, 64);
                } else {
                    loc.getWorld().playEffect(loc, Effect.EXPLOSION, explosionAmount);
                    loc.getWorld().playEffect(loc, Effect.LAVADRIP, lavaAmount);
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "EXPLOSION", explosionAmount, 64);
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "LAVADRIP", lavaAmount, 64);
                    
                }
                CustomSound.KILLEFFECTS_SQUID.reproduce(p);
            }
        }.runTaskTimer(plugin, 0, 2);
    }
    
    @Override
    public void stop(){
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public KillEffect clone(){
        return new KillEffectSquid();
    }
    
}