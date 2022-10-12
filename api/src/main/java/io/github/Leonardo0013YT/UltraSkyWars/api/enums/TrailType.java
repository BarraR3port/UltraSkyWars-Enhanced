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

package io.github.Leonardo0013YT.UltraSkyWars.api.enums;

import lombok.Getter;

@Getter
public enum TrailType {
    
    NORMAL(0, 0, 1),
    SPIRAL(1.5, 20, 1),
    PYRAMID(1.5, 3, 3),
    HELIX(1.5, 15, 3);
    
    private final double radius;
    private final int count;
    private final int speed;
    
    TrailType(double radius, int speed, int count){
        this.radius = radius;
        this.speed = speed;
        this.count = count;
    }
    
}