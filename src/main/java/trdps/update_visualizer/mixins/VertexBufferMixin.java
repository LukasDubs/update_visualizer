package trdps.update_visualizer.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BuiltBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import trdps.update_visualizer.render.BoxRenderer;

import java.nio.ByteBuffer;

@Mixin(VertexBuffer.class)
public abstract class VertexBufferMixin {
    @Shadow
    private int indexBufferId;

    @Inject(method = "uploadIndexBuffer(Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;Ljava/nio/ByteBuffer;)Lcom/mojang/blaze3d/systems/RenderSystem$ShapeIndexBuffer;", at = @At("RETURN"))
    private void onConfigureIndexBuffer(BuiltBuffer.DrawParameters parameters, ByteBuffer indexBuffer, CallbackInfoReturnable<RenderSystem.ShapeIndexBuffer> info) {
        if (info.getReturnValue() == null) BoxRenderer.CURRENT_IBO = this.indexBufferId;
        else BoxRenderer.CURRENT_IBO = ((ShapeIndexBufferAccessor)((Object) info.getReturnValue())).getId();
    }
}
