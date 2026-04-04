package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.InventoryScreen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.MobSpawner;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.level.tile.Tile$SoundType;

public final class HardcoreGameMode extends GameMode
{
	public HardcoreGameMode(Minecraft minecraft)
	{
		super(minecraft);
	}

	private int hitX;
	private int hitY;
	private int hitZ;
	private int hits;
	private int hardness = 0;
	private int hitDelay;
	private MobSpawner spawner;

	public void apply(Level level)
	{
		super.apply(level);
		
		level.gamemode = 2;
		
		level.rendererContext$5cd64a7f.isFlying = false;

		spawner = new MobSpawner(level);
	}

	
	public void hitBlock(int x, int y, int z, int side)
	{
		if(hitDelay > 0)
		{
			hitDelay--;
		} else if(x == hitX && y == hitY && z == hitZ) {
			int type = minecraft.level.getTile(x, y, z);

			if(type != 0)
			{
				Block block = Block.blocks[type];

				hardness = block.strength(this.minecraft.player);

				if (minecraft.settings.particles) {
				   block.spawnBlockParticles(minecraft.level, x, y, z, side, minecraft.particleManager);
				}
				
				if(this.hits % 4 == 0 && block != null) {
					String var10004 = "step." + block.stepsound.name;
					String var10007 = "random." + block.stepsound.name;
					float var10005 = (block.stepsound.getVolume() + 1.0F) / 8.0F;
					float var10006 = block.stepsound.getPitch() * 0.5F;
					
					if(block.stepsound != Tile$SoundType.none) {
						minecraft.level.playSound(var10004, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, var10005, var10006);
					    minecraft.level.playSound(var10007, (float)x + 0.5F, (float)y + 0.5F, (float)y + 0.5F, var10005, var10006);
				    }
				}
				
				hits++;

				if(hits == hardness + 1)
				{
					breakBlock(x, y, z);

					hits = 0;
					hitDelay = 5;
				}

			}
		} else {
			
			hits = 0;
			hitX = x;
			hitY = y;
			hitZ = z;
		}
	}

	public void breakBlock(int x, int y, int z)
	{
		int block = minecraft.level.getTile(x, y, z);
		Block.blocks[block].onBreak(minecraft.level, x, y, z);

		super.breakBlock(x, y, z);
	}

	public void hitBlock(int x, int y, int z)
	{
		int block = this.minecraft.level.getTile(x, y, z);

		if(block > 0 && Block.blocks[block].strength(this.minecraft.player) <= 0)
		{
			breakBlock(x, y, z);
		}
	}

	public void resetHits()
	{
		this.hits = 0;
		this.hitDelay = 0;
	}

	public void applyCracks(float time)
	{
		if(hits <= 0)
		{
			minecraft.levelRenderer.cracks = 0.0F;
		} else {
			minecraft.levelRenderer.cracks = ((float)hits + time - 1.0F) / (float)hardness;
		}
	}

	public float getReachDistance()
	{
		return 4.0F;
	}

	public void openInventory()
	{
		InventoryScreen inventoryScreen = new InventoryScreen(this.minecraft.player.inventory);
		
		minecraft.setCurrentScreen(inventoryScreen);
	}
	
	public void preparePlayer(Player player)
	{
		player.inventory.Inventory[8] = new ItemStack(Items.bow.shiftedIndex);
	}

	public void spawnMob()
	{
		int area = spawner.level.width * spawner.level.height * spawner.level.depth / 64 / 64 / 64;

		if(spawner.level.random.nextInt(100) < area && spawner.level.countInstanceOf(Mob.class) < area * 20)
		{
			spawner.spawn(area, spawner.level.player, null);
		}

	}
	
	public void prepareLevel(Level level)
	{
		spawner = new MobSpawner(level);

		minecraft.progressBar.setText("Spawning..");

		int area = level.width * level.height * level.depth / 800;

		spawner.spawn(area, null, minecraft.progressBar);
	}
	
	public boolean isSurvival()
	{
		return true;
	}
	
	public boolean isCreative()
	{
		return false;
	}
}
