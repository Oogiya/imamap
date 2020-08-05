package xyz.oogiya.imamap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.oogiya.imamap.forge.ForgeEvents;
import xyz.oogiya.imamap.map.MapRender;
import xyz.oogiya.imamap.map.MapSettings;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("imamap")
public class ImAMap
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private final MapSettings mapSettings;



    public ImAMap() {
        this.mapSettings = new MapSettings(Minecraft.getInstance());
        MinecraftForge.EVENT_BUS.register(new ForgeEvents(new MapRender(this.mapSettings)));

    }

}
