package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;

import java.util.Random;

public class Flower extends Tile {
	protected Flower(int i1, int i2) {
		super(i1);
		this.setTicking(true);
		float f3 = 0.2F;
		this.setShape(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 3.0F, f3 + 0.5F);
	}

	public void tick(Level level, int x, int y, int z, Random random) {
		int i6 = level.getTile(x, y - 1, z);
		if(!level.isLit(x, y, z) || i6 != Tile.dirt.id && i6 != Tile.grass.id) {
			level.setTile(x, y, z, 0);
		}

	}

	public final AABB getTileAABB(int x, int y, int z) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}

	public final boolean isOpaque() {
		return false;
	}
}