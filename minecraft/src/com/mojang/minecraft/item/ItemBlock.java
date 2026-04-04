package com.mojang.minecraft.item;

import java.util.Random;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.gamemode.CreativeGameMode;
import com.mojang.minecraft.gamemode.HardcoreGameMode;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.Tile$SoundType;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.render.ItemRenderer;

public final class ItemBlock extends Items {
   public transient Random random = new Random();
   public int id;

   public ItemBlock(int var1) {
      super(var1);
      this.id = var1;
   }
   
   public final void onPlaced(ItemStack var1, Level var2, int var3, int var4, int var5, int var6) {
	   
		if(var6 == 0) {
			--var4;
		}

		if(var6 == 1) {
			++var4;
		}

		if(var6 == 2) {
			--var5;
		}

		if(var6 == 3) {
			++var5;
		}

		if(var6 == 4) {
			--var3;
		}

		if(var6 == 5) {
			++var3;
		}
		
		if(var1.stackSize != 0) {
				var6 = var2.getTile(var3, var4, var5);
				Block var9 = Block.blocks[var6];
				if(this.id > 0 && var9 == null || var9 == Block.WATER || var9 == Block.STATIONARY_WATER || var9 == Block.LAVA || var9 == Block.STATIONARY_LAVA) {
					var9 = Block.blocks[this.id];
					AABB var12 = var9.getCollisionBox(var3, var4, var5);
					if(var12 != null) {
						AABB var7 = var2.player.bb;
						if(!((var12.x1 > var7.x0 && var12.x0 < var7.x1 ? (var12.y1 > var7.y0 && var12.y0 < var7.y1 ? var12.z1 > var7.z0 && var12.z0 < var7.z1 : false) : false) ? false : var2.isFree((var12)))) {
							return;
						}
					}
					
					if(this.id == Block.BROWN_MUSHROOM.id) {
						
						if(var2.rendererContext$5cd64a7f.player.health == 20) {
							var2.netSetTile(var3, var4, var5, Block.BROWN_MUSHROOM.id);
							var2.rendererContext$5cd64a7f.renderer.itemRenderer.pos = 0.0F;
							var9 = Block.blocks[this.id];
							Block.blocks[var1.itemID].onPlace(var2, var3, var4, var5);	
							String var10004 = "step." + var9.stepsound.name;
							String var10007 = "ramdom." + var9.stepsound.name;
							float var10005 = (var9.stepsound.getVolume() + 1.0F) / 2.0F;
							float var10006 = var9.stepsound.getPitch() * 0.8F;
							
							if(var9.stepsound != Tile$SoundType.none) {
							    var2.playSound(var10004, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
							    var2.playSound(var10007, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
						    }
						
						    if(var2.rendererContext$5cd64a7f.gamemode.isSurvival()) {
								--var1.stackSize;
						    }
				            
						} else if(var2.rendererContext$5cd64a7f.player.health > 0 && var2.rendererContext$5cd64a7f.player.health < var2.rendererContext$5cd64a7f.player.MAX_HEALTH) {
							
						    if(var2.rendererContext$5cd64a7f.gamemode.isSurvival()) {
								--var1.stackSize;
						    }
						    
							var2.rendererContext$5cd64a7f.renderer.itemRenderer.pos = 0.0F;
							var2.rendererContext$5cd64a7f.player.heal(5);
						}
						
					}
					
					if(this.id == Block.RED_MUSHROOM.id) {
					  if(var2.rendererContext$5cd64a7f.gamemode.isCreative()) {
						var2.netSetTile(var3, var4, var5, Block.RED_MUSHROOM.id);
						var2.rendererContext$5cd64a7f.renderer.itemRenderer.pos = 0.0F;
						var9 = Block.blocks[this.id];
						Block.blocks[var1.itemID].onPlace(var2, var3, var4, var5);	
						String var10004 = "step." + var9.stepsound.name;
						String var10007 = "ramdom." + var9.stepsound.name;
						float var10005 = (var9.stepsound.getVolume() + 1.0F) / 2.0F;
						float var10006 = var9.stepsound.getPitch() * 0.8F;
							
						if(var9.stepsound != Tile$SoundType.none) {
							var2.playSound(var10004, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
							var2.playSound(var10007, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
						}
						
				      }
						
					    if(var2.rendererContext$5cd64a7f.gamemode.isSurvival()) {
							--var1.stackSize;
					    }
					    
						var2.rendererContext$5cd64a7f.renderer.itemRenderer.pos = 0.0F;
						var2.rendererContext$5cd64a7f.player.hurt((Entity)null, 5);	
					}
					
					if(this.id != Block.BROWN_MUSHROOM.id && this.id != Block.RED_MUSHROOM.id) {
					var2.netSetTile(var3, var4, var5, this.id);
					
                    if(var2.rendererContext$5cd64a7f.isOnline()) {
                    	var2.rendererContext$5cd64a7f.networkManager.sendBlockChange(var3, var4, var5, this.id, this.id);
                    }
                    
					var2.rendererContext$5cd64a7f.renderer.itemRenderer.pos = 0.0F;
					var9 = Block.blocks[this.id];
					Block.blocks[var1.itemID].onPlace(var2, var3, var4, var5);	
					String var10004 = "step." + var9.stepsound.name;
					String var10007 = "ramdom." + var9.stepsound.name;
					float var10005 = (var9.stepsound.getVolume() + 1.0F) / 2.0F;
					float var10006 = var9.stepsound.getPitch() * 0.8F;
					
					if(var9.stepsound != Tile$SoundType.none) {
					    var2.playSound(var10004, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
					    var2.playSound(var10007, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, var10005, var10006);
				    }
				
				    if(var2.rendererContext$5cd64a7f.gamemode.isSurvival()) {
						--var1.stackSize;
				    }
				}
		    }
		}
	}
}
