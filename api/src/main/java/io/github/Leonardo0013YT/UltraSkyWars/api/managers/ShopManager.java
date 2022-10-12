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
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Glass;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.KillSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.Parting;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.killeffects.UltraKillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.kits.KitLevel;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.taunts.Taunt;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.trails.Trail;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.windances.UltraWinDance;
import io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.wineffects.UltraWinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.entity.Player;

public class ShopManager {
    
    public void buy(Player p, Purchasable purchasable, String name){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        if (!purchasable.isBuy()){
            p.sendMessage(plugin.getLang().get(p, "messages.noBuy"));
            CustomSound.NOBUY.reproduce(p);
            return;
        }
        if (plugin.getAdm().getCoins(p) < purchasable.getPrice()){
            p.sendMessage(plugin.getLang().get(p, "messages.noCoins"));
            CustomSound.NOBUY.reproduce(p);
            return;
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        plugin.getAdm().removeCoins(p, purchasable.getPrice());
        if (purchasable instanceof KitLevel){
            KitLevel k = (KitLevel) purchasable;
            if (plugin.getCm().isKitLevelsOrder() && !isLastLevel(sw, k)){
                p.sendMessage(plugin.getLang().get(p, "messages.buyLastLevel"));
                return;
            }
            sw.addKitLevel(k.getKit().getId(), k.getLevel());
        } else if (purchasable instanceof Glass){
            Glass k = (Glass) purchasable;
            sw.addGlasses(k.getId());
        } else if (purchasable instanceof Balloon){
            Balloon k = (Balloon) purchasable;
            sw.addBalloons(k.getId());
        } else if (purchasable instanceof UltraKillEffect){
            UltraKillEffect k = (UltraKillEffect) purchasable;
            sw.addKillEffects(k.getId());
        } else if (purchasable instanceof KillSound){
            KillSound k = (KillSound) purchasable;
            sw.addKillSounds(k.getId());
        } else if (purchasable instanceof Parting){
            Parting k = (Parting) purchasable;
            sw.addPartings(k.getId());
        } else if (purchasable instanceof Taunt){
            Taunt k = (Taunt) purchasable;
            sw.addTaunts(k.getId());
        } else if (purchasable instanceof Trail){
            Trail k = (Trail) purchasable;
            sw.addTrails(k.getId());
        } else if (purchasable instanceof UltraWinDance){
            UltraWinDance k = (UltraWinDance) purchasable;
            sw.addWinDances(k.getId());
        } else if (purchasable instanceof UltraWinEffect){
            UltraWinEffect k = (UltraWinEffect) purchasable;
            sw.addWinEffects(k.getId());
        }
        Utils.updateSB(p);
        p.sendMessage(plugin.getLang().get(p, "messages.bought").replaceAll("<name>", name));
    }
    
    public boolean isLastLevel(SWPlayer sw, KitLevel kl){
        if (kl.getLevel() == 1){
            return true;
        }
        return sw.hasKitLevel(kl.getKit().getId(), kl.getLevel() - 1);
    }
    
}