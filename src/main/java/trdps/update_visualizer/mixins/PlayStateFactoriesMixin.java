package trdps.update_visualizer.mixins;

import net.minecraft.network.NetworkStateBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.state.PlayStateFactories;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.packet.ScheduledBlockUpdatePacket;

@Mixin(PlayStateFactories.class)
public class PlayStateFactoriesMixin {

    @Inject(method = "method_55958", at = @At("TAIL"))
    private static void onBuild(NetworkStateBuilder<ClientPlayPacketListener, RegistryByteBuf> builder, CallbackInfo ci) {
        Update_visualizer.LOGGER.info("Adding packet!");
        builder.add(ScheduledBlockUpdatePacket.PACKET_TYPE, ScheduledBlockUpdatePacket.CODEC);
    }

}
