package com.mojang.minecraft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public final class ResourceDownloadThread extends Thread {
   private File resourcesFolder;
   private Minecraft mc;
   boolean closing = false;

   public ResourceDownloadThread(File var1, Minecraft var2) {
      this.mc = var2;
      this.setName("Resource download thread");
      this.setDaemon(true);
      this.resourcesFolder = new File(var1, "resources/");
      if (!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
         throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
      }
   }

   public final void run() {
      try {
         ArrayList var1 = new ArrayList();
         URL var2 = new URL("https://fordwork113.github.io/Enchanted-Classic-Res/");
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));

         String var4;
         while((var4 = var3.readLine()) != null) {
            var1.add(var4);
         }

         var3.close();

         for(int var5 = 0; var5 < var1.size(); ++var5) {
            String var6 = (String)var1.get(var5);
            URL var7 = var2;
            File var8 = new File(this.resourcesFolder, "newmusic");

            try {
               label187: {
                  String[] var10;
                  String var11 = (var10 = var6.split(","))[0];
                  int var12 = Integer.parseInt(var10[1]);
                  Long.parseLong(var10[2]);
                  File var13 = new File(this.resourcesFolder, var11);
                  if (!var13.exists() || var13.length() != (long)var12) {
                     var13.getParentFile().mkdirs();
                     this.download(new URL(var7, var11.replaceAll(" ", "%20")), var13);
                     if (this.closing) {
                        break label187;
                     }
                  }

                  Minecraft var14 = this.mc;
                  int var15 = var11.indexOf("/");
                  String var16 = var11.substring(0, var15);
                  String var17 = var11.substring(var15 + 1);
                  if (var16.equalsIgnoreCase("sound")) {
                     var14.sound.registerSound(var13, var17);
                  } else if (var16.equalsIgnoreCase("music")) {
                     var14.sound.registerMusic(var17, var13);
                  } else if (var16.equalsIgnoreCase("newmusic")) {
                     var14.sound.registerMusic(var17, var13);
                  } else if (var16.equalsIgnoreCase("newsound")) {
                     var14.sound.registerSound(var13, var17);
                  }
               }
            } catch (Exception var35) {
               var35.printStackTrace();
            }

            if (this.closing) {
               return;
            }
         }
      } catch (IOException var36) {
         var36.printStackTrace();
      }

   }

   private void download(URL var1, File var2) throws IOException {
      byte[] var3 = new byte[4096];
      DataInputStream var4 = new DataInputStream(var1.openStream());
      Object var5 = null;
      Object var6 = null;

      try {
         DataOutputStream var7 = new DataOutputStream(new FileOutputStream(var2));

         try {
            do {
               int var8;
               if ((var8 = var4.read(var3)) < 0) {
                  var4.close();
                  var7.close();
                  return;
               }

               var7.write(var3, 0, var8);
            } while(!this.closing);
         } finally {
            if (var7 != null) {
               var7.close();
            }

         }

         return;
      } catch (Throwable var13) {
         if (var5 != null) {
            if (var5 != var13) {
               ((Throwable)var5).addSuppressed(var13);
            }
         }
      }

   }

   public final void closeMinecraft() {
      this.closing = true;
   }
}
