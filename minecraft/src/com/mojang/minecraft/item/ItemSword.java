package com.mojang.minecraft.item;

import com.mojang.minecraft.level.tile.Block;

public final class ItemSword extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.LEAVES, Block.SPRUCE_LEAVES, Block.BIRCH_LEAVES, Block.REDBERRY_LEAVES, Block.BLACKBERRY_LEAVES, Block.OTHER_REDBERRY_LEAVES, Block.OTHER_REDBERRY_LEAVES, Block.SNOW_LEAVES, Block.SNOW_BIRCH_LEAVES, Block.SNOW_SPRUCE_LEAVES, Block.SNOW_OTHER_REDBERRY_LEAVES, Block.SNOW_OTHER_BLACKBERRY_LEAVES, Block.COBWEB, Block.CACTUS};
	private int weaponDamage;
	
	public ItemSword(int var1, int var2) {
		super(var1, blocksEffectiveAgainst);
		this.maxDamage = 32 << var2;
		this.weaponDamage = 4 + (var2 << 1);
	}

	public final void hitEntity(ItemStack var1) {
	    var1.damageItem(1);
	}
	
	public final int getAttackDamage() {
		return this.weaponDamage;
	}
}
