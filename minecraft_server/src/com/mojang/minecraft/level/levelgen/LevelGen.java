package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Random;

import util.Mth;

public final class LevelGen {
	private MinecraftServer levelLoaderListener;
	private int width;
	private int height;
	private int depth;
	private Random random = new Random();
	private byte[] blocks;
	private int waterLevel;
	private int[] coords = new int[1048576];

	public LevelGen(MinecraftServer levelLoaderListener) {
		this.levelLoaderListener = levelLoaderListener;
	}

	public final Level generateLevel(String creator, int width, int height, int depth) {
		this.levelLoaderListener.beginLevelLoading("Generating level");
		this.width = 256;
		this.height = 256;
		this.depth = 64;
		this.waterLevel = 32;
		this.blocks = new byte[256 << 8 << 6];
		this.levelLoaderListener.levelLoadUpdate("Raising..");
		LevelGen levelGen31 = this;
		Distort distort5 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort6 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise perlinNoise7 = new PerlinNoise(this.random, 6);
		int[] i8 = new int[this.width * this.height];
		float f9 = 1.3F;

		int i10;
		int i14;
		for(i14 = 0; i14 < levelGen31.width; ++i14) {
			for(i10 = 0; i10 < levelGen31.height; ++i10) {
				double d16 = distort5.getValue((double)((float)i14 * f9), (double)((float)i10 * f9)) / 6.0D + (double)-4;
				double d18 = distort6.getValue((double)((float)i14 * f9), (double)((float)i10 * f9)) / 5.0D + 10.0D + (double)-4;
				if(perlinNoise7.getValue((double)i14, (double)i10) / 8.0D > 0.0D) {
					d18 = d16;
				}

				double d22;
				if((d22 = Math.max(d16, d18) / 2.0D) < 0.0D) {
					d22 *= 0.8D;
				}

				i8[i14 + i10 * levelGen31.width] = (int)d22;
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Eroding..");
		int[] i32 = i8;
		levelGen31 = this;
		distort6 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort37 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int i41;
		int i43;
		int i48;
		for(i41 = 0; i41 < levelGen31.width; ++i41) {
			for(i43 = 0; i43 < levelGen31.height; ++i43) {
				double d13 = distort6.getValue((double)(i41 << 1), (double)(i43 << 1)) / 8.0D;
				i10 = distort37.getValue((double)(i41 << 1), (double)(i43 << 1)) > 0.0D ? 1 : 0;
				if(d13 > 2.0D) {
					i48 = ((i32[i41 + i43 * levelGen31.width] - i10) / 2 << 1) + i10;
					i32[i41 + i43 * levelGen31.width] = i48;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Soiling..");
		i32 = i8;
		levelGen31 = this;
		int i35 = this.width;
		int i39 = this.height;
		i41 = this.depth;
		PerlinNoise perlinNoise44 = new PerlinNoise(this.random, 8);

		int i17;
		int i19;
		int i20;
		int i46;
		int i53;
		for(i46 = 0; i46 < i35; ++i46) {
			for(i14 = 0; i14 < i39; ++i14) {
				i10 = (int)(perlinNoise44.getValue((double)i46, (double)i14) / 24.0D) - 4;
				i17 = (i48 = i32[i46 + i14 * i35] + levelGen31.waterLevel) + i10;
				i32[i46 + i14 * i35] = Math.max(i48, i17);
				if(i32[i46 + i14 * i35] > i41 - 2) {
					i32[i46 + i14 * i35] = i41 - 2;
				}

				if(i32[i46 + i14 * i35] < 1) {
					i32[i46 + i14 * i35] = 1;
				}

				for(i53 = 0; i53 < i41; ++i53) {
					i19 = (i53 * levelGen31.height + i14) * levelGen31.width + i46;
					i20 = 0;
					if(i53 <= i48) {
						i20 = Tile.dirt.id;
					}

					if(i53 <= i17) {
						i20 = Tile.rock.id;
					}

					if(i53 == 0) {
						i20 = Tile.lava.id;
					}

					levelGen31.blocks[i19] = (byte)i20;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Carving..");
		boolean z36 = true;
		boolean z33 = false;
		levelGen31 = this;
		i39 = this.width;
		i41 = this.height;
		i43 = this.depth;
		i46 = i39 * i41 * i43 / 256 / 64 << 1;

		for(i14 = 0; i14 < i46; ++i14) {
			float f45 = levelGen31.random.nextFloat() * (float)i39;
			float f49 = levelGen31.random.nextFloat() * (float)i43;
			float f50 = levelGen31.random.nextFloat() * (float)i41;
			i53 = (int)((levelGen31.random.nextFloat() + levelGen31.random.nextFloat()) * 200.0F);
			float f54 = levelGen31.random.nextFloat() * (float)Math.PI * 2.0F;
			float f55 = 0.0F;
			float f21 = levelGen31.random.nextFloat() * (float)Math.PI * 2.0F;
			float f57 = 0.0F;
			float f23 = levelGen31.random.nextFloat() * levelGen31.random.nextFloat();

			for(height = 0; height < i53; ++height) {
				f45 += Mth.sin(f54) * Mth.cos(f21);
				f50 += Mth.cos(f54) * Mth.cos(f21);
				f49 += Mth.sin(f21);
				f54 += f55 * 0.2F;
				f55 = (f55 *= 0.9F) + (levelGen31.random.nextFloat() - levelGen31.random.nextFloat());
				f21 = (f21 + f57 * 0.5F) * 0.5F;
				f57 = (f57 *= 0.75F) + (levelGen31.random.nextFloat() - levelGen31.random.nextFloat());
				if(levelGen31.random.nextFloat() >= 0.25F) {
					float f34 = f45 + (levelGen31.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f38 = f49 + (levelGen31.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f11 = f50 + (levelGen31.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f12 = ((float)levelGen31.depth - f38) / (float)levelGen31.depth;
					f12 = 1.2F + (f12 * 3.5F + 1.0F) * f23;
					f12 = Mth.sin((float)height * (float)Math.PI / (float)i53) * f12;

					for(int i15 = (int)(f34 - f12); i15 <= (int)(f34 + f12); ++i15) {
						for(int i24 = (int)(f38 - f12); i24 <= (int)(f38 + f12); ++i24) {
							for(int i25 = (int)(f11 - f12); i25 <= (int)(f11 + f12); ++i25) {
								float f26 = (float)i15 - f34;
								float f27 = (float)i24 - f38;
								float f28 = (float)i25 - f11;
								if(f26 * f26 + f27 * f27 * 2.0F + f28 * f28 < f12 * f12 && i15 >= 1 && i24 >= 1 && i25 >= 1 && i15 < levelGen31.width - 1 && i24 < levelGen31.depth - 1 && i25 < levelGen31.height - 1) {
									int i59 = (i24 * levelGen31.height + i25) * levelGen31.width + i15;
									if(levelGen31.blocks[i59] == Tile.rock.id) {
										levelGen31.blocks[i59] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.addOre(Tile.coalOre.id, 90, 1, 4);
		this.addOre(Tile.ironOre.id, 70, 2, 4);
		this.addOre(Tile.goldOre.id, 50, 3, 4);
		this.levelLoaderListener.levelLoadUpdate("Watering..");
		levelGen31 = this;
		i41 = Tile.calmWater.id;

		for(i43 = 0; i43 < levelGen31.width; ++i43) {
			levelGen31.floodFillWithLiquid(i43, levelGen31.depth / 2 - 1, 0, 0, i41);
			levelGen31.floodFillWithLiquid(i43, levelGen31.depth / 2 - 1, levelGen31.height - 1, 0, i41);
		}

		for(i43 = 0; i43 < levelGen31.height; ++i43) {
			levelGen31.floodFillWithLiquid(0, levelGen31.depth / 2 - 1, i43, 0, i41);
			levelGen31.floodFillWithLiquid(levelGen31.width - 1, levelGen31.depth / 2 - 1, i43, 0, i41);
		}

		i43 = levelGen31.width * levelGen31.height / 8000;

		for(i46 = 0; i46 < i43; ++i46) {
			i14 = levelGen31.random.nextInt(levelGen31.width);
			i10 = levelGen31.waterLevel - 1 - levelGen31.random.nextInt(2);
			i48 = levelGen31.random.nextInt(levelGen31.height);
			if(levelGen31.blocks[(i10 * levelGen31.height + i48) * levelGen31.width + i14] == 0) {
				levelGen31.floodFillWithLiquid(i14, i10, i48, 0, i41);
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Melting..");
		levelGen31 = this;
		i35 = this.width * this.height * this.depth / 20000;

		for(i39 = 0; i39 < i35; ++i39) {
			i41 = levelGen31.random.nextInt(levelGen31.width);
			i43 = (int)(levelGen31.random.nextFloat() * levelGen31.random.nextFloat() * (float)(levelGen31.waterLevel - 3));
			i46 = levelGen31.random.nextInt(levelGen31.height);
			if(levelGen31.blocks[(i43 * levelGen31.height + i46) * levelGen31.width + i41] == 0) {
				levelGen31.floodFillWithLiquid(i41, i43, i46, 0, Tile.calmLava.id);
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Growing..");
		i32 = i8;
		levelGen31 = this;
		i35 = this.width;
		i39 = this.height;
		i41 = this.depth;
		perlinNoise44 = new PerlinNoise(this.random, 8);
		PerlinNoise perlinNoise47 = new PerlinNoise(this.random, 8);

		int i56;
		for(i14 = 0; i14 < i35; ++i14) {
			for(i10 = 0; i10 < i39; ++i10) {
				boolean z51 = perlinNoise44.getValue((double)i14, (double)i10) > 8.0D;
				boolean z52 = perlinNoise47.getValue((double)i14, (double)i10) > 12.0D;
				i19 = ((i53 = i32[i14 + i10 * i35]) * levelGen31.height + i10) * levelGen31.width + i14;
				if(((i20 = levelGen31.blocks[((i53 + 1) * levelGen31.height + i10) * levelGen31.width + i14] & 255) == Tile.water.id || i20 == Tile.calmWater.id) && i53 <= i41 / 2 - 1 && z52) {
					levelGen31.blocks[i19] = (byte)Tile.gravel.id;
				}

				if(i20 == 0) {
					i56 = Tile.grass.id;
					if(i53 <= i41 / 2 - 1 && z51) {
						i56 = Tile.sand.id;
					}

					levelGen31.blocks[i19] = (byte)i56;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Planting..");
		i32 = i8;
		levelGen31 = this;
		i35 = this.width;
		i39 = this.width * this.height / 3000;

		for(i41 = 0; i41 < i39; ++i41) {
			i43 = levelGen31.random.nextInt(2);
			i46 = levelGen31.random.nextInt(levelGen31.width);
			i14 = levelGen31.random.nextInt(levelGen31.height);

			for(i10 = 0; i10 < 10; ++i10) {
				i48 = i46;
				i17 = i14;

				for(i53 = 0; i53 < 5; ++i53) {
					i48 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					i17 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					if((i43 < 2 || levelGen31.random.nextInt(4) == 0) && i48 >= 0 && i17 >= 0 && i48 < levelGen31.width && i17 < levelGen31.height) {
						i19 = i32[i48 + i17 * i35] + 1;
						if((levelGen31.blocks[(i19 * levelGen31.height + i17) * levelGen31.width + i48] & 255) == 0) {
							i56 = (i19 * levelGen31.height + i17) * levelGen31.width + i48;
							if((levelGen31.blocks[((i19 - 1) * levelGen31.height + i17) * levelGen31.width + i48] & 255) == Tile.grass.id) {
								if(i43 == 0) {
									levelGen31.blocks[i56] = (byte)Tile.flower.id;
								} else if(i43 == 1) {
									levelGen31.blocks[i56] = (byte)Tile.rose.id;
								}
							}
						}
					}
				}
			}
		}

		i32 = i8;
		levelGen31 = this;
		i35 = this.width;
		i41 = this.width * this.height * this.depth / 2000;

		for(i43 = 0; i43 < i41; ++i43) {
			i46 = levelGen31.random.nextInt(2);
			i14 = levelGen31.random.nextInt(levelGen31.width);
			i10 = levelGen31.random.nextInt(levelGen31.depth);
			i48 = levelGen31.random.nextInt(levelGen31.height);

			for(i17 = 0; i17 < 20; ++i17) {
				i53 = i14;
				i19 = i10;
				i20 = i48;

				for(i56 = 0; i56 < 5; ++i56) {
					i53 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					i19 += levelGen31.random.nextInt(2) - levelGen31.random.nextInt(2);
					i20 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					if((i46 < 2 || levelGen31.random.nextInt(4) == 0) && i53 >= 0 && i20 >= 0 && i19 >= 1 && i53 < levelGen31.width && i20 < levelGen31.height && i19 < i32[i53 + i20 * i35] - 1 && (levelGen31.blocks[(i19 * levelGen31.height + i20) * levelGen31.width + i53] & 255) == 0) {
						int i58 = (i19 * levelGen31.height + i20) * levelGen31.width + i53;
						if((levelGen31.blocks[((i19 - 1) * levelGen31.height + i20) * levelGen31.width + i53] & 255) == Tile.rock.id) {
							if(i46 == 0) {
								levelGen31.blocks[i58] = (byte)Tile.mushroom1.id;
							} else if(i46 == 1) {
								levelGen31.blocks[i58] = (byte)Tile.mushroom2.id;
							}
						}
					}
				}
			}
		}

		Level level30;
		(level30 = new Level()).waterLevel = this.waterLevel;
		level30.setData(256, 64, 256, this.blocks);
		level30.createTime = System.currentTimeMillis();
		level30.creator = creator;
		level30.name = "A Nice World";
		int[] i42 = i8;
		Level level40 = level30;
		levelGen31 = this;
		i39 = this.width;
		i41 = this.width * this.height / 4000;

		for(i43 = 0; i43 < i41; ++i43) {
			i46 = levelGen31.random.nextInt(levelGen31.width);
			i14 = levelGen31.random.nextInt(levelGen31.height);

			for(i10 = 0; i10 < 20; ++i10) {
				i48 = i46;
				i17 = i14;

				for(i53 = 0; i53 < 20; ++i53) {
					i48 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					i17 += levelGen31.random.nextInt(6) - levelGen31.random.nextInt(6);
					if(i48 >= 0 && i17 >= 0 && i48 < levelGen31.width && i17 < levelGen31.height) {
						i19 = i42[i48 + i17 * i39] + 1;
						if(levelGen31.random.nextInt(4) == 0) {
							level40.maybeGrowTree(i48, i19, i17);
						}
					}
				}
			}
		}

		return level30;
	}

	private void addOre(int tile, int rarity, int min, int max) {
		byte b25 = (byte)tile;
		min = this.width;
		max = this.height;
		int i5 = this.depth;
		int i6 = min * max * i5 / 256 / 64 * rarity / 100;

		for(int i7 = 0; i7 < i6; ++i7) {
			float f8 = this.random.nextFloat() * (float)min;
			float f9 = this.random.nextFloat() * (float)i5;
			float f10 = this.random.nextFloat() * (float)max;
			int i11 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)rarity / 100.0F);
			float f12 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float f13 = 0.0F;
			float f14 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float f15 = 0.0F;

			for(int i16 = 0; i16 < i11; ++i16) {
				f8 += Mth.sin(f12) * Mth.cos(f14);
				f10 += Mth.cos(f12) * Mth.cos(f14);
				f9 += Mth.sin(f14);
				f12 += f13 * 0.2F;
				f13 = (f13 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				f14 = (f14 + f15 * 0.5F) * 0.5F;
				f15 = (f15 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				float f17 = Mth.sin((float)i16 * (float)Math.PI / (float)i11) * (float)rarity / 100.0F + 1.0F;

				for(int i18 = (int)(f8 - f17); i18 <= (int)(f8 + f17); ++i18) {
					for(int i19 = (int)(f9 - f17); i19 <= (int)(f9 + f17); ++i19) {
						for(int i20 = (int)(f10 - f17); i20 <= (int)(f10 + f17); ++i20) {
							float f21 = (float)i18 - f8;
							float f22 = (float)i19 - f9;
							float f23 = (float)i20 - f10;
							if(f21 * f21 + f22 * f22 * 2.0F + f23 * f23 < f17 * f17 && i18 >= 1 && i19 >= 1 && i20 >= 1 && i18 < this.width - 1 && i19 < this.depth - 1 && i20 < this.height - 1) {
								int i24 = (i19 * this.height + i20) * this.width + i18;
								if(this.blocks[i24] == Tile.rock.id) {
									this.blocks[i24] = b25;
								}
							}
						}
					}
				}
			}
		}

	}

	private long floodFillWithLiquid(int x, int y, int z, int source, int tt) {
		byte b20 = (byte)tt;
		ArrayList arrayList21 = new ArrayList();
		byte b6 = 0;
		int i7 = 1;

		int i8;
		for(i8 = 1; 1 << i7 < this.width; ++i7) {
		}

		while(1 << i8 < this.height) {
			++i8;
		}

		int i9 = this.height - 1;
		int i10 = this.width - 1;
		int i22 = b6 + 1;
		this.coords[0] = ((y << i8) + z << i7) + x;
		long j13 = 0L;
		x = this.width * this.height;

		while(i22 > 0) {
			--i22;
			y = this.coords[i22];
			if(i22 == 0 && arrayList21.size() > 0) {
				this.coords = (int[])arrayList21.remove(arrayList21.size() - 1);
				i22 = this.coords.length;
			}

			z = y >> i7 & i9;
			int i11 = y >> i7 + i8;

			int i12;
			int i15;
			for(i15 = i12 = y & i10; i12 > 0 && this.blocks[y - 1] == 0; --y) {
				--i12;
			}

			while(i15 < this.width && this.blocks[y + i15 - i12] == 0) {
				++i15;
			}

			int i16 = y >> i7 & i9;
			int i17 = y >> i7 + i8;
			if(i16 != z || i17 != i11) {
				System.out.println("Diagonal flood!?");
			}

			boolean z23 = false;
			boolean z24 = false;
			boolean z18 = false;
			j13 += (long)(i15 - i12);

			for(i12 = i12; i12 < i15; ++i12) {
				this.blocks[y] = b20;
				boolean z19;
				if(z > 0) {
					if((z19 = this.blocks[y - this.width] == 0) && !z23) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - this.width;
					}

					z23 = z19;
				}

				if(z < this.height - 1) {
					if((z19 = this.blocks[y + this.width] == 0) && !z24) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y + this.width;
					}

					z24 = z19;
				}

				if(i11 > 0) {
					byte b25 = this.blocks[y - x];
					if((b20 == Tile.lava.id || b20 == Tile.calmLava.id) && (b25 == Tile.water.id || b25 == Tile.calmWater.id)) {
						this.blocks[y - x] = (byte)Tile.rock.id;
					}

					if((z19 = b25 == 0) && !z18) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - x;
					}

					z18 = z19;
				}

				++y;
			}
		}

		return j13;
	}
}