package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.util.MathHelper;

public final class IceBlock extends Block {

	private boolean showNeighborSides = false;
	
    public IceBlock(int var1, int var2) {
	   super(var1);
	   this.textureId = var2;
	}

    public int getRenderPass() {
        return 1;
    }

    public final boolean isSolid() {
        return false;
    }
    
    public final int getDropCount() {
        return 0;
    }
    
    public final boolean canRenderSide(Level level, int x, int y, int z, int side) {
        int var6 = level.getTile(x, y, z);
        return !this.showNeighborSides && var6 == this.id?false:super.canRenderSide(level, x, y, z, side);
    }

    /*public final void onRemoved(Level var1, int x, int y, int z) {
        var1.setTile(x, y, z, Block.WATER.id);
    }*/
    
}
