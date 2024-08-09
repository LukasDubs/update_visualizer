package trdps.update_visualizer.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.llamalad7.mixinextras.sugar.Local;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.render.BoxRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    MinecraftClient client;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 1) Matrix4f matrix4f2, @Local MatrixStack matrixStack) {
        client.getProfiler().swap(Update_visualizer.MODID+"_render");

        RenderSystem.getModelViewStack().pushMatrix().mul(matrix4f2);
        BoxRenderer.onRender(matrixStack);
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.applyModelViewMatrix();

    }

}
