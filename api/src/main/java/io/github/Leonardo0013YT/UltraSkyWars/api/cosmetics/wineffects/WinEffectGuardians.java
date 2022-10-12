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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.wineffects;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class WinEffectGuardians implements WinEffect, Cloneable {
    
    private final ArrayList<ArmorStand> armors = new ArrayList<>();
    private final ArrayList<Guardian> guardians = new ArrayList<>();
    private BukkitTask task;
    
    @Override
    public void start(Player p, Game game){
        World world = game.getSpectator().getWorld();
        Location l1 = getCicle(p.getLocation(), 0, 2);
        Location l2 = getCicle(p.getLocation(), 10, 2);
        Location l3 = getCicle(p.getLocation(), 15, 2);
        ArmorStand a1 = apply(l1.getWorld().spawn(l1, ArmorStand.class));
        ArmorStand a2 = apply(l2.getWorld().spawn(l2, ArmorStand.class));
        ArmorStand a3 = apply(l3.getWorld().spawn(l3, ArmorStand.class));
        Guardian g1 = apply(l1.getWorld().spawn(l1, Guardian.class));
        Guardian g2 = apply(l1.getWorld().spawn(l2, Guardian.class));
        Guardian g3 = apply(l1.getWorld().spawn(l3, Guardian.class));
        a1.setPassenger(g1);
        a2.setPassenger(g2);
        a3.setPassenger(g3);
        g1.setTarget(p);
        g2.setTarget(p);
        g3.setTarget(p);
        armors.add(a1);
        armors.add(a2);
        armors.add(a3);
        guardians.add(g1);
        guardians.add(g2);
        guardians.add(g3);
        task = new BukkitRunnable() {
            final double add = Math.PI / 36;
            double lastStart = 0;
            double angle = 0;
            
            @Override
            public void run(){
                if (!p.getWorld().getName().equals(world.getName())){
                    cancel();
                    return;
                }
                if (!p.isOnline()){
                    cancel();
                    return;
                }
                lastStart = angle - (add * 2);
                for ( ArmorStand as : armors ){
                    angle += add;
                    Location now = getCicle(p.getLocation(), angle, 2);
                    as.teleport(now);
                }
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 0, 2);
    }
    
    @Override
    public void stop(){
        for ( Guardian s : guardians ){
            if (s == null || s.isDead()) continue;
            s.remove();
        }
        for ( ArmorStand s : armors ){
            if (s == null || s.isDead()) continue;
            s.remove();
        }
        guardians.clear();
        armors.clear();
        if (task != null){
            task.cancel();
        }
    }
    
    @Override
    public WinEffect clone(){
        return new WinEffectGuardians();
    }
    
    public Guardian apply(Guardian g){
        g.setNoDamageTicks(Integer.MAX_VALUE);
        return g;
    }
    
    public ArmorStand apply(ArmorStand a){
        a.setGravity(false);
        a.setSmall(true);
        a.setVisible(false);
        return a;
    }
    
    public Location getCicle(Location loc, double angle, double radius){
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);
        return loc.clone().add(x, 0, z);
    }
    
}