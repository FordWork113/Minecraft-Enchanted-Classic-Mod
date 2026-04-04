package com.mojang.minecraft;

import com.mojang.minecraft.player.Player;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

public class SkinDownloadThread extends Thread {
   private Minecraft minecraft;

   public SkinDownloadThread(Minecraft var1) {
      this.minecraft = var1;
   }

   public void run() {
      if (this.minecraft.session != null) {
         HttpURLConnection var1 = null;

         try {
            var1 = (HttpURLConnection)(new URL("http://betacraft.uk/skin/" + this.minecraft.session.username + ".png")).openConnection();
            var1.setDoInput(true);
            var1.setDoOutput(false);
            var1.connect();
            if (var1.getResponseCode() != 404) {
               Player.newTexture = ImageIO.read(var1.getInputStream());
               return;
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         } finally {
            if (var1 != null) {
               var1.disconnect();
            }

         }
      }

   }
}
