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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.perks;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.perks.InjectionPerks;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Perk;

public class PotionPerk extends Perk {
    
    private final int duration, amplifier;
    
    public PotionPerk(InjectionPerks plugin, String path, int duration, int amplifier){
        super(plugin, path);
        this.duration = plugin.getPerks().getIntOrDefault(path + ".duration", duration) * 20;
        this.amplifier = plugin.getPerks().getIntOrDefault(path + ".amplifier", amplifier);
    }
    
    public int getDuration(){
        return duration;
    }
    
    public int getAmplifier(){
        return amplifier;
    }
}