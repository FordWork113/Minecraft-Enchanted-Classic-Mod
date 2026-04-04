package com.mojang.minecraft.item;

import com.mojang.minecraft.level.tile.Block;

public final class ItemAxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.WOOD, Block.BOOKSHELF, Block.LOG, Block.BIRCH_LOG, Block.SPRUCE_LOG, Block.BIRCH_WOOD, Block.SPRUCE_WOOD, Block.WOOD_SLAB, Block.WORKBENCH};

	public ItemAxe(int var1) {
		super(var1, blocksEffectiveAgainst);
	}
}
