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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.soulwell.soulwell;

public class SoulWellPath {
    
    private final int[] path;
    
    public SoulWellPath(int[] path){
        this.path = path;
    }
    
    public int getStart(){
        return path[0];
    }
    
    public int getSecond(){
        return path[1];
    }
    
    public int getThree(){
        return path[2];
    }
    
    public int getFour(){
        return path[3];
    }
    
    public int getFive(){
        return path[4];
    }
    
}