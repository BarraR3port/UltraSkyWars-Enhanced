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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.types;

import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.SpecialItems.items.SpecialItem;
import lombok.Getter;

@Getter
public class CompassItem extends SpecialItem {
    
    private final int time;
    private final double range;
    private final String noTarget, targeted, countdown;
    
    public CompassItem(InjectionSpecialItems isi){
        this.range = isi.getSpecial_items().getDouble("items.compass.range");
        this.time = isi.getSpecial_items().getInt("items.compass.time");
        this.noTarget = isi.getSpecial_items().get("items.compass.noTarget");
        this.targeted = isi.getSpecial_items().get("items.compass.targeted");
        this.countdown = isi.getSpecial_items().get("items.compass.countdown");
    }
    
}