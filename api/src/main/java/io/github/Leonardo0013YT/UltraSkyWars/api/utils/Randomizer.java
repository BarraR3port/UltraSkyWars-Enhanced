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

package io.github.Leonardo0013YT.UltraSkyWars.api.utils;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    
    private final Integer[][] randomizers = new Integer[30][25];
    private final Integer[][] selectors = new Integer[30][6];
    
    public Randomizer(){
        for ( int i = 0; i < randomizers.length; i++ ){
            HashSet<Integer> selected = new HashSet<>();
            while (selected.size() < randomizers[i].length) {
                selected.add(ThreadLocalRandom.current().nextInt(0, 27));
            }
            HashSet<Integer> unique = new HashSet<>();
            while (unique.size() < 6) {
                unique.add(ThreadLocalRandom.current().nextInt(0, 27));
            }
            selectors[i] = selected.toArray(new Integer[0]);
            randomizers[i] = selected.toArray(new Integer[0]);
        }
    }
    
    public Integer[] getSelectors(){
        return selectors[ThreadLocalRandom.current().nextInt(0, 30)];
    }
    
    public Integer[] getRandomizer(){
        return randomizers[ThreadLocalRandom.current().nextInt(0, 30)];
    }
    
}