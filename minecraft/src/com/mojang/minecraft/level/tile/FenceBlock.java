package com.mojang.minecraft.level.tile;

import java.util.ArrayList;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;

public class FenceBlock extends Block {

    public FenceBlock(int var1, int var2) {
	   super(var1);
	   this.textureId = var2;
	}
    
    public final boolean isOpaque() {
        return false;
    }

    public final boolean isSolid() {
        return false;
    }
    
    public final boolean isCube() {
        return false;
    }
}
