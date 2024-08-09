package trdps.update_visualizer.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trdps.update_visualizer.render.BoxRenderer;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "finishInitialization", at = @At("HEAD"), remap = false)
    private static void onInit(CallbackInfo ci){
        BoxRenderer.init();
    }

}
