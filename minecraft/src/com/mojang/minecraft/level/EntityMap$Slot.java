package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.EntityMap;
import com.mojang.minecraft.level.SyntheticClass;
import java.io.Serializable;

class EntityMap$Slot implements Serializable {

   public static final long serialVersionUID = 0L;
   private int xSlot;
   private int ySlot;
   private int zSlot;
   // $FF: synthetic field
   final EntityMap entityMap;


   private EntityMap$Slot(EntityMap var1) {
      this.entityMap = var1;
   }

   public EntityMap$Slot init(float var1, float var2, float var3) {
      this.xSlot = (int)(var1 / 16.0F);
      this.ySlot = (int)(var2 / 16.0F);
      this.zSlot = (int)(var3 / 16.0F);
      if(this.xSlot < 0) {
         this.xSlot = 0;
      }

      if(this.ySlot < 0) {
         this.ySlot = 0;
      }

      if(this.zSlot < 0) {
         this.zSlot = 0;
      }

      if(this.xSlot >= EntityMap.getWidth(this.entityMap)) {
         this.xSlot = EntityMap.getWidth(this.entityMap) - 1;
      }

      if(this.ySlot >= EntityMap.getDepth(this.entityMap)) {
         this.ySlot = EntityMap.getDepth(this.entityMap) - 1;
      }

      if(this.zSlot >= EntityMap.getHeight(this.entityMap)) {
         this.zSlot = EntityMap.getHeight(this.entityMap) - 1;
      }

      return this;
   }

   public void add(Entity var1) {
      if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
         this.entityMap.entityGrid[(this.zSlot * EntityMap.getDepth(this.entityMap) + this.ySlot) * EntityMap.getWidth(this.entityMap) + this.xSlot].add(var1);
      }

   }

   public void remove(Entity var1) {
      if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
         this.entityMap.entityGrid[(this.zSlot * EntityMap.getDepth(this.entityMap) + this.ySlot) * EntityMap.getWidth(this.entityMap) + this.xSlot].remove(var1);
      }

   }

   // $FF: synthetic method
   EntityMap$Slot(EntityMap var1, SyntheticClass var2) {
      this(var1);
   }

   // $FF: synthetic method
   static int getXSlot(EntityMap$Slot var0) {
      return var0.xSlot;
   }

   // $FF: synthetic method
   static int getYSlot(EntityMap$Slot var0) {
      return var0.ySlot;
   }

   // $FF: synthetic method
   static int getZSlot(EntityMap$Slot var0) {
      return var0.zSlot;
   }
}
