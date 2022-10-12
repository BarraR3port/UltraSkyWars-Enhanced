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

package io.github.Leonardo0013YT.UltraSkyWars.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWeather(WeatherChangeEvent e){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getGm().getWorlds().containsKey(e.getWorld().getName())){
            e.setCancelled(e.toWeatherState());
        }
    }
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (plugin.getCm().isNaturalSpawnEggs() && (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG) || e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) || e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DISPENSE_EGG))){
            return;
        }
        if (e.getEntity().getType().equals(EntityType.PLAYER) || e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM) || e.getEntityType().equals(EntityType.WITHER) || e.getEntityType().equals(EntityType.ENDER_DRAGON)){
            return;
        }
        if (plugin.getGm().getWorlds().containsKey(e.getLocation().getWorld().getName())){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onTarget(EntityTargetEvent e){
        if (e.getEntity() instanceof Wither){
            if (!(e.getTarget() instanceof Player)){
                e.setCancelled(true);
                e.setTarget(null);
            }
        }
    }
    
}