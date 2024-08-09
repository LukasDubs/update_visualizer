package trdps.update_visualizer.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.Update_visualizer;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "loadWorld", at = @At("HEAD"))
    public void onLoadWorld(CallbackInfo ci) {
        Update_visualizer.server = (MinecraftServer) (Object) this;
    }

}
