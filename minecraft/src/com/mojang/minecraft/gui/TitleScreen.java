package com.mojang.minecraft.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.util.MathHelper;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;

public final class TitleScreen extends GuiScreen {
	private String splashString = "missingno";
	private String authorString = "Create by &eFordWork113&w, &eKaban Ivanovych";
    
	public TitleScreen() {
		try {
			ArrayList arrayList1 = new ArrayList();
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(TitleScreen.class.getResourceAsStream("/title/splashes.txt")));
			String string3 = "";

			while((string3 = bufferedReader2.readLine()) != null) {
				string3 = string3.trim();
				if(string3.length() > 0) {
					arrayList1.add(string3);
				}
			}

			this.splashString = (String)arrayList1.get(this.random.nextInt(arrayList1.size()));
		} catch (Exception exception4) {
		}

	}
	
	public final void tick() {
		this.ticks += 0.01F;
	}

	protected final void onKeyPress(char var1, int var2) {}

	public final void onOpen() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		if(calendar1.get(2) + 1 == 6 && calendar1.get(5) == 1) {
			this.splashString = "Happy birthday, Notch!";
		} else if(calendar1.get(2) + 1 == 12 && calendar1.get(5) == 24) {
			this.splashString = "Merry X-mas!";
		} else if(calendar1.get(2) + 1 == 1 && calendar1.get(5) == 1) {
			this.splashString = "Happy new year!";
		}
		
		this.buttons.clear();
	    this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
	    this.buttons.add(new Button(1, this.width / 2 - 92 - 8, this.height / 6 + 92, 98, 12, "Load level.."));
	    this.buttons.add(new Button(2, this.width / 2 - -10 - 8, this.height / 6 + 92, 98, 12, "Multiplayer"));
	    this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
	    this.buttons.add(new Button(4, this.width / 2 - 92 - 8, this.height / 4 + 128, 98, 12, "Options..."));
	    this.buttons.add(new Button(5, this.width / 2 - -10 - 8, this.height / 4 + 128, 98, 12, "Quit Game"));
		
		if(!this.minecraft.devMode) {
			((Button)this.buttons.get(3)).active = false;
		}
		
		if(this.minecraft.session == null) {
			((Button)this.buttons.get(1)).active = false;
			((Button)this.buttons.get(2)).active = false;
			((Button)this.buttons.get(3)).active = false;
		}

	}

	protected final void onButtonClick(Button var1) {

		if(var1.id == 0) {
			this.minecraft.setCurrentScreen(new GenerateLevelScreen(this));
		}

		if(this.minecraft.session != null && var1.id == 1) {
			this.minecraft.setCurrentScreen(new LoadLevelScreen(this));
		}
	    
		if(this.minecraft.session != null && var1.id == 2) {
			this.minecraft.setCurrentScreen(new MultiPlayerScreen(this));  
		}
		
		if(var1.id == 3) {
			this.minecraft.playTutorialLevel(minecraft);
		}
		
		if(var1.id == 4) {
			this.minecraft.setCurrentScreen(new OptionsScreen(this, this.minecraft.settings));
		}
		
		if(var1.id == 5) {
			this.minecraft.shutdown();
		}

	}
	
	public final void render(int var1, int var2) {
		this.drawAnimateBackground();
		ShapeRenderer var6 = ShapeRenderer.instance;
		GL11.glEnable(3553);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textureManager.load("/title/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var6.color(16777215);
		this.drawImage((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f5 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		f5 = f5 * 100.0F / (float)(this.fontRenderer.getWidth(this.splashString) + 32);
		GL11.glScalef(f5, f5, f5);
		drawCenteredString(this.fontRenderer, "&e" + splashString, 0, -8, 16777215);
		GL11.glPopMatrix();
		drawString(this.fontRenderer, this.authorString, this.width - this.fontRenderer.getWidth(this.authorString) - 2, this.height - 10, 16777215);
		drawString(this.fontRenderer, this.minecraft.MOD_NAME + " " + this.minecraft.VER, - -2, this.height - 10, 16777215);
		
		if(this.minecraft.devMode) {
			drawString(this.fontRenderer, "&eDeveloper Mode: " + this.minecraft.devMode, this.width - 110, 2, 16777215);
		}
		
		super.render(var1, var2);
	}
	
    public boolean doesGuiPause() {
  	  return false;
    }
}
