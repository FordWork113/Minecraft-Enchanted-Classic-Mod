package com.mojang.minecraft.gui;

import org.lwjgl.opengl.GL11;

public class GenerateLevelScreen extends GuiScreen {
	private String[] levelType = new String[]{"Classic", "Island", "Floating", "Flat"};
	private String[] levelShape = new String[]{"Square", "Long", "Deep"};
	private String[] levelSize = new String[]{"Small", "Normal", "Huge", "Giant"};
	//private String[] levelTheme = new String[]{"Normal", "Hell", "Paradise", "Woods", "Snow", "Desert"};
	private String[] levelTheme = new String[]{"Normal", "Hell", "Snow", "Desert"};
	private String[] gameMode = new String[]{"Creative", "Survival", "Hardcore"};
	private int selectedLevelType = 0;
	private int selectedLevelShape = 0;
	private int selectedLevelSize = 1;
	private int selectedLevelTheme = 0;
	private GuiScreen parent;

	public GenerateLevelScreen(GuiScreen var1) {
	      this.parent = var1;
	}
	
	public final void onOpen() {
		  this.buttons.clear();
		  this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + -10, "Type: "));
		  this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 14, "Shape:"));
		  this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 38, "Size: "));
		  this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 62, "Theme: "));
		  this.buttons.add(new Button(4, this.width / 2 - 100, this.height / 4 + 86, "Gamemode: "));
		  this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 4 + 110 + 12, "Create"));
		  this.buttons.add(new Button(6, this.width / 2 - 100, this.height / 4 + 134 + 12, "Cancel"));
		  
		  this.levelSettings();
	}

	private void levelSettings() {
		  ((Button)this.buttons.get(0)).text = "Type: " + this.levelType[this.selectedLevelType];
		  ((Button)this.buttons.get(1)).text = "Shape: " + this.levelShape[this.selectedLevelShape];
	      ((Button)this.buttons.get(2)).text = "Size: " + this.levelSize[this.selectedLevelSize];
	      ((Button)this.buttons.get(3)).text = "Theme: " + this.levelTheme[this.selectedLevelTheme];
	      ((Button)this.buttons.get(4)).text = "Gamemode: " + this.gameMode[this.minecraft.selectedGameMode];
	}

	protected final void onButtonClick(Button var1) {
		if(var1.id == 6) {
			this.minecraft.setCurrentScreen(this.parent);
		} else if(var1.id == 5) {
			this.minecraft.generateLevel(this.selectedLevelSize, this.selectedLevelShape, this.selectedLevelType, this.selectedLevelTheme);
			this.minecraft.setCurrentScreen((GuiScreen)null);
			this.minecraft.grabMouse();
		} else if(var1.id == 0) {
			this.selectedLevelType = (this.selectedLevelType + 1) % this.levelType.length;
		} else if(var1.id == 1) {
			this.selectedLevelShape = (this.selectedLevelShape + 1) % this.levelShape.length;
		} else if(var1.id == 2) {
			this.selectedLevelSize = (this.selectedLevelSize + 1) % this.levelSize.length;
		} else if(var1.id == 3) {
			this.selectedLevelTheme = (this.selectedLevelTheme + 1) % this.levelTheme.length;
		} else if(var1.id == 4) {
			this.minecraft.selectedGameMode = (this.minecraft.selectedGameMode + 1) % this.gameMode.length;
		}

		this.levelSettings();
	}

	   protected final void onKeyPress(char var1, int var2) {
		   if(var2 == 1 && this.minecraft.level != null) {
			   this.minecraft.setCurrentScreen((GuiScreen)null);
			   this.minecraft.grabMouse();
		   } else if(var2 == 1 && this.minecraft.level == null) {
			   this.minecraft.setCurrentScreen(this.parent);
		   }

	   }
	
	public final void render(int var1, int var2) {
	   if(this.minecraft.level == null) {
		 this.drawBackground();
	   } else {
		 drawFadingBox(0, 0, this.width, this.height, 1610941696, -1607454624);
	   }
	   
	   drawCenteredString(this.fontRenderer, "Generate new level", this.width / 2, 30, 16777215);
	   super.render(var1, var2);
    }
}
