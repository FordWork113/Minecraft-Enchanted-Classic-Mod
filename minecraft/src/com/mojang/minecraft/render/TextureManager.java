package com.mojang.minecraft.render;

import com.mojang.minecraft.GameSettings;
import com.mojang.minecraft.ThreadDownloadImageData;
import com.mojang.minecraft.render.texture.TextureFX;
import com.mojang.minecraft.skins.TexturePack;
import com.mojang.minecraft.skins.TexturePackDir;
import com.mojang.minecraft.GLAllocation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureManager {

   public static boolean useMipmaps = false;
   public HashMap textures = new HashMap();
   public HashMap textureImages = new HashMap();
   public IntBuffer idBuffer = BufferUtils.createIntBuffer(1);
   public ByteBuffer textureBuffer = BufferUtils.createByteBuffer(262144);
   public List animations = new ArrayList();
   public GameSettings settings;
   private TexturePackDir skins;
   private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);
   private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
   private boolean clampTexture = false;
   private boolean blurTexture = false;
   private Map urlToImageDataMap = new HashMap();

   public TextureManager(TexturePackDir skins, GameSettings var1) {
      this.settings = var1;
      this.skins = skins;
   }

   public final int load(String var1) {
	  TexturePack skin = this.skins.selected;
      Integer var2;
      if((var2 = (Integer)this.textures.get(var1)) != null) {
         return var2.intValue();
      } else {
         try {
            this.idBuffer.clear();
            GL11.glGenTextures(this.idBuffer);
            int var4 = this.idBuffer.get(0);
			if (var1.startsWith("##")) {
               this.load(this.load1(ImageIO.read(skin.getResource(var1.substring(2)))), var4);
            } else if (var1.startsWith("%clamp%")) {
               this.clampTexture = true;
               this.load(ImageIO.read(skin.getResource(var1.substring(7))), var4);
               this.clampTexture = false;
            } else if (var1.startsWith("%blur%")) {
               this.blurTexture = true;
               this.load(ImageIO.read(skin.getResource(var1.substring(6))), var4);
               this.blurTexture = false;
            } else {
               this.load(ImageIO.read(skin.getResource(var1)), var4);
            }

            this.textures.put(var1, Integer.valueOf(var4));
            return var4;
         } catch (IOException var3) {
            throw new RuntimeException("!!");
         }
      }
   }

   public static BufferedImage load1(BufferedImage var0) {
      int var1 = var0.getWidth() / 16;
      BufferedImage var2;
      Graphics var3 = (var2 = new BufferedImage(16, var0.getHeight() * var1, 2)).getGraphics();

      for(int var4 = 0; var4 < var1; ++var4) {
         var3.drawImage(var0, -var4 << 4, var4 * var0.getHeight(), (ImageObserver)null);
      }

      var3.dispose();
      return var2;
   }

   public final int load(BufferedImage var1) {
      this.idBuffer.clear();
      GL11.glGenTextures(this.idBuffer);
      int var2 = this.idBuffer.get(0);
      this.load(var1, var2);
      this.textureImages.put(Integer.valueOf(var2), var1);
      return var2;
   }

   public void load(BufferedImage var1, int var2) {
      GL11.glBindTexture(3553, var2);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int[] var4 = new int[var2 * var3];
      byte[] var5 = new byte[var2 * var3 << 2];
      var1.getRGB(0, 0, var2, var3, var4, 0, var2);

      for(int var11 = 0; var11 < var4.length; ++var11) {
         int var6 = var4[var11] >>> 24;
         int var7 = var4[var11] >> 16 & 255;
         int var8 = var4[var11] >> 8 & 255;
         int var9 = var4[var11] & 255;
         if(this.settings.anaglyph) {
            int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
            var8 = (var7 * 30 + var8 * 70) / 100;
            var9 = (var7 * 30 + var9 * 70) / 100;
            var7 = var10;
            var8 = var8;
            var9 = var9;
         }

         var5[var11 << 2] = (byte)var7;
         var5[(var11 << 2) + 1] = (byte)var8;
         var5[(var11 << 2) + 2] = (byte)var9;
         var5[(var11 << 2) + 3] = (byte)var6;
      }

      this.textureBuffer.clear();
      this.textureBuffer.put(var5);
      this.textureBuffer.position(0).limit(var5.length);
      GL11.glTexImage2D(3553, 0, 6408, var2, var3, 0, 6408, 5121, this.textureBuffer);
   }

   public final void registerAnimation(TextureFX var1) {
      this.animations.add(var1);
      var1.animate();
   }
   
   public void setupTexture(BufferedImage var1, int var2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		if(useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 * 4];
		var1.getRGB(0, 0, var3, var4, var5, 0, var3);

		int var7;
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;
		int var13;
		int var14;
		for(var7 = 0; var7 < var5.length; ++var7) {
			var8 = var5[var7] >> 24 & 255;
			var9 = var5[var7] >> 16 & 255;
			var10 = var5[var7] >> 8 & 255;
			var11 = var5[var7] & 255;
			if(this.settings != null && this.settings.anaglyph) {
				var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				var13 = (var9 * 30 + var10 * 70) / 100;
				var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			var6[var7 * 4 + 0] = (byte)var9;
			var6[var7 * 4 + 1] = (byte)var10;
			var6[var7 * 4 + 2] = (byte)var11;
			var6[var7 * 4 + 3] = (byte)var8;
		}

		this.imageData.clear();
		this.imageData.put(var6);
		this.imageData.position(0).limit(var6.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var3, var4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
		if(useMipmaps) {
			for(var7 = 1; var7 <= 4; ++var7) {
				var8 = var3 >> var7 - 1;
				var9 = var3 >> var7;
				var10 = var4 >> var7;

				for(var11 = 0; var11 < var9; ++var11) {
					for(var12 = 0; var12 < var10; ++var12) {
						var13 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 0) * var8) * 4);
						var14 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 0) * var8) * 4);
						int var15 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 1) * var8) * 4);
						int var16 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 1) * var8) * 4);
						int var17 = this.weightedAverageColor(this.weightedAverageColor(var13, var14), this.weightedAverageColor(var15, var16));
						this.imageData.putInt((var11 + var12 * var9) * 4, var17);
					}
				}

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, var7, GL11.GL_RGBA, var9, var10, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
			}
		}

	}
   
	private int weightedAverageColor(int var1, int var2) {
		int var3 = (var1 & -16777216) >> 24 & 255;
		int var4 = (var2 & -16777216) >> 24 & 255;
		short var5 = 255;
		if(var3 + var4 == 0) {
			var3 = 1;
			var4 = 1;
			var5 = 0;
		}

		int var6 = (var1 >> 16 & 255) * var3;
		int var7 = (var1 >> 8 & 255) * var3;
		int var8 = (var1 & 255) * var3;
		int var9 = (var2 >> 16 & 255) * var4;
		int var10 = (var2 >> 8 & 255) * var4;
		int var11 = (var2 & 255) * var4;
		int var12 = (var6 + var9) / (var3 + var4);
		int var13 = (var7 + var10) / (var3 + var4);
		int var14 = (var8 + var11) / (var3 + var4);
		return var5 << 24 | var12 << 16 | var13 << 8 | var14;
	}
   
	public void deleteTexture(int var1) {
		this.textureImages.remove(Integer.valueOf(var1));
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(var1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}
	
	public int allocateAndSetupTexture(BufferedImage var1) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int var2 = this.singleIntBuffer.get(0);
		this.setupTexture(var1, var2);
		this.textureImages.put(Integer.valueOf(var2), var1);
		return var2;
	}
	
	public void bind(int id) {
		 if (id >= 0) {
		    GL11.glBindTexture(3553, id);
		 }
	}
	
    private BufferedImage makeStrip(BufferedImage source) {
		int cols = source.getWidth() / 16;
		BufferedImage out = new BufferedImage(16, source.getHeight() * cols, 2);
	    Graphics g = out.getGraphics();

		for(int i = 0; i < cols; ++i) {
		   g.drawImage(source, -i * 16, i * source.getHeight(), (ImageObserver)null);
		}

		g.dispose();
		return out;
	}
	
	public void refreshTextures() {
		TexturePack skin = this.skins.selected;
		Iterator var2 = this.textureImages.keySet().iterator();

		BufferedImage var4;
		while(var2.hasNext()) {
			int var3 = ((Integer)var2.next()).intValue();
			var4 = (BufferedImage)this.textureImages.get(Integer.valueOf(var3));
			this.setupTexture(var4, var3);
		}

		ThreadDownloadImageData var7;
		for(var2 = this.urlToImageDataMap.values().iterator(); var2.hasNext(); var7.textureSetupComplete = false) {
			var7 = (ThreadDownloadImageData)var2.next();
		}

		var2 = this.textures.keySet().iterator();

		while(var2.hasNext()) {
			String var8 = (String)var2.next();

			try {
				if(var8.startsWith("##")) {
					var4 = this.load1(ImageIO.read(skin.getResource(var8.substring(2))));
				} else if(var8.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = ImageIO.read(skin.getResource(var8.substring(7)));
				} else if(var8.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = ImageIO.read(skin.getResource(var8.substring(6)));
				} else {
					var4 = ImageIO.read(skin.getResource(var8));
				}

				int var5 = ((Integer)this.textures.get(var8)).intValue();
				this.setupTexture(var4, var5);
			} catch (IOException var6) {
				var6.printStackTrace();
			}
		}

	}
}
