package com.mojang.minecraft.item;

import com.mojang.minecraft.player.Player;

import java.util.Calendar;
import java.util.Random;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;

public abstract class Items {
	public static Items[] itemsList = new Items[1024];
	public static Items shovel;
	public static Items sword;
	public static Items pickaxe;
	public static Items axe;
	public static Items apple;
	public static Items stick;
	public static Items flint;
	public static Items silk;
	public static Items feather;
	public static Items gunpowder;
	public static Items pork;
	public static Items beef;
	public static Items chicken;
	public static Items mutton;
	public static Items rottenflesh;
	public static Items redberry;
	public static Items blackberry;
	public static Items bow;
	public static Items quiver;
	public static Items bone;
	public static Items bucket;
	public static Items waterbucket;
	public static Items lavabucket;
	public static Items milkbucket;
	public static Items sign;
	public static Items key;
	public static Items shield;
	
	public static Items spawnegg_human;
	public static Items spawnegg_pig;
	public static Items spawnegg_sheep;
	public static Items spawnegg_cow;
	public static Items spawnegg_chicken;
	public static Items spawnegg_wolf;
	public static Items spawnegg_zombie;
	public static Items spawnegg_skeleton;
	public static Items spawnegg_creeper;
	public static Items spawnegg_spider;
	public static Items spawnegg_slime;
	public final int shiftedIndex;
	protected int iconIndex;
	public int limitStack = 99;
	protected int maxDamage = 32;

	protected Items(int var1) {
		this.shiftedIndex = var1 + 256;
		itemsList[var1 + 256] = this;
	}

	public final int getIconIndex() {
		return this.iconIndex;
	}

	public float getStrVsBlock(Block var1) {
		return 1.0F;
	}

	public boolean useItem(ItemStack var1, Player var2) {
		return false;
	}
	
	public boolean onItemRightClick(ItemStack var1, Player var2, Level var3) {
		return false;
	}
	
	public void onPlaced(ItemStack var1, Level var2, int var3, int var4, int var5, int var6) {
	}
	
	public final int getItemStackLimit() {
		return this.limitStack;
	}

	public void onBlockDestroyed(ItemStack var1) {
	}

	public int getAttackDamage() {
		return 1;
	}
	
	public void hitEntity(ItemStack var1) {
	}

	public final int getMaxDamage() {
		return this.maxDamage;
	}
	
	static {
		for(int var0 = 0; var0 < 256; ++var0) {
			if(Block.blocks[var0] != null) {
				itemsList[var0] = new ItemBlock(var0);
			}
		}
		
		ItemShovel var10000 = new ItemShovel(256);
		byte texture = 16;
		ItemShovel var0 = var10000;
		var0.iconIndex = texture;
		shovel = var0;
		ItemPickaxe var10 = new ItemPickaxe(257);
		texture = 32;
		ItemPickaxe var2 = var10;
		var2.iconIndex = texture;
		pickaxe = var2;
		ItemAxe var11 = new ItemAxe(258);
		texture = 48;
		ItemAxe var3 = var11;
		var3.iconIndex = texture;
		axe = var3;
		ItemSword var12 = new ItemSword(259, 2);
		texture = 0;
		ItemSword var13 = var12;
		var13.iconIndex = texture;
		sword = var13;
		ItemFood var6 = new ItemFood(260, 2);
		texture = 4;
		ItemFood var4 = var6;
		var4.iconIndex = texture;
		apple = var4;
		ItemDefault var7 = new ItemDefault(261);
		texture = 2;
		ItemDefault var9 = var7;
		var9.iconIndex = texture;
		stick = var9;
		ItemDefault var18 = new ItemDefault(262);
		texture = 18;
		ItemDefault var14 = var18;
		var14.iconIndex = texture;
		flint = var14;
		ItemDefault var19 = new ItemDefault(263);
		texture = 34;
		ItemDefault var15 = var19;
		var15.iconIndex = texture;
		silk = var15;
		ItemDefault var20 = new ItemDefault(264);
		texture = 50;
		ItemDefault var16 = var20;
		var16.iconIndex = texture;
		feather = var16;
		ItemDefault var21 = new ItemDefault(265);
		texture = 66;
		ItemDefault var17 = var21;
		var17.iconIndex = texture;
		gunpowder = var17;	
		ItemFood var22 = new ItemFood(266, 4);
		texture = 20;
		ItemFood var23 = var22;
		var23.iconIndex = texture;
		pork = var23;	
		ItemFood var24 = new ItemFood(267, 6);
		texture = 36;
		ItemFood var25 = var24;
		var25.iconIndex = texture;
		beef = var25;
		ItemFood var26 = new ItemFood(268, 3);
		texture = 52;
		ItemFood var27 = var26;
		var27.iconIndex = texture;
		chicken = var27;
		ItemFood var28 = new ItemFood(269, 4);
		texture = 68;
		ItemFood var29 = var28;
		var29.iconIndex = texture;
		mutton = var29;
		ItemRottenFlesh var30 = new ItemRottenFlesh(270);
		texture = 21;
		ItemRottenFlesh var31 = var30;
		var31.iconIndex = texture;
		rottenflesh = var31;
		ItemFood var32 = new ItemFood(271, 1);
		texture = 37;
		ItemFood var33 = var32;
		var33.iconIndex = texture;
		redberry = var33;
		ItemFood var34 = new ItemFood(272, 1);
		texture = 53;
		ItemFood var35 = var34;
		var35.iconIndex = texture;
		blackberry = var35;	
		ItemBow var38 = new ItemBow(273);
		texture = 17;
		ItemBow var39 = var38;
		var39.iconIndex = texture;
		bow = var39;		
		ItemQuiver var40 = new ItemQuiver(274);
		texture = 33;
		ItemQuiver var41 = var40;
		var41.iconIndex = texture;
		quiver = var41;
		ItemDefault var46 = new ItemDefault(275);
		texture = 82;
		ItemDefault var47 = var46;
		var47.iconIndex = texture;
		bone = var47;
		ItemDefault var53 = new ItemDefault(276);
		texture = 40;
		ItemDefault var54 = var53;
		var54.iconIndex = texture;
		bucket = var54;
		ItemBucket var56 = new ItemBucket(277);
		texture = 56;
		ItemBucket var55 = var56;
		var55.iconIndex = texture;
		waterbucket = var55;
		ItemBucket var57 = new ItemBucket(278);
		texture = 72;
		ItemBucket var58 = var57;
		var58.iconIndex = texture;
		lavabucket = var58;
		ItemSign var59 = new ItemSign(279);
		texture = 8;
		ItemSign var60 = var59;
		var60.iconIndex = texture;
		sign = var60;
		
		ItemSpawnEgg var61 = new ItemSpawnEgg(280);
		texture = 94;
		ItemSpawnEgg var62 = var61;
		var62.iconIndex = texture;
		spawnegg_human = var62;
		ItemSpawnEgg var63 = new ItemSpawnEgg(281);
		texture = 15;
		ItemSpawnEgg var64 = var63;
		var64.iconIndex = texture;
		spawnegg_pig = var64;
		ItemSpawnEgg var65 = new ItemSpawnEgg(282);
		texture = 47;
		ItemSpawnEgg var66 = var65;
		var66.iconIndex = texture;
		spawnegg_sheep = var66;
		ItemSpawnEgg var67 = new ItemSpawnEgg(284);
		texture = 31;
		ItemSpawnEgg var68 = var67;
		var68.iconIndex = texture;
		spawnegg_cow = var68;
		ItemSpawnEgg var69 = new ItemSpawnEgg(285);
		texture = 63;
		ItemSpawnEgg var70 = var69;
		var70.iconIndex = texture;
		spawnegg_chicken = var70;
		ItemSpawnEgg var71 = new ItemSpawnEgg(286);
		texture = 79;
		ItemSpawnEgg var72 = var71;
		var72.iconIndex = texture;
		spawnegg_wolf = var72;
		ItemSpawnEgg var73 = new ItemSpawnEgg(287);
		texture = 46;
		ItemSpawnEgg var74 = var73;
		var74.iconIndex = texture;
		spawnegg_zombie = var74;
		ItemSpawnEgg var75 = new ItemSpawnEgg(288);
		texture = 62;
		ItemSpawnEgg var76 = var75;
		var76.iconIndex = texture;
		spawnegg_skeleton = var76;
		ItemSpawnEgg var77 = new ItemSpawnEgg(289);
		texture = 14;
		ItemSpawnEgg var78 = var77;
		var78.iconIndex = texture;
		spawnegg_creeper = var78;
		ItemSpawnEgg var79 = new ItemSpawnEgg(290);
		texture = 30;
		ItemSpawnEgg var80 = var79;
		var79.iconIndex = texture;
		spawnegg_spider = var80;
		ItemSpawnEgg var81 = new ItemSpawnEgg(291);
		texture = 78;
		ItemSpawnEgg var82 = var81;
		var82.iconIndex = texture;
		spawnegg_slime = var82;
		
		ItemKey var83 = new ItemKey(292);
		texture = 25;
		ItemKey var84 = var83;
		var84.iconIndex = texture;
		key = var84;
		ItemShield var85 = new ItemShield(293);
		texture = 64;
		ItemShield var86 = var85;
		var86.iconIndex = texture;
		shield = var86;
	}
}

