package com.mojang.minecraft.sound;

public final class SoundFx implements Audio {

   private AudioInfo info;
   private float pitch = 0.0F;
   private float volume = 1.0F;
   private static short[] data = new short[1];


   public SoundFx(AudioInfo var1) {
      this.info = var1;
      this.volume = var1.volume;
   }

   public final boolean play(int[] var1, int[] var2, int var3) {
      if(data.length < var3) {
         data = new short[var3];
      }

      int var4;
      boolean var5 = (var4 = this.info.update(data, var3)) > 0;
      int var8 = (int)((this.pitch > 0.0F?1.0F - this.pitch:1.0F) * this.volume * 65536.0F);
      int var9 = (int)((this.pitch < 0.0F?1.0F + this.pitch:1.0F) * this.volume * 65536.0F);
      int var12;
      int var13;
      int var14;
         if(var8 >= 0 || var9 != 0) {
            var12 = var8;
            var13 = var9;

            for(var14 = 0; var14 < var4; ++var14) {
               var1[var14] += data[var14] * var12 >> 16;
               var2[var14] += data[var14] * var13 >> 16;
            }
         } else {
         for(var12 = 0; var12 < var4; ++var12) {
            var13 = var8 * var12 / var3;
            var14 = var9 * var12 / var3;
            var1[var12] += data[var12] * var13 >> 16;
            var2[var12] += data[var12] * var14 >> 16;
         }
      }

      this.pitch = var8;
      this.volume = var9;
      return var5;
   }

}
