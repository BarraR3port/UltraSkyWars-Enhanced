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

package io.github.Leonardo0013YT.UltraSkyWars.controllers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.IVersionController;
import io.github.Leonardo0013YT.UltraSkyWars.api.nametags.Nametags;
import io.github.Leonardo0013YT.UltraSkyWars.api.nms.NMS;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_10_R1.v1_10_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_11_R1.v1_11_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_12_R1.v1_12_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_13_R2.v1_13_R2;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_14_R1.v1_14_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_15_R1.v1_15_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_16_R3.v1_16_R3;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_17_R1.v1_17_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_18_R2.v1_18_R2;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_19_R1.v1_19_R1;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_8_R3.v1_8_R3;
import io.github.Leonardo0013YT.UltraSkyWars.support.version.v1_9_R2.v1_9_R2;
import org.bukkit.Bukkit;

public class VersionController implements IVersionController {
    
    private final UltraSkyWarsApi plugin;
    private String version;
    private NMS nms;
    private boolean is1_13to17 = false;
    private boolean is1_9to17 = false;
    private boolean is1_12 = false;
    
    public VersionController(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        setupVersion();
        
    }
    
    private void setupVersion(){
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            switch(version){
                case "v1_8_R3":
                    nms = new v1_8_R3();
                    break;
                case "v1_9_R1":
                    plugin.sendLogMessage("§cYou have an outdated version §e1.9§c, please use version §a1.9.4§c.");
                    disable();
                    break;
                case "v1_9_R2":
                    nms = new v1_9_R2();
                    is1_9to17 = true;
                    break;
                case "v1_10_R1":
                    nms = new v1_10_R1();
                    is1_9to17 = true;
                    break;
                case "v1_11_R1":
                    nms = new v1_11_R1();
                    is1_9to17 = true;
                    break;
                case "v1_12_R1":
                    nms = new v1_12_R1();
                    is1_9to17 = true;
                    is1_12 = true;
                    break;
                case "v1_13_R1":
                    plugin.sendLogMessage("§cYou have an outdated version §e1.13.1§c, please use version §a1.13.2§c.");
                    disable();
                    break;
                case "v1_13_R2":
                    nms = new v1_13_R2();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_14_R1":
                    nms = new v1_14_R1();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_15_R1":
                    nms = new v1_15_R1();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_16_R1":
                case "v1_16_R2":
                    plugin.sendLogMessage("§cYou have an outdated version §e1.16 - 1.16.4§c, please use version §a1.16.5§c.");
                    disable();
                    break;
                case "v1_16_R3":
                    nms = new v1_16_R3();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_17_R1":
                    nms = new v1_17_R1();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_18_R1":
                    nms = new v1_18_R2();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                case "v1_19_R1":
                    nms = new v1_19_R1();
                    is1_9to17 = true;
                    is1_13to17 = true;
                    break;
                default:
                    plugin.sendLogMessage("§cYou have an outdated version §e1.8§c, please use version §a1.8.8§c.");
                    disable();
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
    
    public void disable(){
        Bukkit.getScheduler().cancelTasks(plugin);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
    
    public NMS getNMS(){
        return nms;
    }
    
    public Nametags getNameTag(String name, String display, String prefix){
        return new Nametags(name, display, prefix);
    }
    
    public String getVersion(){
        return version;
    }
    
    public boolean is1_12(){
        return is1_12;
    }
    
    public boolean is1_9to17(){
        return is1_9to17;
    }
    
    public boolean is1_13to17(){
        return is1_13to17;
    }
}