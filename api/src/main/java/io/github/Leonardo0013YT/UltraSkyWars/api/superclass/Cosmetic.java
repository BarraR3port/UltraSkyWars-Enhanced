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

package io.github.Leonardo0013YT.UltraSkyWars.api.superclass;


import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Purchasable;

public abstract class Cosmetic implements Purchasable {
    
    public String name, permission, autoGivePermission;
    public boolean isBuy, needPermToBuy;
    public int id, slot, page, price;
    
    public Cosmetic(Settings config, String path, String type){
        this.name = config.get(path + ".name");
        this.id = config.getInt(path + ".id");
        this.slot = config.getInt(path + ".slot");
        this.page = config.getInt(path + ".page");
        this.price = config.getInt(path + ".price");
        this.permission = config.get(path + ".permission");
        this.autoGivePermission = config.getOrDefault(path + ".autoGivePermission", "ultraskywars." + type + ".autogive." + name);
        this.isBuy = config.getBoolean(path + ".isBuy");
        this.needPermToBuy = config.getBooleanOrDefault(path + ".needPermToBuy", false);
    }
    
    @Override
    public String getPermission(){
        return permission;
    }
    
    @Override
    public String getAutoGivePermission(){
        return autoGivePermission;
    }
    
    @Override
    public boolean isBuy(){
        return isBuy;
    }
    
    @Override
    public boolean needPermToBuy(){
        return needPermToBuy;
    }
    
    @Override
    public int getPrice(){
        return price;
    }
    
    public String getName(){
        return name;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public int getPage(){
        return page;
    }
    
    public int getId(){
        return id;
    }
}