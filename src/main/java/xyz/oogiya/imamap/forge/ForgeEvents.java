package xyz.oogiya.imamap.forge;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.oogiya.imamap.map.MapRender;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    private final MapRender mapRender;

    public ForgeEvents(MapRender mapRender) {
        this.mapRender = mapRender;
    }


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (this.mapRender != null) {
                mapRender.drawMap();
            }
        }
    }

}
