package xyz.oogiya.imamap.map;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.NetherBiome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.client.gui.GuiUtils;
import xyz.oogiya.imamap.tasks.BackgroundExecutor;
import xyz.oogiya.imamap.tasks.MapUpdateTask;
import xyz.oogiya.imamap.util.Render;
import xyz.oogiya.imamap.util.Utils;

public class MapRender {

    private final MapSettings mapSettings;

    private static byte[] colorBytes = new byte[Utils.MAP_SIZE * Utils.MAP_SIZE];

    private DynamicTexture dynamicTexture;

    private final BackgroundExecutor backgroundExecutor;

    private final NativeImage bufferedImage;


    //TODO
    private void drawCoordinates() {}

    private void colorByHeight() {}
    ////////////////////////////////



    public MapRender(MapSettings mapSettings) {
        this.mapSettings = mapSettings;
        this.bufferedImage = new NativeImage(NativeImage.PixelFormat.RGBA, Utils.MAP_SIZE, Utils.MAP_SIZE, true);
        this.backgroundExecutor = new BackgroundExecutor();
    }

    public void drawMap() {

        int playerSize = Utils.MAP_PLAYER_SIZE;
        this.mapSettings.setResolution();
        this.backgroundExecutor.addTask(new MapUpdateTask(this));
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        this.drawBorder();
        this.drawTexturedMap();
        this.drawPlayer();
        GlStateManager.popMatrix();
    }


    private void drawTexturedMap() {
        double rad = Math.sqrt((this.mapSettings.getScreenWidth() * this.mapSettings.getScreenHeight())/Math.PI);
        GlStateManager.pushMatrix();
        float playerRotation = this.mapSettings.getMinecraft().player.rotationYaw;
        GlStateManager.translated(this.mapSettings.getXTranslation(), this.mapSettings.getYTranslation(), -1500);
        this.dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.mapSettings.getMinecraft().getTextureManager().bindTexture(
                this.mapSettings.getMinecraft().getTextureManager().getDynamicTextureLocation("imamap", this.dynamicTexture));
        Render.drawCircle(
                (float)(this.mapSettings.getMapX() / 0.75),
                (float)(this.mapSettings.getMapY() / 0.75),
                (float)(Math.sqrt((this.mapSettings.getScreenWidth() * this.mapSettings.getScreenHeight())/Math.PI) * 0.88),
                180 - playerRotation);
        this.drawCompass(-3, -3, (float)(rad), (90 - playerRotation));
        GlStateManager.popMatrix();
    }

    private void drawCompass(int x, int y, float radius, float rotation) {
        GlStateManager.pushMatrix();
        String[] directions = new String[]{"N", "E", "S", "W"};

        for(int i = 0; i < 4; i++){
            int xPos = (int) (MathHelper.cos((float) Math.toRadians(rotation + (i * 90))) * radius);
            int yPos = (int) (MathHelper.sin((float) Math.toRadians(rotation + (i * 90))) * radius);

            this.mapSettings.getMinecraft().fontRenderer.drawStringWithShadow(directions[i], xPos + x, yPos + y, 0xFFFFFFFF);
        }
        GlStateManager.popMatrix();
    }

    private void drawPlayer() {
        GlStateManager.pushMatrix();
        int playerSize = Utils.MAP_PLAYER_SIZE;
        GlStateManager.translated(this.mapSettings.getXTranslation() - (playerSize/2),
                this.mapSettings.getYTranslation() - (playerSize/2), -1000);
        this.mapSettings.getMinecraft().getTextureManager().bindTexture(Utils.MARKER);
        Render.drawTexturedRect(0,
                0,
                playerSize,
                playerSize,
                0, 0, 1, 1);
        GlStateManager.popMatrix();
    }

    private void drawBorder() {
        GlStateManager.pushMatrix();
        double rad = Math.sqrt((this.mapSettings.getScreenWidth() * this.mapSettings.getScreenHeight())/Math.PI);
        GlStateManager.translated(this.mapSettings.getXTranslation(), this.mapSettings.getYTranslation(), -2000);
        this.mapSettings.getMinecraft().getTextureManager().bindTexture(Utils.BORDER);
        Render.drawCircle((float)(this.mapSettings.getMapX()), (float)(this.mapSettings.getMapY()),
                (float)(rad / 0.85), 0);
        GlStateManager.popMatrix();
    }

    public void updateTexture() {

        for (int i = 0; i < Utils.MAP_SIZE; i++) {
            for (int j = 0; j < Utils.MAP_SIZE; j++) {
                int p = this.colorBytes[i + j * Utils.MAP_SIZE] & 255;

                if (p / 4 == 0) {
                    this.bufferedImage.setPixelRGBA(i, j, ((i + j) + (i + j)/ Utils.MAP_SIZE & 1) * 8 + 16 << 24);
                } else {
                    this.bufferedImage.setPixelRGBA(i, j, MaterialColor.COLORS[p / 4].getMapColor(p & 3));
                }
            }
        }
    }

    public void ChunkToPixels() {
        int playerX = (int)Math.round(this.mapSettings.getPlayerX());
        int playerZ = (int)Math.round(this.mapSettings.getPlayerZ());

        int mapSize = Utils.MAP_SIZE;
        World world = this.mapSettings.getMinecraft().world;


        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                BlockPos.Mutable blockPos = new BlockPos.Mutable();
                Chunk chunk = (Chunk) this.mapSettings.getMinecraft().world.getChunk(new BlockPos((playerX + i - mapSize/2),
                        0, (playerZ + j - mapSize/2)));
                BlockState blockState = Blocks.AIR.getDefaultState();
                if (!chunk.isEmpty()) {
                    if (world.getDimension().isNether()) {
                        int lowestBlock = 1;
                        while(lowestBlock < 128) {
                            blockState = chunk.getBlockState(blockPos.setPos((playerX + i - (mapSize/2)) & 15,
                                    lowestBlock + 1, ((playerZ + j - (mapSize/2)) & 15)));
                            if (blockState.getBlock().equals(Blocks.AIR) || blockState.equals(Blocks.CAVE_AIR) ||
                                    blockState.equals(Blocks.VOID_AIR)) {
                                blockState = chunk.getBlockState(blockPos.setPos((playerX + i - (mapSize/2)) & 15,
                                        lowestBlock, ((playerZ + j - (mapSize/2)) & 15))); break;
                            }
                            ++lowestBlock;
                        }
                    } else {
                        int highestBlock = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, ((playerX + i - (mapSize/2)) & 15),
                                ((playerZ + j - (mapSize/2)) & 15));
                        if (highestBlock > 1) {

                            while (highestBlock > 0 && highestBlock < mapSize) {
                                blockState = chunk.getBlockState(blockPos.setPos(((playerX + i - (mapSize/2)) & 15),
                                        highestBlock, ((playerZ + j - (mapSize/2)) & 15)));

                                if(!(blockState.getBlock() instanceof ShearableDoublePlantBlock) &&
                                        !(blockState.getBlock() instanceof CropsBlock) &&
                                        !(blockState.getBlock() instanceof BushBlock)) {
                                    if (blockState.getMaterialColor(world, blockPos) != MaterialColor.AIR || highestBlock <= 0) break;
                                }

                                highestBlock--;
                            }
                        }
                    }

                    this.colorBytes[i + j * Utils.MAP_SIZE] = (byte)(blockState.getMaterialColor(this.mapSettings.getMinecraft().world, blockPos).colorIndex * 4);
                }
            }
        }
    }

}
