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

package io.github.Leonardo0013YT.UltraSkyWars;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.PasteBuilder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WorldEdit;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.GlassBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class WorldEditUtils_New implements WorldEdit {
    
    private final HashMap<String, Clipboard> cache = new HashMap<>();
    private final String path;
    private final UltraSkyWarsApi plugin;
    
    public WorldEditUtils_New(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        this.path = Bukkit.getWorldContainer() + "/plugins/FastAsyncWorldEdit/schematics";
    }
    
    @Override
    public void paste(Location loc, String schematic, boolean air, CallBackAPI<Boolean> done){
        if (loc == null || loc.getWorld() == null) return;
        String s = schematic.replaceAll(".schematic", "").replaceAll(".schem", "");
        BlockVector3 v = BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        World w = FaweAPI.getWorld(loc.getWorld().getName());
        if (!cache.containsKey(s)){
            try {
                File file = new File(path, s + ".schem");
                ClipboardFormat format = ClipboardFormats.findByFile(file);
                try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                    cache.put(s, reader.read());
                }
            } catch (IOException ignored) {
            }
        }
        if (cache.containsKey(s)){
            Clipboard clipboard = cache.get(s);
            try {
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().newEditSession(w);
                PasteBuilder operation = holder.createPaste(editSession).to(v).ignoreAirBlocks(air);
                Operations.completeBlindly(operation.build());
                done.done(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public HashMap<Vector, GlassBlock> getBlocks(String schematic){
        HashMap<Vector, GlassBlock> blocks = new HashMap<>();
        String s = schematic.replaceAll(".schematic", "").replaceAll(".schem", "");
        if (!cache.containsKey(s)){
            try {
                File file = new File(path, s + ".schem");
                ClipboardFormat format = ClipboardFormats.findByFile(file);
                try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                    cache.put(s, reader.read());
                }
            } catch (IOException ignored) {
            }
        }
        if (cache.containsKey(s)){
            Clipboard clipboard = cache.get(s);
            for ( int x2 = clipboard.getMinimumPoint().getBlockX(); x2 <= clipboard.getMaximumPoint().getBlockX(); x2++ ){
                for ( int y2 = clipboard.getMinimumPoint().getBlockY(); y2 <= clipboard.getMaximumPoint().getBlockY(); y2++ ){
                    for ( int z2 = clipboard.getMinimumPoint().getBlockZ(); z2 <= clipboard.getMaximumPoint().getBlockZ(); z2++ ){
                        int x = x2 - clipboard.getMinimumPoint().getBlockX();
                        int y = y2 - clipboard.getMinimumPoint().getBlockY();
                        int z = z2 - clipboard.getMinimumPoint().getBlockZ();
                        BaseBlock block = clipboard.getFullBlock(BlockVector3.at(x2, y2, z2));
                        if (!block.getBlockType().getMaterial().isAir()){
                            blocks.put(new Vector(x, y, z), new GlassBlock(Material.valueOf(block.getBlockType().getId().replaceFirst("minecraft:", "").toUpperCase()), 0));
                        }
                    }
                }
            }
        }
        return blocks;
    }
    
}