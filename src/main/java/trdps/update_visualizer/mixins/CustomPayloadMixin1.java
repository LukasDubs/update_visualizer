package trdps.update_visualizer.mixins;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

import java.util.Map;

@Mixin(targets = "net.minecraft.network.packet.CustomPayload$1")
public class CustomPayloadMixin1 {

    @Shadow
    @Final
    Map<Identifier, PacketCodec<?, ?>> field_48658;

    @Shadow
    @Final
    CustomPayload.CodecFactory<?> field_48659;

    @Inject(method = "getCodec(Lnet/minecraft/util/Identifier;)Lnet/minecraft/network/codec/PacketCodec;", at = @At("HEAD"), cancellable = true)
    private void onGetCodec(Identifier id, CallbackInfoReturnable<PacketCodec<?, ?>> cir) {
        PacketCodec<?, ?> fct = field_48658.get(id);
        if(fct != null)
            cir.setReturnValue(fct);
        else {
            if (id.compareTo(UpdateVisualizerHelloPacket.ID.id()) == 0) {
                Object obj = field_48658.get(UpdateVisualizerHelloPacket.ID.id());
                if(obj != null) {
                    Update_visualizer.LOGGER.warn("BRUH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                cir.setReturnValue(UpdateVisualizerHelloPacket.CODEC);
            } else
                cir.setReturnValue(field_48659.create(id));
        }
    }

}
