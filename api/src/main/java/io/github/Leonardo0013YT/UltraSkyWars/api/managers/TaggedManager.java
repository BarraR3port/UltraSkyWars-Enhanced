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

import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Tagged;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TaggedManager {
    
    private final HashMap<Player, Tagged> tagged = new HashMap<>();
    
    public void clear(){
        tagged.clear();
    }
    
    public void setTag(Player damager, Player damaged, double damage, Game game){
        if (!tagged.containsKey(damaged)){
            tagged.put(damaged, new Tagged(damaged, game));
        }
        Tagged tag = tagged.get(damaged);
        tag.addPlayerDamage(damager, damage);
    }
    
    public void executeRewards(Player death, double maxHealth){
        if (tagged.containsKey(death)){
            Tagged tag = tagged.get(death);
            tag.executeRewards(maxHealth);
            tagged.remove(death);
        }
    }
    
    public void removeTag(Player p){
        tagged.remove(p);
    }
    
    public Tagged getTagged(Player p){
        return tagged.get(p);
    }
    
    public boolean hasTag(Player p){
        return tagged.containsKey(p);
    }
    
}