package com.mojang.minecraft.level.tile;

public class WorkbenchBlock extends Block {
	
	protected WorkbenchBlock(int var1, int var2) {
		super(var1, var2);
		this.textureId = var2;
	}
	
	protected final int getTextureId(int texture) {
		return texture == 1 ? this.textureId - 1 : (texture == 0 ? this.textureId - 1 : (texture == 3 ? this.textureId + 1 : this.textureId));
	}

	public final int getDropCount() {
	    return 1;
    }
	
}
