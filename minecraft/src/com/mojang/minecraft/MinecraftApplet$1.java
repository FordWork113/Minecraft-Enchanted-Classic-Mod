package com.mojang.minecraft;

import java.awt.Canvas;

public class MinecraftApplet$1 extends Canvas {
   private static final long serialVersionUID = 1L;
   private MinecraftApplet applet;

   public MinecraftApplet$1(MinecraftApplet var1) {
      this.applet = var1;
   }

   public synchronized void addNotify() {
      super.addNotify();
      this.applet.startGameThread();
   }

   public synchronized void removeNotify() {
      this.applet.stopGameThread();
      super.removeNotify();
   }
}
