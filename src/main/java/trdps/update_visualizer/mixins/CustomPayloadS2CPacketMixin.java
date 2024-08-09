package trdps.update_visualizer.mixins;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

import java.util.ArrayList;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;", ordinal = 0), index = 0)
    private static Object onCreateCodec(Object list) {
        if(list instanceof ArrayList<?>) {
            Update_visualizer.LOGGER.info("adding packet to list");
            @SuppressWarnings("unchecked")
            ArrayList<CustomPayload.Type> list1 = (ArrayList<CustomPayload.Type>) list;
            list1.add(new CustomPayload.Type<>(UpdateVisualizerHelloPacket.ID, UpdateVisualizerHelloPacket.CODEC));
            for(CustomPayload.Type t : list1) {
                Update_visualizer.LOGGER.info("Type:{}", t.id());
            }
            return list1;
        }
        return list;
    }

}
