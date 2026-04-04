package com.mojang.minecraft.level.tile;

public final class LogTile extends Tile {
	protected LogTile(int i1) {
		super(17);
	}

	public final int resourceCount() {
		return random.nextInt(3) + 3;
	}
}