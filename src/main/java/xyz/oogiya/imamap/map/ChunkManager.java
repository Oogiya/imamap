package xyz.oogiya.imamap.map;

import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {

    private Map<Long, Chunk> chunkMap;

    public ChunkManager() {
        this.chunkMap = new HashMap<Long, Chunk>();
    }

    public void insertChunk(Chunk chunk) {
        int chunkMiddleX = (chunk.getPos().getXStart() + chunk.getPos().getXEnd()) / 2 & 0xffff;
        int chunkMiddleZ = (chunk.getPos().getZStart() + chunk.getPos().getXEnd()) / 2 & 0xffff;
        this.chunkMap.put(Long.valueOf(
                chunkMiddleZ << 16 | chunkMiddleX
        ), chunk);
    }

}
