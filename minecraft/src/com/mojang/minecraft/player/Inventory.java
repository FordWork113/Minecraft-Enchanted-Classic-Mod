package com.mojang.minecraft.player;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.SessionData;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.tile.Block;

import java.io.Serializable;

public class Inventory implements Serializable {

   public int selected = 0;
   public ItemStack[] Inventory = new ItemStack[45];
   public static final long serialVersionUID = 0L;
   public static final int POP_TIME_DURATION = 5;
   private Player player;
   
       public Inventory(Player var1) {
	      this.player = var1;
	   }
       
       public ItemStack getSelected() {
	        return this.Inventory[this.selected];
       }
       
	   public final int getInventorySlotContainItem(int var1) {
	      for(int var2 = 0; var2 < this.Inventory.length; ++var2) {
	         if (this.Inventory[var2] != null && this.Inventory[var2].itemID == var1) {
	            return var2;
	         }
	      }

	      return -1;
	   }

		private int storeItemStack() {
			for(int var1 = 0; var1 < this.Inventory.length; ++var1) {
				if(this.Inventory[var1] == null) {
					return var1;
				}
			}

			return -1;
		}

		 public void grabBlock(int var1, boolean var2) {
			      int var3;
			      if((var3 = this.getInventorySlotContainItem(var1)) >= 0) {
			         this.selected = var3;
			      } else {
			         if(var2 && var1 > 0 && SessionData.allowedBlocks.contains(Block.blocks[var1])) {
			            this.replaceSlot(Block.blocks[var1]);
			         }

			      }
		 }

		public final boolean consumeInventoryItem(int var1) {
			var1 = this.getInventorySlotContainItem(var1);
			if(var1 < 0) {
				return false;
			} else {
				if(--this.Inventory[var1].stackSize <= 0) {
					this.Inventory[var1] = null;
				}

				return true;
			}
		}
	   
	   public final void swapSlot(int var1, int var2) {
	      ItemStack var3 = this.Inventory[var2];
	      this.Inventory[var2] = this.Inventory[var1];
	      this.Inventory[var1] = var3;
	   }
	   

		public final boolean addResource(ItemStack var1) {
			int var4 = var1.stackSize;
			int var3 = var1.itemID;
			int var6 = var3;
			Inventory var5 = this;
			int var7 = 0;

			int var10001;
			ItemStack var8;
			while(true) {
				if(var7 >= var5.Inventory.length) {
					var10001 = -1;
					break;
				}

				if(var5.Inventory[var7] != null && var5.Inventory[var7].itemID == var6) {
					var8 = var5.Inventory[var7];
					if(var5.Inventory[var7].stackSize < var8.getItem().getItemStackLimit() && var5.Inventory[var7].stackSize < var8.getItem().getItemStackLimit()) {
						var10001 = var7;
						break;
					}
				}

				++var7;
			}

			int var9 = var10001;
			if(var9 < 0) {
				var9 = this.storeItemStack();
			}

			if(var9 < 0) {
				var10001 = var4;
			} else {
				if(this.Inventory[var9] == null) {
					this.Inventory[var9] = new ItemStack(var3, 0);
				}

				var3 = var4;
				var8 = this.Inventory[var9];
				if(var4 > var8.getItem().getItemStackLimit() - this.Inventory[var9].stackSize) {
					var8 = this.Inventory[var9];
					var3 = var8.getItem().getItemStackLimit() - this.Inventory[var9].stackSize;
				}

				if(var3 > var8.getItem().getItemStackLimit() - this.Inventory[var9].stackSize) {
					var3 = var8.getItem().getItemStackLimit() - this.Inventory[var9].stackSize;
				}

				if(var3 == 0) {
					var10001 = var4;
				} else {
					var4 -= var3;
					this.Inventory[var9].stackSize += var3;
					this.Inventory[var9].animationsToGo = 5;
					var10001 = var4;
				}
			}

			var1.stackSize = var10001;
			if(var1.stackSize == 0) {
				return true;
			} else {
				int var2 = this.storeItemStack();
				if(var2 >= 0) {
					this.Inventory[var2] = var1;
					this.Inventory[var2].animationsToGo = 5;
					return true;
				} else {
					return false;
				}
			}
		}
		
		   public void tick() {
			      for(int var1 = 0; var1 < this.Inventory.length; ++var1) {
			          if(this.Inventory[var1].animationsToGo  > 0) {
			             --this.Inventory[var1].animationsToGo;
			          }
			       }

		   }
		   
			public final ItemStack decrStackSize(int var1, int var2) {
				if(this.Inventory[var1] != null) {
					ItemStack var3;
					if(this.Inventory[var1].stackSize <= var2) {
						var3 = this.Inventory[var1];
						this.Inventory[var1] = null;
						return var3;
					} else {
						var3 = this.Inventory[var1].splitStack(var2);
						if(this.Inventory[var1].stackSize == 0) {
							this.Inventory[var1] = null;
						}

						return var3;
					}
				} else {
					return null;
				}
			}
			
			public final void setInventorySlotContents(int var1, ItemStack var2) {
				this.Inventory[var1] = var2;
			}
		   
			public final ItemStack getStackInSlot(int var1) {
				return this.Inventory[var1];
			}
			
			public final int getSizeInventory() {
				return this.Inventory.length;
			}
			
			   public void replaceSlot(int var1) {
				      if(var1 >= 0) {
				         this.replaceSlot((Block)SessionData.allowedBlocks.get(var1));
				      }

				   }

				   public void replaceSlot(Block var1) {
				      if(var1 != null) {
				         int var2;
				         if((var2 = this.getInventorySlotContainItem(var1.id)) >= 0) {
				            this.Inventory[var2] = this.Inventory[this.selected];
				         }

				         this.Inventory[this.selected] = new ItemStack(var1.id);
				      }

				   }
				   
				   public void dropAll() {
					      int var1;
					      for(var1 = 0; var1 < this.Inventory.length; ++var1) {
					         if (this.Inventory[var1] != null) {
					            this.player.drop(this.Inventory[var1]);
					            this.Inventory[var1] = null;
					         }
					      }

					   }
				   
				   
					public final int getInventoryStackLimit() {
						return 99;
					}


}
