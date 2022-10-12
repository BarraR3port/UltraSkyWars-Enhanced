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

package io.github.Leonardo0013YT.UltraSkyWars.api.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.api.nametags.Nametags;
import io.github.Leonardo0013YT.UltraSkyWars.api.nms.NMS;

public interface IVersionController {
    
    void disable();
    
    NMS getNMS();
    
    Nametags getNameTag(String name, String display, String prefix);
    
    
    String getVersion();
    
    boolean is1_12();
    
    boolean is1_9to17();
    
    boolean is1_13to17();
}
