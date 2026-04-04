package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.phys.AABB;

import java.util.Random;

public class Tile {
	protected static Random random = new Random();
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static boolean[] isSolid = new boolean[256];
	private static boolean[] isOpaque = new boolean[256];
	public static final boolean[] isLiquid = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock;
	public static final Tile grass;
	public static final Tile dirt;
	public static final Tile stoneBrick;
	public static final Tile wood;
	public static final Tile bush;
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public static final Tile sand;
	public static final Tile gravel;
	public static final Tile goldOre;
	public static final Tile ironOre;
	public static final Tile coalOre;
	public static final Tile log;
	public static final Tile leaf;
	public static final Tile sponge;
	public static final Tile glass;
	public static final Tile clothRed;
	public static final Tile clothOrange;
	public static final Tile clothYellow;
	public static final Tile clothChartreuse;
	public static final Tile clothGreen;
	public static final Tile clothSpringGreen;
	public static final Tile clothCyan;
	public static final Tile clothCapri;
	public static final Tile clothUltramarine;
	public static final Tile clothViolet;
	public static final Tile clothPurple;
	public static final Tile clothMagenta;
	public static final Tile clothRose;
	public static final Tile clothDarkGray;
	public static final Tile clothGray;
	public static final Tile clothWhite;
	public static final Tile flower;
	public static final Tile rose;
	public static final Tile mushroom1;
	public static final Tile mushroom2;
	public static final Tile gold;
	public static final Tile iron;
	public static final Tile slabFull;
	public static final Tile slabHalf;
	public static final Tile brick;
	public static final Tile tnt;
	public static final Tile bookshelf;
	public static final Tile mossStone;
	public static final Tile obsidian;
	public static final Tile cactus;
	public static final Tile diamondOre;
	public static final Tile diamond;
	public final int id;
	public Tile$SoundType soundType;
	protected boolean explodeable;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;

	protected Tile(int id) {
		this.explodeable = true;
		tiles[id] = this;
		this.id = id;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		isSolid[id] = this.isSolid();
		isOpaque[id] = this.isOpaque();
		isLiquid[id] = false;
	}

	public boolean isOpaque() {
		return true;
	}

	protected final void setTicking(boolean tick) {
		shouldTick[this.id] = tick;
	}

	protected final void setShape(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.xx0 = x0;
		this.yy0 = y0;
		this.zz0 = z0;
		this.xx1 = x1;
		this.yy1 = y1;
		this.zz1 = z1;
	}

	protected Tile(int id, int tex) {
		this(id);
	}

	public final void setTickSpeed(int ts) {
		tickSpeed[this.id] = 16;
	}

	public AABB getTileAABB(int x, int y, int z) {
		return new AABB((float)x + this.xx0, (float)y + this.yy0, (float)z + this.zz0, (float)x + this.xx1, (float)y + this.yy1, (float)z + this.zz1);
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level level, int x, int y, int z, Random random) {
	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level level, int x, int y, int z, int type) {
	}

	public void onPlace(Level level, int x, int y, int z) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level level, int x, int y, int z) {
	}

	public void onTileRemoved(Level level, int x, int y, int z) {
	}

	public int resourceCount() {
		return 1;
	}

	public void spawnResources(Level level, float chance) {
		if(!level.creativeMode) {
			int i3 = this.resourceCount();

			for(int i4 = 0; i4 < i3; ++i4) {
				if(random.nextFloat() <= chance) {
					random.nextFloat();
					random.nextFloat();
					random.nextFloat();
				}
			}

		}
	}

	public final boolean isExplodeable() {
		return this.explodeable;
	}

	public final HitResult clip(int x, int y, int z, Vec3 v0, Vec3 v1) {
		v0 = v0.add((float)(-x), (float)(-y), (float)(-z));
		v1 = v1.add((float)(-x), (float)(-y), (float)(-z));
		Vec3 vec36 = v0.clipX(v1, this.xx0);
		Vec3 vec37 = v0.clipX(v1, this.xx1);
		Vec3 vec38 = v0.clipY(v1, this.yy0);
		Vec3 vec39 = v0.clipY(v1, this.yy1);
		Vec3 vec310 = v0.clipZ(v1, this.zz0);
		v1 = v0.clipZ(v1, this.zz1);
		if(!this.containsX(vec36)) {
			vec36 = null;
		}

		if(!this.containsX(vec37)) {
			vec37 = null;
		}

		if(!this.containsY(vec38)) {
			vec38 = null;
		}

		if(!this.containsY(vec39)) {
			vec39 = null;
		}

		if(!this.containsZ(vec310)) {
			vec310 = null;
		}

		if(!this.containsZ(v1)) {
			v1 = null;
		}

		Vec3 vec311 = null;
		if(vec36 != null) {
			vec311 = vec36;
		}

		if(vec37 != null && (vec311 == null || v0.distanceTo(vec37) < v0.distanceTo(vec311))) {
			vec311 = vec37;
		}

		if(vec38 != null && (vec311 == null || v0.distanceTo(vec38) < v0.distanceTo(vec311))) {
			vec311 = vec38;
		}

		if(vec39 != null && (vec311 == null || v0.distanceTo(vec39) < v0.distanceTo(vec311))) {
			vec311 = vec39;
		}

		if(vec310 != null && (vec311 == null || v0.distanceTo(vec310) < v0.distanceTo(vec311))) {
			vec311 = vec310;
		}

		if(v1 != null && (vec311 == null || v0.distanceTo(v1) < v0.distanceTo(vec311))) {
			vec311 = v1;
		}

		if(vec311 == null) {
			return null;
		} else {
			byte v01 = -1;
			if(vec311 == vec36) {
				v01 = 4;
			}

			if(vec311 == vec37) {
				v01 = 5;
			}

			if(vec311 == vec38) {
				v01 = 0;
			}

			if(vec311 == vec39) {
				v01 = 1;
			}

			if(vec311 == vec310) {
				v01 = 2;
			}

			if(vec311 == v1) {
				v01 = 3;
			}

			return new HitResult(x, y, z, v01, vec311.add((float)x, (float)y, (float)z));
		}
	}

	private boolean containsX(Vec3 xa) {
		return xa == null ? false : xa.y >= this.yy0 && xa.y <= this.yy1 && xa.z >= this.zz0 && xa.z <= this.zz1;
	}

	private boolean containsY(Vec3 ya) {
		return ya == null ? false : ya.x >= this.xx0 && ya.x <= this.xx1 && ya.z >= this.zz0 && ya.z <= this.zz1;
	}

	private boolean containsZ(Vec3 za) {
		return za == null ? false : za.x >= this.xx0 && za.x <= this.xx1 && za.y >= this.yy0 && za.y <= this.yy1;
	}

	static {
		StoneTile stoneTile10000 = new StoneTile(1, 1);
		float f0 = 1.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		Tile$SoundType tile$SoundType1 = Tile$SoundType.stone;
		StoneTile stoneTile2 = stoneTile10000;
		stoneTile10000.soundType = tile$SoundType1;
		boolean z3 = false;
		stoneTile2.explodeable = false;
		rock = stoneTile2;
		GrassTile grassTile11 = new GrassTile(2);
		f0 = 0.6F;
		f0 = 1.0F;
		f0 = 0.9F;
		tile$SoundType1 = Tile$SoundType.grass;
		GrassTile grassTile4 = grassTile11;
		grassTile11.soundType = tile$SoundType1;
		grass = grassTile4;
		DirtTile dirtTile12 = new DirtTile(3, 2);
		f0 = 0.5F;
		f0 = 1.0F;
		f0 = 0.8F;
		tile$SoundType1 = Tile$SoundType.grass;
		DirtTile dirtTile5 = dirtTile12;
		dirtTile12.soundType = tile$SoundType1;
		dirt = dirtTile5;
		Tile tile13 = new Tile(4, 16);
		f0 = 1.5F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		Tile tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		z3 = false;
		tile6.explodeable = false;
		stoneBrick = tile6;
		tile13 = new Tile(5, 4);
		f0 = 1.5F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.wood;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		wood = tile6;
		Bush bush14 = new Bush(6, 15);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.none;
		Bush bush7 = bush14;
		bush14.soundType = tile$SoundType1;
		bush = bush7;
		tile13 = new Tile(7, 17);
		f0 = 999.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		z3 = false;
		tile6.explodeable = false;
		unbreakable = tile6;
		LiquidTile liquidTile16 = new LiquidTile(8, Liquid.water);
		f0 = 100.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.none;
		LiquidTile liquidTile8 = liquidTile16;
		liquidTile16.soundType = tile$SoundType1;
		water = liquidTile8;
		CalmLiquidTile calmLiquidTile17 = new CalmLiquidTile(9, Liquid.water);
		f0 = 100.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.none;
		CalmLiquidTile calmLiquidTile9 = calmLiquidTile17;
		calmLiquidTile17.soundType = tile$SoundType1;
		calmWater = calmLiquidTile9;
		liquidTile16 = new LiquidTile(10, Liquid.lava);
		f0 = 100.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.none;
		liquidTile8 = liquidTile16;
		liquidTile16.soundType = tile$SoundType1;
		lava = liquidTile8;
		calmLiquidTile17 = new CalmLiquidTile(11, Liquid.lava);
		f0 = 100.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.none;
		calmLiquidTile9 = calmLiquidTile17;
		calmLiquidTile17.soundType = tile$SoundType1;
		calmLava = calmLiquidTile9;
		FallingTile fallingTile18 = new FallingTile(12, 18);
		f0 = 0.5F;
		f0 = 1.0F;
		f0 = 0.8F;
		tile$SoundType1 = Tile$SoundType.gravel;
		FallingTile fallingTile10 = fallingTile18;
		fallingTile18.soundType = tile$SoundType1;
		sand = fallingTile10;
		fallingTile18 = new FallingTile(13, 19);
		f0 = 0.6F;
		f0 = 1.0F;
		f0 = 0.8F;
		tile$SoundType1 = Tile$SoundType.gravel;
		fallingTile10 = fallingTile18;
		fallingTile18.soundType = tile$SoundType1;
		gravel = fallingTile10;
		OreTile oreTile19 = new OreTile(14, 32);
		f0 = 3.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		OreTile oreTile15 = oreTile19;
		oreTile19.soundType = tile$SoundType1;
		z3 = false;
		oreTile15.explodeable = false;
		goldOre = oreTile15;
		oreTile19 = new OreTile(15, 33);
		f0 = 3.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		oreTile15 = oreTile19;
		oreTile19.soundType = tile$SoundType1;
		z3 = false;
		oreTile15.explodeable = false;
		ironOre = oreTile15;
		oreTile19 = new OreTile(16, 34);
		f0 = 3.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		oreTile15 = oreTile19;
		oreTile19.soundType = tile$SoundType1;
		z3 = false;
		oreTile15.explodeable = false;
		coalOre = oreTile15;
		LogTile logTile20 = new LogTile(17);
		f0 = 2.5F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.wood;
		LogTile logTile23 = logTile20;
		logTile20.soundType = tile$SoundType1;
		log = logTile23;
		LeafTile leafTile21 = new LeafTile(18, 22);
		f0 = 0.2F;
		f0 = 0.4F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.grass;
		LeafTile leafTile25 = leafTile21;
		leafTile21.soundType = tile$SoundType1;
		leaf = leafTile25;
		SpongeTile spongeTile22 = new SpongeTile(19);
		f0 = 0.6F;
		f0 = 0.9F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		SpongeTile spongeTile26 = spongeTile22;
		spongeTile22.soundType = tile$SoundType1;
		sponge = spongeTile26;
		GlassTile glassTile24 = new GlassTile(20, 49, false);
		f0 = 0.3F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.metal;
		GlassTile glassTile27 = glassTile24;
		glassTile24.soundType = tile$SoundType1;
		glass = glassTile27;
		tile13 = new Tile(21, 64);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothRed = tile6;
		tile13 = new Tile(22, 65);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothOrange = tile6;
		tile13 = new Tile(23, 66);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothYellow = tile6;
		tile13 = new Tile(24, 67);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothChartreuse = tile6;
		tile13 = new Tile(25, 68);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothGreen = tile6;
		tile13 = new Tile(26, 69);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothSpringGreen = tile6;
		tile13 = new Tile(27, 70);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothCyan = tile6;
		tile13 = new Tile(28, 71);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothCapri = tile6;
		tile13 = new Tile(29, 72);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothUltramarine = tile6;
		tile13 = new Tile(30, 73);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothViolet = tile6;
		tile13 = new Tile(31, 74);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothPurple = tile6;
		tile13 = new Tile(32, 75);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothMagenta = tile6;
		tile13 = new Tile(33, 76);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothRose = tile6;
		tile13 = new Tile(34, 77);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothDarkGray = tile6;
		tile13 = new Tile(35, 78);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothGray = tile6;
		tile13 = new Tile(36, 79);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		clothWhite = tile6;
		Flower flower28 = new Flower(37, 13);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.none;
		Flower flower34 = flower28;
		flower28.soundType = tile$SoundType1;
		flower = flower34;
		flower28 = new Flower(38, 12);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.none;
		flower34 = flower28;
		flower28.soundType = tile$SoundType1;
		rose = flower34;
		Mushroom mushroom29 = new Mushroom(39, 29);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.none;
		Mushroom mushroom35 = mushroom29;
		mushroom29.soundType = tile$SoundType1;
		mushroom1 = mushroom35;
		mushroom29 = new Mushroom(40, 28);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.none;
		mushroom35 = mushroom29;
		mushroom29.soundType = tile$SoundType1;
		mushroom2 = mushroom35;
		MetalTile metalTile30 = new MetalTile(41, 40);
		f0 = 3.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.metal;
		MetalTile metalTile36 = metalTile30;
		metalTile30.soundType = tile$SoundType1;
		z3 = false;
		metalTile36.explodeable = false;
		gold = metalTile36;
		metalTile30 = new MetalTile(42, 39);
		f0 = 5.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.metal;
		metalTile36 = metalTile30;
		metalTile30.soundType = tile$SoundType1;
		z3 = false;
		metalTile36.explodeable = false;
		iron = metalTile36;
		SlabTile slabTile31 = new SlabTile(43, true);
		f0 = 2.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		SlabTile slabTile37 = slabTile31;
		slabTile31.soundType = tile$SoundType1;
		z3 = false;
		slabTile37.explodeable = false;
		slabFull = slabTile37;
		slabTile31 = new SlabTile(44, false);
		f0 = 2.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		slabTile37 = slabTile31;
		slabTile31.soundType = tile$SoundType1;
		z3 = false;
		slabTile37.explodeable = false;
		slabHalf = slabTile37;
		tile13 = new Tile(45, 7);
		f0 = 2.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		z3 = false;
		tile6.explodeable = false;
		brick = tile6;
		TntTile tntTile32 = new TntTile(46, 8);
		f0 = 0.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		TntTile tntTile38 = tntTile32;
		tntTile32.soundType = tile$SoundType1;
		tnt = tntTile38;
		BookshelfTile bookshelfTile33 = new BookshelfTile(47, 35);
		f0 = 1.5F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.wood;
		BookshelfTile bookshelfTile39 = bookshelfTile33;
		bookshelfTile33.soundType = tile$SoundType1;
		bookshelf = bookshelfTile39;
		tile13 = new Tile(48, 36);
		f0 = 1.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		z3 = false;
		tile6.explodeable = false;
		mossStone = tile6;
		stoneTile10000 = new StoneTile(49, 37);
		f0 = 10.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		stoneTile2 = stoneTile10000;
		stoneTile10000.soundType = tile$SoundType1;
		z3 = false;
		stoneTile2.explodeable = false;
		obsidian = stoneTile2;
		metalTile30 = new MetalTile(52, 41);
		f0 = 5.0F;
		f0 = 1.0F;
		f0 = 0.7F;
		tile$SoundType1 = Tile$SoundType.metal;
		metalTile36 = metalTile30;
		metalTile30.soundType = tile$SoundType1;
		z3 = false;
		metalTile36.explodeable = false;
		diamond = metalTile36;
		OreTile oreTile20 = new OreTile(51, 50);
		f0 = 3.0F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.stone;
		OreTile oreTile21 = oreTile19;
		oreTile19.soundType = tile$SoundType1;
		z3 = false;
		oreTile21.explodeable = false;
		diamondOre = oreTile21;
		tile13 = new CactusTile(50, 51);
		f0 = 0.8F;
		f0 = 1.0F;
		f0 = 1.0F;
		tile$SoundType1 = Tile$SoundType.cloth;
		tile6 = tile13;
		tile13.soundType = tile$SoundType1;
		cactus = tile6;
	}
}
