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

package io.github.Leonardo0013YT.UltraSkyWars.api.nms;

import io.github.Leonardo0013YT.UltraSkyWars.api.enums.DamageCauses;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.nms.NametagVersion;

public abstract class OldNMS extends NMS {
    
    public OldNMS(){
        try {
            nametagVersion = NametagVersion.valueOf(version);
            packet = getNMSClass("Packet");
            enumTimes = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
            enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            packetPlayOutTimes = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            packetPlayOutTitle = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
            a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
            position = getNMSClass("Entity").getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
            causes = DamageCauses.valueOf(version);
            isNewAction =
                    version.equals("v1_12_R1") ||
                            version.equals("v1_13_R2") ||
                            version.equals("v1_14_R1") ||
                            version.equals("v1_15_R1") ||
                            version.equals("v1_16_R1") ||
                            version.equals("v1_16_R2") ||
                            version.equals("v1_16_R3") ||
                            version.equals("v1_17_R1") ||
                            version.equals("v1_18_R1") ||
                            version.equals("v1_19_R1");
            if (!isNewAction){
                packetPlayOutChat = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
                a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
