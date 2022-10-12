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

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.Party;
import io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party.PartyInvite;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PartyCMD implements CommandExecutor {
    
    private final InjectionParty ip;
    
    public PartyCMD(InjectionParty ip){
        this.ip = ip;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (args.length < 1){
                sendHelp(sender);
                return true;
            }
            switch(args[0].toLowerCase()){
                case "help":
                    sendHelp(sender);
                    break;
                case "menu":{
                    if (ip.getPam().isInParty(p)){
                        ip.getPem().createMembersMenu(p, ip.getPam().getPartyByPlayer(p));
                        return true;
                    }
                    UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
                    plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("mainparty"));
                    break;
                }
                case "setleader":{
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get(p, "noInParty"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                        return true;
                    }
                    Party party = ip.getPam().getPartyByPlayer(p);
                    UUID on = party.getUUIDByName(args[1]);
                    if (on == null){
                        p.sendMessage(ip.getParties().get(p, "noOnline"));
                        return true;
                    }
                    if (party.getLeader().equals(on)){
                        p.sendMessage(ip.getParties().get("alreadyLeader"));
                        return true;
                    }
                    if (!party.getMembers().containsKey(on)){
                        p.sendMessage(ip.getParties().get(p, "noYourParty"));
                        return true;
                    }
                    ip.getPam().sendNewLeader(party, on, args[1]);
                    break;
                }
                case "privacy":{
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get(p, "noInParty"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                        return true;
                    }
                    Party party = ip.getPam().getPartyByPlayer(p);
                    ip.getPam().setPrivacy(party.getLeader(), !party.isPrivacy());
                    break;
                }
                case "list":{
                    if (!ip.getPam().isInParty(p.getUniqueId())){
                        p.sendMessage(ip.getParties().get(p, "noInParty"));
                        return true;
                    }
                    Party par = ip.getPam().getPartyByPlayer(p.getUniqueId());
                    for ( String header : ip.getParties().get("list.header").split("\\n") ){
                        if (header.contains("<players>")){
                            for ( Map.Entry<UUID, String> entry : par.getMembers().entrySet() ){
                                if (entry.getKey().equals(par.getLeader())){
                                    continue;
                                }
                                p.sendMessage(ip.getParties().get("list.player").replace("<name>", entry.getValue()));
                            }
                        } else {
                            p.sendMessage(header.replace("<leader>", par.getLeaderName()));
                        }
                    }
                    break;
                }
                case "invite":{
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    Player on = Bukkit.getPlayer(args[1]);
                    if (on == null){
                        p.sendMessage(ip.getParties().get(p, "noOnline"));
                        return true;
                    }
                    if (ip.getPam().isInParty(p) && !ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                        return true;
                    }
                    if (on.getUniqueId().equals(p.getUniqueId())){
                        p.sendMessage(ip.getParties().get(p, "noInviteYou"));
                        return true;
                    }
                    if (ip.getPam().isInParty(on)){
                        p.sendMessage(ip.getParties().get(p, "isPartyMember"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        ip.getPam().createPartyByCommand(p.getUniqueId(), true, ip.getPam().getMaxParty(p), p.getName());
                    }
                    p.sendMessage(ip.getParties().get(p, "inviter").replaceAll("<player>", on.getName()));
                    on.sendMessage(ip.getParties().get(on, "invited").replaceAll("<player>", p.getName()));
                    ip.getPam().createPartyInvite(p.getUniqueId(), on.getUniqueId());
                    Party par = ip.getPam().getPartyByPlayer(p.getUniqueId());
                    inviteSend(p, on, par);
                    break;
                }
                case "kick":{
                    if (args.length < 2){
                        sendHelp(sender);
                        return true;
                    }
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get(p, "noInParty"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                        return true;
                    }
                    Party party1 = ip.getPam().getPartyByPlayer(p);
                    UUID on1 = party1.getUUIDByName(args[1]);
                    if (on1 == null){
                        p.sendMessage(ip.getParties().get(p, "noOnline"));
                        return true;
                    }
                    if (!ip.getPam().isInParty(on1)){
                        p.sendMessage(ip.getParties().get(p, "anyParty"));
                        return true;
                    }
                    if (!party1.getMembers().containsKey(on1)){
                        p.sendMessage(ip.getParties().get(p, "noYourParty"));
                        return true;
                    }
                    ip.getPam().sendPartyKick(p.getUniqueId(), on1);
                    break;
                }
                case "accept":{
                    if (!ip.getPam().isInvited(p)){
                        p.sendMessage(ip.getParties().get(p, "noInvited"));
                        return true;
                    }
                    if (ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get("alreadyParty"));
                        return true;
                    }
                    PartyInvite pi = ip.getPam().getPartyInvite(p.getUniqueId());
                    Party party = ip.getPam().getPartyByPlayer(pi.getInviter());
                    if (party == null){
                        p.sendMessage(ip.getParties().get(p, "disbandParty"));
                        return true;
                    }
                    ip.getPam().joinParty(p, party);
                    break;
                }
                case "chat":{
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get("noInParty"));
                        return true;
                    }
                    Party party = ip.getPam().getPartyByPlayer(p.getUniqueId());
                    if (party == null){
                        p.sendMessage(ip.getParties().get(p, "noInParty"));
                        return true;
                    }
                    ip.getPam().sendPartyChat(party, p.getUniqueId());
                    if (party.getChatMembers().contains(p.getUniqueId())){
                        p.sendMessage(ip.getParties().get("joiningPartyChat"));
                    } else {
                        p.sendMessage(ip.getParties().get("leavingPartyChat"));
                    }
                    break;
                }
                case "deny":{
                    if (!ip.getPam().isInvited(p)){
                        p.sendMessage(ip.getParties().get(p, "noInvited"));
                        return true;
                    }
                    if (ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get("alreadyParty"));
                        return true;
                    }
                    PartyInvite pi2 = ip.getPam().getPartyInvite(p.getUniqueId());
                    Party party2 = ip.getPam().getPartyByPlayer(pi2.getInviter());
                    if (party2 == null){
                        p.sendMessage(ip.getParties().get(p, "disbandParty"));
                        return true;
                    }
                    ip.getPam().denyParty(p, party2);
                    break;
                }
                case "disband":{
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get("noInParty"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get("onlyLeader"));
                        return true;
                    }
                    ip.getPam().sendDelete(p.getUniqueId());
                    break;
                }
                case "leave":{
                    if (!ip.getPam().isInParty(p)){
                        p.sendMessage(ip.getParties().get("noInParty"));
                        return true;
                    }
                    Party party = ip.getPam().getPartyByPlayer(p.getUniqueId());
                    if (party == null) return true;
                    ip.getPam().sendPartyLeave(party.getLeader(), p.getUniqueId());
                    break;
                }
                default:
                    Player on2 = Bukkit.getPlayer(args[0]);
                    if (on2 == null){
                        p.sendMessage(ip.getParties().get(p, "noOnline"));
                        return true;
                    }
                    if (ip.getPam().isInParty(p) && !ip.getPam().isLeader(p)){
                        p.sendMessage(ip.getParties().get(p, "noIsLeader"));
                        return true;
                    }
                    if (on2.getUniqueId().equals(p.getUniqueId())){
                        p.sendMessage(ip.getParties().get(p, "noInviteYou"));
                        return true;
                    }
                    if (ip.getPam().isInParty(on2)){
                        p.sendMessage(ip.getParties().get(p, "isPartyMember"));
                        return true;
                    }
                    if (!ip.getPam().isLeader(p)){
                        ip.getPam().createPartyByCommand(p.getUniqueId(), true, ip.getPam().getMaxParty(p), p.getName());
                    }
                    p.sendMessage(ip.getParties().get(p, "inviter").replaceAll("<player>", on2.getName()));
                    for ( String msg : ip.getParties().get(on2, "invited").replaceAll("<player>", p.getName()).split("\\n") ){
                        if (msg.equals("<buttons>")){
                            FancyMessage fm = new FancyMessage(ip.getParties().get("accept")).setClick(ClickEvent.Action.RUN_COMMAND, "/swp accept").setHover(HoverEvent.Action.SHOW_TEXT, ip.getParties().get("hoverAccept"))
                                    .addMsg(ip.getParties().get("deny")).setClick(ClickEvent.Action.RUN_COMMAND, "/swp deny").setHover(HoverEvent.Action.SHOW_TEXT, ip.getParties().get("hoverDeny")).build();
                            fm.send(on2);
                        } else {
                            on2.sendMessage(msg);
                        }
                    }
                    ip.getPam().createPartyInvite(p.getUniqueId(), on2.getUniqueId());
                    Party par2 = ip.getPam().getPartyByPlayer(p.getUniqueId());
                    inviteSend(p, on2, par2);
                    break;
            }
        }
        return false;
    }
    
    private void inviteSend(Player p, Player on, Party par){
        for ( UUID j : par.getMembers().keySet() ){
            Player m = Bukkit.getPlayer(j);
            if (m == null || j.equals(p.getUniqueId())) continue;
            m.sendMessage(ip.getParties().get(m, "inviteMembers").replace("<player>", on.getName()));
        }
    }
    
    private void sendHelp(CommandSender s){
        for ( String m : ip.getParties().getList("help") ){
            s.sendMessage(m.replaceAll("&", "§"));
        }
    }
    
}