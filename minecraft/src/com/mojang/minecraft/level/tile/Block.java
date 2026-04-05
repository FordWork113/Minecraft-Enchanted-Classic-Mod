package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.MovingObjectPosition;
import com.mojang.minecraft.item.ItemBlock;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.slab.BrickSlabBlock;
import com.mojang.minecraft.level.tile.slab.CobbleSlabBlock;
import com.mojang.minecraft.level.tile.slab.DirtSlabBlock;
import com.mojang.minecraft.level.tile.slab.MossyCobbleSlabBlock;
import com.mojang.minecraft.level.tile.slab.SandStoneSlabBlock;
import com.mojang.minecraft.level.tile.slab.SlabBlock;
import com.mojang.minecraft.level.tile.slab.StoneBrickSlabBlock;
import com.mojang.minecraft.level.tile.slab.StoneSlabBlock;
import com.mojang.minecraft.level.tile.slab.WoodSlabBlock;
import com.mojang.minecraft.model.Vec3D;
import com.mojang.minecraft.particle.ParticleManager;
import com.mojang.minecraft.particle.TerrainParticle;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Block
{
	protected Block(int id)
	{
		explodes = true;
		blocks[id] = this;
		this.id = id;

		setBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		opaque[id] = isSolid();
		cube[id] = isCube();
		liquid[id] = false;
	}

	protected Block(int id, int textureID)
	{
		this(id);

		textureId = textureID;
	}

	protected static Random random = new Random();
	public static final Block[] blocks = new Block[256];
	public static final boolean[] physics = new boolean[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	public static boolean[] opaque = new boolean[256];
	private static boolean[] cube = new boolean[256];
	public static final boolean[] liquid = new boolean[256];
	private static int[] tickDelay = new int[256];
	public static final Block STONE;
	public static final Block GRASS;
	public static final Block DIRT;
	public static final Block COBBLESTONE;
	public static final Block WOOD;
	public static final Block SAPLING;
	public static final Block BEDROCK;
	public static final Block WATER;
	public static final Block STATIONARY_WATER;
	public static final Block LAVA;
	public static final Block STATIONARY_LAVA;
	public static final Block SAND;
	public static final Block GRAVEL;
	public static final Block GOLD_ORE;
	public static final Block IRON_ORE;
	public static final Block COAL_ORE;
	public static final Block LOG;
	public static final Block LEAVES;
	public static final Block SPONGE;
	public static final Block GLASS;
	public static final Block RED_WOOL;
	public static final Block ORANGE_WOOL;
	public static final Block YELLOW_WOOL;
	public static final Block LIME_WOOL;
	public static final Block GREEN_WOOL;
	public static final Block AQUA_GREEN_WOOL;
	public static final Block CYAN_WOOL;
	public static final Block BLUE_WOOL;
	public static final Block PURPLE_WOOL;
	public static final Block INDIGO_WOOL;
	public static final Block VIOLET_WOOL;
	public static final Block MAGENTA_WOOL;
	public static final Block RED_PINK_WOOL;
	public static final Block DARKGRAY_WOOL;
	public static final Block GRAY_WOOL;
	public static final Block WHITE_WOOL;
	public static final Block DANDELION;
	public static final Block ROSE;
	public static final Block BROWN_MUSHROOM;
	public static final Block RED_MUSHROOM;
	public static final Block GOLD_BLOCK;
	public static final Block IRON_BLOCK;
	public static final Block DOUBLE_SLAB;
	public static final Block SLAB;
	public static final Block BRICK;
	public static final Block TNT;
	public static final Block BOOKSHELF;
	public static final Block MOSSY_COBBLESTONE;
	public static final Block OBSIDIAN;
	public static final Block CACTUS;
	public static final Block DIAMOND_ORE;
	public static final Block DIAMOND_BLOCK;
	public static final Block SNOW_BLOCK;
	public static final Block ICE;
	public static final Block BIRCH_LOG;
	public static final Block SPRUCE_LOG;
	public static final Block SPRUCE_LEAVES;
	public static final Block BIRCH_SAPLING;
	public static final Block SPRUCE_SAPLING;
	public static final Block BLACK_WOOL;
	public static final Block PINK_WOOL;
	public static final Block DARK_GREEN_WOOL;
	public static final Block BROWN_WOOL;
	public static final Block DARK_BLUE_WOOL;
	public static final Block TURQUOISE_WOOL;	
	public static final Block CLAY;
	public static final Block SANDSTONE;
	public static final Block COBWEB;
	public static final Block REDBERRY_LEAVES;
	public static final Block BLACKBERRY_LEAVES;
	public static final Block DIRT_SLAB;
	public static final Block WOOD_SLAB;
	public static final Block COBBLESTONE_SLAB;
	public static final Block MOSSY_COBBLESTONE_SLAB;
	public static final Block STONE_SLAB;
	public static final Block BRICK_SLAB;
	public static final Block STONEBRICK_SLAB;
	public static final Block SANDSTONE_SLAB;
	public static final Block RUBY_ORE;
	public static final Block RUBY_BLOCK;
	public static final Block BLUE_ROSE;
	public static final Block DANGELION;
	public static final Block DEAD_BUSH;
	public static final Block BUSH;
	public static final Block BIRCH_WOOD;
	public static final Block SPRUCE_WOOD;
	public static final Block SNOW_GRASS;
	public static final Block REDBERRY_SAPLING;
	public static final Block BLACKBERRY_SAPLING;
	public static final Block LAVA_SPONGE;
	public static final Block STONEBRICK;
	public static final Block MOSSY_STONEBRICK;
	public static final Block CRACKED_STONEBRICK;
	public static final Block CRYING_OBSIDIAN;
	public static final Block MAGMA;
	public static final Block CRISTAL;
	public static final Block REEDS;
	public static final Block BIRCH_LEAVES;
	public static final Block SNOW_LEAVES;
	public static final Block SNOW_BIRCH_LEAVES;
	public static final Block SNOW_SPRUCE_LEAVES;
	public static final Block OTHER_REDBERRY_LEAVES;
	public static final Block OTHER_BLACKBERRY_LEAVES;
	public static final Block SNOW_OTHER_REDBERRY_LEAVES;
	public static final Block SNOW_OTHER_BLACKBERRY_LEAVES;
	public static final Block SLIME_BLOCK;
	public static final Block DUNGEON_CHEST;
	public static final Block WORKBENCH;
	public static final Block CRISTAL_BLOCK;
	//public static final Block TORCH;
	//public static final Block DOOR;
	public int textureId;
	public final int id;
	public Tile$SoundType stepsound;
	private int hardness;
	private boolean explodes;
	public float x1;
	public float y1;
	public float z1;
	public float x2;
	public float y2;
	public float z2;
	public float particleGravity;
	public float friction;
	public String name;
	int overrideBlockTexture = -1;
	ShapeRenderer shapeRenderer;
	Level level;
	int x;
	int y;
	int z;

	public boolean isCube()
	{
		return true;
	}

	protected Block setData(Tile$SoundType soundType, float var2, float particleGravity, float hardness)
	{
		this.particleGravity = particleGravity;
		this.stepsound = soundType;
		this.hardness = (int)(hardness * 20.0F);

		return this;
	}

	protected void setPhysics(boolean physics)
	{
		this.physics[id] = physics;
	}

	protected void setBounds(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}

	public void setTickDelay(int tickDelay)
	{
		this.tickDelay[id] = tickDelay;
	}

	public void renderFullbright(ShapeRenderer shapeRenderer) 
	{
		float red = 0.5F;
		float green = 0.8F;
		float blue = 0.6F;

		shapeRenderer.color(red, red, red);
		renderInside(shapeRenderer, -2, 0, 0, 0);

		shapeRenderer.color(1.0F, 1.0F, 1.0F);
		renderInside(shapeRenderer, -2, 0, 0, 1);

		shapeRenderer.color(green, green, green);
		renderInside(shapeRenderer, -2, 0, 0, 2);

		shapeRenderer.color(green, green, green);
		renderInside(shapeRenderer, -2, 0, 0, 3);

		shapeRenderer.color(blue, blue, blue);
		renderInside(shapeRenderer, -2, 0, 0, 4);

		shapeRenderer.color(blue, blue, blue);
		renderInside(shapeRenderer, -2, 0, 0, 5);
	}

	protected float getBrightness(Level level, int x, int y, int z)
	{
		return level.getBrightness(x, y, z);
	}

	public boolean canRenderSide(Level level, int x, int y, int z, int side)
	{
		return !level.isSolidTile(x, y, z);
	}
	
	/*public boolean canRenderSide(Level level, int x, int y, int z, int var5, int side)
	{
		return var5 == 1 ? false : !level.isSolidTile(x, y, z);
	}*/


	protected int getTextureId(int var1, int var2) {
		return this.getTextureId(var1);
	}
	
	protected int getTextureId(int texture)
	{
		return textureId;
	}

	public void renderInside(ShapeRenderer shapeRenderer, int x, int y, int z, int side)
	{
		int textureID1 = getTextureId(side);

		renderSide(shapeRenderer, x, y, z, side, textureID1);
	}

	public void renderSide(ShapeRenderer shapeRenderer, int x, int y, int z, int side, int textureID)
	{
		int var7 = textureID % 16 << 4;
		int var8 = textureID / 16 << 4;
		float var9 = (float)var7 / 256.0F;
		float var17 = ((float)var7 + 15.99F) / 256.0F;
		float var10 = (float)var8 / 256.0F;
		float var11 = ((float)var8 + 15.99F) / 256.0F;
		if(side >= 2 && textureID < 240) {
			if(this.y1 >= 0.0F && this.y2 <= 1.0F) {
				var10 = ((float)var8 + this.y1 * 15.99F) / 256.0F;
				var11 = ((float)var8 + this.y2 * 15.99F) / 256.0F;
			} else {
				var10 = (float)var8 / 256.0F;
				var11 = ((float)var8 + 15.99F) / 256.0F;
			}
		}

		float var16 = (float)x + this.x1;
		float var14 = (float)x + this.x2;
		float var18 = (float)y + this.y1;
		float var15 = (float)y + this.y2;
		float var12 = (float)z + this.z1;
		float var13 = (float)z + this.z2;
		if(side == 0) {
			shapeRenderer.vertexUV(var16, var18, var13, var9, var11);
			shapeRenderer.vertexUV(var16, var18, var12, var9, var10);
			shapeRenderer.vertexUV(var14, var18, var12, var17, var10);
			shapeRenderer.vertexUV(var14, var18, var13, var17, var11);
		} else if(side == 1) {
			shapeRenderer.vertexUV(var14, var15, var13, var17, var11);
			shapeRenderer.vertexUV(var14, var15, var12, var17, var10);
			shapeRenderer.vertexUV(var16, var15, var12, var9, var10);
			shapeRenderer.vertexUV(var16, var15, var13, var9, var11);
		} else if(side == 2) {
			shapeRenderer.vertexUV(var16, var15, var12, var17, var10);
			shapeRenderer.vertexUV(var14, var15, var12, var9, var10);
			shapeRenderer.vertexUV(var14, var18, var12, var9, var11);
			shapeRenderer.vertexUV(var16, var18, var12, var17, var11);
		} else if(side == 3) {
			shapeRenderer.vertexUV(var16, var15, var13, var9, var10);
			shapeRenderer.vertexUV(var16, var18, var13, var9, var11);
			shapeRenderer.vertexUV(var14, var18, var13, var17, var11);
			shapeRenderer.vertexUV(var14, var15, var13, var17, var10);
		} else if(side == 4) {
			shapeRenderer.vertexUV(var16, var15, var13, var17, var10);
			shapeRenderer.vertexUV(var16, var15, var12, var9, var10);
			shapeRenderer.vertexUV(var16, var18, var12, var9, var11);
			shapeRenderer.vertexUV(var16, var18, var13, var17, var11);
		} else if(side == 5) {
			shapeRenderer.vertexUV(var14, var18, var13, var9, var11);
			shapeRenderer.vertexUV(var14, var18, var12, var17, var11);
			shapeRenderer.vertexUV(var14, var15, var12, var17, var10);
			shapeRenderer.vertexUV(var14, var15, var13, var9, var10);
		}
	}

	public final void renderSide(ShapeRenderer var1, int var2, int var3, int var4, int var5) {
		int var6;
		float var7;
		float var8 = (var7 = (float)((var6 = this.getTextureId(var5)) % 16) / 16.0F) + 0.0624375F;
		float var16;
		float var9 = (var16 = (float)(var6 / 16) / 16.0F) + 0.0624375F;
		float var10 = (float)var2 + this.x1;
		float var14 = (float)var2 + this.x2;
		float var11 = (float)var3 + this.y1;
		float var15 = (float)var3 + this.y2;
		float var12 = (float)var4 + this.z1;
		float var13 = (float)var4 + this.z2;
		if(var5 == 0) {
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var14, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var12, var7, var16);
			var1.vertexUV(var10, var11, var13, var7, var9);
		}

		if(var5 == 1) {
			var1.vertexUV(var10, var15, var13, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var15, var13, var8, var9);
		}

		if(var5 == 2) {
			var1.vertexUV(var10, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var7, var9);
			var1.vertexUV(var14, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var12, var8, var16);
		}

		if(var5 == 3) {
			var1.vertexUV(var14, var15, var13, var8, var16);
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var13, var7, var9);
			var1.vertexUV(var10, var15, var13, var7, var16);
		}

		if(var5 == 4) {
			var1.vertexUV(var10, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var12, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var13, var8, var16);
		}

		if(var5 == 5) {
			var1.vertexUV(var14, var15, var13, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var13, var7, var9);
		}

	}

	public AABB getSelectionBox(int x, int y, int z)
	{
		AABB aabb = new AABB((float)x + x1, (float)y + y1, (float)z + z1, (float)x + x2, (float)y + y2, (float)z + z2);;

		return aabb;
	}

	public AABB getCollisionBox(int x, int y, int z)
	{
		AABB aabb = new AABB((float)x + x1, (float)y + y1, (float)z + z1, (float)x + x2, (float)y + y2, (float)z + z2);;

		return aabb;
	}

	public boolean isOpaque()
	{
		return true;
	}

	public boolean isSolid()
	{
		return true;
	}

	public void update(Level level, int x, int y, int z, Random rand)
	{
	}

	public void spawnBreakParticles(Level level, int x, int y, int z, ParticleManager particleManager)
	{
		for(int var6 = 0; var6 < 4; ++var6)
		{
			for(int var7 = 0; var7 < 4; ++var7)
			{
				for(int var8 = 0; var8 < 4; ++var8)
				{
					float var9 = (float)x + ((float)var6 + 0.5F) / (float)4;
					float var10 = (float)y + ((float)var7 + 0.5F) / (float)4;
					float var11 = (float)z + ((float)var8 + 0.5F) / (float)4;

					if(level.rendererContext$5cd64a7f.settings.particles) {
					particleManager.spawnParticle(new TerrainParticle(level, var9, var10, var11, var9 - (float) x - 0.5F, var10 - (float) y - 0.5F, var11 - (float) z - 0.5F, this));
					}
				}
			}
		}

	}

	public final void spawnBlockParticles(Level var1, int var2, int var3, int var4, int var5, ParticleManager var6) {
		float var7 = 0.1F;
		float var8 = (float)var2 + random.nextFloat() * (this.x2 - this.x1 - var7 * 2.0F) + var7 + this.x1;
		float var9 = (float)var3 + random.nextFloat() * (this.y2 - this.y1 - var7 * 2.0F) + var7 + this.y1;
		float var10 = (float)var4 + random.nextFloat() * (this.z2 - this.z1 - var7 * 2.0F) + var7 + this.z1;
		if(var5 == 0) {
			var9 = (float)var3 + this.y1 - var7;
		}

		if(var5 == 1) {
			var9 = (float)var3 + this.y2 + var7;
		}

		if(var5 == 2) {
			var10 = (float)var4 + this.z1 - var7;
		}

		if(var5 == 3) {
			var10 = (float)var4 + this.z2 + var7;
		}

		if(var5 == 4) {
			var8 = (float)var2 + this.x1 - var7;
		}

		if(var5 == 5) {
			var8 = (float)var2 + this.x2 + var7;
		}

		if(var1.rendererContext$5cd64a7f.settings.particles) {
		var6.spawnParticle((new TerrainParticle(var1, var8, var9, var10, 0.0F, 0.0F, 0.0F, this)).setPower(0.2F).scale(0.6F));
		}
	}

	public LiquidType getLiquidType()
	{
		return LiquidType.NOT_LIQUID;
	}

	public void onNeighborChange(Level var1, int var2, int var3, int var4, int var5) {}

	public void onPlace(Level level, int x, int y, int z) {}
	
	public int getTickDelay()
	{
		return 0;
	}

	public void onAdded(Level level, int x, int y, int z) {}

	public void onRemoved(Level var1, int var2, int var3, int var4) {}

	public int getDropCount() {
		return 1;
	}

	public int getDrop(Level var1, Random var2) {
		return this.id;
	}
	
	public int getRandomDrop(ItemStack var1, Level var2, Random var3) {
		if(var3.nextInt(2) != 0) { 
		  return var3.nextInt(106) + 1;
	    } else {
	      return var1.getRandomDrop(var3);
	    }
	}
	
	public void onBreak(Level var1, int var2, int var3, int var4) {
		this.dropItems(var1, var2, var3, var4, 1.0F);
	}

	public void dropItems(Level var1, int var2, int var3, int var4, float var5) {
		if(!var1.rendererContext$5cd64a7f.gamemode.isCreative()) {
			int var6 = this.getDropCount();

			for(int var7 = 0; var7 < var6; ++var7) {
				if(var1.random.nextFloat() <= var5) {
					float var8 = 0.7F;
					float var9 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					float var10 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					float var11 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					Item var12 = new Item(var1, (float)var2 + var9, (float)var3 + var10, (float)var4 + var11, new ItemStack(this.getDrop(var1, var1.random)));
					var12.delayBeforeCanPickup = 20;
					var1.releaseSkin(var12);
 
				}
			}

		}
	}
	
	public void dropRandomItems(ItemStack ItemStack, Level var1, int var2, int var3, int var4, float var5) {
		if(!var1.rendererContext$5cd64a7f.gamemode.isCreative()) {
			int var6 = this.getDropCount();

			for(int var7 = 0; var7 < var6; ++var7) {
				if(var1.random.nextFloat() <= var5) {
					float var8 = 0.7F;
					float var9 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					float var10 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					float var11 = var1.random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
					Item var12 = new Item(var1, (float)var2 + var9, (float)var3 + var10, (float)var4 + var11, new ItemStack(this.getRandomDrop(ItemStack, var1, var1.random)));
					var12.delayBeforeCanPickup = 20;
					var1.releaseSkin(var12);
 
				}
			}

		}
	}

	public void renderPreview(ShapeRenderer var1) {
		var1.begin();

		for(int var2 = 0; var2 < 6; ++var2) {
			if(var2 == 0) {
				var1.normal(0.0F, 1.0F, 0.0F);
			}

			if(var2 == 1) {
				var1.normal(0.0F, -1.0F, 0.0F);
			}

			if(var2 == 2) {
				var1.normal(0.0F, 0.0F, 1.0F);
			}

			if(var2 == 3) {
				var1.normal(0.0F, 0.0F, -1.0F);
			}

			if(var2 == 4) {
				var1.normal(1.0F, 0.0F, 0.0F);
			}

			if(var2 == 5) {
				var1.normal(-1.0F, 0.0F, 0.0F);
			}

			this.renderInside(var1, 0, 0, 0, var2);
		}

		var1.end();
	}

	public final boolean canExplode() {
		return this.explodes;
	}

	public final MovingObjectPosition clip(int var1, int var2, int var3, Vec3D var4, Vec3D var5) {
		var4 = var4.add((float)(-var1), (float)(-var2), (float)(-var3));
		var5 = var5.add((float)(-var1), (float)(-var2), (float)(-var3));
		Vec3D var6 = var4.getXIntersection(var5, this.x1);
		Vec3D var7 = var4.getXIntersection(var5, this.x2);
		Vec3D var8 = var4.getYIntersection(var5, this.y1);
		Vec3D var9 = var4.getYIntersection(var5, this.y2);
		Vec3D var10 = var4.getZIntersection(var5, this.z1);
		var5 = var4.getZIntersection(var5, this.z2);
		if(!this.xIntersects(var6)) {
			var6 = null;
		}

		if(!this.xIntersects(var7)) {
			var7 = null;
		}

		if(!this.yIntersects(var8)) {
			var8 = null;
		}

		if(!this.yIntersects(var9)) {
			var9 = null;
		}

		if(!this.zIntersects(var10)) {
			var10 = null;
		}

		if(!this.zIntersects(var5)) {
			var5 = null;
		}

		Vec3D var11 = null;
		if(var6 != null) {
			var11 = var6;
		}

		if(var7 != null && (var11 == null || var4.distance(var7) < var4.distance(var11))) {
			var11 = var7;
		}

		if(var8 != null && (var11 == null || var4.distance(var8) < var4.distance(var11))) {
			var11 = var8;
		}

		if(var9 != null && (var11 == null || var4.distance(var9) < var4.distance(var11))) {
			var11 = var9;
		}

		if(var10 != null && (var11 == null || var4.distance(var10) < var4.distance(var11))) {
			var11 = var10;
		}

		if(var5 != null && (var11 == null || var4.distance(var5) < var4.distance(var11))) {
			var11 = var5;
		}

		if(var11 == null) {
			return null;
		} else {
			byte var12 = -1;
			if(var11 == var6) {
				var12 = 4;
			}

			if(var11 == var7) {
				var12 = 5;
			}

			if(var11 == var8) {
				var12 = 0;
			}

			if(var11 == var9) {
				var12 = 1;
			}

			if(var11 == var10) {
				var12 = 2;
			}

			if(var11 == var5) {
				var12 = 3;
			}

			return new MovingObjectPosition(var1, var2, var3, var12, var11.add((float)var1, (float)var2, (float)var3));
		}
	}

	private boolean xIntersects(Vec3D var1) {
		return var1 == null?false:var1.y >= this.y1 && var1.y <= this.y2 && var1.z >= this.z1 && var1.z <= this.z2;
	}

	private boolean yIntersects(Vec3D var1) {
		return var1 == null?false:var1.x >= this.x1 && var1.x <= this.x2 && var1.z >= this.z1 && var1.z <= this.z2;
	}

	private boolean zIntersects(Vec3D var1) {
		return var1 == null?false:var1.x >= this.x1 && var1.x <= this.x2 && var1.y >= this.y1 && var1.y <= this.y2;
	}

	public void explode(Level var1, int var2, int var3, int var4) {}

	public boolean render(Level var1, int var2, int var3, int var4, ShapeRenderer var5) {
		boolean var6 = false;
		float var7 = 0.5F;
		float var8 = 0.8F;
		float var9 = 0.6F;
		float var10;
		if(this.canRenderSide(var1, var2, var3 - 1, var4, 0)) {
			var10 = this.getBrightness(var1, var2, var3 - 1, var4);
			var5.color(var7 * var10, var7 * var10, var7 * var10);
			this.renderInside(var5, var2, var3, var4, 0);
			var6 = true;
		}

		if(this.canRenderSide(var1, var2, var3 + 1, var4, 1)) {
			var10 = this.getBrightness(var1, var2, var3 + 1, var4);
			var5.color(var10 * 1.0F, var10 * 1.0F, var10 * 1.0F);
			this.renderInside(var5, var2, var3, var4, 1);
			var6 = true;
		}

		if(this.canRenderSide(var1, var2, var3, var4 - 1, 2)) {
			var10 = this.getBrightness(var1, var2, var3, var4 - 1);
			var5.color(var8 * var10, var8 * var10, var8 * var10);
			this.renderInside(var5, var2, var3, var4, 2);
			var6 = true;
		}

		if(this.canRenderSide(var1, var2, var3, var4 + 1, 3)) {
			var10 = this.getBrightness(var1, var2, var3, var4 + 1);
			var5.color(var8 * var10, var8 * var10, var8 * var10);
			this.renderInside(var5, var2, var3, var4, 3);
			var6 = true;
		}

		if(this.canRenderSide(var1, var2 - 1, var3, var4, 4)) {
			var10 = this.getBrightness(var1, var2 - 1, var3, var4);
			var5.color(var9 * var10, var9 * var10, var9 * var10);
			this.renderInside(var5, var2, var3, var4, 4);
			var6 = true;
		}

		if(this.canRenderSide(var1, var2 + 1, var3, var4, 5)) {
			var10 = this.getBrightness(var1, var2 + 1, var3, var4);
			var5.color(var9 * var10, var9 * var10, var9 * var10);
			this.renderInside(var5, var2, var3, var4, 5);
			var6 = true;
		}

		return var6;
	}

	public int getRenderPass() {
		return 0;
	}
	
	public final int strength(Player var1) {
		return (int)(this.hardness / var1.canHarvestBlock(this));
	}
	
	/*public final int getHardness() {
		return this.hardness;
	}*/
	
	public AABB getTileAABB(Level var1, int var2, int var3, int var4) {
		return new AABB((float)var2 + this.x1, (float)var3 + this.y1, (float)var4 + this.z1, (float)var2 + this.x2, (float)var3 + this.y2, (float)var4 + this.z2);
	}

    public void addAABBs(Level var1, int var2, int var3, int var4, AABB var5, ArrayList<AABB> var6) {
		AABB var7 = this.getAABB(var1, var2, var3, var4);
		if (var7 != null && var5.intersects(var7)) {
		    var6.add(var7);
		}

	}

	public AABB getAABB(Level var1, int var2, int var3, int var4) {
		return new AABB ((float)var2 + this.x1, (float)var3 + this.y1, (float)var4 + this.z1, (float)var2 + this.x2, (float)var3 + this.y2, (float)var4 + this.z2);
	}
	
    public void randomTick(Level var1, int var2, int var3, int var4, Random var5) {
    }
    
	protected final void setTickOnLoad(boolean var1) {
		tickOnLoad[this.id] = var1;
	}
	
	public boolean blockActivated(Level var1, int var2, int var3, int var4, Player var5) {
		return false;
	}
	
	public boolean specialBlockActivated(ItemStack var1, Level var2, int var3, int var4, int var5, Player var6) {
		return false;
	}
	
	public void setFancy(boolean var1) {
    }
	
	static {
		Block var10000 = (new StoneBlock(1, 1)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		boolean var0 = false;
		Block var1 = var10000;
		var10000.explodes = false;
		STONE = var1;
		GRASS = (new GrassBlock(2, 3)).setData(Tile$SoundType.grass, 0.9F, 1.0F, 0.6F);
		DIRT = (new DirtBlock(3, 2)).setData(Tile$SoundType.grass, 0.8F, 1.0F, 0.5F);
		var10000 = (new Block(4, 16)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.5F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		COBBLESTONE = var1;
		WOOD = (new Block(5, 4)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		SAPLING = (new SaplingBlock(6, 15)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		var10000 = (new Block(7, 17)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 999.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		BEDROCK = var1;
		WATER = (new LiquidBlock(8, LiquidType.WATER)).setData(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		STATIONARY_WATER = (new StillLiquidBlock(9, LiquidType.WATER)).setData(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		LAVA = (new LiquidBlock(10, LiquidType.LAVA)).setData(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		STATIONARY_LAVA = (new StillLiquidBlock(11, LiquidType.LAVA)).setData(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		SAND = (new SandBlock(12, 18)).setData(Tile$SoundType.sand, 0.8F, 1.0F, 0.5F);
		GRAVEL = (new GravelBlock(13, 19)).setData(Tile$SoundType.gravel, 0.8F, 1.0F, 0.6F);
		var10000 = (new OreBlock(14, 32)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		GOLD_ORE = var1;
		var10000 = (new OreBlock(15, 33)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		IRON_ORE = var1;
		var10000 = (new OreBlock(16, 34)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		COAL_ORE = var1;
		LOG = (new WoodBlock(17, 20)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 2.5F);
		LEAVES = (new LeavesBlock(18, 22)).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SPONGE = (new SpongeBlock(19, 48)).setData(Tile$SoundType.grass, 1.0F, 0.9F, 0.6F);
		GLASS = (new GlassBlock(20, 49, false)).setData(Tile$SoundType.glass, 1.0F, 1.0F, 0.3F);
		RED_WOOL = (new Block(21, 64)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		ORANGE_WOOL = (new Block(22, 65)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		YELLOW_WOOL = (new Block(23, 66)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		LIME_WOOL = (new Block(24, 67)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		GREEN_WOOL = (new Block(25, 68)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		AQUA_GREEN_WOOL = (new Block(26, 69)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		CYAN_WOOL = (new Block(27, 70)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		BLUE_WOOL = (new Block(28, 71)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		PURPLE_WOOL = (new Block(29, 72)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		INDIGO_WOOL = (new Block(30, 73)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		VIOLET_WOOL = (new Block(31, 74)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		MAGENTA_WOOL = (new Block(32, 75)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		RED_PINK_WOOL = (new Block(33, 76)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		DARKGRAY_WOOL = (new Block(34, 77)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		GRAY_WOOL = (new Block(35, 78)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		WHITE_WOOL = (new Block(36, 79)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		DANDELION = (new FlowerBlock(37, 13)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		ROSE = (new FlowerBlock(38, 12)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		BROWN_MUSHROOM = (new MushroomBlock(39, 29)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		RED_MUSHROOM = (new MushroomBlock(40, 28)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		var10000 = (new MetalBlock(41, 40)).setData(Tile$SoundType.metal, 0.7F, 1.0F, 3.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		GOLD_BLOCK = var1;
		var10000 = (new MetalBlock(42, 39)).setData(Tile$SoundType.metal, 0.7F, 1.0F, 5.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		IRON_BLOCK = var1;
		var10000 = (new SlabBlock(43, true)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		DOUBLE_SLAB = var1;
		var10000 = (new SlabBlock(44, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		SLAB = var1;
		var10000 = (new Block(45, 7)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		BRICK = var1;
		TNT = (new TNTBlock(46, 8)).setData(Tile$SoundType.tnt, 1.0F, 1.0F, 0.0F);
		BOOKSHELF = (new BookshelfBlock(47, 35)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		var10000 = (new Block(48, 36)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		MOSSY_COBBLESTONE = var1;
		var10000 = (new StoneBlock(49, 37)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 10.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		OBSIDIAN = var1;
		CACTUS = (new CactusBlock(50, 51)).setData(Tile$SoundType.grass, 1.0F, 1.0F, 0.8F);
		DIAMOND_ORE = (new OreBlock(51, 50)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		DIAMOND_ORE.explodes = false;
		DIAMOND_BLOCK = (new MetalBlock(52, 41)).setData(Tile$SoundType.metal, 0.7F, 1.0F, 5.0F);
		DIAMOND_BLOCK.explodes = false;
		SNOW_BLOCK = (new Block(53, 82)).setData(Tile$SoundType.snow, 1.0F, 1.0F, 0.8F);
		ICE = (new IceBlock(54, 81)).setData(Tile$SoundType.glass, 1.0F, 1.0F, 0.3F);
		BIRCH_LOG = (new WoodBlock(55, 86)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 2.5F);
		SPRUCE_LOG = (new WoodBlock(56, 84)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 2.5F);
		SPRUCE_LEAVES = (new SpruceLeavesBlock(57, 38)).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		BIRCH_SAPLING = (new SaplingBlock(58, 63)).setData(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);		
		BLACK_WOOL = (new Block(59, 88)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		PINK_WOOL = (new Block(60, 89)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		DARK_GREEN_WOOL = (new Block(61, 90)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		BROWN_WOOL = (new Block(62, 91)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		DARK_BLUE_WOOL = (new Block(63, 92)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		TURQUOISE_WOOL = (new Block(64, 93)).setData(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);		
		CLAY = (new SandBlock(65, 54)).setData(Tile$SoundType.gravel, 0.8F, 1.0F, 0.5F);	
		var10000 = (new SandStoneBlock(66, 43)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		SANDSTONE = var1;
		REDBERRY_LEAVES = (new RedBerryLeavesBlock(67, 96)).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.3F);
		BLACKBERRY_LEAVES = (new BlackBerryLeavesBlock(68, 97)).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.3F);	
		RUBY_ORE = (new OreBlock(69, 94)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		RUBY_ORE.explodes = false;
		RUBY_BLOCK = (new MetalBlock(70, 42)).setData(Tile$SoundType.metal, 0.7F, 1.0F, 5.0F);
		RUBY_BLOCK.explodes = false;
		BLUE_ROSE = (new FlowerBlock(71, 44)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		DANGELION = (new FlowerBlock(72, 45)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		DEAD_BUSH = (new BushBlock(73, 95)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		BUSH = (new BushBlock(74, 62)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		BIRCH_WOOD = (new Block(75, 98)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		SPRUCE_WOOD = (new Block(76, 99)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		SNOW_GRASS = (new SnowGrassBlock(77, 83)).setData(Tile$SoundType.snow, 0.9F, 1.0F, 0.6F);
		REEDS = (new ReedsBlock(78, 31)).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		LAVA_SPONGE = (new SpongeBlock(79, 104)).setData(Tile$SoundType.grass, 1.0F, 0.9F, 0.6F);
		var10000 = (new Block(80, 100)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		STONEBRICK = var1;	
		var10000 = (new Block(81, 101)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		MOSSY_STONEBRICK = var1;
		var10000 = (new Block(82, 102)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		CRACKED_STONEBRICK = var1;	
		COBWEB = (new CobWebBlock(83, 11)).setData(Tile$SoundType.cloth, 0.8F, 1.0F, 0.5F);
		DIRT_SLAB = (new DirtSlabBlock(84, false)).setData(Tile$SoundType.grass, 0.8F, 1.0F, 0.5F);
		WOOD_SLAB = (new WoodSlabBlock(85, false)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		var10000 = (new CobbleSlabBlock(86, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.5F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		COBBLESTONE_SLAB = var1;
		var10000 = (new MossyCobbleSlabBlock(87, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		MOSSY_COBBLESTONE_SLAB = var1;
		var10000 = (new StoneSlabBlock(88, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		STONE_SLAB = var1;
		var10000 = (new BrickSlabBlock(89, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		BRICK_SLAB = var1;
		var10000 = (new StoneBrickSlabBlock(90, false)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		STONEBRICK_SLAB = var1;
		var10000 = (new CryingObsidianBlock(91, 106)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 8.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		CRYING_OBSIDIAN = var1;
		var10000 = (new Block(92, 107)).setData(Tile$SoundType.stone, 1.0F, 1.0F, 4.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		MAGMA = var1;
		BIRCH_LEAVES = new LeavesBlock(93, 22).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		REDBERRY_SAPLING = new SaplingBlock(94, 60).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		BLACKBERRY_SAPLING = new SaplingBlock(95, 61).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		var10000 = new SandStoneSlabBlock(96, false).setData(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		var0 = false;
		var1 = var10000;
		var10000.explodes = false;
		SANDSTONE_SLAB = var1;
		SNOW_LEAVES = new SnowLeavesBlock(97, 110).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SNOW_SPRUCE_LEAVES = new SnowLeavesBlock(98, 111).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SPRUCE_SAPLING = new SaplingBlock(99, 47).setData(Tile$SoundType.grass, 0.7F, 1.0F, 0.0F);
		SNOW_BIRCH_LEAVES = new SnowLeavesBlock(100, 110).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		OTHER_REDBERRY_LEAVES = new LeavesBlock(101, 22).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		OTHER_BLACKBERRY_LEAVES = new LeavesBlock(102, 22).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SNOW_OTHER_REDBERRY_LEAVES = new SnowLeavesBlock(103, 110).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SNOW_OTHER_BLACKBERRY_LEAVES = new SnowLeavesBlock(104, 110).setData(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		SLIME_BLOCK = new SlimeBlock(105, 114).setData(Tile$SoundType.none, 1.0F, 1.0F, 0.0F);
		DUNGEON_CHEST = new DungeonChestBlock(106, 130).setData(Tile$SoundType.wood, 1.0F, 1.0F, 3.0F);
		//TORCH = (new TorchBlock(%, 80)).setData(Tile$SoundType.wood, 0.7F, 1.0F, 0.0F);
		//DOOR = (new DoorBlock(%, 112)).setData(Tile$SoundType.wood, 1.0F, 1.0F, 2.0F);
		WORKBENCH = new WorkbenchBlock(107).setData(Tile$SoundType.wood, 1.0F, 1.0F, 0.8F);
		CRISTAL = new CristalBlock(108, 105).setData(Tile$SoundType.metal, 1.0F, 1.0F, 0.1F);
		CRISTAL_BLOCK = new Block(109, 113).setData(Tile$SoundType.metal, 1.0F, 1.0F, 2.0F);

	      for(int var7 = 0; var7 < 256; ++var7) {
	          if (blocks[var7] != null && Items.itemsList[var7] == null) {
	             Items.itemsList[var7] = new ItemBlock(var7 - 256);
	          }
	       }
	      
	}
	
	

}
