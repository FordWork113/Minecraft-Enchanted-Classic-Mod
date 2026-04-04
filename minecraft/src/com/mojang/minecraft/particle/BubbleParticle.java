package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import java.util.Random;

public final class BubbleParticle extends Particle {
   private static final long serialVersionUID = 1L;

   public BubbleParticle(Level level, float f2, float f3, float f4, float f5, float f6, float f7) {
      super(level, f2, f3, f4, f5, f6, f7);
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.tex = 32;
      this.setSize(0.02F, 0.02F);
      this.age = (int)((float)this.age * (this.random.nextFloat() * 0.6F + 0.2F));
      this.xd = f5 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
      this.yd = f6 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
      this.zd = f7 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
   }

   public final void onEntityUpdate() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd = (float)((double)this.yd + 0.002D);
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.85F;
      this.yd *= 0.85F;
      this.zd *= 0.85F;
      if (this.level.getTile((int)this.x, (int)this.y, (int)this.z) != Block.STATIONARY_WATER.id || this.level.getTile((int)this.x, (int)this.y, (int)this.z) != Block.WATER.id) {
         this.remove();
      }

      if (this.lifetime-- <= 0) {
         this.remove();
      }

   }
}
