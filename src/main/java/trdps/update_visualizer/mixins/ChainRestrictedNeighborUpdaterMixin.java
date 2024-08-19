package trdps.update_visualizer.mixins;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.ServerNetworkHandler;
import trdps.update_visualizer.network.packet.ScheduledBlockUpdatePacket;

import java.awt.*;
import java.util.ArrayList;

import static trdps.update_visualizer.network.packet.ScheduledBlockUpdatePacket.UpdateData;

@Mixin(ChainRestrictedNeighborUpdater.class)
public class ChainRestrictedNeighborUpdaterMixin {

    @Unique
    private final ArrayList<UpdateData> objs = new ArrayList<>();

    @Inject(method = "enqueue", at = @At("HEAD"))
    private void onEnqueue(BlockPos pos, ChainRestrictedNeighborUpdater.Entry entry, CallbackInfo ci) {
        if(Update_visualizer.server == null || !Update_visualizer.server.getTickManager().isFrozen()) return;
        switch (entry) {
            case ChainRestrictedNeighborUpdater.StateReplacementEntry ignored ->
                    objs.add(new UpdateData(pos, Color.RED));
            case ChainRestrictedNeighborUpdater.SimpleEntry ignored ->
                    objs.add(new UpdateData(pos, Color.GREEN));
            case ChainRestrictedNeighborUpdater.StatefulEntry ignored ->
                    objs.add(new UpdateData(pos, Color.BLUE));
            case ChainRestrictedNeighborUpdater.SixWayEntry ignored ->
                    objs.add(new UpdateData(pos, Color.MAGENTA));
            case null, default ->
                    Update_visualizer.LOGGER.warn("Unknown update entry found:{}", entry);
        }
    }

    @Inject(method = "runQueuedUpdates", at = @At("TAIL"))
    private void onRunQueuedUpdates(CallbackInfo ci) {
        UpdateData[] data = new UpdateData[objs.size()];
        ServerNetworkHandler.sendAllValid(new ScheduledBlockUpdatePacket(objs.toArray(data)));
        objs.clear();
    }

}
