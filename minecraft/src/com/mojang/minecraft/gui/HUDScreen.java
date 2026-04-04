package com.mojang.minecraft.gui;

import com.mojang.minecraft.ChatLine;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gamemode.HardcoreGameMode;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.gui.ChatInputScreen;
import com.mojang.minecraft.gui.FontRenderer;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.render.ItemGuiRenderer;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class HUDScreen extends Screen {

   public List chat = new ArrayList();
   private Random random = new Random();
   private Minecraft mc;
   public String hoveredPlayer = null;
   public int ticks = 0;
   private Object ChatOpen;

   public HUDScreen(Minecraft var1) {
	  this.mc = var1;
   }

   public final void render(float var1, boolean var2, int var3, int var4) {
      Inventory var8 = this.mc.player.inventory;
      int var15;
      FontRenderer var5 = this.mc.fontRenderer;
      ScaledResolution slc = new ScaledResolution(this.mc.width, this.mc.height);
      int widt = slc.getScaledWidth();
      int heig = slc.getScaledHeight();

      TextureManager var6 = this.mc.textureManager;
      GL11.glBindTexture(3553, this.mc.textureManager.load("/gui/gui.png"));
      ShapeRenderer var7 = ShapeRenderer.instance;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      this.imgZ = -90.0F;
      this.drawImage(widt / 2 - 91, heig - 22, 0, 0, 182, 22);
      this.drawImage(widt / 2 - 91 - 1 + var8.selected * 20, heig - 22 - 1, 0, 22, 24, 22);
      GL11.glBindTexture(3553, this.mc.textureManager.load("/gui/icons.png"));
      this.drawImage(widt / 2 - 7, heig / 2 - 7, 0, 0, 16, 16);
      boolean var9 = this.mc.player.invulnerableTime / 3 % 2 == 1;
      if(this.mc.player.invulnerableTime < 10) {
         var9 = false;
      }

      int var10 = this.mc.player.health;
      int var11 = this.mc.player.lastHealth;
      this.random.setSeed((long)(this.ticks * 312871));
      int var12;
      int var14;
      int var26;
      
      int var29;
      int var30;
      if(this.mc.gamemode.isSurvival()) {
         for(var12 = 0; var12 < 10; ++var12) {
            byte var13 = 0;
            if(var9) {
               var13 = 1;
            }

            var14 = widt / 2 - 91 + (var12 << 3);
            var15 = heig - 32;
            
            if(this.mc.devMode) {
              var29 = widt / 2 - -10 + (var12 << 3);
              var30 = heig - 32;
              this.drawImage(var29, var30, 16, 9, 9, 9);
            }
            
            if(var10 <= 4) {
               var15 += this.random.nextInt(2);
            }

            if(this.mc.gamemode instanceof SurvivalGameMode) {
            this.drawImage(var14, var15, 16 + var13 * 9, 0, 9, 9);
            if(var9) {
               if((var12 << 1) + 1 < var11) {
                  this.drawImage(var14, var15, 70, 0, 9, 9);
               }

               if((var12 << 1) + 1 == var11) {
                  this.drawImage(var14, var15, 79, 0, 9, 9);
               }
            }

            if((var12 << 1) + 1 < var10) {
               this.drawImage(var14, var15, 52, 0, 9, 9);
            }

            if((var12 << 1) + 1 == var10) {
               this.drawImage(var14, var15, 61, 0, 9, 9);
            }} else if (this.mc.gamemode instanceof HardcoreGameMode) {
                this.drawImage(var14, var15, 16 + var13 * 9, 0, 9, 9);
                if(var9) {
                   if((var12 << 1) + 1 < var11) {
                      this.drawImage(var14, var15, 106, 0, 9, 9);
                   }

                   if((var12 << 1) + 1 == var11) {
                      this.drawImage(var14, var15, 115, 0, 9, 9);
                   }
                }

                if((var12 << 1) + 1 < var10) {
                   this.drawImage(var14, var15, 88, 0, 9, 9);
                }

                if((var12 << 1) + 1 == var10) {
                   this.drawImage(var14, var15, 97, 0, 9, 9);
                }
             }
         }

         if(this.mc.player.isUnderWater()) {
            var12 = (int)Math.ceil((double)(this.mc.player.airSupply - 2) * 10.0D / 300.0D);
            var26 = (int)Math.ceil((double)this.mc.player.airSupply * 10.0D / 300.0D) - var12;

            for(var14 = 0; var14 < var12 + var26; ++var14) {
               if(var14 < var12) {
                  this.drawImage(widt / 2 - 91 + (var14 << 3), heig - 32 - 9, 16, 18, 9, 9);
               } else {
                  this.drawImage(widt / 2 - 91 + (var14 << 3), heig - 32 - 9, 25, 18, 9, 9);
               }
            }
         }
      }

      GL11.glDisable(3042);

    String var23;
  	GL11.glDisable(GL11.GL_BLEND);
	GL11.glEnable(GL11.GL_NORMALIZE);
	GL11.glPushMatrix();
	GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
	ItemGuiRenderer.enableStandardItemLighting();
	GL11.glPopMatrix();
	int var20;
	
	for(var12 = 0; var12 < 9; ++var12) {
		var26 = widt / 2 - 90 + var12 * 20 + 2;
		var14 = heig - 16 - 3;
		ItemStack var13 = this.mc.player.inventory.Inventory[var12];
		if(var13 == null) {
			if(var12 > 45) {
				GL11.glDisable(GL11.GL_LIGHTING);
				var15 = var6.load("/items.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
				this.drawImage(var26, var14, 240, 63 - var12 << 4, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
			}
		} else {
			String var38 = null;
			var20 = var13.itemID;
			if(var13.itemID < 256) {
				GL11.glDisable(GL11.GL_LIGHTING);
				var15 = var6.load("/terrain.png");
				GL11.glBindTexture(3553, var15);
				Block var37 = Block.blocks[var20];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(var26 - 2), (float)(var14 + 3), -50.0F);
				
				/*if((float)var8.Inventory[var12].animationsToGo > 0) {
		            float var18;
		            float var21 = -MathHelper.sin((var18 = ((float)var8.Inventory[var12].animationsToGo - var1) / 5.0F) * var18 * 3.1415927F) * 8.0F;
		            float var19 = MathHelper.sin(var18 * var18 * 3.1415927F) + 1.0F;
		            float var16 = MathHelper.sin(var18 * 3.1415927F) + 1.0F;
		            GL11.glTranslatef(10.0F, var21 + 10.0F, 0.0F);
		            GL11.glScalef(var19, var16, 1.0F);
		            GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
		        }*/
				
	            GL11.glScalef(10.0F, 10.0F, 10.0F);
	            GL11.glTranslatef(1.0F, 0.5F, 0.0F);
	            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
	            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	            GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
	            GL11.glScalef(-1.0F, -1.0F, -1.0F);
			    var7.begin();
		        var37.renderFullbright(var7);
		        var7.end();
				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_LIGHTING);
			} else if(var13.getItem().getIconIndex() >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				var15 = this.mc.textureManager.load("/items.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
				this.drawImage(var26, var14, var13.getItem().getIconIndex() % 16 << 4, var13.getItem().getIconIndex() / 16 << 4, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);				
			}
			
			if(var13.stackSize > 1) {
				var38 = "" + var13.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				var5.render(var38, var26 + 19 - 2 - var5.getWidth(var38), var14 + 6 + 3, 16777215);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
			
		}
	  }

      ItemGuiRenderer.disableStandardItemLighting();
      GL11.glDisable(GL11.GL_NORMALIZE);

      
	  var5.render(this.mc.MOD_NAME + " " + this.mc.VER + " " + this.mc.TAG, 2, 2, 16777215);
	  
	  if(this.mc.settings.debugScreen) {
		  var5.render(this.mc.debug, 2, 12, 16777215);
		  long maxmem = Runtime.getRuntime().maxMemory();
		  long totalmem = Runtime.getRuntime().totalMemory();
		  long freemem = Runtime.getRuntime().freeMemory();
		  long mem = maxmem - freemem;
		  var5.render("Free memory: " + mem * 100L / maxmem + "% of " + maxmem / 1024L / 1024L + "MB", 2, 32, 16777215);
		  var5.render("Allocated memory: " + totalmem * 100L / maxmem + "% (" + totalmem / 1024L / 1024L + "MB)", 2, 42, 16777215);		
		  var5.render("XYZ: " + this.mc.player.x + " / " + this.mc.player.y + " / " + this.mc.player.z, 2, 62, 16777215);
		  var5.render("R: " + this.mc.player.xRot + " / " + this.mc.player.yRot, 2, 72, 16777215);
		  
          String gamemode = "Gamemode: ";
          String creative = "Creative";
          String survival = "Survival";
          String hardcore = "Hardcore";
          if(this.mc.level.gamemode == 0) {
        	  var5.render(gamemode + creative, 2, 82, 16777215);
          } else if (this.mc.level.gamemode == 1) {
        	  var5.render(gamemode + survival, 2, 82, 16777215);
          } else if (this.mc.level.gamemode == 2) {
        	  var5.render(gamemode + hardcore, 2, 82, 16777215);
          }
          
          var5.render("Level size: " + this.mc.level.width + " / " + this.mc.level.height + " / " + this.mc.level.depth, 2, 92, 16777215);
          
          String theme = "Level theme: ";
          String defaultTheme = "Normal";
          String hellTheme = "Hell";
          String snowTheme = "Snow";
          String desertTheme = "Desert";
                    
          if(this.mc.level.levelType == 0) {
        	  var5.render(theme + defaultTheme, 2, 102, 16777215);
          } else if (this.mc.level.levelType == 1) {
        	  var5.render(theme + hellTheme, 2, 102, 16777215);
          } else if (this.mc.level.levelType == 2) {
        	  var5.render(theme + snowTheme, 2, 102, 16777215);
          } else if (this.mc.level.levelType == 3) {
        	  var5.render(theme + desertTheme, 2, 102, 16777215);
          }
	      
	      if(this.mc.devMode) {
	    	  var5.render("Level seed: " + this.mc.level.seed, 2, 112, 16777215);
	    	  var5.render("Developer Mode: " + this.mc.devMode, 2, 122, 16777215);
	      }

	      
	  }
	  
      if(this.mc.gamemode.isSurvival()) {   	 
         String var24 = "Score: &e" + this.mc.player.getScore();
         var5.render(var24, widt - var5.getWidth(var24) - 2, 2, 16777215);
         if(this.mc.devMode) {
        	 var5.render("Arrows: " + this.mc.player.arrows, widt / 2 + 18, heig - 41, 16777215);
         } else {
        	 var5.render("Arrows: " + this.mc.player.arrows, widt / 2 + 8, heig - 33, 16777215);
         }
      }

      byte var25 = 10;
      boolean var27 = false;
      if(this.mc.currentScreen instanceof ChatInputScreen) {
         var25 = 20;
         var27 = true;
      }

      for(var14 = 0; var14 < this.chat.size() && var14 < var25; ++var14) {
         if(((ChatLine)this.chat.get(var14)).time < 200 || var27) {
        	drawBox(2, heig - 8 - var14 * 9 - 20, ((ChatLine)this.chat.get(var14)).message.length() * 6, heig - 2 - var14 * 9 - 17, Integer.MIN_VALUE);
            var5.render(((ChatLine)this.chat.get(var14)).message, 2, heig - 8 - var14 * 9 - 20, 16777215);
         }
      }

      this.hoveredPlayer = null;
      
      var14 = widt / 2;
      var15 = heig / 2;
      
      if(Keyboard.isKeyDown(this.mc.settings.playersListKey.key) && this.mc.networkManager != null && this.mc.networkManager.isConnected() && this.mc.currentScreen == null | this.mc.currentScreen instanceof ChatInputScreen) {
          List var22 = this.mc.networkManager.getPlayers();
          GL11.glEnable(3042);
          GL11.glDisable(3553);
          GL11.glBlendFunc(770, 771);
          GL11.glBegin(7);
          GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
          GL11.glVertex2f((float)(var14 + 128), (float)(var15 - 68 - 12));
          GL11.glVertex2f((float)(var14 - 128), (float)(var15 - 68 - 12));
          GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
          GL11.glVertex2f((float)(var14 - 128), (float)(var15 + 68));
          GL11.glVertex2f((float)(var14 + 128), (float)(var15 + 68));
          GL11.glEnd();
          GL11.glDisable(3042);
          GL11.glEnable(3553);
          var23 = "Connected players:";
          var5.render(var23, var14 - var5.getWidth(var23) / 2, var15 - 64 - 12, 16777215);

          for(var11 = 0; var11 < var22.size(); ++var11) {
             int var28 = var14 + var11 % 2 * 120 - 120;
             int var17 = var15 - 64 + (var11 / 2 << 3);
             if(var2 && var3 >= var28 && var4 >= var17 && var3 < var28 + 120 && var4 < var17 + 8) {
                this.hoveredPlayer = (String)var22.get(var11);
                var5.renderNoShadow((String)var22.get(var11), var28 + 2, var17, 16777215);
             } else {
                var5.renderNoShadow((String)var22.get(var11), var28, var17, 15658734);
             }
          }
       }
      
   }
      
	public boolean ChatOpen(ChatInputScreen var1)
	{
		return var1 != null;
	}
	
    public final void addChat(String var1) {
       this.chat.add(0, new ChatLine(var1));

       while(this.chat.size() > 50) {
          this.chat.remove(this.chat.size() - 1);
       }

    }
 }
