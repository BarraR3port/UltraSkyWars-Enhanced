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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties;

import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.cmds.PartyCMD;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.inventories.PartyMainMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.inventories.parties.PartyMemberMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.inventories.parties.PartyPartiesMenu;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.listeners.PartyListener;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.managers.PartyManager;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.menus.PartyMenu;
import org.bukkit.command.CommandExecutor;

public class InjectionParty implements Injection {
    
    private Settings parties;
    private PartyManager pam;
    private Gson gson;
    private PartyMenu pem;
    
    @Override
    public void loadInjection(UltraSkyWarsApi main){
        this.gson = new Gson();
        parties = new Settings("modules/parties", true, false);
        pam = new PartyManager(main, this);
        pem = new PartyMenu(main, this);
        main.getUim().getMenus().put("mainparty", new PartyMainMenu(this, "mainparty"));
        main.getUim().getMenus().put("partymember", new PartyMemberMenu(this, "partymember"));
        main.getUim().getMenus().put("partylist", new PartyPartiesMenu(this, "partylist"));
        main.getServer().getPluginManager().registerEvents(new PartyListener(this), main);
        CommandExecutor ce = new PartyCMD(this);
        main.getCommand("swp").setExecutor(ce);
        if (parties.getBoolean("partyCommand")){
            main.getCommand("party").setExecutor(ce);
        }
    }
    
    @Override
    public void reload(){
        pam.reload();
    }
    
    @Override
    public void disable(){
    
    }
    
    public PartyMenu getPem(){
        return pem;
    }
    
    public Gson getGson(){
        return gson;
    }
    
    public Settings getParties(){
        return parties;
    }
    
    public PartyManager getPam(){
        return pam;
    }
}