package com.mojang.minecraft.level.tile;

public class SlimeBlock extends Block {

    public SlimeBlock(int var1, int var2) {
	   super(var1);
	   this.textureId = var2;
	}

    public final boolean isOpaque() {
        return false;
    }

    public final boolean isSolid() {
        return false;
    }
}
