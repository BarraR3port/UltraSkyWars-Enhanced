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


import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.HealthType;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.ProjectileType;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Tagged;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class SpecialListener implements Listener {
    
    @EventHandler
    public void onHealth(EntityRegainHealthEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game == null){
                return;
            }
            if (plugin.getTgm().hasTag(p)){
                Tagged tag = plugin.getTgm().getTagged(p);
                tag.removeDamage(e.getAmount());
            }
            if (game.getHealthType().equals(HealthType.UHC)){
                if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING) || e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onHit(ProjectileHitEvent e){
        if (e.getEntity().getShooter() instanceof Player){
            Projectile proj = e.getEntity();
            Player p = (Player) proj.getShooter();
            if (proj.hasMetadata("TYPE")){
                String type = proj.getMetadata("TYPE").get(0).asString();
                ProjectileType pt = ProjectileType.valueOf(type);
                if (proj instanceof Arrow){
                    if (pt.isDestructor()){
                        for ( Block b : getAroundBlock(proj.getLocation()) ){
                            b.setType(Material.AIR);
                        }
                    }
                    if (pt.isExplosive()){
                        World w = proj.getWorld();
                        w.createExplosion(proj.getLocation(), 3, true);
                    }
                    if (pt.isTeleporter()){
                        p.teleport(proj.getLocation().add(0, 1, 0));
                    }
                }
                proj.removeMetadata("TYPE", UltraSkyWars.get());
            }
        }
    }
    
    public List<Block> getAroundBlock(Location loc){
        List<Block> blocks = new ArrayList<>();
        Location l = loc.clone();
        if (!l.getBlock().getType().equals(Material.AIR)){
            blocks.add(l.getBlock());
        }
        if (!l.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(0, 1, 0).getBlock());
        }
        if (!l.clone().add(1, 0, 0).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(1, 0, 0).getBlock());
        }
        if (!l.clone().add(0, 0, 1).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(0, 0, 1).getBlock());
        }
        if (!l.clone().add(-1, 0, 0).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(-1, 0, 0).getBlock());
        }
        if (!l.clone().add(0, 0, -1).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(0, 0, -1).getBlock());
        }
        if (!l.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)){
            blocks.add(l.clone().add(0, -1, 0).getBlock());
        }
        return blocks;
    }
    
}