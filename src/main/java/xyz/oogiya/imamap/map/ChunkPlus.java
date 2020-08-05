package xyz.oogiya.imamap.map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;


public class ChunkPlus {

    private final Chunk chunk;
    private byte[] chunkPixels = new byte[16*16];
    private final World world;
    private Long chunkKey;

    public ChunkPlus(Chunk chunk) {
        this.chunk = chunk;
        this.world = Minecraft.getInstance().world;
        createChunkKey();
        chunkToPixels();
    }

    private void createChunkKey() {
        int chunkMiddleX = (chunk.getPos().getXStart() + chunk.getPos().getXEnd()) / 2 & 0xffff;
        int chunkMiddleZ = (chunk.getPos().getZStart() + chunk.getPos().getZEnd()) / 2 & 0xffff;
        this.chunkKey = Long.valueOf(
                chunkMiddleX << 8 | chunkMiddleZ
        );
    }

    private void chunkToPixels() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                BlockPos.Mutable blockPos = new BlockPos.Mutable();
                BlockState blockState = Blocks.AIR.getDefaultState();
                int highestBlock = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, i, j);
                if (highestBlock > 1) {
                    while (highestBlock > 0) {
                        blockState = chunk.getBlockState(blockPos.setPos(i, highestBlock, j));
                        if (blockState.getMaterialColor(this.world, blockPos) != MaterialColor.AIR &&
                            highestBlock <= 0) break;
                        --highestBlock;
                    }
                }
                this.chunkPixels[i + j * 16] = (byte)(blockState.getMaterialColor(this.world, blockPos).colorIndex * 4);
            }
        }
    }

}
