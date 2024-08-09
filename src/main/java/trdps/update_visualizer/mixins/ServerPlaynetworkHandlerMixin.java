package trdps.update_visualizer.mixins;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlaynetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    public void onOnCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        CustomPayload pl = packet.payload();
        if(pl instanceof UpdateVisualizerHelloPacket hp) {
            if(hp.version() == 0x234) {
                Update_visualizer.update_visualizerPlayers.add(player);
            }
            ci.cancel();
        }
    }

}
