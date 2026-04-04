package com.mojang.minecraft.level;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.ProgressBarDisplay;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class LevelIO {
   private ProgressBarDisplay progressBar;
   protected Minecraft minecraft;

   public LevelIO(ProgressBarDisplay var1) {
      this.progressBar = var1;
   }

   public final boolean save(Level var1, File var2) {
      try {
         FileOutputStream var5 = new FileOutputStream(var2);
         save(var1, (OutputStream)var5);
         var5.close();
         return true;
      } catch (Exception var6) {
         var6.printStackTrace();
         if (this.progressBar != null) {
            this.progressBar.setText("Failed!");
         }

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var5) {
         }

         return false;
      }
   }

   public final Level load(File var1) {
      try {
         FileInputStream var5 = new FileInputStream(var1);
         Level var2 = this.load((InputStream)var5);
         var5.close();
         return var2;
      } catch (Exception var5) {
         if (this.progressBar != null) {
            this.progressBar.setText("File not found.");
         }

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var4) {
         }

         return null;
      }
   }

   public final boolean saveSlot(Level var1, String var2, String var3, String var4, String var5, int slot) {
      try {
         if (this.progressBar != null) {
            this.progressBar.setTitle("Save Level");
            this.progressBar.setText("Saving...");
         }

         String mcDir = System.getProperty("user.dir");
         File varlist = new File(mcDir, "levels/levels.txt");
         BufferedReader readsave = new BufferedReader(new FileReader(varlist));
         String[] varsaveslot = readsave.readLine().split(";");
         String Killevel = "" + slot;
         File leveldelete = new File(mcDir, "/levels/" + Killevel + ".dat");
         leveldelete.delete();
         varsaveslot[slot] = var5;
         readsave.close();
         BufferedWriter writesave = new BufferedWriter(new FileWriter(varlist));
         writesave.write(varsaveslot[0] + ";" + varsaveslot[1] + ";" + varsaveslot[2] + ";" + varsaveslot[3] + ";" + varsaveslot[4]);
         writesave.close();
         save(var1, (OutputStream)(new FileOutputStream(new File(mcDir + "/levels/" + slot + ".dat"))));
      } catch (FileNotFoundException var15) {
         var15.printStackTrace();
      } catch (IOException var16) {
         if (this.progressBar != null) {
            this.progressBar.setText("Failed!");

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var14) {
            }
         }
      }

      if (this.progressBar != null) {
         this.progressBar.setText("Saved!");

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var13) {
         }
      }

      return true;
   }

   public final Level loadSlot(String var1, String var2, int slot) {
      String mcDir = System.getProperty("user.dir");
      File varurl = new File(mcDir, "levels/levels.txt");

      try {
         BufferedReader var4 = new BufferedReader(new FileReader(varurl));
         var4.close();
         File jarga = new File(mcDir, "levels/" + slot + ".dat");
         return this.load(jarga);
      } catch (IOException var7) {
         System.out.println("File not found");
         return null;
      }
   }

   public final Level load(InputStream var1) {
      if (this.progressBar != null) {
         this.progressBar.setTitle("Loading level");
      }

      if (this.progressBar != null) {
         this.progressBar.setText("Reading..");
      }

      try {
         DataInputStream var10;
         if ((var10 = new DataInputStream(new GZIPInputStream(var1))).readInt() != 656127880) {
            return null;
         } else {
            byte var12;
            if ((var12 = var10.readByte()) > 2) {
               return null;
            } else if (var12 <= 1) {
               String var14 = var10.readUTF();
               String var15 = var10.readUTF();
               long var3 = var10.readLong();
               short var5 = var10.readShort();
               short var6 = var10.readShort();
               short var7 = var10.readShort();
               byte[] var8 = new byte[var5 * var6 * var7];
               var10.readFully(var8);
               var10.close();
               Level var11;
               (var11 = new Level()).setData(var5, var7, var6, var8);
               var11.name = var14;
               var11.creator = var15;
               var11.createTime = var3;
               return var11;
            } else {
               Level var2;
               LevelObjectInputStream var13;
               (var2 = (Level)(var13 = new LevelObjectInputStream(var10)).readObject()).initTransient();
               var13.close();

               return var2;
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
         System.out.println("Failed to load level: " + var13.toString());
         return null;
      }
   }

   public static void save(Level var0, OutputStream var1) {
      try {
         DataOutputStream var3;
         (var3 = new DataOutputStream(new GZIPOutputStream(var1))).writeInt(656127880);
         var3.writeByte(2);
         ObjectOutputStream var4;
         (var4 = new ObjectOutputStream(var3)).writeObject(var0);
         var4.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static byte[] decompress(InputStream var0) {
      try {
         DataInputStream var3;
         byte[] var1 = new byte[(var3 = new DataInputStream(new GZIPInputStream(var0))).readInt()];
         var3.readFully(var1);
         var3.close();
         return var1;
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }
}
