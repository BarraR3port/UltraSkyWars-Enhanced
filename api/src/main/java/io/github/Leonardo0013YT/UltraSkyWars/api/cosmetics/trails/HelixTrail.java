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

package io.github.Leonardo0013YT.UltraSkyWars.api.cosmetics.trails;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class HelixTrail {
    
    private final String particle;
    private final double radius;
    private final int degressPerIncrement;
    private final int helixCount;
    private final int helixDegreeSeperation;
    private int theta = 0;
    private double cosX, sinX, cosZ, sinZ;
    
    public HelixTrail(String particle, int helixCount, Vector direction, double radius, int rotationalSpeed){
        this.particle = particle;
        this.radius = radius;
        degressPerIncrement = 360 / Math.max(rotationalSpeed, 1);
        this.helixCount = Math.max(helixCount, 1);
        this.helixDegreeSeperation = 360 / Math.max(helixCount, 1);
        calculateDeviations(direction);
    }
    
    protected void calculateDeviations(Vector direction){
        Vector zDeviationComponent = new Vector(direction.getX(), direction.getY(), 0);
        Vector xDeviationComponent = new Vector(0, direction.getY(), direction.getZ());
        Vector yAxis = new Vector(0, 1, 0);
        float xDeviation = yAxis.angle(xDeviationComponent);
        float zDeviation = yAxis.angle(zDeviationComponent);
        cosX = Math.cos(xDeviation);
        sinX = Math.sin(xDeviation);
        cosZ = Math.cos(zDeviation);
        sinZ = Math.sin(zDeviation);
    }
    
    public void spawn(Location location){
        for ( int i = 0; i < helixCount; i++ ){
            Location spawnLoc = location.clone().add(offsetFromCenter(theta + (i * helixDegreeSeperation)));
            UltraSkyWarsApi.get().getVc().getNMS().broadcastParticle(spawnLoc, 0, 0, 0, 0, particle, 1, 20);
        }
        theta += degressPerIncrement;
    }
    
    protected Vector offsetFromCenter(int degrees){
        double radians = Math.toRadians(degrees);
        double x = Math.cos(radians);
        double z = Math.sin(radians);
        Vector v = new Vector(x, 0, z);
        v.multiply(radius);
        rotateAroundAxisX(v, cosX, sinX);
        rotateAroundAxisZ(v, cosZ, sinZ);
        return v;
    }
    
    private void rotateAroundAxisX(Vector v, double cos, double sin){
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        v.setY(y).setZ(z);
    }
    
    private void rotateAroundAxisZ(Vector v, double cos, double sin){
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        v.setX(x).setY(y);
    }
    
}