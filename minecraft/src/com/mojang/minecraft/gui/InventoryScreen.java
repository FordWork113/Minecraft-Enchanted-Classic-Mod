package com.mojang.minecraft.gui;

import com.mojang.minecraft.SessionData;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.gui.GuiScreen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;

import com.mojang.minecraft.render.ItemGuiRenderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class InventoryScreen extends GuiScreen {

	private static ItemGuiRenderer itemRenderer = new ItemGuiRenderer();
	private ItemStack selectedItem = null;
	protected List inventorySlots = new ArrayList();
	protected Inventory inventory;
	protected int xSize = 180;
	protected int ySize = 260;
	
   public InventoryScreen(Inventory inv) {
		int var1;
		int var2;

		for(var1 = 0; var1 < 4; ++var1) {
			for(var2 = 0; var2 < 9; ++var2) {
				this.inventorySlots.add(new Slot(this, inv, var2 + (var1 + 1) * 9, 8 + var2 * 18, 84 + var1 * 18));	
			}
		}

		for(var1 = 0; var1 < 9; ++var1) {
			this.inventorySlots.add(new Slot(this, inv, var1, 8 + var1 * 18, 162));
		}

   }
   
   public final void onOpen() {
	    Keyboard.enableRepeatEvents(true);
   }

   public final void onClose() {
	   
		if(this.selectedItem != null) {
			this.minecraft.player.drop(this.selectedItem);
		}
		
	    Keyboard.enableRepeatEvents(false);
   }

   public final void render(int var1, int var2) {
        drawFadingBox(this.width / 2 - 100, 50, this.width / 2 + 100, 180, -1878719232, -1070583712);
        drawCenteredString(this.fontRenderer, "Inventory", this.width / 2, 60, 16777215);
        int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		ItemGuiRenderer.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var3, (float)var4, 0.0F);		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glTranslatef(52.0F, 73.0F, 24.0F);
		GL11.glScalef(24.0F, -24.0F, 24.0F);
		GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		Inventory inv = this.inventory;
		int var8;
		for(var8 = 0; var8 < this.inventorySlots.size(); ++var8) {
			Slot var6 = (Slot)this.inventorySlots.get(var8);
			int var11 = var6.yPos;
			int var10 = var6.xPos;
			int var17 = var6.slotIndex;
			Inventory var14 = var6.inventory;
			ItemStack var15 = var14.getStackInSlot(var17);
			this.drawSlot(this.fontRenderer, this.minecraft.textureManager, var15, var10, var11);
			
			if(var6.getIsMouseOverSlot(var1, var2)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int var7 = var6.xPos;
				int var9 = var6.yPos;	
				drawFadingBox(var7, var9, var7 + 16, var9 + 16, -1862270977, -1056964609);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		if(this.selectedItem != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			this.drawSlot(this.fontRenderer, this.minecraft.textureManager, this.selectedItem, var1 - var3 - 8, var2 - var4 - 8);
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		ItemGuiRenderer.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

        super.render(var1, var2);
   }
   
	public void drawSlot(FontRenderer var1, TextureManager var2, ItemStack var3, int var4, int var5) {
		ShapeRenderer var8 = ShapeRenderer.instance;
		int var15;
		if(var3 != null) {
			String var7 = null;
	        
			for(int var12 = 0; var12 < 9; ++var12) {
			if(var3.itemID < 256) {
				GL11.glDisable(GL11.GL_LIGHTING);
				var15 = var2.load("/terrain.png");
				GL11.glBindTexture(3553, var15);
				Block var37 = Block.blocks[var3.itemID];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(var4 - 2), (float)(var5 + 3), -50.0F);				
	            GL11.glScalef(10.0F, 10.0F, 10.0F);
	            GL11.glTranslatef(1.6F, 0.2F, 8.0F);
	            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);			
				GL11.glTranslatef(-1.5F, 1.2F, -0.35F);
	            GL11.glScalef(-1.0F, -1.0F, -1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			    var8.begin();
		        var37.renderFullbright(var8);
		        var8.end();
				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_LIGHTING);
			} else if(var3.getItem().getIconIndex() >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				var15 = this.minecraft.textureManager.load("/items.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
				this.drawImage(var4, var5, var3.getItem().getIconIndex() % 16 << 4, var3.getItem().getIconIndex() / 16 << 4, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
			}

			if(var3.stackSize > 1) {
				var7 = "" + var3.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				var1.render(var7, var4 + 19 - 2 - this.fontRenderer.getWidth(var7), var5 + 6 + 3, 16777215);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			}

		}
	}
	
	protected final void onMouseClick(int var1, int var2, int var3) {
		if(var3 == 0 || var3 == 1) {
			int var6 = var2;
			int var4 = var1;
			int var7 = 0;

			Slot var10000;
			while(true) {
				if(var7 >= this.inventorySlots.size()) {
					var10000 = null;
					break;
				}

				Slot var8 = (Slot)this.inventorySlots.get(var7);
				if(var8.getIsMouseOverSlot(var4, var6)) {
					var10000 = var8;
					break;
				}

				++var7;
			}

			Slot var11 = var10000;
			if(var11 != null) {
				ItemStack var12 = var11.inventory.getStackInSlot(var11.slotIndex);
				if(var12 == null && this.selectedItem == null) {
					return;
				}

				if(var12 != null && this.selectedItem == null) {
					var6 = var3 == 0 ? var12.stackSize : (var12.stackSize + 1) / 2;
					this.selectedItem = var11.inventory.decrStackSize(var11.slotIndex, var6);
					if(var12.stackSize == 0) {
						var11.putStack((ItemStack)null);
					}

					var11.onPickupFromSlot();
				} else if(var12 == null && this.selectedItem != null && var11.isItemValid(this.selectedItem)) {
					var6 = var3 == 0 ? this.selectedItem.stackSize : 1;
					if(var6 > var11.inventory.getInventoryStackLimit()) {
						var6 = var11.inventory.getInventoryStackLimit();
					}

					var11.putStack(this.selectedItem.splitStack(var6));
					if(this.selectedItem.stackSize == 0) {
						this.selectedItem = null;
					}
				} else {
					if(var12 == null || this.selectedItem == null) {
						return;
					}

					ItemStack var9;
					if(!var11.isItemValid(this.selectedItem)) {
						if(var12.itemID == this.selectedItem.itemID) {
							var9 = this.selectedItem;
							if(var9.getItem().getItemStackLimit() > 1) {
								var6 = var12.stackSize;
								if(var6 > 0) {
									int var14 = var6 + this.selectedItem.stackSize;
									var9 = this.selectedItem;
									if(var14 <= var9.getItem().getItemStackLimit()) {
										this.selectedItem.stackSize += var6;
										var12.splitStack(var6);
										if(var12.stackSize == 0) {
											var11.putStack((ItemStack)null);
										}

										var11.onPickupFromSlot();
										return;
									}
								}

								return;
							}
						}

						return;
					}

					if(var12.itemID != this.selectedItem.itemID) {
						if(this.selectedItem.stackSize > var11.inventory.getInventoryStackLimit()) {
							return;
						}

						var11.putStack(this.selectedItem);
						this.selectedItem = var12;
					} else {
						if(var12.itemID != this.selectedItem.itemID) {
							return;
						}

						if(var3 == 0) {
							var6 = this.selectedItem.stackSize;
							if(var6 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.selectedItem;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.selectedItem;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.selectedItem.splitStack(var6);
							if(this.selectedItem.stackSize == 0) {
								this.selectedItem = null;
							}

							var12.stackSize += var6;
						} else {
							if(var3 != 1) {
								return;
							}

							var6 = 1;
							if(1 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.selectedItem;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.selectedItem;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.selectedItem.splitStack(var6);
							if(this.selectedItem.stackSize == 0) {
								this.selectedItem = null;
							}

							var12.stackSize += var6;
						}
					}
				}
			} else if(this.selectedItem != null) {
				int var13 = (this.width - this.xSize) / 2;
				var6 = (this.height - this.ySize) / 2;
				if(var1 < var13 || var2 < var6 || var1 >= var13 + this.xSize || var2 >= var6 + this.xSize) {
					Player var10 = this.minecraft.player;
					if(var3 == 0) {
						var10.drop(this.selectedItem);
						this.selectedItem = null;
					}

					if(var3 == 1) {
						var10.drop(this.selectedItem.splitStack(1));
						if(this.selectedItem.stackSize == 0) {
							this.selectedItem = null;
						}
					}
				}
			} else if(this.selectedItem == null) {
			      if(var3 == 0) {
			          this.minecraft.setCurrentScreen((GuiScreen)null);
			       }
				
			}
		}

	}

	protected final void onKeyPress(char var1, int var2) {
		if(var2 == this.minecraft.settings.inventoryKey.key | var2 == 1) {
			this.minecraft.setCurrentScreen((GuiScreen)null);
	        this.minecraft.grabMouse();
		}

	}
	
    public boolean doesGuiPause() {
  	  return false;
    }
 
}
