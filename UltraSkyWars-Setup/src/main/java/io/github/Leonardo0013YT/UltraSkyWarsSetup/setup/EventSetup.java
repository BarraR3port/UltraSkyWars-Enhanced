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

public class EventSetup {
    
    private final String type;
    private int seconds;
    
    public EventSetup(String type){
        this.type = type;
        this.seconds = 60;
    }
    
    public String getType(){
        return type;
    }
    
    public int getSeconds(){
        return seconds;
    }
    
    public void setSeconds(int seconds){
        this.seconds = seconds;
    }
    
    public void addSeconds(int seconds){
        this.seconds += seconds;
    }
    
    public void removeSeconds(int seconds){
        this.seconds -= seconds;
    }
    
}