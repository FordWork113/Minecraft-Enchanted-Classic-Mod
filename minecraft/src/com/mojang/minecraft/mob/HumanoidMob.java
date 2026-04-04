package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.Model;
import com.mojang.minecraft.render.TextureManager;

import java.awt.image.BufferedImage;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {

   public static final long serialVersionUID = 0L;
   public boolean helmet = Math.random() < 0.20000000298023224D;
   public boolean armor = Math.random() < 0.20000000298023224D;
   Random rand;
   public String plate = "/armor/plate.png";
   public String chain = "/armor/chain.png";
   private final String[] armorTexture = {this.plate, this.chain};
   private int[] armorId = new int[armorTexture.length];
   
   public HumanoidMob(Level var1, float var2, float var3, float var4) {
      super(var1);
      this.modelName = "humanoid";
      this.heightOffset = 1.62F;
      this.setPos(var2, var3, var4);
   }

   public void renderModel(TextureManager var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      super.renderModel(var1, var2, var3, var4, var5, var6, var7);
      Model var9 = modelCache.getModel(this.modelName);
      GL11.glEnable(3008);
      if(this.allowAlpha) {
         GL11.glEnable(2884);
      }

      if(this.hasHair) {
         GL11.glDisable(2884);
         HumanoidModel var10 = null;
         (var10 = (HumanoidModel)var9).headwear.yaw = var10.head.yaw;
         var10.headwear.pitch = var10.head.pitch;
         var10.headwear.render(var7);
         GL11.glEnable(2884);
      }

      if(this.armor || this.helmet) {
         this.setRandomTexture(var1);
         GL11.glDisable(2884);
         HumanoidModel var8;
         (var8 = (HumanoidModel)modelCache.getModel("humanoid.armor")).head.render = this.helmet;
         var8.body.render = this.armor;
         var8.rightArm.render = this.armor;
         var8.leftArm.render = this.armor;
         var8.rightLeg.render = this.armor;
         var8.leftLeg.render = this.armor;
         HumanoidModel var11 = (HumanoidModel)var9;
         var8.head.yaw = var11.head.yaw;
         var8.head.pitch = var11.head.pitch;
         var8.rightArm.pitch = var11.rightArm.pitch;
         var8.rightArm.roll = var11.rightArm.roll;
         var8.leftArm.pitch = var11.leftArm.pitch;
         var8.leftArm.roll = var11.leftArm.roll;
         var8.rightLeg.pitch = var11.rightLeg.pitch;
         var8.leftLeg.pitch = var11.leftLeg.pitch;
         var8.head.render(var7);
         var8.body.render(var7);
         var8.rightArm.render(var7);
         var8.leftArm.render(var7);
         var8.rightLeg.render(var7);
         var8.leftLeg.render(var7);
         GL11.glEnable(2884);
      }

      GL11.glDisable(3008);
   }
   
   public void setRandomTexture(TextureManager var1) {
	      int id = 0;      
	      id = this.randomTextureId;
	      
	      for (int i = 0; i < armorTexture.length; i++) {
	    	  armorId[i] = var1.load(armorTexture[i]);
	      } 
	      
	      GL11.glBindTexture(3553, armorId[id]);
   }
}
