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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.cubelets.animation2;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.cubelets.cubelets.Cubelets;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.InstantFirework;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AnimationHead {
    
    private final UltraSkyWarsApi plugin;
    private final Cubelets box;
    private final Location loc;
    private Location armorStandLocation;
    private int noteValue;
    private float notePitch;
    private int time = 0;
    private BukkitTask music, animation;
    private Location c1, c2, c3, c4;
    
    public AnimationHead(UltraSkyWarsApi plugin, Cubelets box, Location loc){
        this.plugin = plugin;
        this.box = box;
        this.loc = loc;
    }
    
    public void execute(){
        final Location l = loc.clone().add(0.5, 0, 0.5);
        final ArmorStand head = l.getWorld().spawn(l, ArmorStand.class);
        armorStandLocation = head.getLocation();
        head.setVisible(false);
        head.setHelmet(plugin.getCm().getHead());
        head.setSmall(true);
        head.setGravity(false);
        head.setArms(false);
        head.setBasePlate(false);
        this.c1 = this.loc.clone().add(0.05D, 0.875D - 0.325D, 0.05D);
        this.c2 = this.loc.clone().add(0.95D, 0.875D - 0.325D, 0.05D);
        this.c3 = this.loc.clone().add(0.95D, 0.875D - 0.325D, 0.95D);
        this.c4 = this.loc.clone().add(0.05D, 0.875D - 0.325D, 0.95D);
        animation = new BukkitRunnable() {
            @Override
            public void run(){
                if (time <= 50){
                    armorStandLocation.add(0.0D, 0.02D, 0.0D);
                }
                head.teleport(armorStandLocation);
                head.setHeadPose(head.getHeadPose().add(0.0D, 0.159D, 0.0D));
                time++;
                if (time == 98){
                    FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Utils.getRandomColor()).build();
                    new InstantFirework(effect, l);
                } else if (time == 100){
                    head.remove();
                    music.cancel();
                    box.reward();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
        music = new BukkitRunnable() {
            @Override
            public void run(){
                sound();
            }
        }.runTaskTimer(plugin, 0, 4L);
    }
    
    public void sound(){
        ++this.noteValue;
        if (this.noteValue >= 1 && this.noteValue <= 3){
            loc.getWorld().playSound(this.loc, XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 0.05F);
        } else {
            loc.getWorld().playSound(this.loc, XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 0.05F + this.notePitch);
            this.notePitch = (float) ((double) this.notePitch + 0.044D);
        }
    }
    
}