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

package io.github.Leonardo0013YT.UltraSkyWars.api.utils;


import io.github.Leonardo0013YT.UltraSkyWars.api.UltraSkyWarsApi;
import io.github.Leonardo0013YT.UltraSkyWars.api.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.api.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.game.GamePlayer;
import io.github.Leonardo0013YT.UltraSkyWars.api.objects.Multiplier;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.api.superclass.GameEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.xseries.XMaterial;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
    
    private final static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ"};
    private final static DecimalFormat df = new DecimalFormat("##.#");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final static UltraSkyWarsApi plugin = UltraSkyWarsApi.get();
    private final static Random random = new Random();
    private final static ItemStack[] gifs = {NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2NzMwZGU3ZTViOTQxZWZjNmU4Y2JhZjU3NTVmOTQyMWEyMGRlODcxNzU5NjgyY2Q4ODhjYzRhODEyODIifX19"),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA4Y2U3ZGViYTU2YjcyNmE4MzJiNjExMTVjYTE2MzM2MTM1OWMzMDQzNGY3ZDVlM2MzZmFhNmZlNDA1MiJ9fX0="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjNzVhMDViMzQ0ZWEwNDM4NjM5NzRjMTgwYmE4MTdhZWE2ODY3OGNiZWE1ZTRiYTM5NWY3NGQ0ODAzZDFkIn19fQ=="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0MTlmY2U1MDZhNDk1MzQzYTFkMzY4YTcxZDIyNDEzZjA4YzZkNjdjYjk1MWQ2NTZjZDAzZjgwYjRkM2QzIn19fQ=="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUzYThmZDA4NTI5Nzc0NDRkOWZkNzc5N2NhYzA3YjhkMzk0OGFkZGM0M2YwYmI1Y2UyNWFlNzJkOTVkYyJ9fX0="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNlNThlYTdmMzExM2NhZWNkMmIzYTZmMjdhZjUzYjljYzljZmVkN2IwNDNiYTMzNGI1MTY4ZjEzOTFkOSJ9fX0="),
            NBTEditor.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjU2MTJkYzdiODZkNzFhZmMxMTk3MzAxYzE1ZmQ5NzllOWYzOWU3YjFmNDFkOGYxZWJkZjgxMTU1NzZlMmUifX19")};
    
    public static ItemStack getIcon(Settings config, String path){
        if (config.isSet(path + ".icon.value")){
            return new ItemUtils(XMaterial.PLAYER_HEAD).setUrl(config.get(path + ".icon.value")).setDisplayName(config.get(path + ".icon.meta.display-name")).setLore(config.get(path + ".icon.meta.lore")).build();
        } else {
            if (config.isSet(path + ".icon")){
                return config.getConfig().getItemStack(path + ".icon");
            }
            return config.getConfig().getItemStack(path + ".item");
        }
    }
    
    public static boolean isEmpty(Inventory inv){
        if (inv == null) return false;
        for ( ItemStack item : inv.getContents() ){
            if (item != null){
                return false;
            }
        }
        return true;
    }
    
    public static void firework(Location loc){
        Firework fa = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fam = fa.getFireworkMeta();
        FireworkEffect.Type tipo = FireworkEffect.Type.values()[random.nextInt(4)];
        Color c1 = Color.fromBGR(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        Color c2 = Color.fromBGR(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        FireworkEffect ef = FireworkEffect.builder().withColor(c1).withFade(c2).with(tipo).build();
        fam.addEffect(ef);
        fam.setPower(0);
        fa.setFireworkMeta(fam);
    }
    
    public static Location getRightSide(Location l, double value){
        float yaw = l.getYaw() / 60.0F;
        return l.clone().subtract((new Vector(Math.cos((double) yaw + 5.1D), 0.0D, Math.sin((double) yaw + 5.1D))).normalize().multiply(value));
    }
    
    public static Giant spawn(Location ballon, Location fence, ItemStack item){
        Location l = ballon.clone();
        l.setYaw(0);
        l.setPitch(0);
        Giant giant = ballon.getWorld().spawn(l, Giant.class);
        giant.setMetadata("Balloon", new FixedMetadataValue(plugin, ""));
        giant.setCustomNameVisible(false);
        giant.getEquipment().setItemInHand(item);
        giant.setCanPickupItems(false);
        giant.setRemoveWhenFarAway(false);
        giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 999));
        ArmorStand as = getArmorStand(l);
        as.setPassenger(giant);
        if (fence != null){
            Location armor = l.clone().add(-2, 9.75, 3.75);
            ArmorStand asl = getArmorStand(armor);
            Bat s = l.getWorld().spawn(armor, Bat.class);
            asl.setPassenger(s);
            plugin.getVc().getNMS().freezeMob(s);
            s.setMetadata("Balloon", new FixedMetadataValue(plugin, ""));
            s.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 999));
            s.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 999));
            fence.getBlock().setType(XMaterial.OAK_FENCE.parseMaterial());
            ArmorStand leash = getArmorStand(fence);
            s.setLeashHolder(leash);
            asl.setPassenger(s);
        }
        return giant;
    }
    
    public static ArmorStand getArmorStand(Location loc){
        ArmorStand armor = getChestStand(loc);
        armor.setMetadata("Balloon", new FixedMetadataValue(plugin, ""));
        return armor;
    }
    
    public static ArmorStand getChestStand(Location loc){
        ArmorStand armor = loc.getWorld().spawn(loc, ArmorStand.class);
        armor.setGravity(false);
        armor.setVisible(false);
        armor.setSmall(true);
        return armor;
    }
    
    public static void setData(Block b, byte data){
        b.setData(data);
    }
    
    public static String formatDouble(double d){
        return df.format(d);
    }
    
    public static List<String> orderABC(List<String> names){
        return names.stream().sorted().collect(Collectors.toList());
    }
    
    public static List<Integer> orderDESC(List<Integer> names){
        names.sort(Collections.reverseOrder());
        return names;
    }
    
    public static String convertTime(int timeInSeconds){
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        String formattedTime = "";
        if (hours > 0){
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }
        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";
        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;
        return formattedTime;
    }
    
    public static String getDate(){
        return sdf.format(new Date());
    }
    
    public static void updateSB(Player p){
        if (!plugin.isStop()){
            plugin.getSb().update(p, null, plugin);
        }
    }
    
    public static void updateSB(){
        if (!plugin.isStop()){
            World w = Bukkit.getWorld(plugin.getCm().getLobbyWorld());
            if (w != null){
                w.getPlayers().forEach(p -> plugin.getSb().update(p, null, plugin));
            }
        }
    }
    
    public static void updateSB(Game game, boolean deleted){
        if (!plugin.isStop()){
            GameEvent ge = game.getNowEvent();
            game.getCached().forEach(p -> {
                if (deleted){
                    plugin.getSb().clear(p);
                }
                plugin.getSb().update(p, ge, plugin);
            });
        }
    }
    
    public static String parseBoolean(boolean bool){
        return (bool) ? plugin.getLang().get("activated") : plugin.getLang().get("deactivated");
    }
    
    public static String getFormatedLocation(Location loc){
        if (loc == null){
            return "§cNot set!";
        }
        return loc.getWorld().getName() + ", " + df.format(loc.getX()) + ", " + df.format(loc.getY()) + ", " + df.format(loc.getZ());
    }
    
    public static Location getStringLocation(String location){
        if (location == null) return null;
        String[] l = location.split(";");
        if (l.length < 6) return null;
        World world = Bukkit.getWorld(l[0]);
        double x = Double.parseDouble(l[1]);
        double y = Double.parseDouble(l[2]);
        double z = Double.parseDouble(l[3]);
        float yaw = Float.parseFloat(l[4]);
        float pitch = Float.parseFloat(l[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public static String getLocationString(Location loc){
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }
    
    public static boolean existsFile(String schematic){
        String s = schematic + ".schematic";
        String s2 = schematic + ".schem";
        File file = new File(Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics", s);
        File file2 = new File(Bukkit.getWorldContainer() + "/plugins/FastAsyncWorldEdit/schematics", s2);
        if (plugin.getVc().is1_13to17()){
            return file2.exists();
        }
        return file.exists();
    }
    
    public static String toGson(SWPlayer sw){
        return UltraSkyWarsApi.getGson().toJson(sw, SWPlayer.class);
    }
    
    public static SWPlayer fromGson(String data){
        return UltraSkyWarsApi.getGson().fromJson(data, SWPlayer.class);
    }
    
    public static void setCleanPlayer(Player p){
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        softCleanPlayer(p);
    }
    
    public static void softCleanPlayer(Player p){
        for ( PotionEffect e : p.getActivePotionEffects() ){
            p.removePotionEffect(e.getType());
        }
        p.setFlying(false);
        p.setAllowFlight(false);
        p.setFireTicks(0);
        p.setLevel(0);
        p.setExp(0);
        p.setSaturation(10.0F);
        p.setWalkSpeed(0.2f);
        p.setFlySpeed(0.1f);
        p.setFoodLevel(20);
        p.setMaxHealth(20.0D);
        p.setHealth(20.0D);
        p.setGameMode(GameMode.SURVIVAL);
    }
    
    @SuppressWarnings("deprecation")
    public static Block getBlockFaced(Block b){
        switch(b.getData()){
            case 2:
                return b.getRelative(BlockFace.SOUTH);
            case 3:
                return b.getRelative(BlockFace.NORTH);
            case 4:
                return b.getRelative(BlockFace.EAST);
            case 5:
                return b.getRelative(BlockFace.WEST);
            default:
                return b;
        }
    }
    
    public static int getMaxPages(int size, int maxPerPage){
        return (size / Math.max(maxPerPage, 1)) + 1;
    }
    
    public static Color getRandomColor(){
        Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.AQUA, Color.LIME};
        return colors[ThreadLocalRandom.current().nextInt(0, colors.length)];
    }
    
    public static String getProgressBar(int current, int max){
        float percent = (float) current / max;
        double por = percent * 100;
        return new DecimalFormat("####.#").format(por);
    }
    
    public static String getProgressBar(int current, int max, int totalBars){
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        int leftOver = (totalBars - progressBars);
        StringBuilder sb = new StringBuilder();
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        int a = 0;
        for ( int i = 0; i < progressBars; i++ ){
            if (a >= totalBars){
                break;
            }
            in.append(plugin.getLang().get("progressBar.symbol"));
            a++;
        }
        for ( int i = 0; i < leftOver; i++ ){
            if (a >= totalBars){
                break;
            }
            out.append(plugin.getLang().get("progressBar.symbol"));
            a++;
        }
        sb.append(plugin.getLang().get("progressBar.in"));
        sb.append(in);
        sb.append(plugin.getLang().get("progressBar.out"));
        sb.append(out);
        double por = percent * 100;
        String p = new DecimalFormat("####.#").format(por);
        return plugin.getLang().get("progressBar.finish").replaceAll("<progress>", sb.toString()).replaceAll("<percent>", p);
    }
    
    public static void sendRewards(Player on, GamePlayer pt){
        if (pt == null){
            return;
        }
        int gxp = pt.getXP();
        int gcoins = pt.getCoins();
        int gsouls = pt.getSouls();
        pt.addXP((int) plugin.getMm().getPlayerMultiplier(on, "XP", gxp));
        pt.addCoins((int) plugin.getMm().getPlayerMultiplier(on, "COINS", gcoins));
        pt.addSouls((int) plugin.getMm().getPlayerMultiplier(on, "SOULS", gsouls));
        Multiplier mx = plugin.getMm().getServerMultiplier("XP");
        Multiplier mc = plugin.getMm().getServerMultiplier("COINS");
        Multiplier ms = plugin.getMm().getServerMultiplier("SOULS");
        for ( String s : plugin.getLang().get(on, "messages.summonary").split("\\n") ){
            if (s.contains("<center>")){
                on.sendMessage(CenterMessage.getCenteredMessage(s.replaceAll("<center>", "")));
            } else if (s.contains("<xpGame>")){
                on.sendMessage(plugin.getLang().get(on, "messages.xpGame").replaceAll("<amount>", String.valueOf(gxp)));
            } else if (s.contains("<soulsGame>")){
                on.sendMessage(plugin.getLang().get(on, "messages.soulsGame").replaceAll("<amount>", String.valueOf(gsouls)));
            } else if (s.contains("<coinsGame>")){
                on.sendMessage(plugin.getLang().get(on, "messages.coinsGame").replaceAll("<amount>", String.valueOf(gcoins)));
            } else if (s.contains("<multiplierXP>")){
                if (mx != null){
                    double amount = (mx.getAmount() * gxp) - gxp;
                    on.sendMessage(plugin.getLang().get(on, "messages.xp").replaceAll("<amount>", String.valueOf(amount)).replace("<player>", mx.getName()).replaceAll("<value>", "" + mx.getAmount()));
                    pt.addXP((int) amount);
                }
            } else if (s.contains("<multiplierXPYou>")){
                if (plugin.getMm().getPlayerMultiplier(on, "XP", gxp) > 1){
                    String mxp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "XP"));
                    String xp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "XP", gxp));
                    on.sendMessage(plugin.getLang().get(on, "messages.xpYou").replaceAll("<amount>", xp).replaceAll("<value>", mxp));
                }
            } else if (s.contains("<multiplierSouls>")){
                if (ms != null){
                    double amount = (ms.getAmount() * gsouls) - gsouls;
                    on.sendMessage(plugin.getLang().get(on, "messages.souls").replaceAll("<amount>", String.valueOf(amount)).replace("<player>", ms.getName()).replaceAll("<value>", "" + ms.getAmount()));
                    pt.addSouls((int) amount);
                }
            } else if (s.contains("<multiplierSoulsYou>")){
                if (plugin.getMm().getPlayerMultiplier(on, "SOULS", gsouls) > 1){
                    String mxp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "SOULS"));
                    String xp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "SOULS", gsouls));
                    on.sendMessage(plugin.getLang().get(on, "messages.soulsYou").replaceAll("<amount>", xp).replaceAll("<value>", mxp));
                }
            } else if (s.contains("<multiplierCoins>")){
                if (mc != null){
                    double amount = (mc.getAmount() * gcoins) - gcoins;
                    on.sendMessage(plugin.getLang().get(on, "messages.coins").replaceAll("<amount>", String.valueOf(amount)).replace("<player>", mc.getName()).replaceAll("<value>", "" + mc.getAmount()));
                    pt.addCoins((int) amount);
                }
            } else if (s.contains("<multiplierCoinsYou>")){
                if (plugin.getMm().getPlayerMultiplier(on, "COINS", gcoins) > 1){
                    String mxp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "COINS"));
                    String xp = String.valueOf(plugin.getMm().getPlayerMultiplier(on, "COINS", gcoins));
                    on.sendMessage(plugin.getLang().get(on, "messages.coinsYou").replaceAll("<amount>", xp).replaceAll("<value>", mxp));
                }
            } else {
                on.sendMessage(s.replaceAll("<xp>", "" + pt.getXP())
                        .replaceAll("<coins>", "" + pt.getCoins())
                        .replaceAll("<souls>", "" + pt.getSouls()));
            }
        }
    }
    
    public static ItemStack[] getGifs(){
        return gifs;
    }
    
    public static String formatList(Collection<String> collection){
        return collection.toString().replace("[", "§b").replace("]", "").replaceAll(", ", "§e, §b");
    }
}