package trdps.update_visualizer.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.client.Update_visualizerClient;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    protected ClientPlayNetworkHandlerMixin(final MinecraftClient client, final ClientConnection connection, final ClientConnectionState state) {
        super(client, connection, state);
    }

    @Inject(method = "warnOnUnknownPayload", at = @At("HEAD"), cancellable = true)
    private void onWarnOnUnknownPayload(CustomPayload payload, CallbackInfo ci){
        if(payload instanceof UpdateVisualizerHelloPacket hello) {
            if(hello.version() == 0x123) {
                Update_visualizerClient.LOGGER.info("Registered Update Visualizer mod on server");
                Update_visualizerClient.mc.getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(new UpdateVisualizerHelloPacket(0x234)));
                ci.cancel();
            }
        }
    }

}
