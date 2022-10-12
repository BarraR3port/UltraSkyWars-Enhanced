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

package io.github.Leonardo0013YT.UltraSkyWars.addons.nametags;

import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.NametagAddon;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.TablistFormatManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TabAPIAddon implements NametagAddon {
    private final HashMap<UUID, String> prefix = new HashMap<>();
    private final HashMap<UUID, String> suffix = new HashMap<>();
    private final TabAPI tab = TabAPI.getInstance();
    private final TablistFormatManager manager = tab.getTablistFormatManager();
    
    @Override
    public void addPlayerNameTag(Player p){
        TabPlayer tabPlayer = tab.getPlayer(p.getUniqueId());
        tab.getTablistFormatManager().getCustomPrefix(tabPlayer);
        prefix.put(p.getUniqueId(), manager.getCustomPrefix(tabPlayer));
        suffix.put(p.getUniqueId(), manager.getCustomSuffix(tabPlayer));
    }
    
    @Override
    public void removePlayerNameTag(Player p){
        prefix.remove(p.getUniqueId());
        suffix.remove(p.getUniqueId());
    }
    
    @Override
    public void resetPlayerNameTag(Player p){
        TabPlayer tabPlayer = tab.getPlayer(p.getUniqueId());
        manager.setPrefix(tabPlayer, prefix.get(p.getUniqueId()));
        manager.setSuffix(tabPlayer, suffix.get(p.getUniqueId()));
        removePlayerNameTag(p);
    }
    
    @Override
    public String getPrefix(Player p){
        TabPlayer tabPlayer = tab.getPlayer(p.getUniqueId());
        String prefix = manager.getCustomPrefix(tabPlayer);
        return prefix == null ? "" : prefix;
    }
    
    @Override
    public String getSuffix(Player p){
        TabPlayer tabPlayer = tab.getPlayer(p.getUniqueId());
        String suffix = manager.getCustomSuffix(tabPlayer);
        return suffix == null ? "" : suffix;
    }
    
}