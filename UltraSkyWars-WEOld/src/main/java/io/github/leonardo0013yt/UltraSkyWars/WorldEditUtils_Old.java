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

package io.github.leonardo0013yt.UltraSkyWars;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.world.World;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.WorldEdit;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.GlassBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WorldEditUtils_Old implements WorldEdit {
    
    private final String path;
    private final UltraSkyWarsApi plugin;
    private final HashMap<String, Schematic> cache = new HashMap<>();
    
    public WorldEditUtils_Old(UltraSkyWarsApi plugin){
        this.plugin = plugin;
        this.path = Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics";
    }
    
    @Override
    public void paste(Location loc, String schematic, boolean air, CallBackAPI<Boolean> done){
        String s = schematic.replaceAll(".schematic", "");
        Vector v = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        World w = FaweAPI.getWorld(loc.getWorld().getName());
        if (!cache.containsKey(s)){
            File file = new File(path, s + ".schematic");
            ClipboardFormat cf = ClipboardFormat.findByFile(file);
            try {
                if (cf != null){
                    cache.put(s, cf.load(file));
                }
            } catch (IOException ignored) {
            }
        }
        if (cache.containsKey(s)){
            Schematic sh = cache.get(s);
            EditSession editSession = sh.paste(w, v, false, air, null);
            editSession.flushQueue();
            done.done(true);
        }
    }
    
    @Override
    public HashMap<org.bukkit.util.Vector, GlassBlock> getBlocks(String schematic){
        HashMap<org.bukkit.util.Vector, GlassBlock> blocks = new HashMap<>();
        String s = schematic.replaceAll(".schematic", "");
        if (!cache.containsKey(s)){
            File file = new File(path, s + ".schematic");
            ClipboardFormat cf = ClipboardFormat.findByFile(file);
            try {
                if (cf != null){
                    cache.put(s, cf.load(file));
                }
            } catch (IOException ignored) {
            }
        }
        if (cache.containsKey(s)){
            Schematic sh = cache.get(s);
            Clipboard clipboard = sh.getClipboard();
            for ( int x2 = clipboard.getMinimumPoint().getBlockX(); x2 <= clipboard.getMaximumPoint().getBlockX(); x2++ ){
                for ( int y2 = clipboard.getMinimumPoint().getBlockY(); y2 <= clipboard.getMaximumPoint().getBlockY(); y2++ ){
                    for ( int z2 = clipboard.getMinimumPoint().getBlockZ(); z2 <= clipboard.getMaximumPoint().getBlockZ(); z2++ ){
                        int x = x2 - clipboard.getMinimumPoint().getBlockX();
                        int y = y2 - clipboard.getMinimumPoint().getBlockY();
                        int z = z2 - clipboard.getMinimumPoint().getBlockZ();
                        BaseBlock block = clipboard.getBlock(new Vector(x2, y2, z2));
                        if (!block.isAir()){
                            blocks.put(new org.bukkit.util.Vector(x, y, z), new GlassBlock(Material.getMaterial(block.getType()), block.getData()));
                        }
                    }
                }
            }
        }
        return blocks;
    }
    
}