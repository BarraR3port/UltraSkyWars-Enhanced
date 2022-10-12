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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.cubelets;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.InjectionCubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Reward;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.InstantFirework;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Cubelets {
    
    private final UltraSkyWarsApi plugin;
    private final Location hologram;
    private final InjectionCubelets injectionCubelets;
    private final Location loc;
    private boolean inUse;
    private Player now;
    
    public Cubelets(UltraSkyWarsApi plugin, InjectionCubelets injectionCubelets, Location loc){
        this.plugin = plugin;
        this.injectionCubelets = injectionCubelets;
        this.loc = loc;
        this.hologram = loc.clone().add(0.5, 0, 0.5);
        this.inUse = false;
        this.now = null;
    }
    
    public void reload(){
        plugin.getAdm().createHologram(hologram, plugin.getLang().getList("holograms.cubelets"));
    }
    
    public void execute(){
        if (plugin.getAdm().hasHologram(hologram)){
            plugin.getAdm().deleteHologram(hologram);
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(now);
        if (sw.getCubeAnimation() == injectionCubelets.getCubelets().getInt("animations.fireworks.id")){
            final Location l = loc.clone().add(0.5, plugin.getCm().getUpYAni1(), 0.5);
            new BukkitRunnable() {
                @Override
                public void run(){
                    if (!now.isOnline()){
                        cancel();
                        return;
                    }
                    if (l.getY() <= loc.getY()){
                        reward();
                        cancel();
                        return;
                    }
                    FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Utils.getRandomColor()).build();
                    new InstantFirework(effect, l);
                    l.setY(l.getY() - 0.25);
                }
            }.runTaskTimer(plugin, 0, plugin.getCm().getTicksAni1());
        }
        if (sw.getCubeAnimation() == injectionCubelets.getCubelets().getInt("animations.head.id")){
            final Location l = loc.clone().add(0.5, 0, 0.5);
            final ArmorStand head = l.getWorld().spawn(l, ArmorStand.class);
            head.setVisible(false);
            head.setHelmet(plugin.getCm().getHead());
            head.setSmall(true);
            head.setGravity(false);
            head.setArms(false);
            head.setBasePlate(false);
            injectionCubelets.getEntities().add(head);
            new BukkitRunnable() {
                @Override
                public void run(){
                    if (!now.isOnline()){
                        cancel();
                        return;
                    }
                    if (loc.getY() + 0.8 <= l.getY()){
                        injectionCubelets.getEntities().remove(head);
                        head.remove();
                        reward();
                        cancel();
                        return;
                    }
                    Location nowl = l.add(0, 0.02, 0);
                    plugin.getVc().getNMS().broadcastParticle(nowl.clone().add(0, 0.6, 0), 0F, 0F, 0F, 0, "CRIT", 1, 5);
                    nowl.setYaw(nowl.getYaw() + 7.5f);
                    head.teleport(nowl);
                    CustomSound.CUBELETS_ANI1.reproduce(now);
                }
            }.runTaskTimer(plugin, 0, 1);
        }
        if (sw.getCubeAnimation() == injectionCubelets.getCubelets().getInt("animations.flames.id")){
            final Location l = loc.clone().add(0.5, 0, 0.5);
            new BukkitRunnable() {
                final int tick = 0;
                int ticks = 0;
                double x = 1;
                double y = 1;
                double z = 1;
                
                @Override
                public void run(){
                    if (!now.isOnline()){
                        cancel();
                        return;
                    }
                    if (ticks > plugin.getCm().getExecutesAni3()){
                        l.getWorld().createExplosion(l.clone().add(0, 1, 0), 1, false);
                        new BukkitRunnable() {
                            @Override
                            public void run(){
                                reward();
                            }
                        }.runTaskLater(plugin, 5);
                        cancel();
                        return;
                    }
                    x -= 0.02;
                    y += 0.02;
                    z -= 0.02;
                    for ( int i = tick; i < 360; ++i ){
                        if (i % (2 + 1) == 0){
                            double var8 = Math.sin(Math.toRadians(i)) * x;
                            double var10 = Math.cos(Math.toRadians(i)) * x;
                            double var12 = z / 10.0D;
                            for ( double var14 = 0.0D; var14 < var12; var14 += 0.1D ){
                                Vector var16 = new Vector(var8, var14 + y, var10);
                                plugin.getVc().getNMS().broadcastParticle(l.clone().add(var16.getX(), var16.getY() - 0.5, var16.getZ()), 0F, 0F, 0F, 0, "FLAME", 1, 5);
                            }
                        }
                    }
                    CustomSound.CUBELETS_ANI3.reproduce(now);
                    ticks++;
                }
            }.runTaskTimer(plugin, 0, plugin.getCm().getTicksAni3());
        }
    }
    
    public void reward(){
        Reward swr = plugin.getLvl().getRandomReward();
        if (plugin.getAdm().hasHologramPlugin()){
            if (plugin.getAdm().hasHologram(hologram)){
                plugin.getAdm().deleteHologram(hologram);
            }
            List<String> list = new ArrayList<>();
            for ( String l : plugin.getLang().getList("holograms.rewards") ){
                list.add(l.replaceAll("<name>", swr.getName()).replaceAll("<rarity>", plugin.getLang().get(null, "soulwell.rarity." + swr.getRarity().name().toLowerCase())));
            }
            ItemStack icon = swr.getIcon().clone();
            icon.setAmount(1);
            plugin.getAdm().createHologram(hologram, list, icon);
        }
        if (now != null && now.isOnline()){
            swr.execute(now);
            CustomSound.CUBELETS_REWARD.reproduce(now);
            now.sendMessage(plugin.getLang().get(now, "cubeletReward").replaceAll("<reward>", swr.getName()).replaceAll("<rarity>", plugin.getLang().get(null, "soulwell.rarity." + swr.getRarity().name().toLowerCase())));
        }
        new BukkitRunnable() {
            @Override
            public void run(){
                setInUse(false);
                setNow(null);
            }
        }.runTaskLater(plugin, 20 * 2);
    }
    
    public Location getLoc(){
        return loc;
    }
    
    public boolean isInUse(){
        return inUse;
    }
    
    public void setInUse(boolean inUse){
        this.inUse = inUse;
        if (plugin.getAdm().hasHologramPlugin()){
            if (plugin.getAdm().hasHologram(hologram)){
                plugin.getAdm().deleteHologram(hologram);
            }
            plugin.getAdm().createHologram(hologram, plugin.getLang().getList("holograms.cubelets"));
        }
    }
    
    public Player getNow(){
        return now;
    }
    
    public void setNow(Player now){
        this.now = now;
    }
    
}