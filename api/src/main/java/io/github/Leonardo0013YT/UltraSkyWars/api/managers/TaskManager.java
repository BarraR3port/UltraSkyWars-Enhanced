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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Balloon;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {
    
    private final UltraSkyWarsApi plugin;
    private int amount = 0;
    
    public TaskManager(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        loadTasks();
    }
    
    public void loadTasks(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (amount >= 5){
                Utils.updateSB();
                amount = 0;
            }
            amount++;
            plugin.getCos().getAnimatedBalloons().values().stream().filter(Balloon::isAnimated).filter(Balloon::needUpdate).forEach(Balloon::update);
        }, 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDb().loadTopKills();
            plugin.getDb().loadTopWins();
            plugin.getDb().loadTopDeaths();
            plugin.getDb().loadTopCoins();
            plugin.getDb().loadTopElo();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getGm().updateFinish();
                plugin.getTop().createTops();
            });
        }), 6000L, 6000L);
        new BukkitRunnable() {
            @Override
            public void run(){
                plugin.getTop().createTops();
            }
        }.runTaskLater(plugin, 20 * 10);
    }
    
    
}