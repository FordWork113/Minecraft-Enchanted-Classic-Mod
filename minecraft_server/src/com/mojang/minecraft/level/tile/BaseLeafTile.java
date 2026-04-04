package com.mojang.minecraft.level.tile;

public class BaseLeafTile extends Tile {
	protected BaseLeafTile(int i1, int i2, boolean z3) {
		super(i1, i2);
	}

	public final boolean isSolid() {
		return false;
	}

	public final boolean blocksLight() {
		return false;
	}
}