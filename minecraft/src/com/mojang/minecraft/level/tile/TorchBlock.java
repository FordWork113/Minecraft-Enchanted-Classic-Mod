package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.util.MathHelper;

public final class TorchBlock extends Block {
	
	Block block;
	
	protected TorchBlock(int var1, int var2) {
		super(var1, var2);
	}

	public AABB getCollisionBox(int x, int y, int z)
	{
		return null;
	}

	public final boolean isOpaque() {
		return false;
	}

	public final boolean isCube() {
	    return false;
	}

	public final boolean isSolid() {
	    return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}

	private void renderTorch(Block var1, float var2, float var3, float var4, float var5, float var6) {
		int var15 = var1.getTextureId(0);
		if(this.overrideBlockTexture >= 0) {
			var15 = this.overrideBlockTexture;
		}

		int var7 = (var15 & 15) << 4;
		var15 &= 240;
		float var8 = (float)var7 / 256.0F;
		float var17 = ((float)var7 + 15.99F) / 256.0F;
		float var9 = (float)var15 / 256.0F;
		float var16 = ((float)var15 + 15.99F) / 256.0F;
		var2 += 0.5F;
		var4 += 0.5F;
		float var10 = var2 - 0.5F;
		float var11 = var2 + 0.5F;
		float var12 = var4 - 0.5F;
		float var13 = var4 + 0.5F;
		float var14 = 1.0F / 16.0F;
		shapeRenderer.vertexUV(var2 - var14, var3 + 1.0F, var12, var8, var9);
		shapeRenderer.vertexUV(var2 - var14 + var5, var3, var12 + var6, var8, var16);
		shapeRenderer.vertexUV(var2 - var14 + var5, var3, var13 + var6, var17, var16);
		shapeRenderer.vertexUV(var2 - var14, var3 + 1.0F, var13, var17, var9);
		shapeRenderer.vertexUV(var2 + var14, var3 + 1.0F, var13, var8, var9);
		shapeRenderer.vertexUV(var2 + var5 + var14, var3, var13 + var6, var8, var16);
		shapeRenderer.vertexUV(var2 + var5 + var14, var3, var12 + var6, var17, var16);
		shapeRenderer.vertexUV(var2 + var14, var3 + 1.0F, var12, var17, var9);
		shapeRenderer.vertexUV(var10, var3 + 1.0F, var4 + var14, var8, var9);
		shapeRenderer.vertexUV(var10 + var5, var3, var4 + var14 + var6, var8, var16);
		shapeRenderer.vertexUV(var11 + var5, var3, var4 + var14 + var6, var17, var16);
		shapeRenderer.vertexUV(var11, var3 + 1.0F, var4 + var14, var17, var9);
		shapeRenderer.vertexUV(var11, var3 + 1.0F, var4 - var14, var8, var9);
		shapeRenderer.vertexUV(var11 + var5, var3, var4 - var14 + var6, var8, var16);
		shapeRenderer.vertexUV(var10 + var5, var3, var4 - var14 + var6, var17, var16);
		shapeRenderer.vertexUV(var10, var3 + 1.0F, var4 - var14, var17, var9);
	}
}
