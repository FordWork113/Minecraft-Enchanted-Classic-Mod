package com.mojang.minecraft.level.generator;

import com.mojang.minecraft.ProgressBarDisplay;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.generator.noise.CombinedNoise;
import com.mojang.minecraft.level.generator.noise.OctaveNoise;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.phys.AABB;
import com.mojang.util.MathHelper;
import java.util.ArrayList;
import java.util.Random;

public final class LevelGenerator {

   private ProgressBarDisplay progressBar;
   private int width;
   private int depth;
   private int height;
   private Random random = new Random();
   private byte[] blocks;
   private int waterLevel;
   private int groundLevel;
   private int[] h = new int[1048576];
   public boolean islandGen;
   public boolean floatingGen;
   public boolean flatGen;
   public int levelType;
   public Level level;

   public LevelGenerator(ProgressBarDisplay var1) {
      this.progressBar = var1;
   }

	public final Level generate(String var1, int var2, int var3, int var4) {
		this.progressBar.setTitle("Generating level");
		Level var37 = new Level();
		var37.waterLevel = this.waterLevel;
		var37.groundLevel = this.groundLevel;
		this.width = var2;
		this.depth = var3;
		this.height = var4;
		this.blocks = new byte[var2 * var3 * var4];
		int var5 = 1;
		if(this.floatingGen) {
			var5 = (var4 - 64) / 48 + 1;
		}
		
	    long seed = this.random.nextLong();
	    this.random.setSeed(seed);

		LevelGenerator var8;
		int var42;
		int var46;
		int var48;
		for(int var6 = 0; var6 < var5; ++var6) {
			this.waterLevel = var4 - 32 - var6 * 48;
			this.groundLevel = this.waterLevel - 2;
			int[] var7;
			int var18;
			int var21;
			double var28;
			int[] var39;
			int var49;
			int var55;
			if(this.flatGen) {
				var7 = new int[var2 * var3];

				for(int var38 = 0; var38 < var7.length; ++var38) {
					var7[var38] = 0;
				}
			} else {
				this.progressBar.setText("Raising..");
				var8 = this;
				CombinedNoise var9 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
				CombinedNoise var10 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
				OctaveNoise var11 = new OctaveNoise(this.random, 6);
				OctaveNoise var12 = new OctaveNoise(this.random, 2);
				int[] var13 = new int[this.width * this.depth];
				var18 = 0;

				label364:
				while(true) {
					if(var18 >= var8.width) {
						var7 = var13;
						this.progressBar.setText("Eroding..");
						var39 = var13;
						var8 = this;
						var10 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
						CombinedNoise var44 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
						var48 = 0;

						while(true) {
							if(var48 >= var8.width) {
								break label364;
							}

							var8.setProgress(var48 * 100 / (var8.width - 1));

							for(var49 = 0; var49 < var8.depth; ++var49) {
								double var16 = var10.compute((double)(var48 << 1), (double)(var49 << 1)) / 8.0D;
								
								var18 = var44.compute((double)(var48 << 1), (double)(var49 << 1)) > 0.0D ? 1 : 0;
								if(var16 > 2.0D) {
									var55 = var39[var48 + var49 * var8.width];
									var55 = ((var55 - var18) / 2 << 1) + var18;
									var39[var48 + var49 * var8.width] = var55;
								}
							}

							++var48;
						}
					}

					double var19 = Math.abs(((double)var18 / ((double)var8.width - 1.0D) - 0.5D) * 2.0D);
					var8.setProgress(var18 * 100 / (var8.width - 1));

					for(var21 = 0; var21 < var8.depth; ++var21) {
						double var22 = Math.abs(((double)var21 / ((double)var8.depth - 1.0D) - 0.5D) * 2.0D);
						double var24 = var9.compute((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 6.0D + -4.0D;
						double var26 = var10.compute((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 5.0D + 10.0D + -4.0D;
						var28 = var11.compute((double)var18, (double)var21) / 8.0D;
						if(var28 > 0.0D) {
							var26 = var24;
						}

						double var30 = Math.max(var24, var26) / 2.0D;
						if(var8.islandGen) {
							double var32 = Math.sqrt(var19 * var19 + var22 * var22) * (double)1.2F;
							double var35 = var12.compute((double)((float)var18 * 0.05F), (double)((float)var21 * 0.05F)) / 4.0D + 1.0D;
							var32 = Math.min(var32, var35);
							var32 = Math.max(var32, Math.max(var19, var22));
							if(var32 > 1.0D) {
								var32 = 1.0D;
							}

							if(var32 < 0.0D) {
								var32 = 0.0D;
							}

							var32 *= var32;
							var30 = var30 * (1.0D - var32) - var32 * 10.0D + 5.0D;
							if(var30 < 0.0D) {
								var30 -= var30 * var30 * (double)0.2F;
							}
						} else if(var30 < 0.0D) {
							var30 *= 0.8D;
						}

						var13[var18 + var21 * var8.width] = (int)var30;
					}

					++var18;
				}
			}

			this.progressBar.setText("Soiling..");
			var39 = var7;
			var8 = this;
			var42 = this.width;
			var46 = this.depth;
			var48 = this.height;
			OctaveNoise var50 = new OctaveNoise(this.random, 8);
			OctaveNoise var51 = new OctaveNoise(this.random, 8);

			int var17;
			int var20;
			int var25;
			int var27;
			int var70;
			for(var17 = 0; var17 < var42; ++var17) {
				double var53 = Math.abs(((double)var17 / ((double)var42 - 1.0D) - 0.5D) * 2.0D);
				var8.setProgress(var17 * 100 / (var8.width - 1));

				for(var20 = 0; var20 < var46; ++var20) {
					double var60 = Math.abs(((double)var20 / ((double)var46 - 1.0D) - 0.5D) * 2.0D);
					double var23 = Math.max(var53, var60);
					var23 = var23 * var23 * var23;
					var25 = (int)(var50.compute((double)var17, (double)var20) / 24.0D) - 4;
					var70 = var39[var17 + var20 * var42] + var8.waterLevel;
					var27 = var70 + var25;
					var39[var17 + var20 * var42] = Math.max(var70, var27);
					if(var39[var17 + var20 * var42] > var48 - 2) {
						var39[var17 + var20 * var42] = var48 - 2;
					}

					if(var39[var17 + var20 * var42] <= 0) {
						var39[var17 + var20 * var42] = 1;
					}

					var28 = var51.compute((double)var17 * 2.3D, (double)var20 * 2.3D) / 24.0D;
					int var73 = (int)(Math.sqrt(Math.abs(var28)) * Math.signum(var28) * 20.0D) + var8.waterLevel;
					var73 = (int)((double)var73 * (1.0D - var23) + var23 * (double)var8.height);
					if(var73 > var8.waterLevel) {
						var73 = var8.height;
					}

					for(int var31 = 0; var31 < var48; ++var31) {
						int var77 = (var31 * var8.depth + var20) * var8.width + var17;
						int var33 = 0;
						if(var31 <= var70) {
							var33 = Block.DIRT.id;
						}
						
						if(this.levelType == 3 && var31 <= var70) {
							var33 = Block.SANDSTONE.id;
						}

						if(var31 <= var27) {
							var33 = Block.STONE.id;
						}
			               
						if(var8.floatingGen && var31 < var73) {
							var33 = 0;
						}
	
			            if(!var8.floatingGen && var31 == 0) {
			                  var33 = Block.STATIONARY_LAVA.id;
			            }

						if(var8.blocks[var77] == 0) {
							var8.blocks[var77] = (byte)var33;
						}
					}
				}
			}

			int var52;
			if(var6 == var5 - 1) {
				this.progressBar.setText("Carving..");
				boolean var43 = true;
				boolean var40 = false;
				var8 = this;
				var46 = this.width;
				var48 = this.depth;
				var49 = this.height;
				var52 = var46 * var48 * var49 / 256 / 64 << 1;
				var17 = 0;

				while(true) {
					if(var17 >= var52) {
						this.populateOre(Block.COAL_ORE.id, 90, 1, 4);
						this.populateOre(Block.IRON_ORE.id, 70, 2, 4);
						this.populateOre(Block.GOLD_ORE.id, 50, 3, 4);
						this.populateOre(Block.RUBY_ORE.id, 35, 4, 4);
						this.populateOre(Block.DIAMOND_ORE.id, 20, 5, 4);
						break;
					}

					var8.setProgress(var17 * 100 / (var52 - 1) / 4);
					float var54 = var8.random.nextFloat() * (float)var46;
					float var56 = var8.random.nextFloat() * (float)var49;
					float var57 = var8.random.nextFloat() * (float)var48;
					var21 = (int)((var8.random.nextFloat() + var8.random.nextFloat()) * 200.0F);
					float var61 = var8.random.nextFloat() * (float)Math.PI * 2.0F;
					float var63 = 0.0F;
					float var64 = var8.random.nextFloat() * (float)Math.PI * 2.0F;
					float var67 = 0.0F;
					float var71 = var8.random.nextFloat() * var8.random.nextFloat();

					for(var27 = 0; var27 < var21; ++var27) {
						var54 += MathHelper.sin(var61) * MathHelper.cos(var64);
						var57 += MathHelper.cos(var61) * MathHelper.cos(var64);
						var56 += MathHelper.sin(var64);
						var61 += var63 * 0.2F;
						var63 *= 0.9F;
						var63 += var8.random.nextFloat() - var8.random.nextFloat();
						var64 += var67 * 0.5F;
						var64 *= 0.5F;
						var67 *= 12.0F / 16.0F;
						var67 += var8.random.nextFloat() - var8.random.nextFloat();
						if(var8.random.nextFloat() >= 0.25F) {
							float var72 = var54 + (var8.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var29 = var56 + (var8.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var75 = var57 + (var8.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var74 = ((float)var8.height - var29) / (float)var8.height;
							float var78 = 1.2F + (var74 * 3.5F + 1.0F) * var71;
							float var76 = MathHelper.sin((float)var27 * (float)Math.PI / (float)var21) * var78;

							for(int var41 = (int)(var72 - var76); var41 <= (int)(var72 + var76); ++var41) {
								for(int var79 = (int)(var29 - var76); var79 <= (int)(var29 + var76); ++var79) {
									for(int var36 = (int)(var75 - var76); var36 <= (int)(var75 + var76); ++var36) {
										float var45 = (float)var41 - var72;
										float var14 = (float)var79 - var29;
										float var15 = (float)var36 - var75;
										var45 = var45 * var45 + var14 * var14 * 2.0F + var15 * var15;
										if(var45 < var76 * var76 && var41 > 0 && var79 > 0 && var36 > 0 && var41 < var8.width - 1 && var79 < var8.height - 1 && var36 < var8.depth - 1) {
											var42 = (var79 * var8.depth + var36) * var8.width + var41;
											if(var8.blocks[var42] == Block.STONE.id) {
												var8.blocks[var42] = 0;
											}
										}
									}
								}
							}
						}
					}

					++var17;
				}
			}

			if(!this.floatingGen && this.levelType != 3) {
				this.progressBar.setText("Watering..");
				var8 = this;
				var48 = Block.STATIONARY_WATER.id;
				if (this.levelType == 1) {
                    var48 = Block.STATIONARY_LAVA.id;
                }
				
				if(this.levelType == 2) {
					var48 = Block.ICE.id;
				}
				
				this.setProgress(0);

				for(var49 = 0; var49 < var8.width; ++var49) {
					var8.flood(var49, var8.waterLevel - 1, 0, 0, var48);
					var8.flood(var49, var8.waterLevel - 1, var8.depth - 1, 0, var48);
				}

				for(var49 = 0; var49 < var8.depth; ++var49) {
					var8.flood(0, var8.waterLevel - 1, var49, 0, var48);
					var8.flood(var8.width - 1, var8.waterLevel - 1, var49, 0, var48);
				}

				var49 = var8.width * var8.depth / 8000;

				for(var52 = 0; var52 < var49; ++var52) {
					if(var52 % 100 == 0) {
						var8.setProgress(var52 * 100 / (var49 - 1));
					}

					var17 = var8.random.nextInt(var8.width);
					var18 = var8.waterLevel - 1 - var8.random.nextInt(2);
					var55 = var8.random.nextInt(var8.depth);
					if(var8.blocks[(var18 * var8.depth + var55) * var8.width + var17] == 0) {
						var8.flood(var17, var18, var55, 0, var48);
					}
				}

				var8.setProgress(100);
				this.progressBar.setText("Melting..");
				var8 = this;
				var42 = this.width * this.depth * this.height / 20000;

				for(var46 = 0; var46 < var42; ++var46) {
					if(var46 % 100 == 0) {
						var8.setProgress(var46 * 100 / (var42 - 1));
					}

					var48 = var8.random.nextInt(var8.width);
					var49 = (int)(var8.random.nextFloat() * var8.random.nextFloat() * (float)(var8.waterLevel - 3));
					var52 = var8.random.nextInt(var8.depth);
					if(var8.blocks[(var49 * var8.depth + var52) * var8.width + var48] == 0) {
						var8.flood(var48, var49, var52, 0, Block.STATIONARY_LAVA.id);
					}
				}

				var8.setProgress(100);
			}

			this.progressBar.setText("Growing..");
			var39 = var7;
			var8 = this;
			var42 = this.width;
			var46 = this.depth;
			var50 = new OctaveNoise(this.random, 8);
			var51 = new OctaveNoise(this.random, 8);
			var52 = this.waterLevel - 1;
			
			/*if(this.levelType == 2) {
				var52 += 2;
			}*/

			int var62;
			int var65;
			int var68 = 0;
			int var57 = 1;
			for(var17 = 0; var17 < var42; ++var17) {
				var8.setProgress(var17 * 100 / (var8.width - 1));

				for(var18 = 0; var18 < var46; ++var18) {
					boolean var58 = var50.compute((double)var17, (double)var18) > 8.0D;
					
					/*if(var8.levelType == 2) {
						var58 = var50.compute((double)var17, (double)var18) > -32.0D;
					}*/
					
					if(var8.islandGen || var8.levelType == 1 || var8.levelType == 3) {
						var58 = var50.compute((double)var17, (double)var18) > -8.0D;
					}
					
					boolean var59 = var51.compute((double)var17, (double)var18) > 12.0D;
					var21 = var39[var17 + var18 * var42];
					var62 = (var21 * var8.depth + var18) * var8.width + var17;
					var65 = var8.blocks[((var21 + 1) * var8.depth + var18) * var8.width + var17] & 255;
					if((var65 == Block.WATER.id || var65 == Block.STATIONARY_WATER.id) && var21 <= var8.waterLevel - 1 && var59) {
						if(this.random.nextInt(16) != 0) {
						    var8.blocks[var62] = (byte)Block.GRAVEL.id;
						} else {
							var8.blocks[var62] = (byte)Block.CLAY.id;
						}
					}
					
					if(var65 == 0) {
						
						if(this.levelType != 1 && this.levelType != 2 && this.levelType != 3) {
						    var68 = Block.GRASS.id;
						} else if(this.levelType == 1) {
							var68 = Block.DIRT.id;
						} else if(this.levelType == 2) {
							var68 = Block.SNOW_GRASS.id;
						} else if(this.levelType == 3) {
							var68 = Block.SAND.id;
						} else {
							var68 = Block.GRASS.id;
						}

						if(var21 <= var8.waterLevel - 1 && var58 && this.levelType != 2 && this.levelType != 3) {
							var68 = Block.SAND.id;
						}
						
						if(var8.blocks[var62] != 0 && var68 > 0) {
							var8.blocks[var62] = (byte)var68;
						}
									
					}
				}
			}

			this.progressBar.setText("Planting..");
			var39 = var7;
			var8 = this;
			var42 = this.width;
			var46 = this.width * this.depth / 3000;

			for(var48 = 0; var48 < var46; ++var48) {
				var49 = var8.random.nextInt(3);
				var8.setProgress(var48 * 50 / var46);
				var52 = var8.random.nextInt(var8.width);
				var17 = var8.random.nextInt(var8.depth);

				for(var18 = 0; var18 < 10; ++var18) {
					var55 = var52;
					var20 = var17;

					for(var21 = 0; var21 < 5; ++var21) {
						var55 += var8.random.nextInt(6) - var8.random.nextInt(6);
						var20 += var8.random.nextInt(6) - var8.random.nextInt(6);
				        
						if((var49 < 5 || var8.random.nextInt(4) == 0) && var55 >= 0 && var20 >= 0 && var55 < var8.width && var20 < var8.depth) {
							var62 = var39[var55 + var20 * var42] + 1;
							boolean var66 = (var8.blocks[(var62 * var8.depth + var20) * var8.width + var55] & 255) == 0;
							if(var66) {
								var68 = (var62 * var8.depth + var20) * var8.width + var55;
								var25 = var8.blocks[((var62 - 1) * var8.depth + var20) * var8.width + var55] & 255;
								if(var25 == Block.GRASS.id) {
									if(var49 == 0) {
										var8.blocks[var68] = (byte)Block.DANDELION.id;
									} else if(var49 == 1) {
										var8.blocks[var68] = (byte)Block.ROSE.id;
									} else if(var49 == 2) {
										var8.blocks[var68] = (byte)Block.BLUE_ROSE.id;
									} else if(var49 == 3) {
										var8.blocks[var68] = (byte)Block.DANGELION.id;
									} else if(var49 == 4) {
										var8.blocks[var68] = (byte)Block.BUSH.id;
									}
								}
									
						            for(var21 = 0; var21 < 10; ++var21) {
						                if (var52 >= 0 && var17 >= 0 && var52 < var8.width && var17 < var8.depth) {
						                   var62 = var39[var55 + var20 * var42] + 1;
						                   if ((var8.blocks[(var62 * var8.depth + var17) * var8.width + var52] & 255) == 0) {
						                      var68 = (var62 * var8.depth + var17) * var8.width + var52;
						                      if ((var8.blocks[((var62 - 1) * var8.depth + var17) * var8.width + var52] & 255) == Block.SAND.id) {
						                         var8.blocks[var68] = (byte)Block.DEAD_BUSH.id;
						                      }
						                   }
						                }
						             }

							}
						}
					}
				}
			}
			
		    Level var89;
		    (var89 = new Level()).waterLevel = this.waterLevel;
		    var89.createTime = System.currentTimeMillis();
		    var89.creator = var1;
		    var89.name = "A Nice World";
		    var89.seed = seed;
			var39 = var7;
			var8 = this;
			var42 = this.width;
	        var48 = this.width * this.depth * this.height / 2000;
	        
			for(var49 = 0; var49 < var48; ++var49) {
				var52 = var8.random.nextInt(2);
				var8.setProgress(var49 * 50 / (var48 - 1) + 50);
				var17 = var8.random.nextInt(var8.width);
				var18 = var8.random.nextInt(var8.height);
				var55 = var8.random.nextInt(var8.depth);


				for(var20 = 0; var20 < 20; ++var20) {
					var21 = var17;
					var62 = var18;
					var65 = var55;

					for(var68 = 0; var68 < 5; ++var68) {
						var21 += var8.random.nextInt(6) - var8.random.nextInt(6);
						var62 += var8.random.nextInt(2) - var8.random.nextInt(2);
						var65 += var8.random.nextInt(6) - var8.random.nextInt(6);
						if((var52 < 2 || var8.random.nextInt(4) == 0) && var21 >= 0 && var65 >= 0 && var62 > 0 && var21 < var8.width && var65 < var8.depth && var62 < var39[var21 + var65 * var42] - 1) {
							boolean var69 = (var8.blocks[(var62 * var8.depth + var65) * var8.width + var21] & 255) == 0;
							if(var69) {
								var70 = (var62 * var8.depth + var65) * var8.width + var21;
								var27 = var8.blocks[((var62 - 1) * var8.depth + var65) * var8.width + var21] & 255;
								if(var27 == Block.STONE.id) {
									if(var52 == 0) {
										var8.blocks[var70] = (byte)Block.BROWN_MUSHROOM.id;
									} else if(var52 == 1) {
										var8.blocks[var70] = (byte)Block.RED_MUSHROOM.id;
									}
								}

							}
						}
					}
				}
			}
			
	        /*for(int var501 = 0; var501 < var51; ++var501) {
        	this.setProgress(var501 * 50 / (var51 - 1) + 50);
            int var24 = this.random.nextInt(this.width);
            int var27 = this.random.nextInt(this.height);
            int var18 = this.random.nextInt(this.depth);

            for(int var12 = 0; var12 < 20; ++var12) {
                int var491 = var24;
                int var29 = var27;
                int var62 = var18;
                
               int var434 = this.random.nextInt(100);
               if (var434 == 1 && var491 >= 0 && var29 >= 0 && var62 >= 0 && var491 < this.width && var29 < this.height && var62 < this.depth) {
                  var1.createDungeon(var1, this.random.nextInt(this.width), this.random.nextInt(30), this.random.nextInt(this.depth));
               }
            }
         }*/
		
		}

		var37.levelType = this.levelType;	
		var37.cloudHeight = var4 + 2;
		if(this.floatingGen) {
			this.groundLevel = -128;
			this.waterLevel = this.groundLevel + 1;
			var37.borderType = 1;
			var37.cloudHeight = -16;
		} else if(this.islandGen) {
			this.groundLevel = this.waterLevel - 9;
		} else if(this.flatGen){
			this.groundLevel = this.waterLevel + 1;
			this.waterLevel = this.groundLevel - 16;
		}

		if (this.levelType == 1) {
         var37.cloudColor = 2164736;
         var37.fogColor = 1049600;
         var37.skyColor = 1049600;
         if (this.floatingGen) {
            var37.cloudHeight = var4 + 2;
            this.waterLevel = -16;
         }
        }

		/*if(this.levelType == 2) {
			var37.skyColor = 13033215;
			var37.fogColor = 13033215;
			var37.cloudColor = 15658751;
			var37.cloudHeight = var4 + 64;
			var48 = 1000;
		}

		if(this.levelType == 3) {
			var37.skyColor = 7699847;
			var37.fogColor = 5069403;
			var37.cloudColor = 5069403;
		}*/
		
		if(this.levelType == 2) {
			var37.skyColor = 7699847;
			var37.fogColor = 5069403;
			var37.cloudColor = 5069403;
		}
		
		if(!this.flatGen && this.levelType == 3) {
			this.groundLevel = this.waterLevel + 1;
			this.waterLevel = this.groundLevel - 16;
		}

		var37.waterLevel = this.waterLevel;
		var37.groundLevel = this.groundLevel;
		this.progressBar.setText("Calculating light..");
		var37.setData(var2, var4, var3, this.blocks);
		this.progressBar.setText("Post-processing..");

        this.growPlant(var37);

		System.currentTimeMillis();
		return var37;
	}

	private void growPlant(Level var1) {
        int var2 = this.width * this.depth * this.height / 5000;
		
		for(int var3 = 0; var3 < var2; ++var3) {
			this.setProgress(var3 * 50 / var2 + 50);
			int var4 = this.random.nextInt(this.width);
			int var5 = this.random.nextInt(this.height);
			int var6 = this.random.nextInt(this.depth);


			for(int var7 = 0; var7 < 100; ++var7) {
				int var8 = var4;
				int var9 = var5;
				int var10 = var6;

				for(int var11 = 0; var11 < 20; ++var11) {
					var8 += this.random.nextInt(6) - this.random.nextInt(6);
					var10 += this.random.nextInt(6) - this.random.nextInt(6);
					if(var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.width && var9 < this.height && var10 < this.depth && this.random.nextInt(4) == 0) {
						if(this.random.nextInt(4) == 0) {
						var1.maybeGrowSpruceTree(var1, this.random, var8, var9, var10);
						} else if(this.random.nextInt(6) == 0) {
						var1.maybeGrowRedTree(var8, var9, var10);
						} else if(this.random.nextInt(6) == 0) {
						var1.maybeGrowBlackTree(var8, var9, var10);
						}} else if(this.random.nextInt(2) == 0) {
						var1.maybeGrowTree(var8, var9, var10);
						var1.maybeGrowBirchTree(var8, var9, var10);
				}
			}

		}
	  }
		
		int var51 = this.width * this.depth * this.height / 2000;
		
        for(int var501 = 0; var501 < var51; ++var501) {
            this.setProgress(var501 * 50 / (var51 - 1) + 50);

            int var24 = this.random.nextInt(this.width);
            int var11 = this.random.nextInt(this.height);
            int var27 = this.random.nextInt(this.depth);

            for(int var12 = 0; var12 < 20; ++var12) {
               int var491 = var24;
               int var29 = var11;
               int var25 = var27;

               for(int var26 = 0; var26 < 20; ++var26) {
                  var491 += this.random.nextInt(30) - this.random.nextInt(30);
                  var29 += this.random.nextInt(30) - this.random.nextInt(30);
                  var25 += this.random.nextInt(30) - this.random.nextInt(30);

                  if (var491 >= 0 && var29 >= 0 && var25 >= 0 && var491 < this.width && var29 < this.height && var25 < this.depth) {
                     if (this.random.nextInt(12) == 0) {
                        var1.cactusSpawn(var491, var29, var25);
                     }
                  }
               }
            }
         }
       
   }
	
	private void populateOre(int var1, int var2, int var3, int var4) {
		byte var25 = (byte)var1;
		var4 = this.width;
		int var5 = this.depth;
		int var6 = this.height;
		int var7 = var4 * var5 * var6 / 256 / 64 * var2 / 100;

		for(int var8 = 0; var8 < var7; ++var8) {
			this.setProgress(var8 * 100 / (var7 - 1) / 4 + var3 * 100 / 4);
			float var9 = this.random.nextFloat() * (float)var4;
			float var10 = this.random.nextFloat() * (float)var6;
			float var11 = this.random.nextFloat() * (float)var5;
			int var12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)var2 / 100.0F);
			float var13 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float var14 = 0.0F;
			float var15 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float var16 = 0.0F;

			for(int var17 = 0; var17 < var12; ++var17) {
				var9 += MathHelper.sin(var13) * MathHelper.cos(var15);
				var11 += MathHelper.cos(var13) * MathHelper.cos(var15);
				var10 += MathHelper.sin(var15);
				var13 += var14 * 0.2F;
				var14 *= 0.9F;
				var14 += this.random.nextFloat() - this.random.nextFloat();
				var15 += var16 * 0.5F;
				var15 *= 0.5F;
				var16 *= 0.9F;
				var16 += this.random.nextFloat() - this.random.nextFloat();
				float var18 = MathHelper.sin((float)var17 * (float)Math.PI / (float)var12) * (float)var2 / 100.0F + 1.0F;

				for(int var19 = (int)(var9 - var18); var19 <= (int)(var9 + var18); ++var19) {
					for(int var20 = (int)(var10 - var18); var20 <= (int)(var10 + var18); ++var20) {
						for(int var21 = (int)(var11 - var18); var21 <= (int)(var11 + var18); ++var21) {
							float var22 = (float)var19 - var9;
							float var23 = (float)var20 - var10;
							float var24 = (float)var21 - var11;
							var22 = var22 * var22 + var23 * var23 * 2.0F + var24 * var24;
							if(var22 < var18 * var18 && var19 > 0 && var20 > 0 && var21 > 0 && var19 < this.width - 1 && var20 < this.height - 1 && var21 < this.depth - 1) {
								int var26 = (var20 * this.depth + var21) * this.width + var19;
								if(this.blocks[var26] == Block.STONE.id) {
									this.blocks[var26] = var25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setProgress(int var1) {
		this.progressBar.setProgress(var1);
	}

	private long flood(int var1, int var2, int var3, int var4, int var5) {
		byte var20 = (byte)var5;
		ArrayList var21 = new ArrayList();
		byte var6 = 0;
		int var7 = 1;

		int var8;
		for(var8 = 1; 1 << var7 < this.width; ++var7) {
		}

		while(1 << var8 < this.depth) {
			++var8;
		}

		int var9 = this.depth - 1;
		int var10 = this.width - 1;
		int var22 = var6 + 1;
		this.h[0] = ((var2 << var8) + var3 << var7) + var1;
		long var13 = 0L;
		var1 = this.width * this.depth;

		while(var22 > 0) {
			--var22;
			var2 = this.h[var22];
			if(var22 == 0 && var21.size() > 0) {
				this.h = (int[])var21.remove(var21.size() - 1);
				var22 = this.h.length;
			}

			var3 = var2 >> var7 & var9;
			int var11 = var2 >> var7 + var8;
			int var12 = var2 & var10;

			int var15;
			for(var15 = var12; var12 > 0 && this.blocks[var2 - 1] == 0; --var2) {
				--var12;
			}

			while(var15 < this.width && this.blocks[var2 + var15 - var12] == 0) {
				++var15;
			}

			int var16 = var2 >> var7 & var9;
			int var17 = var2 >> var7 + var8;
			if(var16 != var3 || var17 != var11) {
				System.out.println("Diagonal flood!?");
			}

			boolean var23 = false;
			boolean var24 = false;
			boolean var18 = false;
			var13 += (long)(var15 - var12);

			for(var12 = var12; var12 < var15; ++var12) {
				this.blocks[var2] = var20;
				boolean var19;
				if(var3 > 0) {
					var19 = this.blocks[var2 - this.width] == 0;
					if(var19 && !var23) {
						if(var22 == this.h.length) {
							var21.add(this.h);
							this.h = new int[1048576];
							var22 = 0;
						}

						this.h[var22++] = var2 - this.width;
					}

					var23 = var19;
				}

				if(var3 < this.depth - 1) {
					var19 = this.blocks[var2 + this.width] == 0;
					if(var19 && !var24) {
						if(var22 == this.h.length) {
							var21.add(this.h);
							this.h = new int[1048576];
							var22 = 0;
						}

						this.h[var22++] = var2 + this.width;
					}

					var24 = var19;
				}

				if(var11 > 0) {
					byte var25 = this.blocks[var2 - var1];
					if((var20 == Block.LAVA.id || var20 == Block.STATIONARY_LAVA.id) && (var25 == Block.WATER.id || var25 == Block.STATIONARY_WATER.id)) {
						this.blocks[var2 - var1] = (byte)Block.STONE.id;
					}

					var19 = var25 == 0;
					if(var19 && !var18) {
						if(var22 == this.h.length) {
							var21.add(this.h);
							this.h = new int[1048576];
							var22 = 0;
						}

						this.h[var22++] = var2 - var1;
					}

					var18 = var19;
				}

				++var2;
			}
		}

		return var13;
	}

}
