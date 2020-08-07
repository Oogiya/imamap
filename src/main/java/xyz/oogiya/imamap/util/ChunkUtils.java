package xyz.oogiya.imamap.util;

import net.minecraft.world.chunk.Chunk;

public class ChunkUtils {

    public static Long getChunkId(Chunk chunk) {
        int middleX = (chunk.getPos().getXStart() + chunk.getPos().getXEnd()) / 2 & 0xffff;
        int middleZ = (chunk.getPos().getZStart() + chunk.getPos().getZEnd()) / 2 & 0xffff;
        return Long.valueOf(middleX << 8 | middleX);
    }

}
