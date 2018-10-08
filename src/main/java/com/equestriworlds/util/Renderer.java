/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.map.MapCanvas
 *  org.bukkit.map.MapRenderer
 *  org.bukkit.map.MapView
 */
package com.equestriworlds.util;

import com.equestriworlds.Main;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import javax.imageio.ImageIO;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class Renderer
extends MapRenderer {
    private SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public Renderer(String name) throws IOException {
        this.cacheImage = new SoftReference<BufferedImage>(this.getImage(name));
    }

    public void render(MapView view, MapCanvas canvas, Player player) {
        if (this.hasRendered) {
            return;
        }
        if (this.cacheImage.get() != null) {
            canvas.drawImage(0, 0, (Image)this.cacheImage.get());
            this.hasRendered = true;
        } else {
            player.sendMessage((Object)ChatColor.RED + "Attempted to render the image, but the cached image was null!");
            this.hasRendered = true;
        }
    }

    private BufferedImage getImage(String name) throws IOException {
        boolean useCache = ImageIO.getUseCache();
        ImageIO.setUseCache(false);
        BufferedImage image = ImageIO.read(new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), name + ".png"));
        ImageIO.setUseCache(useCache);
        return image;
    }
}
