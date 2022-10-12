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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.mobfriends.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.UUID;

public class TargetListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    
    public TargetListener(UltraSkyWarsApi plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSplash(PotionSplashEvent e){
        if (e.getEntity().getShooter() instanceof Witch){
            Witch w = (Witch) e.getEntity().getShooter();
            if (w.hasMetadata("OWNER") && w.hasMetadata("TEAM")){
                Player p = Bukkit.getPlayer(UUID.fromString(w.getMetadata("OWNER").get(0).asString()));
                if (p == null) return;
                Game g = plugin.getGm().getGameByPlayer(p);
                if (g == null) return;
                Team t = g.getTeams().get(w.getMetadata("TEAM").get(0).asInt());
                if (t == null) return;
                for ( LivingEntity le : e.getAffectedEntities() ){
                    if (le instanceof Player){
                        Player on = (Player) le;
                        Team to = g.getTeamPlayer(on);
                        if (to != null && to.getId() == t.getId()){
                            e.getAffectedEntities().remove(le);
                        }
                    }
                }
            }
        }
    }
    
}