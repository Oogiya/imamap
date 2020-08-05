package xyz.oogiya.imamap.util;

import net.minecraft.util.ResourceLocation;

public class Utils {

    //Stuff to make config files easier later on :)

    //Mod settings
    public static final String MOD_ID = "imamap";
    public static final String MOD_NAME = "ImAMap";

    //Map settings
    public static final int SHIFT = 7;
    public static final int MAP_SIZE = 1 << SHIFT; //pixels!
    public static final int CHUNK_AMOUNT = 9;

    //Map Size (visual)
    public static final int MAP_WIDTH = 50;
    public static final int MAP_HEIGHT = 50;
    public static final int HEIGHT_PERCENT = 30;

    //Map Location
    public static final int MAP_X = 25;
    public static final int MAP_Y = 25;
    public static final int MAP_MARGIN_TOP = 10;
    public static final int MAP_MARGIN_BOTTOM = -1;
    public static final int MAP_MARGIN_LEFT = -1;
    public static final int MAP_MARGIN_RIGHT = 10;

    public static final int MAP_PLAYER_SIZE = 10;

    //Images
    public static final ResourceLocation BORDER = new ResourceLocation("imamap", "border_round.png");
    public static final ResourceLocation MARKER = new ResourceLocation("imamap", "arrow_player.png");

}
