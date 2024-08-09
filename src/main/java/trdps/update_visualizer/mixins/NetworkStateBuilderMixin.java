package trdps.update_visualizer.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.*;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import trdps.update_visualizer.network.packet.UpdateVisualizerHelloPacket;

import java.util.List;

@Mixin(NetworkStateBuilder.class)
public class NetworkStateBuilderMixin {

    /*
    @Inject(method = "buildFactory", at = @At(value = "INVOKE", target = "Ljava/util/List;copyOf(Ljava/util/Collection;)Ljava/util/List;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void onBuildFactory(CallbackInfoReturnable<NetworkState.Factory<?, ?> > cir, @Local List<PacketType<?>> list) {
        list.add(new NetworkStateBuilder.PacketType<PacketListener, Packet<PacketListener>, RegistryByteBuf>(new PacketType(NetworkSide.CLIENTBOUND ,UpdateVisualizerHelloPacket.ID.id()), UpdateVisualizerHelloPacket.CODEC));
    }
     */

}
