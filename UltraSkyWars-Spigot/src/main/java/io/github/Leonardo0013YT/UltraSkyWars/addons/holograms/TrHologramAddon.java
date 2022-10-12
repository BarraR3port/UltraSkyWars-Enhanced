/*REMOVED DUE TO REPO SERVER DOWN
package io.github.Leonardo0013YT.UltraSkyWars.addons.holograms;


import io.github.Leonardo0013YT.UltraSkyWars.api.interfaces.HologramAddon;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TrHologramAddon implements HologramAddon {
    
    private HashMap<Location, Hologram> holograms = new HashMap<>();
    
    @Override
    public void createHologram(Location spawn, List<String> lines){
        Location loc = spawn.clone();
        Hologram h = TrHologramAPI.createHologram(UltraSkyWarsApi.get(), String.valueOf(spawn), loc.clone().add(0, 1.3 + (lines.size() * 0.3), 0), lines);
        holograms.put(spawn, h);
    }
    
    @Override
    public void createHologram(Location spawn, List<String> lines, ItemStack item){
        Location loc = spawn.clone();
        Hologram h = TrHologramAPI.createHologram(UltraSkyWarsApi.get(), String.valueOf(spawn), loc.clone().add(0, 1.3 + (lines.size() * 0.3), 0), lines);
        holograms.put(spawn, h);
    }
    
    @Override
    public void deleteHologram(Location id){
        if (holograms.containsKey(id)){
            holograms.get(id).delete();
            holograms.remove(id);
        }
    }
    
    @Override
    public boolean hasHologram(Location id){
        return holograms.containsKey(id);
    }
    
    @Override
    public void remove(){
        for ( Hologram h : holograms.values() ){
            h.destroyAll();
            h.delete();
        }
        holograms.clear();
    }
    
}*/
