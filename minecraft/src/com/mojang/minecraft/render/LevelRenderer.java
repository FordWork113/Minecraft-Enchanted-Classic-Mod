package com.mojang.minecraft.render;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.MovingObjectPosition;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Weather;
import com.mojang.minecraft.level.generator.LevelGenerator;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.Chunk;
import com.mojang.minecraft.render.ChunkDistanceComparator;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.Vec3D;
import com.mojang.minecraft.phys.AABB;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class LevelRenderer {

   public Level level;
   public TextureManager textureManager;
   public int listId;
   public IntBuffer buffer = BufferUtils.createIntBuffer(65536);
   public List chunks = new ArrayList();
   private Chunk[] loadQueue;
   public Chunk[] chunkCache;
   private int xChunks;
   private int yChunks;
   private int zChunks;
   private int baseListId;
   public Minecraft minecraft;
   private int[] chunkDataCache = new int['\uc350'];
   public int ticks = 0;
   private float lastLoadX = -9999.0F;
   private float lastLoadY = -9999.0F;
   private float lastLoadZ = -9999.0F;
   public float cracks;
   private FloatBuffer cloudBuffer = BufferUtils.createFloatBuffer(16);
   public int cloudOffsetX = 0;
   public Weather weather;
   float var1197;
   
   public LevelRenderer(Minecraft var1, TextureManager var2) {
      this.minecraft = var1;
      this.textureManager = var2;
      this.listId = GL11.glGenLists(2);
      this.baseListId = GL11.glGenLists(4096 << 6 << 1);
   }
   
   public final void refresh() {
		int var1;
		if(this.chunkCache != null) {
			for(var1 = 0; var1 < this.chunkCache.length; ++var1) {
				this.chunkCache[var1].dispose();
			}
		}
		
		this.xChunks = this.level.width / 16;
	    this.yChunks = this.level.depth / 16;
	    this.zChunks = this.level.height / 16;
	    this.chunkCache = new Chunk[this.xChunks * this.yChunks * this.zChunks];
	    this.loadQueue = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		var1 = 0;

		int var2;
		int var4;
		for(var2 = 0; var2 < this.xChunks; ++var2) {
	         for(int var3 = 0; var3 < this.yChunks; ++var3) {
	             for(var4 = 0; var4 < this.zChunks; ++var4) {
					 this.chunkCache[(var4 * this.yChunks + var3) * this.xChunks + var2] = new Chunk(this.level, var2 << 4, var3 << 4, var4 << 4, 16, this.baseListId + var1);
		             this.loadQueue[(var4 * this.yChunks + var3) * this.xChunks + var2] = this.chunkCache[(var4 * this.yChunks + var3) * this.xChunks + var2];
					var1 += 2;
				}
			}
		}

		 for(var2 = 0; var2 < this.chunks.size(); ++var2) {
	         ((Chunk)this.chunks.get(var2)).loaded = false;
	      }

	    this.chunks.clear();
	    GL11.glNewList(this.listId, 4864);
		float var18 = 0.5F;
		GL11.glColor4f(0.5F, var18, var18, 1.0F);
		ShapeRenderer var12 = ShapeRenderer.instance;
		float var13 = this.level.getGroundLevel();

		var4 = 128;
		if(128 > this.level.width) {
			var4 = this.level.width;
		}

		if(var4 > this.level.depth) {
			var4 = this.level.depth;
		}

		int var5 = 2048 / var4;
		var12.begin();
		
		int var6;
		if(this.level.borderType == 0) {
		  for(var6 = -var4 * var5; var6 < this.level.width + var4 * var5; var6 += var4) {
		     for(int var11 = -var4 * var5; var11 < this.level.height + var4 * var5; var11 += var4) {
		        var18 = var13;
		          if (var6 >= 0 && var11 >= 0 && var6 < this.level.width && var11 < this.level.height) {
		             var18 = 0.0F;
		           }

		           var12.vertexUV((float)var6, var18, (float)(var11 + var4), 0.0F, (float)var4);
		           var12.vertexUV((float)(var6 + var4), var18, (float)(var11 + var4), (float)var4, (float)var4);
		           var12.vertexUV((float)(var6 + var4), var18, (float)var11, (float)var4, 0.0F);
		           var12.vertexUV((float)var6, var18, (float)var11, 0.0F, 0.0F);
		    }
		}
	    
	    var12.end();
	    GL11.glColor3f(0.8F, 0.8F, 0.8F);
	    var12.begin();
	    
	    GL11.glColor3f(0.6F, 0.6F, 0.6F);
	     
		} else if(this.level.borderType == 1) {
		for(var6 = -var4 * var5; var6 < this.level.width + var4 * var5; var6 += var4) {
			for(int var7 = -var4 * var5; var7 < this.level.height + var4 * var5; var7 += var4) {
				if(var13 < 0.0F || var6 < 0 || var7 < 0 || var6 >= this.level.width || var7 >= this.level.height) {
					var12.vertexUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
					var12.vertexUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var12.vertexUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var12.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
				}
			}

		}
	      
	    var12.end();
		}
	    GL11.glColor3f(0.8F, 0.8F, 0.8F);
	    var12.begin();

	    GL11.glColor3f(0.6F, 0.6F, 0.6F);

	    for(var6 = 0; var6 < this.level.height; var6 += var4) {
	       var12.vertexUV(0.0F, var13, (float)var6, 0.0F, 0.0F);
	       var12.vertexUV(0.0F, var13, (float)(var6 + var4), (float)var4, 0.0F);
	       var12.vertexUV(0.0F, 0.0F, (float)(var6 + var4), (float)var4, var13);
	       var12.vertexUV(0.0F, 0.0F, (float)var6, 0.0F, var13);
	       var12.vertexUV((float)this.level.width, 0.0F, (float)var6, 0.0F, var13);
	       var12.vertexUV((float)this.level.width, 0.0F, (float)(var6 + var4), (float)var4, var13);
	       var12.vertexUV((float)this.level.width, var13, (float)(var6 + var4), (float)var4, 0.0F);
	       var12.vertexUV((float)this.level.width, var13, (float)var6, 0.0F, 0.0F);
	    }
	    
	    for(var6 = 0; var6 < this.level.width; var6 += var4) {
	       var12.vertexUV((float)var6, 0.0F, 0.0F, 0.0F, 0.0F);
	       var12.vertexUV((float)(var6 + var4), 0.0F, 0.0F, (float)var4, 0.0F);
	       var12.vertexUV((float)(var6 + var4), var13, 0.0F, (float)var4, var13);
	       var12.vertexUV((float)var6, var13, 0.0F, 0.0F, var13);
	       var12.vertexUV((float)var6, var13, (float)this.level.height, 0.0F, var13);
	       var12.vertexUV((float)(var6 + var4), var13, (float)this.level.height, (float)var4, var13);
	       var12.vertexUV((float)(var6 + var4), 0.0F, (float)this.level.height, (float)var4, 0.0F);
	       var12.vertexUV((float)var6, 0.0F, (float)this.level.height, 0.0F, 0.0F);
	    }
	    
		var12.end();
		GL11.glEndList();
		GL11.glNewList(this.listId + 1, 4864);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		float var14 = (float)this.level.getWaterLevel();
		GL11.glBlendFunc(770, 771);
		ShapeRenderer var15 = ShapeRenderer.instance;
		var4 = 128;
		if(128 > this.level.width) {
			var4 = this.level.width;
		}

		if(var4 > this.level.depth) {
			var4 = this.level.depth;
		}

		var5 = 2048 / var4;
		var15.begin();			
		float var16 = Block.WATER.x1;
		float var17 = Block.WATER.z1;

		for(int var8 = -var4 * var5; var8 < this.level.width + var4 * var5; var8 += var4) {
			for(int var9 = -var4 * var5; var9 < this.level.depth + var4 * var5; var9 += var4) {
				float var10 = var14 + Block.WATER.y1;
				if(var14 < 0.0F || var8 < 0 || var9 < 0 || var8 >= this.level.width || var9 >= this.level.height) {
					var15.vertexUV((float)var8 + var16, var10, (float)(var9 + var4) + var17, 0.0F, (float)var4);
					var15.vertexUV((float)(var8 + var4) + var16, var10, (float)(var9 + var4) + var17, (float)var4, (float)var4);
					var15.vertexUV((float)(var8 + var4) + var16, var10, (float)var9 + var17, (float)var4, 0.0F);
					var15.vertexUV((float)var8 + var16, var10, (float)var9 + var17, 0.0F, 0.0F);
					var15.vertexUV((float)var8 + var16, var10, (float)var9 + var17, 0.0F, 0.0F);
					var15.vertexUV((float)(var8 + var4) + var16, var10, (float)var9 + var17, (float)var4, 0.0F);
					var15.vertexUV((float)(var8 + var4) + var16, var10, (float)(var9 + var4) + var17, (float)var4, (float)var4);
					var15.vertexUV((float)var8 + var16, var10, (float)(var9 + var4) + var17, 0.0F, (float)var4);
				}
			}
		}
	

		var15.end();
		GL11.glDisable(3042);
		GL11.glEndList();
		this.queueChunks(0, 0, 0, this.level.width, this.level.depth, this.level.height);
		Block.LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.BIRCH_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SPRUCE_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.BLACKBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.REDBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.OTHER_BLACKBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.OTHER_REDBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SNOW_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SNOW_BIRCH_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SNOW_SPRUCE_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SNOW_OTHER_BLACKBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
		Block.SNOW_OTHER_REDBERRY_LEAVES.setFancy(this.minecraft.settings.fancyGraphics);
	}

   public final int sortChunks(Player var1, int var2) {
      float var3 = var1.x - this.lastLoadX;
      float var4 = var1.y - this.lastLoadY;
      float var5 = var1.z - this.lastLoadZ;
      if(var3 * var3 + var4 * var4 + var5 * var5 > 64.0F) {
         this.lastLoadX = var1.x;
         this.lastLoadY = var1.y;
         this.lastLoadZ = var1.z;
         Arrays.sort(this.loadQueue, new ChunkDistanceComparator(var1));
      }

      int var6 = 0;

      for(int var7 = 0; var7 < this.loadQueue.length; ++var7) {
         var6 = this.loadQueue[var7].appendLists(this.chunkDataCache, var6, var2);
      }

      this.buffer.clear();
      this.buffer.put(this.chunkDataCache, 0, var6);
      this.buffer.flip();
      if(this.buffer.remaining() > 0) {
         GL11.glBindTexture(3553, this.textureManager.load("/terrain.png"));
         GL11.glCallLists(this.buffer);
      }

      return this.buffer.remaining();
   }

   public final void queueChunks(int var1, int var2, int var3, int var4, int var5, int var6) {
      var1 /= 16;
      var2 /= 16;
      var3 /= 16;
      var4 /= 16;
      var5 /= 16;
      var6 /= 16;
      if(var1 < 0) {
         var1 = 0;
      }

      if(var2 < 0) {
         var2 = 0;
      }

      if(var3 < 0) {
         var3 = 0;
      }

      if(var4 > this.xChunks - 1) {
         var4 = this.xChunks - 1;
      }

      if(var5 > this.yChunks - 1) {
         var5 = this.yChunks - 1;
      }

      if(var6 > this.zChunks - 1) {
         var6 = this.zChunks - 1;
      }

      for(var1 = var1; var1 <= var4; ++var1) {
         for(int var7 = var2; var7 <= var5; ++var7) {
            for(int var8 = var3; var8 <= var6; ++var8) {
               Chunk var9;
               if(!(var9 = this.chunkCache[(var8 * this.yChunks + var7) * this.xChunks + var1]).loaded) {
                  var9.loaded = true;
                  this.chunks.add(this.chunkCache[(var8 * this.yChunks + var7) * this.xChunks + var1]);
               }
            }
         }
      }

   }

   public final void renderVolumetricClouds() {
	      GL11.glDisable(2884);
	      ShapeRenderer t = ShapeRenderer.instance;
	      float h = 4.0F;
	      float xo = (this.minecraft.player.xo + (this.minecraft.player.x - this.minecraft.player.xo) * (float)var1197 + (float)(((float)this.ticks + this.cloudOffsetX) * 0.03F)) / 12.0F;
	      float zo = (float) ((this.minecraft.player.zo + (this.minecraft.player.z - this.minecraft.player.zo) * (double)var1197) / 12.0F + 0.33000001311302185D);
	      float yy = (float)this.level.cloudHeight;
	      int xOffs = (int) Math.floor(xo / 2048.0D);
	      int zOffs = (int) Math.floor(zo / 2048.0D);
	      xo -= (float)(xOffs * 2048.0D);
	      zo -= (float)(zOffs * 2048.0D); 
		  GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE1_ARB);
		  GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureManager.load("/fluff.png"));
		  GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
		  GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.flipBuffer(1.0F, 0.0F, 0.0F, 0.0F));
		  GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
		  GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.flipBuffer(0.0F, 0.0F, 1.0F, 0.0F));
		  GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
		  GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
		  GL11.glEnable(GL11.GL_TEXTURE_2D);
		  GL11.glMatrixMode(GL11.GL_TEXTURE);
		  GL11.glLoadIdentity();
		  GL11.glScalef(0.25F, 0.25F, 0.25F);
		  GL11.glTranslatef((float)xo, (float)zo, 0.0F);
		  GL11.glMatrixMode(GL11.GL_MODELVIEW);
		  GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		  GL11.glEnable(GL11.GL_TEXTURE_2D);
		  GL11.glBindTexture(3553, this.textureManager.load("/environment/clouds.png"));
		  GL11.glEnable(3042);
		  GL11.glBlendFunc(770, 771);
	      Vec3D cc = this.level.cloudcolor();
	      float cr = (float)cc.x;
	      float cg = (float)cc.y;
	      float cb = (float)cc.z;
	      float uo;
	      float vo;
	      float scale;
	      
	      if (this.minecraft.settings.anaglyph) {
	          uo = (cr * 30.0F + cg * 59.0F + cb * 11.0F) / 100.0F;
	          vo = (cr * 30.0F + cg * 70.0F) / 100.0F;
	          scale = (cr * 30.0F + cb * 70.0F) / 100.0F;
	          cr = uo;
	          cg = vo;
	          cb = scale;
	       }

	      uo = (float)(xo * 0.0D);
	      vo = (float)(zo * 0.0D);
	      scale = 0.00390625F;
	      uo = (float)Math.floor(xo) * scale;
	      vo = (float)Math.floor(zo) * scale;
	      float xoffs = (float)(xo - (float)Math.floor(xo));
	      float zoffs = (float)(zo - (float)Math.floor(zo));
	      int D = 8;
	      int radius = 4;
	      float e = 9.765625E-4F;
	      GL11.glScalef(12.0F, 1.0F, 12.0F);

	      for(int pass = 0; pass < 2; ++pass) {
	         if (pass == 0) {
	            GL11.glColorMask(false, false, false, false);
	         } else {
	            GL11.glColorMask(true, true, true, true);
	         }

	         for(int xPos = -radius + 1; xPos <= radius; ++xPos) {
	            for(int zPos = -radius + 1; zPos <= radius; ++zPos) {
	               t.begin();
	               float xx = (float)(xPos * D);
	               float zz = (float)(zPos * D);
	               float xp = xx - xoffs;
	               float zp = zz - zoffs;
	               if (yy > -h - 1.0F) {
	                  t.color(cr * 0.75F, cg * 0.75F, cb * 0.75F);
	                  t.normal(0.0F, -1.0F, 0.0F);
	                  t.vertexUV((float)(xp + 0.0F), (float)(yy + 0.0F), (float)(zp + (float)D), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                  t.vertexUV((float)(xp + (float)D), (float)(yy + 0.0F), (float)(zp + (float)D), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                  t.vertexUV((float)(xp + (float)D), (float)(yy + 0.0F), (float)(zp + 0.0F), (float)((xx + (float)D) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                  t.vertexUV((float)(xp + 0.0F), (float)(yy + 0.0F), (float)(zp + 0.0F), (float)((xx + 0.0F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	               }

	               if (yy <= h + 1.0F) {
	                  t.color(cr, cg, cb);
	                  t.normal(0.0F, 1.0F, 0.0F);
	                  t.vertexUV((float)(xp + 0.0F), (float)(yy + h - e), (float)(zp + (float)D), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                  t.vertexUV((float)(xp + (float)D), (float)(yy + h - e), (float)(zp + (float)D), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                  t.vertexUV((float)(xp + (float)D), (float)(yy + h - e), (float)(zp + 0.0F), (float)((xx + (float)D) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                  t.vertexUV((float)(xp + 0.0F), (float)(yy + h - e), (float)(zp + 0.0F), (float)((xx + 0.0F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	               }

	               t.color(cr * 0.9F, cg * 0.9F, cb * 0.9F);
	               int i;
	               if (xPos > -1) {
	                  t.normal(-1.0F, 0.0F, 0.0F);

	                  for(i = 0; i < D; ++i) {
	                     t.vertexUV((float)(xp + (float)i + 0.0F), (float)(yy + 0.0F), (float)(zp + (float)D), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 0.0F), (float)(yy + h), (float)(zp + (float)D), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 0.0F), (float)(yy + h), (float)(zp + 0.0F), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 0.0F), (float)(yy + 0.0F), (float)(zp + 0.0F), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                  }
	               }

	               if (xPos <= 1) {
	                  t.normal(1.0F, 0.0F, 0.0F);

	                  for(i = 0; i < D; ++i) {
	                     t.vertexUV((float)(xp + (float)i + 1.0F - e), (float)(yy + 0.0F), (float)(zp + (float)D), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 1.0F - e), (float)(yy + h), (float)(zp + (float)D), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + (float)D) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 1.0F - e), (float)(yy + h), (float)(zp + 0.0F), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)i + 1.0F - e), (float)(yy + 0.0F), (float)(zp + 0.0F), (float)((xx + (float)i + 0.5F) * scale + uo), (float)((zz + 0.0F) * scale + vo));
	                  }
	               }

	               t.color(cr * 0.8F, cg * 0.8F, cb * 0.8F);
	               if (zPos > -1) {
	                  t.normal(0.0F, 0.0F, -1.0F);

	                  for(i = 0; i < D; ++i) {
	                     t.vertexUV((float)(xp + 0.0F), (float)(yy + h), (float)(zp + (float)i + 0.0F), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)D), (float)(yy + h), (float)(zp + (float)i + 0.0F), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)D), (float)(yy + 0.0F), (float)(zp + (float)i + 0.0F), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + 0.0F), (float)(yy + 0.0F), (float)(zp + (float)i + 0.0F), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                  }
	               }

	               if (zPos <= 1) {
	                  t.normal(0.0F, 0.0F, 1.0F);

	                  for(i = 0; i < D; ++i) {
	                     t.vertexUV((float)(xp + 0.0F), (float)(yy + h), (float)(zp + (float)i + 1.0F - e), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)D), (float)(yy + h), (float)(zp + (float)i + 1.0F - e), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + (float)D), (float)(yy + 0.0F), (float)(zp + (float)i + 1.0F - e), (float)((xx + (float)D) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                     t.vertexUV((float)(xp + 0.0F), (float)(yy + 0.0F), (float)(zp + (float)i + 1.0F - e), (float)((xx + 0.0F) * scale + uo), (float)((zz + (float)i + 0.5F) * scale + vo));
	                  }
	               }

	               t.end();
	            }
	         }
	      }

	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	      GL11.glDisable(3042);
	      GL11.glEnable(2884);
		  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		  GL11.glDisable(GL11.GL_BLEND);
		  GL11.glEnable(GL11.GL_CULL_FACE);
		  GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE1_ARB);
		  GL11.glDisable(GL11.GL_TEXTURE_2D);
		  GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
	   }
   
	private FloatBuffer flipBuffer(float var1, float var2, float var3, float var4) {
		this.cloudBuffer.clear();
		this.cloudBuffer.put(var1).put(0.0F).put(var3).put(0.0F);
		this.cloudBuffer.flip();
		return this.cloudBuffer;
	}
	
	public final void updateRenderers(Mob var1) {
		   Collections.sort(this.chunks, new ChunkDirtyDistanceComparator(var1));
		   int var2 = this.chunks.size() - 1;
		   int var3 = this.chunks.size();

		   for(int var4 = 0; var4 < var3; ++var4) {
		     Chunk var5;
		     if ((var5 = (Chunk)this.chunks.get(var2 - var4)).distanceSquared(var1) > 2500.0F && var4 > 2) {
		        return;
		     }

		     this.chunks.remove(var5);
		     var5.update();
		     var5.loaded = false;
		 }

	}

	public final void clipRenderersByFrustrum(Frustrum var1) {
		for(int var2 = 0; var2 < this.chunkCache.length; ++var2) {
		     this.chunkCache[var2].clip(var1);
	    }

	}

	public final void renderAllRenderLists() {
		GL11.glBindTexture(3553, this.textureManager.load("/terrain.png"));
		GL11.glCallLists(this.buffer);
    }

	public final float getDistanceToCamera(float float1, float float2, float float3) {
		float1 /= this.lastLoadX;
		float2 /= this.lastLoadY;
		float3 /= this.lastLoadZ;
		return float1 * float1 + float2 * float2 + float3 * float3;
	}
	
	   public final void renderHit(Player var1, MovingObjectPosition var2, int var3, ItemStack var4) {
		      ShapeRenderer var5 = ShapeRenderer.instance;
		      GL11.glEnable(3042);
		      GL11.glEnable(3008);
		      GL11.glBlendFunc(770, 1);
		      GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		      float var6;
		      if (var3 == 0) {
		         GL11.glEnable(3042);
		         GL11.glEnable(3008);
		         GL11.glBlendFunc(770, 771);
		         GL11.glDepthMask(true);
		         GL11.glColorMask(true, true, true, true);
		         GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		         GL11.glPushMatrix();
		         GL11.glTranslatef((float)var2.x + 0.5F, (float)var2.y + 0.5F, (float)var2.z + 0.5F);
		         var6 = 1.01F;
		         GL11.glScalef(1.01F, var6, var6);
		         GL11.glTranslatef(-((float)var2.x + 0.5F), -((float)var2.y + 0.5F), -((float)var2.z + 0.5F));
		         var5.begin();
		         var5.noColor();
		         GL11.glDepthMask(false);

		         for(byte var7 = 0; var7 < 6; ++var7) {
		        	 Block.STONE.renderSide(var5, var2.x, var2.y, var2.z, var7, 0);
		         }

		         var5.end();
		         GL11.glDepthMask(true);
		         GL11.glPopMatrix();
		         GL11.glDisable(3553);
		      } else {
		         GL11.glBlendFunc(770, 771);
		         GL11.glColor4f(var6 = (float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.8F, var6, var6, (float)Math.sin((double)System.currentTimeMillis() / 200.0D) * 0.2F + 0.5F);
		         GL11.glEnable(3553);
		         int var9 = this.textureManager.load("/terrain.png");
		         GL11.glBindTexture(3553, var9);
		         var9 = var2.x;
		         var3 = var2.y;
		         int var8 = var2.z;
		         if (var2.face == 0) {
		            --var3;
		         }

		         if (var2.face == 1) {
		            ++var3;
		         }

		         if (var2.face == 2) {
		            --var8;
		         }

		         if (var2.face == 3) {
		            ++var8;
		         }

		         if (var2.face == 4) {
		            --var9;
		         }

		         if (var2.face == 5) {
		            ++var9;
		         }

		         var5.begin();
		         var5.noColor();
		         if (var4 != null && var4.itemID < 256) {
		             Block.blocks[var4.itemID].render(this.level, var9, var3, var8, var5);
		         }
		         
		         var5.end();
		         GL11.glDisable(3553);
		      }

		      GL11.glDisable(3042);
		      GL11.glDisable(3008);
		   }
	   
	   public static void renderHitOutline(MovingObjectPosition var0, int var1) {
		      GL11.glEnable(3042);
		      GL11.glBlendFunc(770, 771);
		      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.2F);
		      float var2 = (float)var0.x;
		      float var3 = (float)var0.y;
		      float var4 = (float)var0.z;
		      if (var1 == 1) {
		         if (var0.face == 0) {
		            --var3;
		         }

		         if (var0.face == 1) {
		            ++var3;
		         }

		         if (var0.face == 2) {
		            --var4;
		         }

		         if (var0.face == 3) {
		            ++var4;
		         }

		         if (var0.face == 4) {
		            --var2;
		         }

		         if (var0.face == 5) {
		            ++var2;
		         }
		      }

		      GL11.glBegin(3);
		      GL11.glVertex3f(var2, var3, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3, var4);
		      GL11.glEnd();
		      GL11.glBegin(3);
		      GL11.glVertex3f(var2, var3 + 1.0F, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3 + 1.0F, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3 + 1.0F, var4);
		      GL11.glEnd();
		      GL11.glBegin(1);
		      GL11.glVertex3f(var2, var3, var4);
		      GL11.glVertex3f(var2, var3 + 1.0F, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4);
		      GL11.glVertex3f(var2 + 1.0F, var3, var4 + 1.0F);
		      GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3, var4 + 1.0F);
		      GL11.glVertex3f(var2, var3 + 1.0F, var4 + 1.0F);
		      GL11.glEnd();
		   }
}
