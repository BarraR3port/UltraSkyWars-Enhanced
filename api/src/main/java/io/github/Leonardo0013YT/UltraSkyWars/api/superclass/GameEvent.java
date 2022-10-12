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

package io.github.Leonardo0013YT.UltraSkyWars.api.superclass;

public abstract class GameEvent {
    
    public String type, name, sound, title, subtitle;
    public int reset, time;
    
    public abstract void start(Game game);
    
    public abstract void stop(Game game);
    
    public abstract void reset();
    
    public abstract GameEvent clone();
    
    public void update(){
        time--;
    }
    
    public int getTime(){
        return time;
    }
    
    public void setTime(int time){
        this.time = time;
        this.reset = time;
    }
    
    public int getReset(){
        return reset;
    }
    
    public String getName(){
        return name;
    }
    
    public String getSound(){
        return sound;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getSubTitle(){
        return subtitle;
    }
    
    public String getType(){
        return type;
    }
    
}