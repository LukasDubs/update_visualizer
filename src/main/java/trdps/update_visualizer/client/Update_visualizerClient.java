package trdps.update_visualizer.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Update_visualizerClient implements ClientModInitializer {

    public static MinecraftClient mc;
    public static final Logger LOGGER = LoggerFactory.getLogger(Update_visualizerClient.class);

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
    }
}
