package com.equestriworlds.horse.command.vet;

import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.Renderer;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.vet.enums.Disease;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class HorseVetXrayCommand
extends CommandBase<HorseManager> {
    HorseVetXrayCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "xray");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.help("/vet xray", "View the symptoms of the horse on the leash", Rank.VET));
        } else {
            MapView view = Bukkit.createMap((World)caller.getWorld());
            view.getRenderers().clear();
            try {
                view.addRenderer((MapRenderer)new Renderer("xray"));
            }
            catch (IOException e) {
                caller.sendMessage((Object)ChatColor.RED + "Error with rendering the image");
            }
            UtilPlayer.message((Entity)caller, F.help("/vet xray", "You have gathered an xray", Rank.VET));
            ItemStack item = new ItemStack(Material.MAP, 1, view.getId());
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(C.convert("&b&lBroken Bone XRAY"));
            item.setItemMeta(itemMeta);
            caller.getInventory().addItem(new ItemStack[]{item});
        }
    }

    private String getImage(Disease disease) {
        switch (disease) {
            case NAVICULAR: {
                return "xray";
            }
        }
        return "";
    }

}
