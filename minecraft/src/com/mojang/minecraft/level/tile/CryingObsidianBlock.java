package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;

public class CryingObsidianBlock extends Block {

	public CryingObsidianBlock(int var1, int var2) {
		super(var1, var2);
	}
	
	public boolean blockActivated(Level var1, int var2, int var3, int var4, Player var5) {
	    if(var1.rendererContext$5cd64a7f.gamemode.isSurvival()) {
           var1.setSpawnPos((int)var5.x, (int)var5.y, (int)var5.z, var5.yRot);
           var1.rendererContext$5cd64a7f.hud.addChat("New spawn point has been set");
           return true;
	    }
	    
	    return false;
	}
	
}
