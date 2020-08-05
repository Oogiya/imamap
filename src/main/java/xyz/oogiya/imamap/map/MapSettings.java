package xyz.oogiya.imamap.map;

import net.minecraft.client.Minecraft;
import xyz.oogiya.imamap.util.Utils;

public class MapSettings {

    private final Minecraft mc;

    //Player's minecraft resolution (Scaled)
    //position
    private int mapX = Utils.MAP_X;
    private int mapY = Utils.MAP_Y;
    //Scale
    private int screenWidth;
    private int screenHeight;

    private int scaledScreenWidth = Utils.MAP_WIDTH;
    private int scaledScreenHeight = Utils.MAP_HEIGHT;

    private int xTranslation;
    private int yTranslation;

    public MapSettings(Minecraft mc) {
        this.mc = mc;
        setResolution();
    }

    public Minecraft getMinecraft() {
        return this.mc;
    }

    public void setResolution() {

        if (mc.getMainWindow() != null) {
            this.scaledScreenWidth = mc.getMainWindow().getScaledWidth();
            this.scaledScreenHeight = mc.getMainWindow().getScaledHeight();

            updateResolution();
        }
    }

    private void updateResolution() {
        int size = (this.scaledScreenHeight * Utils.HEIGHT_PERCENT) / 100;
        int x, y;

        if (Utils.MAP_MARGIN_LEFT >= 0) {
            x = Utils.MAP_MARGIN_LEFT;
            this.screenWidth = size;
        } else if (Utils.MAP_MARGIN_RIGHT >= 0) {
            x = this.scaledScreenWidth - size - Utils.MAP_MARGIN_RIGHT;
            this.screenWidth = size;
        }

        if (Utils.MAP_MARGIN_TOP >= 0) {
            y = Utils.MAP_MARGIN_TOP;
            this.screenHeight = size;
        } else if (Utils.MAP_MARGIN_BOTTOM >= 0) {
            y = this.scaledScreenHeight - size - Utils.MAP_MARGIN_BOTTOM;
            this.screenHeight = size;
        }

        this.screenWidth &= -2;
        this.screenHeight &= -2;

        this.mapX = -(this.screenWidth >> 1);
        this.mapY = -(this.screenHeight >> 1);

        this.xTranslation = x + (this.screenWidth >> 1);
        this.yTranslation = y + (this.screenHeight >> 1);

    }

    // Getters
    public double getPlayerX() {
        return this.mc.player.getPosX();
    }

    public double getPlayerZ() {
        return this.mc.player.getPosZ();
    }

    public int getYTranslation() { return this.yTranslation; }

    public int getXTranslation() { return this.xTranslation; }

    public int getScreenWidth() { return this.screenWidth; }

    public int getScreenHeight() { return this.screenHeight; }

    public int getMapY() { return this.mapY; }

    public int getMapX() { return this.mapX; }

}
