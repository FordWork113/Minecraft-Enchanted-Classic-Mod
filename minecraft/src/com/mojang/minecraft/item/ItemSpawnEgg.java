package com.mojang.minecraft.item;

import java.util.Random;

import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.*;
import com.mojang.minecraft.player.Player;

public final class ItemSpawnEgg extends Items {

	public ItemSpawnEgg(int var1) {
		super(var1);
	}

	public final boolean onItemRightClick(ItemStack var1, Player var2, Level var3) {
		int var4 = 2;
		
	    if(var3.rendererContext$5cd64a7f.selected != null && var3.rendererContext$5cd64a7f.selected.entity == null) {	
		    if(var1.itemID == Items.spawnegg_human.shiftedIndex) {
	    	   var3.addEntity(new HumanoidMob(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
		    } else if(var1.itemID == Items.spawnegg_pig.shiftedIndex) {
		       var3.addEntity(new Pig(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
		    } else if(var1.itemID == Items.spawnegg_sheep.shiftedIndex) {
			   var3.addEntity(new Sheep(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_cow.shiftedIndex) {
			   var3.addEntity(new Cow(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_chicken.shiftedIndex) {
			   var3.addEntity(new Chicken(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_wolf.shiftedIndex) {
			   var3.addEntity(new Wolf(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_zombie.shiftedIndex) {
			   var3.addEntity(new Zombie(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_skeleton.shiftedIndex) {
			   var3.addEntity(new Skeleton(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_creeper.shiftedIndex) {
			   var3.addEntity(new Creeper(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_spider.shiftedIndex) {
			   var3.addEntity(new Spider(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			} else if(var1.itemID == Items.spawnegg_slime.shiftedIndex) {
			   var3.addEntity(new Slime(var3, var3.rendererContext$5cd64a7f.selected.x, var3.rendererContext$5cd64a7f.selected.y + var4, var3.rendererContext$5cd64a7f.selected.z));
			}
		    
		    if(var3.rendererContext$5cd64a7f.gamemode.isSurvival()) {
				--var1.stackSize;
		    }
		    
	    } else {
	    	return false;
	    }

		return true;
	}
}
