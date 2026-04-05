package com.mojang.minecraft.item;

import java.io.Serializable;
import java.util.Random;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.tile.Block;

public final class ItemStack implements Serializable {
	public static final long serialVersionUID = 0L;
	public int stackSize;
	public int iconIndex;
	public int popTime;
	public int itemID;
	public int itemDamage;

	public ItemStack(Block var1, int var2) {
		this(var1.id, var2);
	}

	public ItemStack(Items var1) {
		this((Items)var1, 1);
	}

	public ItemStack(Items var1, int var2) {
		this(var1.shiftedIndex, var2);
	}

	public ItemStack(int var1) {
		this(var1, 1);
	}
	
	public ItemStack(int var1, int var2) {
		this.stackSize = 0;
		this.itemID = var1;
		this.stackSize = var2;
	}
	
	public final ItemStack splitStack(int var1) {
		this.stackSize -= var1;
		return new ItemStack(this.itemID, var1);
	}

	public final Items getItem() {
		return Items.itemsList[this.itemID];
	}
	
	public final int isItemStackDamageable() {
		return Items.itemsList[this.itemID].getMaxDamage();
	}

	public final void damageItem(int var1) {
		this.itemDamage += var1;
		if(this.itemDamage > this.isItemStackDamageable()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
	
	public int getRandomDrop(Random var1) {
		return Items.apple.shiftedIndex;
	}

}
