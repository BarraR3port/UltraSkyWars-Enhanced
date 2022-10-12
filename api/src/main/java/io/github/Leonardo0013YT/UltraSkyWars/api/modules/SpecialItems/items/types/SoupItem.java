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
public class SoupItem extends SpecialItem {
    
    private final String maxHealth;
    private final double health;
    
    public SoupItem(InjectionSpecialItems isi){
        this.maxHealth = isi.getSpecial_items().get("items.soup.maxHealth");
        this.health = isi.getSpecial_items().getDouble("items.soup.hearts");
    }
    
}