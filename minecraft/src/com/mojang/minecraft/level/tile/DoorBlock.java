package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.render.ShapeRenderer;

public final class DoorBlock extends Block {
	
	 protected DoorBlock(int var1, int var2) {
	    super(var1, var2);
	      this.textureId = 112;
	      float var3 = 0.5F;
	      float var4 = 1.0F;
	      this.setBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
	 }
	 
	 public final int getTextureId(int var1, int var2) {
		   if (var1 != 0 && var1 != 1) {
		     int var3 = this.getDir(var2);
		     if ((var3 == 0 || var3 == 2) ^ var1 <= 3) {
		            return this.textureId;
		   } else {
		     int var4 = var3 / 2 + (var1 & 1 ^ var3);
		     var4 += (var2 & 4) / 4;
		     int var5 = this.textureId - (var2 & 8) * 2;
		     if ((var4 & 1) != 0) {
		         var5 = -var5;
		     }

		     return var5;
		   }
		   } else {
		         return this.textureId;
		   }
	 }
	 
	 public int getDir(int var1) {
		return (var1 & 4) == 0 ? var1 - 1 & 3 : var1 & 3;
	 }
	 

	 public void setBounds(int var1) {
	    float var2 = 0.1875F;
	    this.setBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
	    if (var1 == 0) {
	       this.setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
	    }

	    if (var1 == 1) {
	       this.setBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	    }

	    if (var1 == 2) {
	       this.setBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
	    }

	    if (var1 == 3) {
	       this.setBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
	    }

	  }

	 
	 public final boolean isCube() {
		return false;
	 }

	 public final boolean isSolid() {
		return false;
	 }
	 
	 public final boolean isOpaque() {
		return false;
	 }
	 
	 public AABB getCollisionBox(int var1, int var2, int var3) {
		return new AABB((float)var1 + this.x1, (float)var2 + this.y1, (float)var3 + this.z1, (float)var1 + this.x2, (float)var2 + this.y2, (float)var3 + this.z2);
	 }
}
