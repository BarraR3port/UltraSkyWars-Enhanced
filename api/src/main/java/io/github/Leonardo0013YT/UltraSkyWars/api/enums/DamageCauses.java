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

public enum DamageCauses {
    
    v1_8_R3(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM"}),
    v1_9_R2(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL"}),
    v1_10_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_11_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_12_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_13_R2(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_14_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_15_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_16_R1(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_16_R2(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"}),
    v1_16_R3(new String[]{"CONTACT", "ENTITY_ATTACK", "PROJECTILE", "SUFFOCATION", "FALL", "FIRE", "FIRE_TICK", "MELTING", "LAVA", "DROWNING", "BLOCK_EXPLOSION", "ENTITY_EXPLOSION", "VOID", "LIGHTNING", "SUICIDE", "STARVATION", "POISON", "MAGIC", "WITHER", "FALLING_BLOCK", "THORNS", "CUSTOM", "FLY_INTO_WALL", "HOT_FLOOR", "CRAMMING", "ENTITY_SWEEP_ATTACK", "DRAGON_BREATH"});
    
    private final String[] causes;
    
    DamageCauses(String[] causes){
        this.causes = causes;
    }
    
    public String[] getCauses(){
        return causes;
    }
}