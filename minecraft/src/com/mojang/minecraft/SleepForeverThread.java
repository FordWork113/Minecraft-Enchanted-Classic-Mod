package com.mojang.minecraft;

public class SleepForeverThread extends Thread {
   public SleepForeverThread(Minecraft var1) {
      this.setDaemon(true);
      this.start();
   }

   public void run() {
      while(true) {
         try {
            while(true) {
               Thread.sleep(2147483647L);
            }
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }
}
