package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;

public final class SplashParticle extends WaterDropParticle {
   private static final long serialVersionUID = 1L;

   public SplashParticle(Level level, float f2, float f3, float f4) {
      super(level, f2, f3, f4);
      ++this.tex;
   }
}
