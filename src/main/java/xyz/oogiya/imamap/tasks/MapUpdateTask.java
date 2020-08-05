package xyz.oogiya.imamap.tasks;

import xyz.oogiya.imamap.map.MapRender;

public class MapUpdateTask extends Task {

    final MapRender mapRender;

    public MapUpdateTask(MapRender mapRender) {
        this.mapRender = mapRender;
    }

    @Override
    public void run() {

        this.mapRender.ChunkToPixels();
        this.mapRender.updateTexture();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public boolean checkForDuplicate() {
        return false;
    }
}
