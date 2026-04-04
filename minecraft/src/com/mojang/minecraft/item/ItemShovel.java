package com.mojang.minecraft.item;

import com.mojang.minecraft.level.tile.Block;

public final class ItemShovel extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new com.mojang.minecraft.level.tile.Block[]{Block.GRASS, Block.DIRT, Block.SAND, Block.GRAVEL, Block.CLAY, Block.SNOW_BLOCK, Block.SNOW_GRASS, Block.WOOD_SLAB};

	public ItemShovel(int var1) {
		super(var1, blocksEffectiveAgainst);
	}
}
