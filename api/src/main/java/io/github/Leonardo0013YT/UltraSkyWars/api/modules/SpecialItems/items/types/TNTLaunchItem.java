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
public class TNTLaunchItem extends SpecialItem {
    
    private final double multiply;
    private final int noFall;
    
    public TNTLaunchItem(InjectionSpecialItems isi){
        this.multiply = isi.getSpecial_items().getInt("items.TNTLaunch.multiply");
        this.noFall = isi.getSpecial_items().getInt("items.TNTLaunch.noFall");
    }
    
}