package trdps.update_visualizer.mixins;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.UpdateTickRateS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.render.BoxRenderer;

@Mixin(UpdateTickRateS2CPacket.class)
public class UpdateTickRateS2CPacketMixin {

    @Shadow
    @Final
    boolean isFrozen;

    @Inject(method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", at = @At("TAIL"))
    public void onApply(ClientPlayPacketListener p, CallbackInfo info) {
        if(!isFrozen) {
            BoxRenderer.clearStack();
        }
    }

}
