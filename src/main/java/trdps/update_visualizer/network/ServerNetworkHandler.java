package trdps.update_visualizer.network;

import net.minecraft.server.network.ServerPlayerEntity;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.packet.ScheduledBlockUpdatePacket;

public class ServerNetworkHandler {

    public static void sendAllValid(ScheduledBlockUpdatePacket packet) {
        for(ServerPlayerEntity player : Update_visualizer.update_visualizerPlayers) {
            player.networkHandler.sendPacket(packet);
        }
    }

}
