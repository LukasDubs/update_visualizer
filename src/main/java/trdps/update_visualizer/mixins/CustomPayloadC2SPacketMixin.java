package trdps.update_visualizer.mixins;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

import java.util.List;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;", ordinal = 0), index = 1)
    private static List<CustomPayload.Type<?, ?>> onCreateCodec(List<CustomPayload.Type<?, ?>> types) {
        types.add(new CustomPayload.Type<PacketByteBuf, UpdateVisualizerHelloPacket>(UpdateVisualizerHelloPacket.ID, UpdateVisualizerHelloPacket.CODEC)); // somehow has no effect?
        return types;
    }

}
