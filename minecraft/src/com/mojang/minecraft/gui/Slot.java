package com.mojang.minecraft.gui;

import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.player.Inventory;

final class Slot {
   public final int slotIndex;
   public final int xPos;
   public final int yPos;
   InventoryScreen inventoryScreen;
   Inventory inventory;

   public Slot(InventoryScreen var1, Inventory inv, int var2, int var3, int var4) {
      this.inventoryScreen = var1;
      this.inventory = inv;
      this.slotIndex = var2;
      this.xPos = var3;
      this.yPos = var4;
   }

   public final boolean getIsMouseOverSlot(int var1, int var2) {
      int var3 = (this.inventoryScreen.width - 180) / 2;
      int var4 = (this.inventoryScreen.height - 260) / 2;
      var1 -= var3;
      var2 -= var4;
      return var1 >= this.xPos - 1 && var1 < this.xPos + 16 && var2 >= this.yPos - 1 && var2 < this.yPos + 16;
   }

   public void onPickupFromSlot() {
   }

   public final void putStack(ItemStack var1) {
      this.inventory.setInventorySlotContents(this.slotIndex, var1);
   }

   public boolean isItemValid(ItemStack var1) {
      return true;
   }
}
