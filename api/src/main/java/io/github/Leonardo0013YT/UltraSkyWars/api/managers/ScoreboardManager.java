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

package io.github.Leonardo0013YT.UltraSkyWars.api.managers;

import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.scoreboard.Netherboard;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    
    private final Netherboard board = Netherboard.instance();
    private final HashMap<String, HashMap<Integer, String>> linesUpdated = new HashMap<>();
    
    public ScoreboardManager(){
        reload();
    }
    
    public void reload(){
        UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
        linesUpdated.clear();
        String[] starting = plugin.getLang().get("scoreboards.starting").split("\\n");
        linesUpdated.put("starting", new HashMap<>());
        for ( int n = 1, n2 = starting.length - 1; n < starting.length + 1; ++n, --n2 ){
            String line = starting[n2];
            linesUpdated.get("starting").put(n, line);
        }
        String[] lobby = plugin.getLang().get("scoreboards.lobby").split("\\n");
        linesUpdated.put("lobby", new HashMap<>());
        for ( int n = 1, n2 = lobby.length - 1; n < lobby.length + 1; ++n, --n2 ){
            String line = lobby[n2];
            linesUpdated.get("lobby").put(n, line);
        }
        String[] pregame = plugin.getLang().get("scoreboards.pregame").split("\\n");
        linesUpdated.put("pregame", new HashMap<>());
        for ( int n = 1, n2 = pregame.length - 1; n < pregame.length + 1; ++n, --n2 ){
            String line = pregame[n2];
            linesUpdated.get("pregame").put(n, line);
        }
        String[] game = plugin.getLang().get("scoreboards.game").split("\\n");
        linesUpdated.put("game", new HashMap<>());
        for ( int n = 1, n2 = game.length - 1; n < game.length + 1; ++n, --n2 ){
            String line = game[n2];
            linesUpdated.get("game").put(n, line);
        }
        String[] team = plugin.getLang().get("scoreboards.team").split("\\n");
        linesUpdated.put("team", new HashMap<>());
        for ( int n = 1, n2 = team.length - 1; n < team.length + 1; ++n, --n2 ){
            String line = team[n2];
            linesUpdated.get("team").put(n, line);
        }
        String[] ranked = plugin.getLang().get("scoreboards.ranked").split("\\n");
        linesUpdated.put("ranked", new HashMap<>());
        for ( int n = 1, n2 = ranked.length - 1; n < ranked.length + 1; ++n, --n2 ){
            String line = ranked[n2];
            linesUpdated.get("ranked").put(n, line);
        }
        String[] tnt_madness = plugin.getLang().get("scoreboards.tnt_madness").split("\\n");
        linesUpdated.put("tnt_madness", new HashMap<>());
        for ( int n = 1, n2 = tnt_madness.length - 1; n < tnt_madness.length + 1; ++n, --n2 ){
            String line = tnt_madness[n2];
            linesUpdated.get("tnt_madness").put(n, line);
        }
        String[] main = plugin.getLang().get("scoreboards.main").split("\\n");
        linesUpdated.put("main", new HashMap<>());
        for ( int n = 1, n2 = main.length - 1; n < main.length + 1; ++n, --n2 ){
            String line = main[n2];
            linesUpdated.get("main").put(n, line);
        }
    }
    
    public void clear(Player p){
        if (board.hasBoard(p)){
            board.deleteBoard(p);
        }
    }
    
    public void update(Player p, GameEvent ge, UltraSkyWarsApi plugin){
        if (p == null || !p.isOnline()) return;
        if (plugin.getCm().isDisableAllScoreboards()) return;
        if (!plugin.getGm().isPlayerInGame(p) && plugin.getCm().isLobbyScoreboard()){
            if (!board.hasBoard(p)){
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    board.createBoard(p, plugin.getLang().get(p, "scoreboards.main-title"));
                    board.getBoard(p).setName(plugin.getLang().get(p, "scoreboards.main-title"));
                    for ( Map.Entry<Integer, String> entry : linesUpdated.get("main").entrySet() ){
                        board.getBoard(p).set(plugin.getAdm().parsePlaceholders(p, entry.getValue()), entry.getKey());
                    }
                });
            } else {
                board.getBoard(p).setName(plugin.getLang().get(p, "scoreboards.main-title"));
                for ( Map.Entry<Integer, String> entry : linesUpdated.get("main").entrySet() ){
                    board.getBoard(p).set(plugin.getAdm().parsePlaceholders(p, entry.getValue()), entry.getKey());
                }
            }
            return;
        }
        if (!plugin.getGm().isPlayerInGame(p)){
            return;
        }
        Game game = plugin.getGm().getGameByPlayer(p);
        if (!board.hasBoard(p)){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                board.createBoard(p, replaceTitle(plugin.getLang().get(p, "scoreboards.lobby-title"), game, plugin));
                setLobbyScoreboard(p, plugin, game);
            });
            return;
        }
        if (game.isState(State.WAITING)){
            setLobbyScoreboard(p, plugin, game);
        } else if (game.isState(State.STARTING)){
            board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.starting-title"), game, plugin));
            for ( Map.Entry<Integer, String> entry : linesUpdated.get("starting").entrySet() ){
                board.getBoard(p).set(starting(plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, plugin), entry.getKey());
            }
        } else if (game.isState(State.PREGAME)){
            board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.pregame-title"), game, plugin));
            for ( Map.Entry<Integer, String> entry : linesUpdated.get("pregame").entrySet() ){
                board.getBoard(p).set(pregame(plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, plugin), entry.getKey());
            }
        } else if (game.isState(State.GAME) || game.isState(State.FINISH)){
            switch(game.getGameType()){
                case "SOLO":
                    board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.game-title"), game, plugin));
                    for ( Map.Entry<Integer, String> entry : linesUpdated.get("game").entrySet() ){
                        board.getBoard(p).set(game(p, plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, ge, plugin), entry.getKey());
                    }
                    break;
                case "TEAM":
                    board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.team-title"), game, plugin));
                    for ( Map.Entry<Integer, String> entry : linesUpdated.get("team").entrySet() ){
                        board.getBoard(p).set(game(p, plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, ge, plugin), entry.getKey());
                    }
                    break;
                case "TNT_MADNESS":
                    board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.tnt_madness-title"), game, plugin));
                    for ( Map.Entry<Integer, String> entry : linesUpdated.get("tnt_madness").entrySet() ){
                        board.getBoard(p).set(game(p, plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, ge, plugin), entry.getKey());
                    }
                    break;
                default:
                    board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.ranked-title"), game, plugin));
                    for ( Map.Entry<Integer, String> entry : linesUpdated.get("ranked").entrySet() ){
                        board.getBoard(p).set(game(p, plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, ge, plugin), entry.getKey());
                    }
                    break;
            }
        }
    }
    
    private void setLobbyScoreboard(Player p, UltraSkyWarsApi plugin, Game game){
        board.getBoard(p).setName(replaceTitle(plugin.getLang().get(p, "scoreboards.lobby-title"), game, plugin));
        for ( Map.Entry<Integer, String> entry : linesUpdated.get("lobby").entrySet() ){
            board.getBoard(p).set(lobby(plugin.getAdm().parsePlaceholders(p, entry.getValue()), game, plugin), entry.getKey());
        }
    }
    
    private String replaceTitle(String c, Game game, UltraSkyWarsApi plugin){
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()));
    }
    
    private String lobby(String c, Game game, UltraSkyWarsApi plugin){
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName());
    }
    
    private String starting(String c, Game game, UltraSkyWarsApi plugin){
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName())
                .replaceAll("<time>", String.valueOf(game.getStarting()))
                .replaceAll("<s>", (game.getStarting() > 1) ? "s" : "");
    }
    
    private String pregame(String c, Game game, UltraSkyWarsApi plugin){
        return c.replaceAll("<mode>", plugin.getLang().get("modes." + game.getGameType().toLowerCase()))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<map>", game.getName())
                .replaceAll("<time>", String.valueOf(game.getPregame()))
                .replaceAll("<s>", (game.getPregame() > 1) ? "s" : "");
    }
    
    private String game(Player p, String c, Game game, GameEvent ge, UltraSkyWarsApi plugin){
        GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
        String name;
        int time;
        if (ge == null){
            name = plugin.getLang().get(p, "events.noMore");
            time = 0;
        } else {
            name = plugin.getLang().get(p, "events." + ge.getName());
            time = ge.getTime();
        }
        int elo = (plugin.getDb().getSWPlayer(p) == null) ? 0 : plugin.getDb().getSWPlayer(p).getElo();
        return c.replaceAll("<mode>", plugin.getLang().get(p, "modes." + game.getGameType().toLowerCase()))
                .replaceAll("<eventTime>", Utils.convertTime(time))
                .replaceAll("<kills>", String.valueOf((gp == null) ? 0 : gp.getKills()))
                .replaceAll("<teamkills>", String.valueOf(game.getTeamPlayer(p) == null ? 0 : game.getTeamPlayer(p).getKills()))
                .replaceAll("<event>", name)
                .replaceAll("<max>", String.valueOf(game.getMax()))
                .replaceAll("<elo>", String.valueOf(elo))
                .replaceAll("<date>", Utils.getDate())
                .replaceAll("<players>", String.valueOf(game.getPlayers().size()))
                .replaceAll("<map>", game.getName());
    }
    
    public void remove(Player p){
        if (board.hasBoard(p)){
            board.removeBoard(p);
        }
    }
    
}