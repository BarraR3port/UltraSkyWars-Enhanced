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

import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class SoulWellRow {
    
    private final List<SoulWellPath> paths = new ArrayList<>();
    private final int[] glass;
    private final int[] fenix;
    private final List<Integer> result;
    private final List<Vector> armors;
    
    public SoulWellRow(int rows){
        if (rows == 1){
            paths.add(new SoulWellPath(new int[]{4, 13, 22, 31, 40}));
            glass = new int[]{21, 23};
            fenix = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 16, 15, 14, 13, 12, 11, 10, 9, 18, 19, 20, 21, 23, 24, 25, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 36, 37, 38, 39, 40, 41, 42, 43, 44};
            result = Collections.singletonList(22);
            armors = Collections.singletonList(new Vector(0, 0.3, 0));
        } else if (rows == 2){
            paths.add(new SoulWellPath(new int[]{3, 12, 21, 30, 39}));
            paths.add(new SoulWellPath(new int[]{5, 14, 23, 32, 41}));
            glass = new int[]{20, 22, 24};
            fenix = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 16, 15, 14, 13, 12, 11, 10, 9, 18, 19, 20, 22, 24, 25, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 36, 37, 38, 39, 40, 41, 42, 43, 44};
            result = Arrays.asList(21, 23);
            armors = Arrays.asList(new Vector(0, 0.3, -0.7), new Vector(0, 0.3, 0.7));
        } else if (rows == 3){
            paths.add(new SoulWellPath(new int[]{2, 11, 20, 29, 38}));
            paths.add(new SoulWellPath(new int[]{4, 13, 22, 31, 40}));
            paths.add(new SoulWellPath(new int[]{6, 15, 24, 33, 42}));
            glass = new int[]{19, 21, 23, 25};
            fenix = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 16, 15, 14, 13, 12, 11, 10, 9, 18, 19, 21, 23, 25, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 36, 37, 38, 39, 40, 41, 42, 43, 44};
            result = Arrays.asList(20, 22, 24);
            armors = Arrays.asList(new Vector(0, 0.3, -1.4), new Vector(0, 0.3, 0), new Vector(0, 0.3, 1.4));
        } else if (rows == 4){
            paths.add(new SoulWellPath(new int[]{1, 10, 19, 28, 37}));
            paths.add(new SoulWellPath(new int[]{3, 12, 21, 30, 39}));
            paths.add(new SoulWellPath(new int[]{5, 14, 23, 32, 41}));
            paths.add(new SoulWellPath(new int[]{7, 16, 25, 34, 43}));
            glass = new int[]{18, 20, 22, 24, 26};
            fenix = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 16, 15, 14, 13, 12, 11, 10, 9, 18, 20, 22, 24, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 36, 37, 38, 39, 40, 41, 42, 43, 44};
            result = Arrays.asList(19, 21, 23, 25);
            armors = Arrays.asList(new Vector(0, 0.3, -2.1), new Vector(0, 0.3, -0.7), new Vector(0, 0.3, 0.7), new Vector(0, 0.3, 2.1));
        } else {
            paths.add(new SoulWellPath(new int[]{0, 9, 18, 27, 36}));
            paths.add(new SoulWellPath(new int[]{2, 11, 20, 29, 38}));
            paths.add(new SoulWellPath(new int[]{4, 13, 22, 31, 40}));
            paths.add(new SoulWellPath(new int[]{6, 15, 24, 33, 42}));
            paths.add(new SoulWellPath(new int[]{8, 17, 26, 35, 44}));
            glass = new int[]{17, 19, 21, 23, 25, 27};
            fenix = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 16, 15, 14, 13, 12, 11, 10, 9, 19, 21, 23, 25, 35, 34, 33, 32, 31, 30, 29, 28, 27, 36, 37, 38, 39, 40, 41, 42, 43, 44};
            result = Arrays.asList(18, 20, 22, 24, 26);
            armors = Arrays.asList(new Vector(0, 0.3, -2), new Vector(0, 0.3, -1), new Vector(0, 0.3, 0), new Vector(0, 0.3, 1), new Vector(0, 0.3, 2));
        }
    }
    
}