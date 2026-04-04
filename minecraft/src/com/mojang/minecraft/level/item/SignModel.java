package com.mojang.minecraft.level.item;

import com.mojang.minecraft.model.ModelPart;

public final class SignModel {
   public ModelPart signBoard = new ModelPart(0, 0);
   public ModelPart signStick;

   public SignModel() {
      this.signBoard.setBounds(-12.0F, -14.0F, -1.0F, 24, 12, 2, 0.0F);
      this.signStick = new ModelPart(0, 14);
      this.signStick.setBounds(-1.0F, -2.0F, -1.0F, 2, 14, 2, 0.0F);
   }
}