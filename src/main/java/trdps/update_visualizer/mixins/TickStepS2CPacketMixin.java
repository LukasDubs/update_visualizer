package trdps.update_visualizer.mixins;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.TickStepS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.render.BoxRenderer;

@Mixin(TickStepS2CPacket.class)
public class TickStepS2CPacketMixin {

    @Inject(method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", at = @At("TAIL"))
    public void onApply(ClientPlayPacketListener p, CallbackInfo info) {
        BoxRenderer.clearStack();
    }

}
