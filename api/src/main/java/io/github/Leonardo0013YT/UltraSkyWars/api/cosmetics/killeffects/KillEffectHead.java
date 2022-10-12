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
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XSound;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KillEffectHead implements KillEffect, Cloneable {
    
    private static boolean loaded = false;
    private static XSound punchSound;
    private BukkitTask task;
    private int pased = 0;
    
    @Override
    public void loadCustoms(UltraSkyWarsApi plugin, String path){
        if (!loaded){
            punchSound = XSound.matchXSound(plugin.getKilleffect().getOrDefault(path + ".punchSound", XSound.ENTITY_FIREWORK_ROCKET_BLAST.parseSound().name())).orElse(XSound.ENTITY_FIREWORK_ROCKET_BLAST);
            loaded = true;
        }
    }
    
    @Override
    public void start(Player p, Player death, Location loc){
        if (death == null || !death.isOnline()){
            return;
        }
        ItemStack head = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, "§e", "§e", death.getName());
        ArmorStand armor = loc.getWorld().spawn(loc, ArmorStand.class);
        armor.setVisible(false);
        armor.setCustomName(death.getName());
        armor.setCustomNameVisible(true);
        armor.setHelmet(head);
        armor.setNoDamageTicks(999999999);
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        armor.setMetadata("KILLEFFECT", new FixedMetadataValue(plugin, "KILLEFFECT"));
        task = new BukkitRunnable() {
            @Override
            public void run(){
                pased++;
                if (pased >= 20){
                    armor.getWorld().playEffect(armor.getLocation(), Effect.STEP_SOUND, Material.COAL_BLOCK);
                    p.playSound(p.getLocation(), punchSound.parseSound(), 1.0f, 1.0f);
                    armor.remove();
                    cancel();
                    return;
                }
                Location loc = armor.getLocation().clone().add(0, 0.3 * pased, 0);
                armor.teleport(loc);
                if (plugin.getVc().is1_12() || plugin.getVc().is1_13to17()){
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "SMOKE_NORMAL", 1, 64);
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "DRIP_LAVA", 1, 64);
                } else {
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "SMOKE", 1, 64);
                    plugin.getVc().getNMS().broadcastParticle(loc, 0, 0, 0, 0, "LAVADRIP", 1, 64);
                }
                CustomSound.KILLEFFECTS_HEAD.reproduce(p);
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
        return new KillEffectHead();
    }
    
}