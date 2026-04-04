package com.mojang.minecraft.item;

import com.mojang.minecraft.level.tile.Block;

public final class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.COBBLESTONE, Block.OBSIDIAN, Block.SLAB, Block.DOUBLE_SLAB, Block.STONE, Block.MOSSY_COBBLESTONE, Block.IRON_ORE, Block.IRON_BLOCK, Block.COAL_ORE, Block.GOLD_BLOCK, Block.GOLD_ORE, Block.DIAMOND_BLOCK, Block.DIAMOND_ORE, Block.RUBY_BLOCK, Block.RUBY_ORE, Block.MAGMA, Block.SANDSTONE, Block.CRYING_OBSIDIAN, Block.STONEBRICK, Block.MOSSY_STONEBRICK, Block.CRACKED_STONEBRICK, Block.ICE, Block.STONE_SLAB, Block.BRICK_SLAB, Block.COBBLESTONE_SLAB, Block.MOSSY_COBBLESTONE_SLAB, Block.STONEBRICK_SLAB, Block.SANDSTONE_SLAB};

	public ItemPickaxe(int var1) {
		super(var1, blocksEffectiveAgainst);
	}
}
