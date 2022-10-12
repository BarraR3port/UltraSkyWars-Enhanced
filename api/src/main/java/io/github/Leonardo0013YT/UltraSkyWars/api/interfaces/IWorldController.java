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

import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;

public interface IWorldController {
    
    void clearLobby(Location lobby);
    
    void deleteWorld(World w, CallBackAPI<World> done);
    
    void deleteWorld(String name);
    
    World resetWorld(String name);
    
    World createEmptyWorld(String name);
    
    void backUpWorld(World w, String name);
    
    void copyWorld(File source, File target);
    
    void deleteDirectory(File source);
    
    ChunkGenerator getChunkGenerator();
    
    WorldEdit getEdit();
    
}
