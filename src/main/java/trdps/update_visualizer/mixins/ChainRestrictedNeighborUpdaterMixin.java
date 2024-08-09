package trdps.update_visualizer.mixins;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.ServerNetworkHandler;
import trdps.update_visualizer.network.packet.ScheduledBlockUpdatePacket;

import java.awt.*;

@Mixin(ChainRestrictedNeighborUpdater.class)
public class ChainRestrictedNeighborUpdaterMixin {

    @Inject(method = "enqueue", at = @At("HEAD"))
    private void onEnqueue(BlockPos pos, ChainRestrictedNeighborUpdater.Entry entry, CallbackInfo ci) {
        if(Update_visualizer.server == null || !Update_visualizer.server.getTickManager().isFrozen()) return;
        if(entry instanceof ChainRestrictedNeighborUpdater.StateReplacementEntry) {
            ServerNetworkHandler.sendAllValid(new ScheduledBlockUpdatePacket(pos, Color.RED));
        } else if(entry instanceof ChainRestrictedNeighborUpdater.SimpleEntry) {
            ServerNetworkHandler.sendAllValid(new ScheduledBlockUpdatePacket(pos, Color.GREEN));
        } else if(entry instanceof ChainRestrictedNeighborUpdater.StatefulEntry) {
            ServerNetworkHandler.sendAllValid(new ScheduledBlockUpdatePacket(pos, Color.BLUE));
        } else if(entry instanceof ChainRestrictedNeighborUpdater.SixWayEntry) {
            ServerNetworkHandler.sendAllValid(new ScheduledBlockUpdatePacket(pos, Color.MAGENTA));
        } else {
            Update_visualizer.LOGGER.warn("Unknown update entry found:"+((Object)entry).toString());
        }
    }

}
