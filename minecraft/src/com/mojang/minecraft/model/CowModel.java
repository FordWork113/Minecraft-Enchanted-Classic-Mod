package com.mojang.minecraft.model;

import com.mojang.util.MathHelper;

public class CowModel extends Model {
   private ModelPart head = new ModelPart(0, 0);
   private ModelPart leg1;
   private ModelPart leg2;
   private ModelPart leg3;
   private ModelPart leg4;
   private ModelPart horn1;
   private ModelPart horn2;
   private ModelPart body;
   private ModelPart udders;

   public CowModel() {
      this.head.setBounds(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
      this.head.setPosition(0.0F, 4.0F, -8.0F);
      this.horn1 = new ModelPart(22, 0);
      this.horn1.setBounds(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
      this.horn1.setPosition(0.0F, 3.0F, -7.0F);
      this.horn2 = new ModelPart(22, 0);
      this.horn2.setBounds(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
      this.horn2.setPosition(0.0F, 3.0F, -7.0F);
      this.body = new ModelPart(18, 4);
      this.body.setBounds(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
      this.body.setPosition(0.0F, 5.0F, 2.0F);
      this.udders = new ModelPart(52, 0);
      this.udders.setBounds(-2.0F, -3.0F, 0.0F, 4, 6, 2, 0.0F);
      this.udders.setPosition(0.0F, 14.0F, 6.0F);     
      this.leg1 = new ModelPart(0, 16);
      this.leg1.setBounds(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
      this.leg1.setPosition(-4.0F, (float)(24 - 12), 7.0F);
      this.leg2 = new ModelPart(0, 16);
      this.leg2.setBounds(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
      this.leg2.setPosition(3.0F, (float)(24 - 12), 7.0F);
      this.leg3 = new ModelPart(0, 16);
      this.leg3.setBounds(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
      this.leg3.setPosition(-3.0F, (float)(24 - 12), -5.0F);
      this.leg4 = new ModelPart(0, 16);
      this.leg4.setBounds(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
      this.leg4.setPosition(4.0F, (float)(24 - 12), -5.0F);
   }

   public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.head.render(var6);
      this.horn1.render(var6);
      this.horn2.render(var6);
      this.body.render(var6);
      this.body.pitch = 1.5707964F;
      this.udders.render(var6);
      this.udders.pitch = 1.5707964F;
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
