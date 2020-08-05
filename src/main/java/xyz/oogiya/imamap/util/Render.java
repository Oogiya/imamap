package xyz.oogiya.imamap.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class Render {

    private final static double zDepth = 0.0D;
    public static final double circleSteps = 15.0;

    public static void drawCircle(float x, float y, float radius, float rotation){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.rotatef(rotation, 0, 0, 1);
        GL11.glBegin(GL11.GL_POLYGON);
        for(int i = 360; i >= 0; i-=5){
            GL11.glTexCoord2f((MathHelper.cos((float) Math.toRadians(i)) + 1) * 0.5f, (MathHelper.sin((float) Math.toRadians(i)) + 1) * 0.5f);
            GL11.glVertex2f(MathHelper.cos((float) Math.toRadians(i)) * radius, MathHelper.sin((float) Math.toRadians(i)) * radius);
        }
        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawTexturedRect(double x, double y, double w, double h, int u1, int v1,
                                        int u2, int v2) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x + w, y, zDepth).tex(u2, u1).endVertex();
        bufferBuilder.pos(x, y, zDepth).tex(u1, v1).endVertex();
        bufferBuilder.pos(x, y + h, zDepth).tex(u1, v2).endVertex();
        bufferBuilder.pos(x + w, y + h, zDepth).tex(u2, v2).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

}
