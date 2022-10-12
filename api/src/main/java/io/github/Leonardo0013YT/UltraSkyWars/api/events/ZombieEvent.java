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

package io.github.Leonardo0013YT.UltraSkyWars.api.events;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;

public class ZombieEvent extends GameEvent {
    
    private BukkitTask task;
    private Collection<Zombie> zombies;
    
    public ZombieEvent(UltraSkyWarsApi plugin, int time){
        this.time = time;
        this.reset = time;
        this.zombies = new ArrayList<>();
        this.task = null;
        this.type = "final";
        this.name = "zombie";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }
    
    public ZombieEvent(ZombieEvent e){
        this.time = e.getReset();
        this.reset = e.getReset();
        this.zombies = new ArrayList<>();
        this.task = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }
    
    @Override
    public void start(Game game){
        this.task = new BukkitRunnable() {
            @Override
            public void run(){
                if (zombies.size() >= 30){
                    return;
                }
                for ( Player on : game.getPlayers() ){
                    Zombie z = on.getWorld().spawn(on.getLocation(), Zombie.class);
                    z.setMetadata("GAMEEVENT", new FixedMetadataValue(UltraSkyWarsApi.get(), game.getId()));
                    zombies.add(z);
                }
            }
        }.runTaskTimer(UltraSkyWarsApi.get(), 30, 30);
        World w = game.getLobby().getWorld();
        w.setTime(18000);
        for ( Player on : game.getCached() ){
            w.strikeLightningEffect(on.getLocation());
            on.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
            CustomSound.EVENTS_ZOMBIE.reproduce(on);
        }
    }
    
    @Override
    public void stop(Game game){
        if (task != null){
            task.cancel();
        }
        if (!zombies.isEmpty()){
            zombies.forEach(Entity::remove);
        }
    }
    
    @Override
    public void reset(){
        this.time = this.reset;
        this.zombies = new ArrayList<>();
        this.task = null;
        this.type = "final";
        this.name = "zombie";
    }
    
    @Override
    public ZombieEvent clone(){
        return new ZombieEvent(this);
    }
    
    public Collection<Zombie> getZombies(){
        return zombies;
    }
}