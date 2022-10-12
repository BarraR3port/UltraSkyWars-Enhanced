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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell.upgrades.SoulWellAngelOfDeath;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class GameListener implements Listener {
    
    private final UltraSkyWarsApi plugin;
    private final InjectionSoulWell is;
    
    public GameListener(UltraSkyWarsApi plugin, InjectionSoulWell is){
        this.plugin = plugin;
        this.is = is;
    }
    
    @EventHandler
    public void onGameKill(USWGameKillEvent e){
        Player k = e.getPlayer();
        Player d = e.getDeath();
        Game game = e.getGame();
        SWPlayer sw = plugin.getDb().getSWPlayer(k);
        SoulWellAngelOfDeath sa = is.getSwm().getAngelByLevel(sw.getSoulWellHead());
        int random = ThreadLocalRandom.current().nextInt(0, 101);
        String rarity = is.getSwm().getRandomRarity();
        if (sa.getProbability() >= random){
            if (!sw.hasHead(rarity, d.getName())){
                game.sendGameSound(CustomSound.COLLECT_HEAD);
                game.sendGameMessage(is.getSoulwell().get("collected").replaceAll("<player>", k.getName()).replaceAll("<death>", d.getName()).replaceAll("<rarity>", is.getSoulwell().get("raritys." + rarity)));
                sw.addHead(rarity, d.getName(), System.currentTimeMillis());
            }
        }
    }
    
}