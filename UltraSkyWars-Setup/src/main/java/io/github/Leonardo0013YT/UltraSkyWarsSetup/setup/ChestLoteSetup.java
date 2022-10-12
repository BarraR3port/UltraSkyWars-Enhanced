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

package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import java.util.ArrayList;

public class ChestLoteSetup {
    
    private int chance;
    private boolean center, refill;
    private ArrayList<String> modes;
    
    public ChestLoteSetup(int chance, boolean center, boolean refill, ArrayList<String> modes){
        this.chance = chance;
        this.center = center;
        this.refill = refill;
        this.modes = modes;
    }
    
    public boolean isRefill(){
        return refill;
    }
    
    public void setRefill(boolean refill){
        this.refill = refill;
    }
    
    public ArrayList<String> getModes(){
        return modes;
    }
    
    public void setModes(ArrayList<String> modes){
        this.modes = modes;
    }
    
    public int getChance(){
        return chance;
    }
    
    public void setChance(int chance){
        this.chance = chance;
    }
    
    public boolean isCenter(){
        return center;
    }
    
    public void setCenter(boolean center){
        this.center = center;
    }
}