/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
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
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class RendererBACKUP
extends MapRenderer {
    private boolean hasRendered;
    private int value;
    private HashMap<Integer, BufferedImage> renderedImages = new HashMap();

    public RendererBACKUP(int value) {
        if (value == 0) {
            try {
                this.renderedImages.put(1, ImageIO.read(new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "xray.png")));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        this.value = value;
    }

    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (this.hasRendered) {
            return;
        }
        try {
            mapCanvas.drawImage(0, 0, (Image)ImageIO.read(new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "xray.png")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.hasRendered = true;
    }
}
