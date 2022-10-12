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

package io.github.Leonardo0013YT.UltraSkyWars.api.objects;

import lombok.Getter;

@Getter
public class Multiplier {
    
    private final String type;
    private final String name;
    private final int id;
    private final long remaining;
    private final double amount;
    
    public Multiplier(int id, String type, String name, long remaining, double amount){
        this.id = id;
        this.type = type;
        this.name = name;
        this.remaining = remaining;
        this.amount = amount;
    }
    
}