package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.render.ShapeRenderer;

public final class LavaDropParticle extends Particle {
   private static final long serialVersionUID = 1L;
   private float lavaParticleScale;

   public LavaDropParticle(Level world, float f2, float f3, float f4) {
      super(world, f2, f3, f4, 0.0F, 0.0F, 0.0F);
      this.xd *= 0.8F;
      this.yd *= 0.8F;
      this.zd *= 0.8F;
      this.yd = this.random.nextFloat() * 0.4F + 0.05F;
      this.bCol = 1.0F;
      this.gCol = 1.0F;
      this.rCol = 1.0F;
      this.size *= this.random.nextFloat() * 2.0F + 0.2F;
      this.lavaParticleScale = this.size;
      this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
      this.tex = 49;
   }

   public final float getEntityBrightness(float f2) {
      return 1.0F;
   }

   public final void render(ShapeRenderer tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
      float f8 = ((float)this.age + f2) / (float)this.lifetime;
      this.size = this.lavaParticleScale * (1.0F - f8 * f8);
      super.render(tessellator, f2, f3, f4, f5, f6, f7);
   }

   public final void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      float f2 = (float)this.age / (float)this.lifetime;
      if (this.random.nextFloat() > f2) {
         this.level.particleEngine.spawnParticle(new SmokeParticle(this.level, this.x, this.y, this.z));
      }

      this.yd = (float)((double)this.yd - 0.03D);
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.999F;
      this.yd *= 0.999F;
      this.zd *= 0.999F;
      if (this.onGround) {
         this.xd *= 0.7F;
         this.zd *= 0.7F;
      }

   }
}
