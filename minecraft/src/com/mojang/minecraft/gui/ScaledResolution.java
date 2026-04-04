package com.mojang.minecraft.gui;

public final class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;
	
	public ScaledResolution(int var1, int var2) {
		this.scaledWidth = var1;

		for(this.scaledHeight = var2; this.scaledWidth >= 854 && this.scaledHeight >= 420; this.scaledHeight /= 2) {
			this.scaledWidth /= 2;
		}

	}

	public final int getScaledWidth() {
		return this.scaledWidth;
	}

	public final int getScaledHeight() {
		return this.scaledHeight;
	}
}
