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

package io.github.Leonardo0013YT.UltraSkyWars.api.xseries;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

@SuppressWarnings("deprecation")
public final class XBlock {
    
    private static final boolean ISFLAT = XMaterial.isNewVersion();
    
    public static boolean setColor(Block block, DyeColor color){
        if (ISFLAT){
            String type = block.getType().name();
            if (type.endsWith("WOOL")) block.setType(Material.valueOf(color.name() + "_WOOL"));
            else if (type.endsWith("BED")) block.setType(Material.valueOf(color.name() + "_BED"));
            else if (type.endsWith("STAINED_GLASS")) block.setType(Material.valueOf(color.name() + "_STAINED_GLASS"));
            else if (type.endsWith("STAINED_GLASS_PANE"))
                block.setType(Material.valueOf(color.name() + "_STAINED_GLASS_PANE"));
            else if (type.endsWith("TERRACOTTA")) block.setType(Material.valueOf(color.name() + "_TERRACOTTA"));
            else if (type.endsWith("GLAZED_TERRACOTTA"))
                block.setType(Material.valueOf(color.name() + "_GLAZED_TERRACOTTA"));
            else if (type.endsWith("BANNER")) block.setType(Material.valueOf(color.name() + "_BANNER"));
            else if (type.endsWith("WALL_BANNER")) block.setType(Material.valueOf(color.name() + "_WALL_BANNER"));
            else if (type.endsWith("CARPET")) block.setType(Material.valueOf(color.name() + "_CARPET"));
            else if (type.endsWith("SHULKER_BOX")) block.setType(Material.valueOf(color.name() + "_SHULKERBOX"));
            else if (type.endsWith("CONCRETE")) block.setType(Material.valueOf(color.name() + "_CONCRETE"));
            else if (type.endsWith("CONCRETE_POWDER"))
                block.setType(Material.valueOf(color.name() + "_CONCRETE_POWDER"));
            else return false;
            return true;
        }
        block.setData(color.getWoolData());
        return false;
    }
    
    private static boolean isMaterial(Block block, String... materials){
        String type = block.getType().name();
        for ( String material : materials )
            if (type.equals(material)) return true;
        return false;
    }
    
}