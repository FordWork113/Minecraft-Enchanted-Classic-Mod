package com.mojang.minecraft.level.tile;

import java.util.ArrayList;
import java.util.Random;
import com.mojang.minecraft.Entity;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.model.Vec3D;
import com.mojang.util.MathHelper;

public class StairBlock extends Block {
   private Block base;

   protected StairBlock(int var1, Block var2) {
      super(var1, var2.textureId);
      this.base = var2;
   }

   public final boolean isSolid() {
       return false;
   }
   
   public final boolean isCube() {
       return false;
   }

}
