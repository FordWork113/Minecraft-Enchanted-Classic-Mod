package com.mojang.minecraft.skins;

import java.io.InputStream;
import com.mojang.minecraft.Minecraft;

public abstract class TexturePack {
   public String name;
   public String desc1;
   public String desc2;
   public String id;

   public void select() {
   }

   public void deselect() {
   }

   public void load(Minecraft minecraft) {
   }

   public void unload(Minecraft minecraft) {
   }

   public void bindTexture(Minecraft minecraft) {
   }

   public InputStream getResource(String name) {
      return TexturePack.class.getResourceAsStream(name);
   }
}
