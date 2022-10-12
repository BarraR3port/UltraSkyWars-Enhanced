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

public enum ProjectileType {
    
    NOPROJ(false, false, false, false),
    YESPROJ(true, false, false, false),
    EXPROJ(true, true, false, false),
    DESPROJ(true, false, true, false),
    TELEPROJ(true, false, false, true);
    
    private final boolean appear;
    private final boolean explosive;
    private final boolean destructor;
    private final boolean teleporter;
    
    ProjectileType(boolean appear, boolean explosive, boolean destructor, boolean teleporter){
        this.appear = appear;
        this.explosive = explosive;
        this.destructor = destructor;
        this.teleporter = teleporter;
    }
    
    public boolean isAppear(){
        return appear;
    }
    
    public boolean isExplosive(){
        return explosive;
    }
    
    public boolean isDestructor(){
        return destructor;
    }
    
    public boolean isTeleporter(){
        return teleporter;
    }
}