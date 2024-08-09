package trdps.update_visualizer;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Update_visualizer implements ModInitializer {

    public static final String MODID = "update_visualizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MinecraftServer.class);
    public static final List<ServerPlayerEntity> update_visualizerPlayers = new ArrayList<>();
    public static MinecraftServer server;
    @Override
    public void onInitialize() {

    }
}
