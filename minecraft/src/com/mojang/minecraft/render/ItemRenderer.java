package com.mojang.minecraft.render;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.tile.Block;

public class ItemRenderer
{
	public ItemRenderer(Minecraft minecraft)
	{
		this.minecraft = minecraft;
	}

	public Minecraft minecraft;

	public float pos = 0.0F;
	public float lastPos = 0.0F;

	public int offset = 0;

	public boolean moving = false;
	
	public ItemStack itemToRenderer = null;

}
