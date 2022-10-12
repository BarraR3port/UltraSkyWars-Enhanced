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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.taunts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class TauntType {
    
    private final String damage;
    private final Collection<String> messages;
    
    public TauntType(String damage, Collection<String> messages){
        this.damage = damage;
        this.messages = messages;
    }
    
    public String getDamage(){
        return damage;
    }
    
    public Collection<String> getMessages(){
        return messages;
    }
    
    public String getRandomMessage(){
        return new ArrayList<>(messages).get(ThreadLocalRandom.current().nextInt(0, messages.size()));
    }
    
}