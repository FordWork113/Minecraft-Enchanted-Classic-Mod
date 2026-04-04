package com.mojang.minecraft.model;

import com.mojang.util.MathHelper;

public class WolfModel extends Model {
   private ModelPart head = new ModelPart(0, 0);
   private ModelPart body;
   private ModelPart mane;
   private ModelPart tail;
   private ModelPart ear1;
   private ModelPart ear2;
   private ModelPart snout;
   private ModelPart leg1;
   private ModelPart leg2;
   private ModelPart leg3;
   private ModelPart leg4;

   public WolfModel() {
		float var1 = 0.0F;
		float var2 = 13.5F;
		this.head = new ModelPart(0, 0);
		this.head.setBounds(-3.0F, -3.0F, -2.0F, 6, 6, 4, var1);
		this.head.setPosition(-1.0F, var2, -7.0F);
		this.body = new ModelPart(18, 14);
		this.body.setBounds(-4.0F, -2.0F, -3.0F, 6, 9, 6, var1);
		this.body.setPosition(0.0F, 14.0F, 2.0F);
		this.mane = new ModelPart(21, 0);
		this.mane.setBounds(-4.0F, -2.0F, -3.0F, 6, 5, 6, var1);
		this.mane.setPosition(0.0F, 14.0F, -3.0F);
		this.leg1 = new ModelPart(0, 18);
		this.leg1.setBounds(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
		this.leg1.setPosition(-2.5F, 16.0F, 7.0F);
		this.leg2 = new ModelPart(0, 18);
		this.leg2.setBounds(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
		this.leg2.setPosition(0.5F, 16.0F, 7.0F);
		this.leg3 = new ModelPart(0, 18);
		this.leg3.setBounds(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
		this.leg3.setPosition(-2.5F, 16.0F, -4.0F);
		this.leg4 = new ModelPart(0, 18);
		this.leg4.setBounds(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
		this.leg4.setPosition(0.5F, 16.0F, -4.0F);
		this.tail = new ModelPart(9, 18);
		this.tail.setBounds(-1.0F, 0.0F, -1.0F, 2, 8, 2, var1);
		this.tail.setPosition(-1.0F, 12.0F, 8.0F);
		this.ear1 = new ModelPart(16, 14);
		this.ear1.setBounds(-3.0F, -5.0F, 0.0F, 2, 2, 1, var1);
		this.ear1.setPosition(-1.0F, var2, -7.0F);
		this.ear2 = new ModelPart(16, 14);
		this.ear2.setBounds(1.0F, -5.0F, 0.0F, 2, 2, 1, var1);
		this.ear2.setPosition(-1.0F, var2, -7.0F);
		this.snout = new ModelPart(0, 10);
		this.snout.setBounds(-2.0F, 0.0F, -5.0F, 3, 3, 4, var1);
		this.snout.setPosition(-0.5F, var2, -7.0F);
   }

   public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.head.render(var6);
      this.ear1.render(var6);
      this.ear2.render(var6);
      this.body.render(var6);
      this.body.pitch = 1.5707964F;
      this.mane.render(var6);
      this.mane.pitch = 1.5707964F;
      this.snout.render(var6);
      this.tail.render(var6);
      this.tail.pitch = 0.5707964F;
      //this.tail.pitch = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
      this.leg1.render(var6);
      this.leg2.render(var6);
      this.leg3.render(var6);
      this.leg4.render(var6);
      this.leg1.pitch = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
      this.leg2.pitch = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg3.pitch = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg4.pitch = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
   }
}
