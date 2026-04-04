package com.mojang.minecraft.model;

import com.mojang.util.MathHelper;

public final class SlimeModel extends Model {
	
   private ModelPart body;

   public SlimeModel() {
      this.body = new ModelPart(0, 0);
      this.body.setBounds(-4.0F, 16.0F, -4.0F, 8, 8, 8, 0.0F);
   }
   
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
   }

   public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
	  this.setRotationAngles(var1, var2, var3, var4, var5, var6);
      this.body.render(var6);
   }
}
