package com.mojang.minecraft.level.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.render.TextureManager;

public class TakeEntityAnim extends Entity
{
	public TakeEntityAnim(Level level, Entity item, Entity player)
	{
		super(level);

		this.item = item;
		this.player = player;

		setSize(1.0F, 1.0F);

		xorg = item.x;
		yorg = item.y;
		zorg = item.z;
	}

	public void tick()
	{
		time++;

		if(time >= 3)
		{
			remove();
		}
		
		float distance = (distance = (float)time / 3.0F) * distance;

		xo = item.xo = item.x;
		yo = item.yo = item.y;
		zo = item.zo = item.z;

		x = item.x = xorg + (player.x - xorg) * distance;
		y = item.y = yorg + (player.y - 1.0F - yorg) * distance;
		z = item.z = zorg + (player.z - zorg) * distance;

		setPos(x, y, z);
	}
	
	public void render(TextureManager var2, float var3) {
		
		item.render(var2, var3);
		
	}
	
	public void renderItem(Entity var1, TextureManager var2, float var3, float var4, float var5, float var6, float var7) {
	
		item.renderItem(var1, var2, var3, var4, var5, var6, var7);
		
	}

	private static final long serialVersionUID = 1L;

	private int time = 0;

	private Entity item;
	private Entity player;

	private float xorg;
	private float yorg;
	private float zorg;
}
