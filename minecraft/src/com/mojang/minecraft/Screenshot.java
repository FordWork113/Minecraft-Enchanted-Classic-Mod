package com.mojang.minecraft;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Screenshot {
   private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
   private static ByteBuffer pixels;
   private static byte[] buffer;
   private static int[] pixelBuffer;
   private int rowHeight;
   private DataOutputStream dos;
   private byte[] pb;
   private int w;
   private int h;
   private File file;

   public static String grab(File var0, int var1, int var2) {
      try {
         File var3 = new File(var0, "screenshots");
         var3.mkdir();
         if (pixels == null || pixels.capacity() < var1 * var2) {
            pixels = BufferUtils.createByteBuffer(var1 * var2 * 3);
         }

         if (pixelBuffer == null || pixelBuffer.length < var1 * var2 * 3) {
            buffer = new byte[var1 * var2 * 3];
            pixelBuffer = new int[var1 * var2];
         }

         GL11.glPixelStorei(3333, 1);
         GL11.glPixelStorei(3317, 1);
         pixels.clear();
         GL11.glReadPixels(0, 0, var1, var2, 6407, 5121, pixels);
         pixels.clear();
         String var4 = df.format(new Date());

         File var5;
         int var6;
         for(var6 = 1; (var5 = new File(var3, var4 + (var6 == 1 ? "" : "_" + var6) + ".png")).exists(); ++var6) {
         }

         pixels.get(buffer);

         for(var6 = 0; var6 < var1; ++var6) {
            for(int var7 = 0; var7 < var2; ++var7) {
               int var8 = var6 + (var2 - var7 - 1) * var1;
               int var9 = buffer[var8 * 3 + 0] & 255;
               int var10 = buffer[var8 * 3 + 1] & 255;
               int var11 = buffer[var8 * 3 + 2] & 255;
               int var12 = -16777216 | var9 << 16 | var10 << 8 | var11;
               pixelBuffer[var6 + var7 * var1] = var12;
            }
         }

         BufferedImage var14 = new BufferedImage(var1, var2, 1);
         var14.setRGB(0, 0, var1, var2, pixelBuffer, 0, var1);
         ImageIO.write(var14, "png", var5);
         return "Saved screenshot as " + var5.getName();
      } catch (Exception var13) {
         var13.printStackTrace();
         return "Failed to save: " + var13;
      }
   }

   public void addRegion(ByteBuffer var1, int var2, int var3, int var4, int var5) {
      int var6 = var4;
      int var7 = var5;
      if (var4 > this.w - var2) {
         var6 = this.w - var2;
      }

      if (var5 > this.h - var3) {
         var7 = this.h - var3;
      }

      this.rowHeight = var7;

      for(int var8 = 0; var8 < var7; ++var8) {
         var1.position((var5 - var7) * var4 * 3 + var8 * var4 * 3);
         int var9 = (var2 + var8 * this.w) * 3;
         var1.get(this.pb, var9, var6 * 3);
      }

   }

   public void saveRow() throws IOException {
      this.dos.write(this.pb, 0, this.w * 3 * this.rowHeight);
   }

   public String close() throws IOException {
      this.dos.close();
      return "Saved screenshot as " + this.file.getName();
   }
}
