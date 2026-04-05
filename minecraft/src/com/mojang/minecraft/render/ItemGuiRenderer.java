package com.mojang.minecraft.render;

import java.nio.FloatBuffer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.FontRenderer;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.model.Vec3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class ItemGuiRenderer {
	private static FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(16);
	
	public static void disableStandardItemLighting() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
	}

	public static void enableStandardItemLighting() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glLightModelf(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, 1.0F);
		Vec3D var0 = new Vec3D(-0.4F, 1.0F, 1.0F);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(var0.x, var0.y, var0.z, 0.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(0.6F, 0.6F, 0.6F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(0.8F, 0.8F, 0.8F, 1.0F));
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(0.4F, 0.4F, 0.4F, 1.0F));
	}

	private static FloatBuffer setColorBuffer(float var0, float var1, float var2, float var3) {
		colorBuffer.clear();
		colorBuffer.put(var0).put(var1).put(var2).put(var3);
		colorBuffer.flip();
		return colorBuffer;
	}
	
	public final void renderItemGUI(FontRenderer var1, TextureManager var2, ItemStack var3, int var4, int var5) {
		if(var3 != null) {
			int var7;
			ShapeRenderer var9 = ShapeRenderer.instance;

			if(var3.itemID < 256) {
				int var6 = var3.itemID;
				GL11.glDisable(GL11.GL_LIGHTING);
				var7 = var2.load("/terrain.png");
				GL11.glBindTexture(3553, var7);
				Block var37 = Block.blocks[var6];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(var4 - 2), (float)(var5 + 3), -50.0F);				
	            GL11.glScalef(10.0F, 10.0F, 10.0F);
	            GL11.glTranslatef(1.0F, 0.5F, 0.0F);
	            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
	            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	            GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
	            GL11.glScalef(-1.0F, -1.0F, -1.0F);
			    var9.begin();
		        var37.renderFullbright(var9);
		        var9.end();
				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_LIGHTING);
			} else if(var3.getItem().getIconIndex() >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				if(var3.itemID < 256) {
					var7 = var2.load("/terrain.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
				} else {
					var7 = var2.load("/items.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
				}

				int var10002 = var3.getItem().getIconIndex() % 16 << 4;
				int var10003 = var3.getItem().getIconIndex() / 16 << 4;
				int var8 = var10003;
				var7 = var10002;
				var9.begin();
				var9.vertexUV((float)var4, (float)(var5 + 16), 0.0F, (float)var7 * 0.00390625F, (float)(var8 + 16) * 0.00390625F);
				var9.vertexUV((float)(var4 + 16), (float)(var5 + 16), 0.0F, (float)(var7 + 16) * 0.00390625F, (float)(var8 + 16) * 0.00390625F);
				var9.vertexUV((float)(var4 + 16), (float)var5, 0.0F, (float)(var7 + 16) * 0.00390625F, (float)var8 * 0.00390625F);
				var9.vertexUV((float)var4, (float)var5, 0.0F, (float)var7 * 0.00390625F, (float)var8 * 0.00390625F);
				var9.end();
				GL11.glEnable(GL11.GL_LIGHTING);
			}

		}
	}

}
