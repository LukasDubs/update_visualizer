package trdps.update_visualizer.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4fStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32C;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.client.Update_visualizerClient;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.lwjgl.system.MemoryUtil.*;

public class BoxRenderer {

    public static int CURRENT_IBO = 0;
    private static int vao = 0;
    private static int vbo = 0;
    private static int ibo = 0;
    private static int indices = 0;
    private static int vertices = 0;
    private static ByteBuffer vertex_buf;
    private static ByteBuffer index_buf;
    private static int id = 0;
    private static int matProj = -1;
    private static int matModel = -1;
    private static long vb_start = 0;
    private static long vb_current= 0;
    private static long ib_start = 0;
    private static long ib_current = 0;
    private static boolean rebind = true;

    private static FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    public static void init() {

        vao = GlStateManager._glGenVertexArrays();
        vbo = GlStateManager._glGenBuffers();
        ibo = GlStateManager._glGenBuffers();

        GlStateManager._glBindVertexArray(vao);
        GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, vbo);
        GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GlStateManager._enableVertexAttribArray(0);
        GlStateManager._vertexAttribPointer(0, 3, GL32C.GL_FLOAT, false, 16, 0);
        GlStateManager._enableVertexAttribArray(1);
        GlStateManager._vertexAttribPointer(1, 4, GL32C.GL_UNSIGNED_BYTE, false, 16, 12);

        GlStateManager._glBindVertexArray(0);
        GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, 0);
        GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, CURRENT_IBO);

        vertex_buf = BufferUtils.createByteBuffer(0x10000);
        vb_start = memAddress0(vertex_buf);
        vb_current = vb_start;

        index_buf = BufferUtils.createByteBuffer(0x10000);
        ib_start = memAddress0(index_buf);
        ib_current = ib_start;

        String frag_s;
        String vert_s;
        try {
            frag_s = IOUtils.toString(BoxRenderer.class.getResourceAsStream("/assets/"+Update_visualizer.MODID+"/shaders/simple.frag"), StandardCharsets.UTF_8);
            vert_s = IOUtils.toString(BoxRenderer.class.getResourceAsStream("/assets/"+Update_visualizer.MODID+"/shaders/simple.vert"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int vert = GlStateManager.glCreateShader(GL32C.GL_VERTEX_SHADER);
        GlStateManager.glShaderSource(vert, List.of(vert_s));
        GlStateManager.glCompileShader(vert);
        if(GlStateManager.glGetShaderi(vert, GL32C.GL_COMPILE_STATUS) == GL32C.GL_FALSE) {
            throw new RuntimeException("Error compiling vertex shader:\n"+GlStateManager.glGetShaderInfoLog(vert, 512));
        }

        int frag = GlStateManager.glCreateShader(GL32C.GL_FRAGMENT_SHADER);
        GlStateManager.glShaderSource(frag, List.of(frag_s));
        GlStateManager.glCompileShader(frag);
        if(GlStateManager.glGetShaderi(frag, GL32C.GL_COMPILE_STATUS) == GL32C.GL_FALSE) {
            throw new RuntimeException("Error compiling fragment shader:\n"+GlStateManager.glGetShaderInfoLog(frag, 512));
        }

        id = GlStateManager.glCreateProgram();
        GlStateManager.glAttachShader(id, vert);
        GlStateManager.glAttachShader(id, frag);
        GlStateManager.glLinkProgram(id);

        if(GlStateManager.glGetProgrami(id, GL32C.GL_LINK_STATUS) == GL32C.GL_FALSE) {
            throw new RuntimeException("error linking program:\n"+GlStateManager.glGetProgramInfoLog(id, 512));
        }

        matProj = GlStateManager._glGetUniformLocation(id, "matProj");
        matModel = GlStateManager._glGetUniformLocation(id, "matModel");

        GlStateManager.glDeleteShader(vert);
        GlStateManager.glDeleteShader(frag);

    }

    private static synchronized void putVertex(Vec3d v, Color c) {
        memPutFloat(vb_current, (float)v.x);
        memPutFloat(vb_current + 4, (float)v.y);
        memPutFloat(vb_current + 8, (float)v.z);
        memPutByte(vb_current + 12, (byte)c.getRed());
        memPutByte(vb_current + 13, (byte)c.getGreen());
        memPutByte(vb_current + 14, (byte)c.getBlue());
        memPutByte(vb_current + 15, (byte)c.getAlpha());
        vb_current += 16;
    }

    private static synchronized void putIndices(int[] inds) {
        for(int i : inds) {
            memPutInt(ib_current, i);
            ib_current += 4;
        }
    }

    private static synchronized void checkVBExpansion(int length){
        long b_len = vb_current - vb_start;
        if(vertex_buf.capacity() < b_len + length) {
            ByteBuffer ni = BufferUtils.createByteBuffer(vertex_buf.capacity() + 0x1000);
            long ni_s = memAddress0(ni);
            memCopy(vb_start, ni_s, b_len);
            vertex_buf = ni;
            vb_start = memAddress0(vertex_buf);
            vb_current = vb_start + b_len;
        }
    }

    private static synchronized void checkIBExpansion(int length){
        long b_len = ib_current - ib_start;
        if(index_buf.capacity() < b_len + length) {
            ByteBuffer ni = BufferUtils.createByteBuffer(index_buf.capacity() + 0x1000);
            long ni_s = memAddress0(ni);
            memCopy(ib_start, ni_s, b_len);
            index_buf = ni;
            ib_start = ni_s;
            ib_current = ib_start + b_len;
        }
    }

    public static synchronized void putLine(Vec3d p1, Vec3d p2, Color color) {

        checkVBExpansion(32);

        putVertex(p1, color);
        putVertex(p2, color);

        checkIBExpansion(8);

        putIndices(new int[]{vertices, vertices + 1});

        indices += 2;
        vertices += 2;

        rebind = true;

    }

    public static synchronized void putBox(Vec3i pos, Color c) {
        Vec3d p = Vec3d.of(pos);

        checkVBExpansion(8 * 32);

        putVertex(p, c); // 0
        putVertex(p.add(1.0, 0.0, 0.0), c); // 1
        putVertex(p.add(1.0, 0.0, 1.0), c); // 2
        putVertex(p.add(0.0, 0.0, 1.0), c); // 3
        putVertex(p.add(0.0, 1.0, 0.0), c); // 4
        putVertex(p.add(1.0, 1.0, 0.0), c); // 5
        putVertex(p.add(1.0, 1.0, 1.0), c); // 6
        putVertex(p.add(0.0, 1.0, 1.0), c); // 7

        checkIBExpansion(12  * 8);

        putIndices(new int[]{
                vertices, vertices + 1,
                vertices, vertices + 3,
                vertices, vertices + 4,
                vertices + 1, vertices + 2,
                vertices + 1, vertices + 5,
                vertices + 2, vertices + 3,
                vertices + 2, vertices + 6,
                vertices + 3, vertices + 7,
                vertices + 4, vertices + 5,
                vertices + 4, vertices + 7,
                vertices + 5, vertices + 6,
                vertices + 6, vertices + 7
        });

        vertices += 8;
        indices += 24;

        rebind = true;

    }

    public static void onRender(MatrixStack stack) {

        if(indices <= 0) return;

        GlStateManager._disableDepthTest();

        Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushMatrix();
        if(stack != null) matrixStack.mul(stack.peek().getPositionMatrix());
        Vec3d cameraPos = Update_visualizerClient.mc.gameRenderer.getCamera().getPos();
        matrixStack.translate((float)-cameraPos.x, (float) -cameraPos.y, (float)-cameraPos.z);

        if(rebind) {

            GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, vbo);
            GlStateManager._glBufferData(GL32C.GL_ARRAY_BUFFER, vertex_buf.limit(vertices * 16).position(0), GL32C.GL_STATIC_DRAW);
            GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, 0);

            GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, ibo);
            GlStateManager._glBufferData(GL32C.GL_ELEMENT_ARRAY_BUFFER, index_buf.limit(indices * 4).position(0), GL32C.GL_STATIC_DRAW);
            GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, CURRENT_IBO);

            rebind = false;

        }

        GlStateManager._glUseProgram(id);

        fb = RenderSystem.getProjectionMatrix().get(fb);
        GlStateManager._glUniformMatrix4(matProj, false, fb);

        fb = RenderSystem.getModelViewStack().get(fb);
        GlStateManager._glUniformMatrix4(matModel, false, fb);

        GL32C.glEnable(GL32C.GL_LINE_SMOOTH);
        GL32C.glLineWidth(1);

        GlStateManager._glBindVertexArray(vao);
        GlStateManager._drawElements(GL32C.GL_LINES, indices, GL32C.GL_UNSIGNED_INT, 0);
        GlStateManager._glBindVertexArray(0);

        RenderSystem.getModelViewStack().popMatrix();

    }

    public static void clearStack() {
        indices = 0;
        vertices = 0;
        vb_current = vb_start;
        ib_current = ib_start;
        rebind = true;
    }

}
