package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public class EntitySoundPos extends BaseSoundPos
{
	public EntitySoundPos(Entity source, Entity listener)
	{
		super(listener);

		this.source = source;
	}

	public float getRotationDiff()
	{
		return super.getRotationDiff(source.x, source.z);
	}

	public float getDistanceSq()
	{
		return super.getDistanceSq(source.x, source.y, source.z);
	}

	private Entity source;
}
