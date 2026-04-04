package com.mojang.minecraft;

import com.mojang.minecraft.gamemode.*;
import com.mojang.minecraft.gui.*;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.item.ItemBlock;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.Weather;
import com.mojang.minecraft.level.generator.LevelGenerator;
import com.mojang.minecraft.level.item.Sign;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.Tile$SoundType;
import com.mojang.minecraft.mob.*;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ModelManager;
import com.mojang.minecraft.model.ModelPart;
import com.mojang.minecraft.model.Vec3D;
import com.mojang.minecraft.net.NetworkManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.net.PacketType;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleManager;
import com.mojang.minecraft.particle.WaterDropParticle;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.InputHandlerImpl;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.Chunk;
import com.mojang.minecraft.render.ChunkDirtyDistanceComparator;
import com.mojang.minecraft.render.Frustrum;
import com.mojang.minecraft.render.FrustrumImpl;
import com.mojang.minecraft.render.ItemGuiRenderer;
import com.mojang.minecraft.render.ItemRenderer;
import com.mojang.minecraft.render.LevelRenderer;
import com.mojang.minecraft.render.Renderer;
import com.mojang.minecraft.render.Shaders;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.render.texture.TextureFX;
import com.mojang.minecraft.render.texture.TextureLavaFX;
import com.mojang.minecraft.render.texture.TextureWaterFX;
import com.mojang.minecraft.render.texture.TextureWaterFlowFX;
import com.mojang.minecraft.skins.TexturePackDir;
import com.mojang.minecraft.sound.SoundManager;
import com.mojang.minecraft.sound.SoundPlayer;
import com.mojang.net.NetworkHandler;
import com.mojang.util.MathHelper;
import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public final class Minecraft implements Runnable {
   public final String VER = "v1.10";
   public final String TAG = "";
   public final String MOD_NAME = "Enchanted Classic";
   public final String WINDOW_TITLE = "Minecraft";
   public Mob cameraMob;
   public GameMode gamemode;
   public boolean fullscreen = false;
   public int width;
   public int height;
   public Timer timer = new Timer(20.0F);
   public Level level;
   public LevelRenderer levelRenderer;
   public Player player;
   public ParticleManager particleManager;
   public SessionData session = null;
   public String host;
   public Canvas canvas;
   public boolean levelLoaded = false;
   public volatile boolean waiting = false;
   private Cursor cursor;
   public TextureManager textureManager;
   public FontRenderer fontRenderer;
   public static GuiScreen currentScreen = null;
   public ProgressBarDisplay progressBar = new ProgressBarDisplay(this);
   public Renderer renderer = new Renderer(this);
   public LevelIO levelIo;
   public SoundManager sound;
   private ResourceDownloadThread resourceThread;
   public int ticks;
   private int blockHitTime;
   public String levelName;
   public int levelId;
   public Robot robot;
   public HUDScreen hud;
   public boolean online;
   public NetworkManager networkManager;
   public SoundPlayer soundPlayer;
   public MovingObjectPosition selected;
   public GameSettings settings;
   public GuiScreen guiScreen;
   public MinecraftApplet applet;
   public String server;
   public int port;
   volatile boolean running;
   public String debug;
   public boolean hasMouse;
   private int lastClick;
   public boolean hidegui;
   public TexturePackDir skins;
   public String hoveredPlayer = null;
   public File minecraftDir;
   public Entity entity;
   public boolean raining;
   public boolean snowing;
   private int tempWidth;
   private int tempHeight;
   public boolean devMode = false;
   public int selectedGameMode = 0;
   public boolean editMode;
   public boolean enableShader;
   public static boolean isFlying = false;
   public LevelGenerator levelgen = new LevelGenerator(this.progressBar);
   public String mcdirname = "minecraft";

   public Minecraft(Canvas var1, MinecraftApplet var2, int var3, int var4, boolean var5) {
      this.levelIo = new LevelIO(this.progressBar);
      this.sound = new SoundManager();
      this.ticks = 0;
      this.blockHitTime = 0;
      this.levelName = null;
      this.levelId = 0;
      this.online = false;
      new HumanoidModel(0.0F);
      this.selected = null;
      this.server = null;
      this.port = 0;
      this.running = false;
      this.debug = "";
      this.hasMouse = false;
      this.lastClick = 0;
      this.raining = false;
      this.hidegui = false;
      this.snowing = false;

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var7) {
         var7.printStackTrace();
      }

	  this.tempWidth = var3;
	  this.tempHeight = var4;
	  this.fullscreen = var5;
      this.applet = var2;
      new SleepForeverThread(this);
      this.canvas = var1;
      this.width = var3;
      this.height = var4;
      this.fullscreen = var5;
      if (var1 != null) {
         try {
            this.robot = new Robot();
            return;
         } catch (AWTException var8) {
            var8.printStackTrace();
         }
      }

   }

   public final void setCurrentScreen(GuiScreen var1) {
      if (!(currentScreen instanceof ErrorScreen)) {
         if (currentScreen != null) {
            currentScreen.onClose();
         }

         if (var1 == null && this.level == null && this.networkManager == null) {
            var1 = new TitleScreen();
         }

         if (var1 == null && this.player.health <= 0) {
            var1 = new GameOverScreen();
         }

         currentScreen = (GuiScreen)var1;
         if (var1 != null) {
            if (this.hasMouse) {
               this.hasMouse = false;
               if (this.levelLoaded) {
                  if (this.level != null) {
                     this.player.releaseAllKeys();
                  }

                  try {
                     Mouse.setNativeCursor((Cursor)null);
                  } catch (LWJGLException var4) {
                     var4.printStackTrace();
                  }
               } else {
                  Mouse.setGrabbed(false);
               }
            }

			   ScaledResolution var2 = new ScaledResolution(this.width, this.height);
			   int var3 = var2.getScaledWidth();
			   int var4 = var2.getScaledHeight();
            ((GuiScreen)var1).open(this, var3, var4);
            this.online = false;
         } else {
            this.grabMouse();
         }
      }

   }

   private static void checkGLError(String var0) {
      int var1;
      if ((var1 = GL11.glGetError()) != 0) {
         String var2 = GLU.gluErrorString(var1);
         System.out.println("########## GL ERROR ##########");
         System.out.println("@ " + var0);
         System.out.println(var1 + ": " + var2);
         System.exit(0);
      }

   }

   public final void shutdown() {
      try {
         if (this.soundPlayer != null && !this.waiting) {
            SoundPlayer var1 = this.soundPlayer;
            this.soundPlayer.running = false;
         }

         if (this.resourceThread != null) {
            ResourceDownloadThread var5 = this.resourceThread;
            this.resourceThread.closeMinecraft();
         }
      } catch (Exception var4) {
      }

      Minecraft var6 = this;
      if (!this.levelLoaded) {
         try {
            LevelIO.save(var6.level, new FileOutputStream(new File("level.dat")));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      Mouse.destroy();
      Keyboard.destroy();
      Display.destroy();
      System.exit(0);
   }

   public final void run() {
      this.running = true;

      try {
         Minecraft var1 = this;
         if (this.canvas != null) {
            Display.setParent(this.canvas);
         } else if (this.fullscreen) {
            Display.setFullscreen(true);
            this.width = Display.getDisplayMode().getWidth();
            this.height = Display.getDisplayMode().getHeight();
         } else {
            Display.setDisplayMode(new DisplayMode(this.width, this.height));
         }

         StringBuilder var10000 = new StringBuilder();
         this.getClass();
         var10000 = var10000.append("Minecraft").append(" ");
         this.getClass();
         Display.setTitle(var10000.append("Enchanted Classic").toString());

         try {
            Display.create();
         } catch (LWJGLException var94) {
            var94.printStackTrace();

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var93) {
            }

            Display.create();
         }

         Keyboard.create();
         Mouse.create();

         try {
            Controllers.create();
         } catch (Exception var92) {
            var92.printStackTrace();
         }

         checkGLError("Pre startup");
         GL11.glEnable(3553);
         GL11.glShadeModel(7425);
         GL11.glClearDepth(1.0D);
         GL11.glEnable(2929);
         GL11.glDepthFunc(515);
         GL11.glEnable(3008);
         GL11.glAlphaFunc(516, 0.0F);
         GL11.glCullFace(1029);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5888);
         checkGLError("Startup");
         String var2 = System.getProperty("user.home", ".");
         String var3 = this.mcdirname;
         String var4;
         switch(OperatingSystemLookup.lookup[((var4 = System.getProperty("os.name").toLowerCase()).contains("win") ? Minecraft$OS.windows : (var4.contains("mac") ? Minecraft$OS.macos : (var4.contains("solaris") ? Minecraft$OS.solaris : (var4.contains("sunos") ? Minecraft$OS.solaris : (var4.contains("linux") ? Minecraft$OS.linux : (var4.contains("unix") ? Minecraft$OS.linux : Minecraft$OS.unknown)))))).ordinal()]) {
         case 1:
         case 2:
            this.minecraftDir = new File(var2, '.' + var3 + '/');
            break;
         case 3:
            String var5;
            if ((var5 = System.getenv("APPDATA")) != null) {
               this.minecraftDir = new File(var5, "." + var3 + '/');
            } else {
               this.minecraftDir = new File(var2, '.' + var3 + '/');
            }
            break;
         case 4:
            this.minecraftDir = new File(var2, "Library/Application Support/" + var3);
            break;
         default:
            this.minecraftDir = new File(var2, var3 + '/');
         }

         if (!this.minecraftDir.exists() && !this.minecraftDir.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + this.minecraftDir);
         }

         File var103 = this.minecraftDir;
         this.settings = new GameSettings(this, this.minecraftDir);
         this.skins = new TexturePackDir(this, this.minecraftDir);
         this.textureManager = new TextureManager(this.skins, this.settings);
         
         if (this.settings.animations) {
         this.textureManager.registerAnimation(new TextureLavaFX());
         this.textureManager.registerAnimation(new TextureWaterFX());
         this.textureManager.registerAnimation(new TextureWaterFlowFX());
         }
         
         if (this.devMode) {
            System.out.println("Developer Mode Enable");
         }

         this.fontRenderer = new FontRenderer(this.settings, "/default.png", this.textureManager);
         this.renderLoadingScreen();
         IntBuffer var6;
         (var6 = BufferUtils.createIntBuffer(256)).clear().limit(256);
         this.levelRenderer = new LevelRenderer(this, this.textureManager);
         Mob.modelCache = new ModelManager();
         GL11.glViewport(0, 0, this.width, this.height);
         Level var7;
         if (this.server != null && this.session != null) {
            (var7 = new Level()).setData(8, 8, 8, new byte[512]);
            this.setLevel(var7);
         } else if (this.level == null) {
            this.setCurrentScreen(new TitleScreen());
         } else if(this.level == null) {
             this.generateLevel(0,0,1,0);
         } else {
            try {
               if (var1.levelName != null) {
                  var1.loadSlotLevel(var1.levelName, var1.levelId);
               }
            } catch (Exception var91) {
               var91.printStackTrace();
            }
         }
  

         this.particleManager = new ParticleManager(this.level, this.textureManager);
         if (this.levelLoaded) {
            try {
               var1.cursor = new Cursor(16, 16, 0, 0, 1, var6, (IntBuffer)null);
            } catch (LWJGLException var90) {
               var90.printStackTrace();
            }
         }

         try {
            var1.soundPlayer = new SoundPlayer(var1.settings);
            SoundPlayer var105 = var1.soundPlayer;

            try {
               AudioFormat var8 = new AudioFormat(44100.0F, 16, 2, true, true);
               var105.dataLine = AudioSystem.getSourceDataLine(var8);
               var105.dataLine.open(var8, 4410);
               var105.dataLine.start();
               var105.running = true;
               Thread var9;
               (var9 = new Thread(var105)).setDaemon(true);
               var9.setPriority(10);
               var9.start();
            } catch (Exception var88) {
               var88.printStackTrace();
               var105.running = false;
            }

            var1.resourceThread = new ResourceDownloadThread(var103, var1);
            var1.resourceThread.start();
         } catch (Exception var89) {
         }

         checkGLError("Post startup");
         this.hud = new HUDScreen(this);
         (new SkinDownloadThread(this)).start();
         if (this.server != null && this.session != null) {
            this.networkManager = new NetworkManager(this, this.server, this.port, this.session.username, this.session.mppass);
         }
      } catch (Exception var99) {
         var99.printStackTrace();
         JOptionPane.showMessageDialog((Component)null, var99.toString(), "Failed to start Minecraft", 0);
         return;
      }

      long var100 = System.currentTimeMillis();
      int var101 = 0;

      try {
         while(this.running) {
               if (this.canvas == null && Display.isCloseRequested()) {
                  this.running = false;
               }

               try {
                  Timer var102 = this.timer;
                  long var104;
                  long var106 = (var104 = System.currentTimeMillis()) - var102.lastSysClock;
                  long var107 = System.nanoTime() / 1000000L;
                  double var11;
                  if (var106 > 1000L) {
                     long var13 = var107 - var102.lastHRClock;
                     var11 = (double)var106 / (double)var13;
                     var102.adjustment += (var11 - var102.adjustment) * 0.20000000298023224D;
                     var102.lastSysClock = var104;
                     var102.lastHRClock = var107;
                  }

                  if (var106 < 0L) {
                     var102.lastSysClock = var104;
                     var102.lastHRClock = var107;
                  }

                  double var108;
                  var11 = ((var108 = (double)var107 / 1000.0D) - var102.lastHR) * var102.adjustment;
                  var102.lastHR = var108;
                  if (var11 < 0.0D) {
                     var11 = 0.0D;
                  }

                  if (var11 > 1.0D) {
                     var11 = 1.0D;
                  }
                  
                  if(!this.waiting) {
                  var102.elapsedDelta = (float)((double)var102.elapsedDelta + var11 * (double)var102.speed * (double)var102.tps);
                  var102.elapsedTicks = (int)var102.elapsedDelta;
                  if (var102.elapsedTicks > 100) {
                     var102.elapsedTicks = 100;
                  }

                  if(!this.waiting) {
                     var102.elapsedDelta -= (float)var102.elapsedTicks;
                     var102.delta = var102.elapsedDelta;
                  }
                  }

                  for(int var15 = 0; var15 < this.timer.elapsedTicks; ++var15) {
                     ++this.ticks;
                     this.tick();
                  }

                  checkGLError("Pre render");
                  GL11.glEnable(3553);
                  if (!this.online) {

                     if(this.level != null) {
                     this.gamemode.applyCracks(this.timer.delta);
                     }

                     float var109 = this.timer.delta;
                     Renderer var16 = this.renderer;
                     if (this.renderer.displayActive && !Display.isActive()) {
                        var16.minecraft.pause();
                     }

                     var16.displayActive = Display.isActive();
                     int var17;
                     int var18;
                     int var19;
                     int var20;
                     if (var16.minecraft.hasMouse) {
                        var20 = 0;
                        var19 = 0;
                        if (var16.minecraft.levelLoaded) {
                           if (var16.minecraft.canvas != null) {
                              Point var21;
                              var18 = (var21 = var16.minecraft.canvas.getLocationOnScreen()).x + var16.minecraft.width / 2;
                              var17 = var21.y + var16.minecraft.height / 2;
                              Point var22;
                              var20 = (var22 = MouseInfo.getPointerInfo().getLocation()).x - var18;
                              var19 = -(var22.y - var17);
                              var16.minecraft.robot.mouseMove(var18, var17);
                           } else {
                              Mouse.setCursorPosition(var16.minecraft.width / 2, var16.minecraft.height / 2);
                           }
                        } else {
                           var20 = Mouse.getDX();
                           var19 = Mouse.getDY();
                        }

                        byte var110 = 1;
                        if (var16.minecraft.settings.invertMouse) {
                           var110 = -1;
                        }

                        var16.minecraft.player.turn((float)var20, (float)(var19 * var110));
                     }
                     
                     if(this.isOnline() && this.networkManager.isConnected() && this.networkManager != null) {
                    	 this.waiting = false;
                     }        

                     if (!var16.minecraft.online) {
                        ScaledResolution var69 = new ScaledResolution(this.width, this.height);
                        var20 = var69.getScaledWidth();
                        var19 = var69.getScaledHeight();
                        int var111 = Mouse.getX() * var20 / var16.minecraft.width;
                        var18 = var19 - Mouse.getY() * var19 / var16.minecraft.height - 1;
                        
                        if (var16.minecraft.level != null) {
                           float var112 = var109;
                           Renderer var23 = var16;
                           Renderer var24 = var16;
                           Player var25;
                           float var26 = (var25 = var16.minecraft.player).xRotO + (var25.xRot - var25.xRotO) * var109;
                           float var27 = var25.yRotO + (var25.yRot - var25.yRotO) * var109;
                           Vec3D var28 = var16.getPlayerVector(var109);
                           float var29 = MathHelper.cos(-var27 * 0.017453292F - 3.1415927F);
                           float var30 = MathHelper.sin(-var27 * 0.017453292F - 3.1415927F);
                           float var31 = MathHelper.cos(-var26 * 0.017453292F);
                           float var32 = MathHelper.sin(-var26 * 0.017453292F);
                           float var33 = var30 * var31;
                           float var34 = var29 * var31;
                           float var35 = var16.minecraft.gamemode.getReachDistance();
                           Vec3D var36 = var28.add(var33 * var35, var32 * var35, var34 * var35);
                           var16.minecraft.selected = var16.minecraft.level.clip(var28, var36);
                           var31 = var35;
                           if (var16.minecraft.selected != null) {
                              var31 = var16.minecraft.selected.vec.distance(var16.getPlayerVector(var109));
                           }

                           var28 = var16.getPlayerVector(var109);
                           if (var16.minecraft.gamemode.isCreative()) {
                              var35 = 32.0F;
                           } else {
                              var35 = var31;
                           }

                           var36 = var28.add(var33 * var35, var32 * var35, var34 * var35);
                           var16.entity = null;
                           List var37 = var16.minecraft.level.entityMap.getEntities(var25, var25.bb.expand(var33 * var35, var32 * var35, var34 * var35));
                           float var38 = 0.0F;

                           for(var20 = 0; var20 < var37.size(); ++var20) {
                              Entity var39;
                              if ((var39 = (Entity)var37.get(var20)).isPickable()) {
                                 var31 = 0.1F;
                                 MovingObjectPosition var40;
                                 if ((var40 = var39.bb.grow(var31, var31, var31).clip(var28, var36)) != null && ((var31 = var28.distance(var40.vec)) < var38 || var38 == 0.0F)) {
                                    var24.entity = var39;
                                    var38 = var31;
                                 }
                              }
                           }

                           if (var24.entity != null && !(var24.minecraft.gamemode.isCreative())) {
                              var24.minecraft.selected = new MovingObjectPosition(var24.entity);
                           }

                           int var113 = 0;

                           while(true) {
                              if (var113 >= 2) {
                                 GL11.glColorMask(true, true, true, false);
                                 break;
                              }

                              if (var23.minecraft.settings.anaglyph) {
                                 if (var113 == 0) {
                                    GL11.glColorMask(false, true, true, false);
                                 } else {
                                    GL11.glColorMask(true, false, false, false);
                                 }
                              }
                              Block var46;
                              Player var114 = var23.minecraft.player;
                              Level var41 = var23.minecraft.level;
                              LevelRenderer var42 = var23.minecraft.levelRenderer;
                              ParticleManager var43 = var23.minecraft.particleManager;
                              GL11.glViewport(0, 0, var23.minecraft.width, var23.minecraft.height);
                              Level var44 = var23.minecraft.level;
                              var25 = var23.minecraft.player;
                              var26 = 1.0F / (float)(4 - var23.minecraft.settings.viewDistance);
                              var26 = 1.0F - (float)Math.pow((double)var26, 0.25D);
                              var27 = (float)(var44.skyColor >> 16 & 255) / 255.0F;
                              float var45 = (float)(var44.skyColor >> 8 & 255) / 255.0F;
                              var29 = (float)(var44.skyColor & 255) / 255.0F;
                              var23.fogRed = (float)(var44.fogColor >> 16 & 255) / 255.0F;
                              var23.fogBlue = (float)(var44.fogColor >> 8 & 255) / 255.0F;
                              var23.fogGreen = (float)(var44.fogColor & 255) / 255.0F;
                              var23.fogRed += (var27 - var23.fogRed) * var26;
                              var23.fogBlue += (var45 - var23.fogBlue) * var26;
                              var23.fogGreen += (var29 - var23.fogGreen) * var26;
                              var23.fogRed *= var23.fogColorMultiplier;
                              var23.fogBlue *= var23.fogColorMultiplier;     
                              if ((var46 = Block.blocks[var44.getTile((int)var25.x, (int)(var25.y + 0.12F), (int)var25.z)]) != null && var46.getLiquidType() != LiquidType.NOT_LIQUID) {
                                 LiquidType var47;
                                 if ((var47 = var46.getLiquidType()) == LiquidType.WATER) {
                                    var23.fogRed = 0.02F;
                                    var23.fogBlue = 0.02F;
                                    var23.fogGreen = 0.2F;
                                 } else if (var47 == LiquidType.LAVA) {
                                    var23.fogRed = 0.6F;
                                    var23.fogBlue = 0.1F;
                                    var23.fogGreen = 0.0F;
                                 }
                              }

                              if (var23.minecraft.settings.anaglyph) {
                                 var31 = (var23.fogRed * 30.0F + var23.fogBlue * 59.0F + var23.fogGreen * 11.0F) / 100.0F;
                                 var32 = (var23.fogRed * 30.0F + var23.fogBlue * 70.0F) / 100.0F;
                                 var33 = (var23.fogRed * 30.0F + var23.fogGreen * 70.0F) / 100.0F;
                                 var23.fogRed = var31;
                                 var23.fogBlue = var32;
                                 var23.fogGreen = var33;
                              }

                              GL11.glClearColor(var23.fogRed, var23.fogBlue, var23.fogGreen, 0.0F);
                              GL11.glClear(16640);
                              var23.fogColorMultiplier = 1.0F;
                              GL11.glEnable(2884);
                              var23.fogEnd = (float)(512 >> (var23.minecraft.settings.viewDistance << 1));
                              GL11.glMatrixMode(5889);
                              GL11.glLoadIdentity();
                              var26 = 0.07F;
                              
                              if (var23.minecraft.settings.anaglyph) {
                                 GL11.glTranslatef((float)(-((var113 << 1) - 1)) * var26, 0.0F, 0.0F);
                              }

                              Player var115 = var23.minecraft.player;
                              var30 = 70.0F;
                              if (var115.health <= 0) {
                                 var31 = (float)var115.deathTime + var112;
                                 var30 /= (1.0F - 500.0F / (var31 + 500.0F)) * 2.0F + 1.0F;
                              }

                              GLU.gluPerspective(var30, (float)var23.minecraft.width / (float)var23.minecraft.height, 0.05F, var23.fogEnd);
                              GL11.glMatrixMode(5888);
                              GL11.glLoadIdentity();
                              if (var23.minecraft.settings.anaglyph) {
                                 GL11.glTranslatef((float)((var113 << 1) - 1) * 0.1F, 0.0F, 0.0F);
                              }

                              var23.hurtEffect(var112);
                              if (var23.minecraft.settings.viewBobbing) {
                                 var23.applyBobbing(var112);
                              }

                              var115 = var23.minecraft.player;
                              GL11.glTranslatef(0.0F, 0.0F, -0.1F);
                              GL11.glRotatef(var115.xRotO + (var115.xRot - var115.xRotO) * var112, 1.0F, 0.0F, 0.0F);
                              GL11.glRotatef(var115.yRotO + (var115.yRot - var115.yRotO) * var112, 0.0F, 1.0F, 0.0F);
                              var30 = var115.xo + (var115.x - var115.xo) * var112;
                              var31 = var115.yo + (var115.y - var115.yo) * var112;
                              var32 = var115.zo + (var115.z - var115.zo) * var112;
                              GL11.glTranslatef(-var30, -var31, -var32);
                              Frustrum var48 = FrustrumImpl.update();
                              Frustrum var49 = var48;
                              LevelRenderer var50 = var23.minecraft.levelRenderer;

                              int var51;
                              for(var51 = 0; var51 < var50.chunkCache.length; ++var51) {
                                 var50.chunkCache[var51].clip(var49);
                              }

                              var50 = var23.minecraft.levelRenderer;
                              Collections.sort(var23.minecraft.levelRenderer.chunks, new ChunkDirtyDistanceComparator(var114));
                              var51 = var50.chunks.size() - 1;
                              int var52;
                              if ((var52 = var50.chunks.size()) > 3) {
                                 var52 = 3;
                              }

                              int var53;
                              for(var53 = 0; var53 < var52; ++var53) {
                                 Chunk var54;
                                 (var54 = (Chunk)var50.chunks.remove(var51 - var53)).update();
                                 var54.loaded = false;
                              }

                              var23.updateFog();
                              GL11.glEnable(2912);
                              var42.sortChunks(var114, 0);
                              ShapeRenderer var55;
                              int var56;
                              int var57;
                              int var59;
                              int var60;
                              int var116;
                              if (var41.isSolid(var114.x, var114.y, var114.z, 0.1F)) {
                                 var59 = (int)var114.x;
                                 var60 = (int)var114.y;
                                 var116 = (int)var114.z;

                                 for(int var58 = var59 - 1; var58 <= var59 + 1; ++var58) {
                                    for(var57 = var60 - 1; var57 <= var60 + 1; ++var57) {
                                       for(int var61 = var116 - 1; var61 <= var116 + 1; ++var61) {
                                          var52 = var61;
                                          var51 = var57;
                                          int var62 = var58;
                                          if ((var53 = var42.level.getTile(var58, var57, var61)) != 0 && Block.blocks[var53].isSolid()) {
                                             GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
                                             GL11.glDepthFunc(513);
                                             var55 = ShapeRenderer.instance;
                                             ShapeRenderer.instance.begin();

                                             for(var56 = 0; var56 < 6; ++var56) {
                                                Block.blocks[var53].renderInside(var55, var62, var51, var52, var56);
                                             }

                                             var55.end();
                                             GL11.glCullFace(1028);
                                             var55.begin();

                                             for(var56 = 0; var56 < 6; ++var56) {
                                                Block.blocks[var53].renderInside(var55, var62, var51, var52, var56);
                                             }

                                             var55.end();
                                             GL11.glCullFace(1029);
                                             GL11.glDepthFunc(515);
                                          }
                                       }
                                    }
                                 }
                              }

                              var23.setLighting(true);
                              Vec3D var117 = var23.getPlayerVector(var112);
                              var42.level.entityMap.render(var117, var48, var42.textureManager, var112);
                              var23.setLighting(false);
                              var23.updateFog();
                              float var118 = var112;
                              ParticleManager var63 = var43;
                              var26 = -MathHelper.cos(var114.yRot * 3.1415927F / 180.0F);
                              var45 = -(var27 = -MathHelper.sin(var114.yRot * 3.1415927F / 180.0F)) * MathHelper.sin(var114.xRot * 3.1415927F / 180.0F);
                              var29 = var26 * MathHelper.sin(var114.xRot * 3.1415927F / 180.0F);
                              var30 = MathHelper.cos(var114.xRot * 3.1415927F / 180.0F);

                              for(var60 = 0; var60 < 2; ++var60) {
                                 if (var63.particles[var60].size() != 0) {
                                    var116 = 0;
                                    if (var60 == 0) {
                                       var116 = var63.textureManager.load("/particles.png");
                                    }

                                    if (var60 == 1) {
                                       var116 = var63.textureManager.load("/terrain.png");
                                    }

                                    if (var60 == 2) {
                                       var116 = var63.textureManager.load("/items.png");
                                    }

                                    GL11.glBindTexture(3553, var116);
                                    ShapeRenderer var64 = ShapeRenderer.instance;
                                    ShapeRenderer.instance.begin();

                                    for(var59 = 0; var59 < var63.particles[var60].size(); ++var59) {
                                       ((Particle)var63.particles[var60].get(var59)).render(var64, var118, var26, var30, var27, var45, var29);
                                    }

                                    var64.end();
                                 }
                              }

                              GL11.glBindTexture(3553, var42.textureManager.load("/rock.png"));
                              GL11.glEnable(3553);
                              GL11.glCallList(var42.listId);
                              var23.updateFog();
                              var50 = var42;
                              var55 = ShapeRenderer.instance;
                              GL11.glDisable(3553);
                              var55.begin();
                              var33 = (float)(var42.level.skyColor >> 16 & 255) / 255.0F;
                              var38 = (float)(var42.level.skyColor >> 8 & 255) / 255.0F;
                              var34 = (float)(var42.level.skyColor & 255) / 255.0F;
                              if (var42.minecraft.settings.anaglyph) {
                                 var35 = (var33 * 30.0F + var38 * 59.0F + var34 * 11.0F) / 100.0F;
                                 var30 = (var33 * 30.0F + var38 * 70.0F) / 100.0F;
                                 var31 = (var33 * 30.0F + var34 * 70.0F) / 100.0F;
                                 var33 = var35;
                                 var38 = var30;
                                 var34 = var31;
                              }

                              var55.color(var33, var38, var34);
                              var31 = (float)(var42.level.depth + 10);

                              for(var57 = -2048; var57 < var50.level.width + 2048; var57 += 512) {
                                 for(var17 = -2048; var17 < var50.level.height + 2048; var17 += 512) {
                                    var55.vertex((float)var57, var31, (float)var17);
                                    var55.vertex((float)(var57 + 512), var31, (float)var17);
                                    var55.vertex((float)(var57 + 512), var31, (float)(var17 + 512));
                                    var55.vertex((float)var57, var31, (float)(var17 + 512));
                                 }
                              }

                              var55.end();
                              GL11.glEnable(3553);
                              var23.updateFog();
                              
                              ItemStack var9 = this.player.inventory.getSelected();
                              
                              if (var23.minecraft.selected != null) {
                                 GL11.glDisable(3008);
                                 MovingObjectPosition var65 = var23.minecraft.selected;
                                 boolean var66 = false;
                                 MovingObjectPosition var67 = var65;
                                 var50 = var42;
                                 ShapeRenderer var68 = ShapeRenderer.instance;
                                 GL11.glEnable(3042);
                                 GL11.glEnable(3008);
                                 GL11.glBlendFunc(770, 1);
                                 GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
                                 if (var42.cracks > 0.0F) {
                                    GL11.glBlendFunc(774, 768);
                                    int var119 = var42.textureManager.load("/terrain.png");
                                    GL11.glBindTexture(3553, var119);
                                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                                    GL11.glPushMatrix();
                                    Block var691 = (var56 = var42.level.getTile(var65.x, var65.y, var65.z)) > 0 ? Block.blocks[var56] : null;
                                    var46 = var691;
                                    var31 = (var691.x1 + var691.x2) / 2.0F;
                                    var32 = (var691.y1 + var691.y2) / 2.0F;
                                    var33 = (var691.z1 + var691.z2) / 2.0F;
                                    GL11.glTranslatef((float)var65.x + var31, (float)var65.y + var32, (float)var65.z + var33);
                                    var38 = 1.01F;
                                    GL11.glScalef(1.01F, var38, var38);
                                    GL11.glTranslatef(-((float)var65.x + var31), -((float)var65.y + var32), -((float)var65.z + var33));
                                    var68.begin();
                                    var68.noColor();
                                    GL11.glDepthMask(false);
                                    if (var691 == null) {
                                       var46 = Block.STONE;
                                    }

                                    for(var19 = 0; var19 < 6; ++var19) {
                                       var46.renderSide(var68, var67.x, var67.y, var67.z, var19, 240 + (int)(var50.cracks * 10.0F));
                                    }

                                    var68.end();
                                    GL11.glDepthMask(true);
                                    GL11.glPopMatrix();
                                 } else if(this.settings.handleClick) {              
                                	this.levelRenderer.renderHit(this.player, var67, this.editMode ? 1 : 0, var9);
                                    this.levelRenderer.renderHitOutline(var67, this.editMode ? 1 : 0);  
                                 }

                                 GL11.glDisable(3042);
                                 GL11.glDisable(3008);
                                 var65 = var23.minecraft.selected;
                                 var114.inventory.getSelected();
                                 var66 = false;
                                 GL11.glEnable(3042);
                                 GL11.glBlendFunc(770, 771);
                                 GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                                 GL11.glLineWidth(2.0F);
                                 GL11.glDisable(3553);
                                 GL11.glDepthMask(false);
                                 var26 = 0.002F;
                                 if (!this.editMode && (var53 = var42.level.getTile(var65.x, var65.y, var65.z)) > 0) {
                                    AABB var120 = Block.blocks[var53].getSelectionBox(var65.x, var65.y, var65.z).grow(var26, var26, var26);
                                    GL11.glBegin(3);
                                    GL11.glVertex3f(var120.x0, var120.y0, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y0, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y0, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y0, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y0, var120.z0);
                                    GL11.glEnd();
                                    GL11.glBegin(3);
                                    GL11.glVertex3f(var120.x0, var120.y1, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y1, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y1, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y1, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y1, var120.z0);
                                    GL11.glEnd();
                                    GL11.glBegin(1);
                                    GL11.glVertex3f(var120.x0, var120.y0, var120.z0);
                                    GL11.glVertex3f(var120.x0, var120.y1, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y0, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y1, var120.z0);
                                    GL11.glVertex3f(var120.x1, var120.y0, var120.z1);
                                    GL11.glVertex3f(var120.x1, var120.y1, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y0, var120.z1);
                                    GL11.glVertex3f(var120.x0, var120.y1, var120.z1);
                                    GL11.glEnd();
                                 }

                                 GL11.glDepthMask(true);
                                 GL11.glEnable(3553);
                                 GL11.glDisable(3042);
                                 GL11.glEnable(3008);
                              }

                              GL11.glBlendFunc(770, 771);
                              var23.updateFog();
                              GL11.glEnable(3553);
                              GL11.glEnable(3042);
                              if (this.level.levelType != 1 && this.level.levelType != 2) {
                                 GL11.glBindTexture(3553, var42.textureManager.load("/water.png"));
                              } else if (this.level.levelType == 2) {
                                 GL11.glBindTexture(3553, var42.textureManager.load("/ice.png"));
                              } else if (this.level.levelType == 1) {
                                  GL11.glBindTexture(3553, var42.textureManager.load("/lava.png"));
                              }

                              GL11.glCallList(var42.listId + 1);
                              GL11.glDisable(3042);
                              GL11.glEnable(3042);
                              GL11.glColorMask(false, false, false, false);
                              var59 = var42.sortChunks(var114, 1);
                              GL11.glColorMask(true, true, true, true);
                              if (var23.minecraft.settings.anaglyph) {
                                 if (var113 == 0) {
                                    GL11.glColorMask(false, true, true, false);
                                 } else {
                                    GL11.glColorMask(true, false, false, false);
                                 }
                              }

                              if (var59 > 0) {
                                 GL11.glBindTexture(3553, var42.textureManager.load("/terrain.png"));
                                 GL11.glCallLists(var42.buffer);
                              }           

                              if (this.raining || this.snowing || this.level.levelType == 2) {
                                 this.level.weather = new Weather(this, this.textureManager, this.level);
                              }

                              if (this.settings.fancyGraphics && this.settings.viewClouds) {
                                 if (this.settings.fancyGraphics) {
                                    this.levelRenderer.renderVolumetricClouds();
                                 }
                              } else if(this.settings.viewClouds){
                                 GL11.glBindTexture(3553, this.textureManager.load("/environment/clouds.png"));
                                 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                                 var118 = (float)(this.level.cloudColor >> 16 & 255) / 255.0F;
                                 var26 = (float)(this.level.cloudColor >> 8 & 255) / 255.0F;
                                 var27 = (float)(this.level.cloudColor & 255) / 255.0F;
                                 if (this.settings.anaglyph) {
                                    var45 = (var118 * 30.0F + var26 * 59.0F + var27 * 11.0F) / 100.0F;
                                    var29 = (var118 * 30.0F + var26 * 70.0F) / 100.0F;
                                    var30 = (var118 * 30.0F + var27 * 70.0F) / 100.0F;
                                    var118 = var45;
                                    var26 = var29;
                                    var27 = var30;
                                 }

                                 var55 = ShapeRenderer.instance;
                                 var31 = 0.0F;
                                 var32 = 4.8828125E-4F;
                                 var31 = (float)this.level.cloudHeight;
                                 
                                 var33 = ((float)this.levelRenderer.ticks + var118) * var32 * 0.03F;

                                 var55.begin();
                                 var55.color(var118, var26, var27);
                                 var19 = -2048;

                                 while(true) {
                                    if (var19 >= this.level.width + 2048) {
                                       var55.end();
                                       break;
                                    }

                                    for(var57 = -2048; var57 < this.level.depth + 2048; var57 += 512) {
                                       var55.vertexUV((float)var19, var31, (float)(var57 + 512), (float)var19 * var32 + var33, (float)(var57 + 512) * var32);
                                       var55.vertexUV((float)(var19 + 512), var31, (float)(var57 + 512), (float)(var19 + 512) * var32 + var33, (float)(var57 + 512) * var32);
                                       var55.vertexUV((float)(var19 + 512), var31, (float)var57, (float)(var19 + 512) * var32 + var33, (float)var57 * var32);
                                       var55.vertexUV((float)var19, var31, (float)var57, (float)var19 * var32 + var33, (float)var57 * var32);
                                       var55.vertexUV((float)var19, var31, (float)var57, (float)var19 * var32 + var33, (float)var57 * var32);
                                       var55.vertexUV((float)(var19 + 512), var31, (float)var57, (float)(var19 + 512) * var32 + var33, (float)var57 * var32);
                                       var55.vertexUV((float)(var19 + 512), var31, (float)(var57 + 512), (float)(var19 + 512) * var32 + var33, (float)(var57 + 512) * var32);
                                       var55.vertexUV((float)var19, var31, (float)(var57 + 512), (float)var19 * var32 + var33, (float)(var57 + 512) * var32);
                                    }

                                    var19 += 512;
                                 }
                              }                         

                              GL11.glDepthMask(true);
                              GL11.glDisable(3042);
                              GL11.glDisable(2912);
                              if (var23.entity != null) {
                                 var23.entity.renderHover(var23.minecraft.textureManager, var112);
                              }

                              GL11.glClear(256);
                              GL11.glLoadIdentity();
                              if (var23.minecraft.settings.anaglyph) {
                                 GL11.glTranslatef((float)((var113 << 1) - 1) * 0.1F, 0.0F, 0.0F);
                              }

                              var23.hurtEffect(var112);
                              if (var23.minecraft.settings.viewBobbing) {
                                 var23.applyBobbing(var112);
                              }

                              ItemRenderer var70 = var23.itemRenderer;
                              var45 = var23.itemRenderer.lastPos + (var70.pos - var70.lastPos) * var112;
                              var115 = var70.minecraft.player;
                              GL11.glPushMatrix();
                              GL11.glRotatef(var115.xRotO + (var115.xRot - var115.xRotO) * var112, 1.0F, 0.0F, 0.0F);
                              GL11.glRotatef(var115.yRotO + (var115.yRot - var115.yRotO) * var112, 0.0F, 1.0F, 0.0F);
                              var70.minecraft.renderer.setLighting(true);
                              GL11.glPopMatrix();
                              GL11.glPushMatrix();
                              var30 = 0.8F;
                              if (var70.moving) {
                                 var32 = MathHelper.sin((var31 = ((float)var70.offset + var112) / 7.0F) * 3.1415927F);
                                 GL11.glTranslatef(-MathHelper.sin(MathHelper.sqrt(var31) * 3.1415927F) * 0.4F, MathHelper.sin(MathHelper.sqrt(var31) * 3.1415927F * 2.0F) * 0.2F, -var32 * 0.2F);
                              }

                              GL11.glTranslatef(0.7F * var30, -0.65F * var30 - (1.0F - var45) * 0.6F, -0.9F * var30);
                              GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                              GL11.glEnable(2977);
                              if (var70.moving) {
                                 var32 = MathHelper.sin((var31 = ((float)var70.offset + var112) / 7.0F) * var31 * 3.1415927F);
                                 GL11.glRotatef(MathHelper.sin(MathHelper.sqrt(var31) * 3.1415927F) * 80.0F, 0.0F, 1.0F, 0.0F);
                                 GL11.glRotatef(-var32 * 20.0F, 1.0F, 0.0F, 0.0F);
                              }

                              GL11.glColor4f(var31 = var70.minecraft.level.getBrightness((int)var115.x, (int)var115.y, (int)var115.z), var31, var31, 1.0F);
                              ShapeRenderer var71 = ShapeRenderer.instance;
                              var33 = 0.4F;
                              if (!this.hidegui && var70.itemToRenderer != null && var70.itemToRenderer.itemID < 256) {
                                 GL11.glScalef(0.4F, var33, var33);
                                 GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                                 GL11.glBindTexture(3553, var70.minecraft.textureManager.load("/terrain.png"));
                                 Block var122 = Block.blocks[var70.itemToRenderer.itemID];
                                 var122.renderPreview(var71);
                              } else if (!this.hidegui && var70.itemToRenderer != null && var70.itemToRenderer.itemID > 256) {
                                 ItemGuiRenderer.enableStandardItemLighting();
                                 GL11.glScalef(var33, var33, var33);
                                 GL11.glBindTexture(3553, var42.textureManager.load("/items.png"));
                                 GL11.glDisable(2896);
                                 ItemStack var121 = var70.itemToRenderer;
                                 var109 = (float)(var121.getItem().getIconIndex() % 16 << 4) / 256.0F;
                                 var121 = var70.itemToRenderer;
                                 var45 = (float)((var121.getItem().getIconIndex() % 16 << 4) + 16) / 256.0F;
                                 var121 = var70.itemToRenderer;
                                 float var73 = (float)(var121.getItem().getIconIndex() / 16 << 4) / 256.0F;
                                 var121 = var70.itemToRenderer;
                                 float var74 = (float)((var121.getItem().getIconIndex() / 16 << 4) + 16) / 256.0F;
                                 var71.begin();
                                 var71.vertexUV(-0.4F, -0.2F, -0.4F, var109, var74);
                                 var71.vertexUV(0.29999998F, -0.2F, 0.29999998F, var45, var74);
                                 var71.vertexUV(0.29999998F, 0.8F, 0.29999998F, var45, var73);
                                 var71.vertexUV(-0.4F, 0.8F, -0.4F, var109, var73);
                                 var71.end();
                                 GL11.glEnable(2896);
                              } else if (!this.hidegui) {
                                 var115.bindTexture(var70.minecraft.textureManager);
                                 GL11.glScalef(1.0F, -1.0F, -1.0F);
                                 GL11.glTranslatef(0.0F, 0.2F, 0.0F);
                                 GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
                                 GL11.glScalef(1.0F, 1.0F, 1.0F);
                                 var33 = 0.0625F;
                                 ModelPart var72;
                                 if (!(var72 = var70.minecraft.player.getModel().leftArm).hasList) {
                                    var72.generateList(var33);
                                 }

                                 GL11.glCallList(var72.list);
                              }

                              GL11.glDisable(2977);
                              GL11.glPopMatrix();
                              var70.minecraft.renderer.setLighting(false);
                              if (!var23.minecraft.settings.anaglyph) {
                                 break;
                              }

                              ++var113;
                           }

                           Minecraft var10002 = var16.minecraft;
                           var16.enableGuiMode();

                           if(!this.hidegui) {              
                              var16.minecraft.hud.render(var109, currentScreen != null, var111, var18);
                           }

                           if(this.currentScreen != null && this.hidegui) {
    	                        this.hidegui = !this.hidegui;
                           }
                           
                        } else {
                           GL11.glViewport(0, 0, var16.minecraft.width, var16.minecraft.height);
                           GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                           GL11.glClear(16640);
                           GL11.glMatrixMode(5889);
                           GL11.glLoadIdentity();
                           GL11.glMatrixMode(5888);
                           GL11.glLoadIdentity();     
                           var16.enableGuiMode();
                        }

                        Minecraft var123 = var16.minecraft;
                        if (currentScreen != null) {
                           var123 = var16.minecraft;
                           currentScreen.render(var111, var18);
                        }                    

                        if (!Display.isActive()) {
                            if(this.fullscreen) {
                               this.toggleFullScreen();
                            }

                            Thread.sleep(10L);
                         }

                         if (this.canvas != null && !this.fullscreen && (this.canvas.getWidth() != this.width || this.canvas.getHeight() != this.height)) {
                            this.width = this.canvas.getWidth();
                            this.height = this.canvas.getHeight();
                            this.resize(this.width, this.height);
                         }

                        Thread.yield();
                        Display.update();
                     }
                  }

                  if (this.settings.limitFramerate) {
                     Thread.sleep(5L);
                  }

                  checkGLError("Post render");
                  ++var101;
                  this.waiting = this.currentScreen != null && this.currentScreen.doesGuiPause();
               } catch (Exception var95) {
                  this.setCurrentScreen(new ErrorScreen("Client error", "The game broke! [" + var95 + "]"));
                  var95.printStackTrace();
               }

               while(System.currentTimeMillis() >= var100 + 1000L) {
                  this.debug = var101 + " fps, " + Chunk.chunkUpdates + " chunk updates";
                  Chunk.chunkUpdates = 0;
                  var100 += 1000L;
                  var101 = 0;
               }
           }

         return;
      } catch (StopGameException var96) {
         return;
      } catch (Exception var97) {
         var97.printStackTrace();
      } finally {
         this.shutdown();
      }

   }

   public final void grabMouse() {
      if (!this.hasMouse) {
         this.hasMouse = true;
         if (this.levelLoaded) {
            try {
               Mouse.setNativeCursor(this.cursor);
               Mouse.setCursorPosition(this.width / 2, this.height / 2);
            } catch (LWJGLException var2) {
               var2.printStackTrace();
            }

            if (this.canvas == null) {
               this.canvas.requestFocus();
            }
         } else {
            Mouse.setGrabbed(true);
         }

         this.setCurrentScreen((GuiScreen)null);
         this.lastClick = this.ticks + 10000;
      }

   }

   public final void pause() {
      if (currentScreen == null) {
         this.setCurrentScreen(new PauseScreen());
      }

   }

   private void onMouseClick(int var1) {
      if (var1 != 0 || this.blockHitTime <= 0) {
         if (var1 == 0) {
            ItemRenderer var3 = this.renderer.itemRenderer;
            this.renderer.itemRenderer.offset = -1;
            var3.moving = true;
         }

         ItemRenderer var2;
         ItemStack var5;
         if (var1 == 1 && (var5 = this.player.inventory.getSelected()) != null && var5.getItem().useItem(var5, this.player) | var5.getItem().onItemRightClick(var5, this.player, this.level)) {
            if (var5.stackSize == 0) {
               this.player.inventory.Inventory[this.player.inventory.selected] = null;
            }

            var2 = this.renderer.itemRenderer;
            this.renderer.itemRenderer.pos = 0.0F;
         } else if (this.selected == null) {
            if (var1 == 0 && !(this.gamemode.isCreative())) {
               this.blockHitTime = 10;
            }
         } else if (this.selected.entityPos == 1) {
            if (var1 == 0) {
               this.selected.entity.hurt(this.player, 4);
               Entity var13 = this.selected.entity;
               Player var14 = this.player;
               Inventory var15 = var14.inventory;
               var5 = var15.getStackInSlot(var15.selected);
               int var16 = var5 != null ? Items.itemsList[var5.itemID].getAttackDamage() : 1;
               if (var16 > 0) {
                  var13.hurt(var14, var16);
                  var5 = var14.inventory.getSelected();
                  if (var5 != null && var13 instanceof Entity) {
                     Items.itemsList[var5.itemID].hitEntity(var5);
                  }
               }

               return;
            }
         } else if (this.selected.entityPos == 0) {
            int var12 = this.selected.x;
            int var6 = this.selected.y;
            int var7 = this.selected.z;
            int var18 = this.selected.face;

            Block var8 = Block.blocks[this.level.getTile(var12, var6, var7)];
            if (var1 == 0) {
               if (var8 != Block.BEDROCK || this.player.userType >= 100) {
                  this.gamemode.hitBlock(var12, var6, var7);
                  return;
               }

               if (this.gamemode.isCreative() && var8 == Block.BEDROCK && this.networkManager == null) {
                  this.gamemode.hitBlock(var12, var6, var7);
                  return;
               }
            } else {
               ItemStack var9 = this.player.inventory.getSelected();

               int var128 = this.level.getTile(var12, var6, var7);
			   if(var128 > 0 && Block.blocks[var128].blockActivated(this.level, var12, var6, var7, this.player)) {
				  return;
			   }

			   if(var9 != null && var128 > 0 && Block.blocks[var128].specialBlockActivated(var9, this.level, var12, var6, var7, this.player)) {
				  return;
			   }
				
               if (var9 == null) {
                  return;
               }
               
               if (var9.stackSize == 0) {
                  var2 = this.renderer.itemRenderer;
                  this.renderer.itemRenderer.pos = 0.0F;
               }

                  int var13 = var9.stackSize;
				  int var14 = var7;
				  var9.getItem().onPlaced(var9, this.level, var12, var6, var14, var18);
                  		            
				  if(this.gamemode.isSurvival() && var9.stackSize == 0) {
					 this.player.inventory.Inventory[this.player.inventory.selected] = null;
					 return;
				  }

            }
         }
      }

   }

   private void tick() {
      if (!this.waiting && this.soundPlayer != null) {
         SoundPlayer var1 = this.soundPlayer;
         SoundManager var2 = this.sound;
         if (System.currentTimeMillis() > var2.lastMusic && var2.playMusic(var1, "calm")) {
            var2.lastMusic = System.currentTimeMillis() + (long)var2.random.nextInt(900000) + 300000L;
         }
      }

      if (!this.waiting && this.level != null && this.gamemode.isSurvival()) {
         this.gamemode.spawnMob();
      }

      HUDScreen var32 = this.hud;
      if(!this.waiting) {
         ++this.hud.ticks;
      }

      int var33;
      for(var33 = 0; var33 < var32.chat.size(); ++var33) {
    	 if(!this.waiting) {
         ++((ChatLine)var32.chat.get(var33)).time;
    	 }
      }

      GL11.glBindTexture(3553, this.textureManager.load("/terrain.png"));
      TextureManager var3 = this.textureManager;

      if(!this.waiting) {
      for(var33 = 0; var33 < var3.animations.size(); ++var33) {
         TextureFX var4;
         (var4 = (TextureFX)var3.animations.get(var33)).anaglyph = var3.settings.anaglyph;
         var4.animate();
         var3.textureBuffer.clear();
         var3.textureBuffer.put(var4.textureData);
         var3.textureBuffer.position(0).limit(var4.textureData.length);
         GL11.glTexSubImage2D(3553, 0, var4.textureId % 16 << 4, var4.textureId / 16 << 4, 16, 16, 6408, 5121, var3.textureBuffer);
      }
      }

      int var5;
      int var6;
      int var7;
      int var8;
      int var34;
      if (this.networkManager != null && !(currentScreen instanceof ErrorScreen)) {
         if (!this.networkManager.isConnected()) {
            this.progressBar.setTitle("Connecting..");
            this.progressBar.setProgress(0);
         } else {
            NetworkManager var9 = this.networkManager;
            if (this.networkManager.successful) {
               NetworkHandler var10 = var9.netHandler;
               if (var9.netHandler.connected) {
                  try {
                     NetworkHandler var11 = var9.netHandler;
                     var9.netHandler.channel.read(var11.in);
                     var8 = 0;

                     while(var11.in.position() > 0 && var8++ != 100) {
                        var11.in.flip();
                        byte var12 = var11.in.get(0);
                        PacketType var13;
                        if ((var13 = PacketType.packets[var12]) == null) {
                           throw new IOException("Bad command: " + var12);
                        }

                        if (var11.in.remaining() < var13.length + 1) {
                           var11.in.compact();
                           break;
                        }

                        var11.in.get();
                        Object[] var14 = new Object[var13.params.length];

                        for(var34 = 0; var34 < var14.length; ++var34) {
                           var14[var34] = var11.readObject(var13.params[var34]);
                        }

                        NetworkManager var15 = var11.netManager;
                        if (var11.netManager.successful) {
                           if (var13 == PacketType.IDENTIFICATION) {
                              var15.minecraft.progressBar.setTitle(var14[1].toString());
                              var15.minecraft.progressBar.setText(var14[2].toString());
                              var15.minecraft.player.userType = (Byte)var14[3];
                           } else if (var13 == PacketType.LEVEL_INIT) {
                              var15.minecraft.setLevel((Level)null);
                              var15.levelData = new ByteArrayOutputStream();
                           } else {
                              short var16;
                              byte[] var17;
                              if (var13 == PacketType.LEVEL_DATA) {
                                 var16 = (Short)var14[0];
                                 var17 = (byte[])((byte[])((byte[])((byte[])var14[1])));
                                 byte var18 = (Byte)var14[2];
                                 var15.minecraft.progressBar.setProgress(var18);
                                 var15.levelData.write(var17, 0, var16);
                              } else {
                                 short var53;
                                 if (var13 == PacketType.LEVEL_FINALIZE) {
                                    try {
                                       var15.levelData.close();
                                    } catch (IOException var30) {
                                       var30.printStackTrace();
                                    }

                                    var17 = LevelIO.decompress(new ByteArrayInputStream(var15.levelData.toByteArray()));
                                    var15.levelData = null;
                                    var53 = (Short)var14[0];
                                    short var19 = (Short)var14[1];
                                    var16 = (Short)var14[2];
                                    Level var20;
                                    (var20 = new Level()).setNetworkMode(true);
                                    var20.setData(var53, var19, var16, var17);
                                    var15.minecraft.setLevel(var20);
                                    var15.minecraft.online = false;
                                    var15.levelLoaded = true;
                                 } else if (var13 == PacketType.BLOCK_CHANGE) {
                                    if (var15.minecraft.level != null) {
                                       var15.minecraft.level.netSetTile((Short)var14[0], (Short)var14[1], (Short)var14[2], (Byte)var14[3]);
                                    }
                                 } else {
                                    byte var21;
                                    NetworkPlayer var22;
                                    short var23;
                                    short var26;
                                    byte var52;
                                    byte var55;
                                    byte var58;
                                    if (var13 == PacketType.SPAWN_PLAYER) {
                                       var52 = ((Byte)var14[0]).byteValue();
                                       String var24 = (String)var14[1];
                                       var53 = ((Short)var14[2]).shortValue();
                                       var23 = ((Short)var14[3]).shortValue();
                                       short var25 = ((Short)var14[4]).shortValue();
                                       var55 = ((Byte)var14[5]).byteValue();
                                       var58 = ((Byte)var14[6]).byteValue();
                                       if (var52 >= 0) {
                                          var21 = (byte)(var55 + 128);
                                          var26 = (short)(var23 - 22);
                                          var22 = new NetworkPlayer(var15.minecraft, var52, var24, var53, var26, var25, (float)(var21 * 360) / 256.0F, (float)(var58 * 360) / 256.0F);
                                          var15.players.put(var52, var22);
                                          var15.minecraft.level.addEntity(var22);
                                       } else {
                                          var15.minecraft.level.setSpawnPos(var53 / 32, var23 / 32, var25 / 32, (float)(var55 * 320 / 256));
                                          var15.minecraft.player.moveTo((float)var53 / 32.0F, (float)var23 / 32.0F, (float)var25 / 32.0F, (float)(var55 * 360) / 256.0F, (float)(var58 * 360) / 256.0F);
                                       }
                                    } else {
                                       byte var61;
                                       NetworkPlayer var62;
                                       if (var13 == PacketType.POSITION_ROTATION) {
                                          var52 = ((Byte)var14[0]).byteValue();
                                          var26 = ((Short)var14[1]).shortValue();
                                          var53 = ((Short)var14[2]).shortValue();
                                          var23 = ((Short)var14[3]).shortValue();
                                          var55 = ((Byte)var14[4]).byteValue();
                                          var21 = ((Byte)var14[5]).byteValue();
                                          if (var52 < 0) {
                                             var15.minecraft.player.moveTo((float)var26 / 32.0F, (float)var53 / 32.0F, (float)var23 / 32.0F, (float)(var55 * 360) / 256.0F, (float)(var21 * 360) / 256.0F);
                                          } else {
                                             var61 = (byte)(var55 + 128);
                                             var16 = (short)(var53 - 22);
                                             if ((var62 = (NetworkPlayer)var15.players.get(var52)) != null) {
                                                var62.teleport(var26, var16, var23, (float)(var61 * 360) / 256.0F, (float)(var21 * 360) / 256.0F);
                                             }
                                          }
                                       } else {
                                          byte var27;
                                          byte var63;
                                          if (var13 == PacketType.POSITION_ROTATION_UPDATE) {
                                             var52 = ((Byte)var14[0]).byteValue();
                                             var27 = ((Byte)var14[1]).byteValue();
                                             var63 = ((Byte)var14[2]).byteValue();
                                             byte var28 = ((Byte)var14[3]).byteValue();
                                             var55 = ((Byte)var14[4]).byteValue();
                                             var21 = ((Byte)var14[5]).byteValue();
                                             if (var52 >= 0) {
                                                var61 = (byte)(var55 + 128);
                                                if ((var62 = (NetworkPlayer)var15.players.get(var52)) != null) {
                                                   var62.queue(var27, var63, var28, (float)(var61 * 360) / 256.0F, (float)(var21 * 360) / 256.0F);
                                                }
                                             }
                                          } else {
                                             byte var29;
                                             NetworkPlayer var64;
                                             if (var13 == PacketType.ROTATION_UPDATE) {
                                                var52 = ((Byte)var14[0]).byteValue();
                                                var27 = ((Byte)var14[1]).byteValue();
                                                var29 = ((Byte)var14[2]).byteValue();
                                                if (var52 >= 0) {
                                                   var58 = (byte)(var27 + 128);
                                                   if ((var64 = (NetworkPlayer)var15.players.get(var52)) != null) {
                                                      var64.queue((float)(var58 * 360) / 256.0F, (float)(var29 * 360) / 256.0F);
                                                   }
                                                }
                                             } else if (var13 == PacketType.POSITION_UPDATE) {
                                                var52 = ((Byte)var14[0]).byteValue();
                                                var27 = ((Byte)var14[1]).byteValue();
                                                var63 = ((Byte)var14[2]).byteValue();
                                                var29 = ((Byte)var14[3]).byteValue();
                                                if (var52 >= 0 && (var64 = (NetworkPlayer)var15.players.get(var52)) != null) {
                                                   var64.queue(var27, var63, var29);
                                                }
                                             } else if (var13 == PacketType.DESPAWN_PLAYER) {
                                                var12 = (Byte)var14[0];
                                                if (var12 >= 0 && (var22 = (NetworkPlayer)var15.players.remove(var12)) != null) {
                                                   var22.clear();
                                                   var15.minecraft.level.removeEntity(var22);
                                                }
                                             } else if (var13 == PacketType.CHAT_MESSAGE) {
                                                var52 = ((Byte)var14[0]).byteValue();
                                                String var65 = (String)var14[1];
                                                if (var52 < 0) {
                                                   var15.minecraft.hud.addChat("&e" + var65);
                                                } else {
                                                   var15.players.get(var52);
                                                   var15.minecraft.hud.addChat(var65);
                                                }
                                             } else if (var13 == PacketType.DISCONNECT) {
                                                var15.minecraft.networkManager.netHandler.close();
                                                var15.minecraft.networkManager = null;
                                                var15.netHandler.close();
                                                var15.minecraft.setCurrentScreen(new ErrorScreen("Connection lost", (String)var14[0]));
                                             } else if (var13 == PacketType.UPDATE_PLAYER_TYPE) {
                                                var15.minecraft.player.userType = ((Byte)var14[0]).byteValue();
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        if (!var11.connected) {
                           break;
                        }

                        var11.in.compact();
                     }

                     if (var11.out.position() > 0) {
                        var11.out.flip();
                        var11.channel.write(var11.out);
                        var11.out.compact();
                     }
                  } catch (Exception var31) {
                      var9.minecraft.setCurrentScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
                      var9.minecraft.online = false;
                      var31.printStackTrace();
                      var9.netHandler.close();
                      var9.minecraft.networkManager = null;
                   }
               }
            }

            Player var36 = this.player;
            var9 = this.networkManager;
            this.waiting = false;
            if (this.networkManager.levelLoaded) {
               int var40 = (int)(var36.x * 32.0F);
               var8 = (int)(var36.y * 32.0F);
               var5 = (int)(var36.z * 32.0F);
               var6 = (int)(var36.yRot * 256.0F / 360.0F) & 255;
               var7 = (int)(var36.xRot * 256.0F / 360.0F) & 255;
               var9.netHandler.send(PacketType.POSITION_ROTATION, new Object[]{-1, var40, var8, var5, var6, var7});
            }
         }
      }

      if (currentScreen == null && this.player != null && this.player.health <= 0) {
         this.setCurrentScreen((GuiScreen)null);
      }

      if (currentScreen == null || currentScreen.grabsMouse) {
         label542:
         while(true) {
            int var35;
            if (!Mouse.next()) {
               if (this.blockHitTime > 0) {
                  --this.blockHitTime;
               }

               while(true) {
                  do {
                     do {
                        if (!Keyboard.next()) {
                           if (currentScreen == null) {
                              if (Mouse.isButtonDown(0) && (float)(this.ticks - this.lastClick) >= this.timer.tps / 4.0F && this.hasMouse) {
                            	if (this.settings.handleClick) {
                                 if (this.editMode) {
                                     this.onMouseClick(1);
                                  } else {
                                     this.onMouseClick(0);
                                  }
                            	} else {
                            		this.onMouseClick(0);
                                 this.editMode = false;
                            	}
                                 
                                 this.lastClick = this.ticks;
                              }

                              if (Mouse.isButtonDown(1) && (float)(this.ticks - this.lastClick) >= this.timer.tps / 4.0F && this.hasMouse) {
                                 this.onMouseClick(1);
                                 this.lastClick = this.ticks;
                              }
                           }

                           boolean var42 = currentScreen == null && Mouse.isButtonDown(0) && this.hasMouse;
                           boolean var43 = false;
                           if (!this.gamemode.instantBreak && this.blockHitTime <= 0) {
                              if (var42 && this.selected != null && this.selected.entityPos == 0) {
                                 var8 = this.selected.x;
                                 var5 = this.selected.y;
                                 var6 = this.selected.z;
                                 this.gamemode.hitBlock(var8, var5, var6, this.selected.face);
                              } else {
                                 this.gamemode.resetHits();
                              }
                           }
                           break label542;
                        }

                        this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                     } while(!Keyboard.getEventKeyState());

                     if (Keyboard.getEventKey() == this.settings.fullScreenKey.key) {
                    	this.fullscreen = !this.fullscreen;
                        this.toggleFullScreen();
                     }

                     if (currentScreen != null) {
                        currentScreen.keyboardEvent();
                     }

                     if (currentScreen == null) {
                        if (Keyboard.getEventKey() == 1) {
                           this.pause();
                        }

                        if (this.gamemode.isCreative() || this.devMode) {
                           if (Keyboard.getEventKey() == this.settings.loadLocationKey.key) {
                              this.player.resetPos();
                              this.hud.addChat("Last location point has been loaded");
                           }

                           if (Keyboard.getEventKey() == this.settings.saveLocationKey.key) {
                              this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
                              this.hud.addChat("New location point has been set");
                              this.player.resetPos();
                           }

                           if (Keyboard.getEventKey() == 34 && this.networkManager == null) {
                              this.level.addEntity(new HumanoidMob(this.level, this.player.x, this.player.y, this.player.z));
                           }

                           if (Keyboard.getEventKey() == this.settings.flyKey.key) {
                              this.isFlying = !this.isFlying;
                           }
                           
                           if (Keyboard.getEventKey() == this.settings.signKey.key && this.networkManager == null) {
                        	   this.player.releaseAllKeys();
                               this.setCurrentScreen(new SignEditScreen());
                           }
                        }                  

                        if (Keyboard.getEventKey() == this.settings.hideGuiKey.key) {
                           this.hidegui = !this.hidegui;
                        }
                        
                        if (Keyboard.getEventKey() == this.settings.screenshotKey.key) {
                           File var39 = new File(System.getProperty(this.mcdirname, "."));
                           this.hud.addChat(Screenshot.grab(var39, this.width, this.height));
                        }
                        
                        if (Keyboard.getEventKey() == this.settings.debugKey.key) {
                           this.settings.debugScreen = !this.settings.debugScreen;
                        }

                        if (Keyboard.getEventKey() == 63 && this.level.levelType <= 0) {
                           this.raining = !this.raining;
                           this.snowing = false;
                           this.level.cloudColor = 16777215;
                           this.level.skyColor = 10079487;
                           this.level.fogColor = 16777215;
                        }

                        if (Keyboard.getEventKey() == 64 && this.level.levelType <= 0) {
                           this.snowing = !this.snowing;
                           this.raining = false;
                           this.level.cloudColor = 16777215;
                           this.level.skyColor = 10079487;
                           this.level.fogColor = 16777215;
                        }

                        if (Keyboard.getEventKey() == this.settings.inventoryKey.key) {
                           this.gamemode.openInventory();
                        }                     

                        if (Keyboard.getEventKey() == this.settings.chatKey.key) {
                           this.player.releaseAllKeys();
                           this.setCurrentScreen(new ChatInputScreen());
                        }

                        if (this.gamemode.isSurvival() && Keyboard.getEventKey() == this.settings.dropKey.key) {
                           ItemRenderer var2;
                           var2 = this.renderer.itemRenderer;
                           this.renderer.itemRenderer.pos = 0.0F;
                           this.player.drop(this.player.inventory.decrStackSize(this.player.inventory.selected, 1));
                        }

                        if (this.devMode) {
                        	
                            if (Keyboard.getEventKey() == 65) {
                                this.renderer.grabIsometricScreenshot();
                             }
                            
                        	if(Keyboard.getEventKey() == 15 && this.gamemode.isSurvival() && this.player.arrows > 0) {
                                this.level.addEntity(new Arrow(this.level, this.player, this.player.x, this.player.y, this.player.z, this.player.yRot, this.player.xRot, 1.2F));
                                --this.player.arrows;
                                this.level.playSound("random.bow", this.player.x, this.player.y, this.player.z, 1.0F, 1.0F / (this.level.random.nextFloat() * 0.4F + 0.8F));
                            }
                        	
                           if (Keyboard.getEventKey() == 67) {
                              this.level.addEntity(new Slime(this.level, this.player.x, this.player.y, this.player.z));
                           }

                           if (Keyboard.getEventKey() == 68) {
                              this.level.addEntity(new Pigman(this.level, this.player.x, this.player.y, this.player.z));
                           }
                           
                           if (Keyboard.getEventKey() == 62) {
                               this.level.addEntity(new GiantZombie(this.level, this.player.x, this.player.y, this.player.z));
                            }

                           if (Keyboard.isKeyDown(44)) {
                              this.sound.playMusic(this.soundPlayer, "calm");
                           }                       
                           
                           if (Keyboard.getEventKey() == 66) {
                               this.setCurrentScreen(new ConnectScreen(currentScreen));
                           }
                           
                           if (Keyboard.getEventKey() == 48 && this.gamemode.isSurvival()) {
                        	   ItemStack var10 = this.player.inventory.getSelected();
           					   float var88 = 0.7F;
        					   float var900 = this.level.random.nextFloat() * var88 + (1.0F - var88) * 0.5F;
        					   float var102 = this.level.random.nextFloat() * var88 + (1.0F - var88) * 0.5F;
        					   float var156 = this.level.random.nextFloat() * var88 + (1.0F - var88) * 0.5F;
                               if (var10.itemID == Block.LOG.id) {
                                  this.level.addEntity(new Item(this.level, (float)this.player.x + var900, (float)this.player.y + var102, (float)this.player.z + var156, new ItemStack(Block.WOOD.id)));
                                  this.level.addEntity(new Item(this.level, (float)this.player.x + var900, (float)this.player.y + var102, (float)this.player.z + var156, new ItemStack(Block.WOOD.id)));
                                  this.level.addEntity(new Item(this.level, (float)this.player.x + var900, (float)this.player.y + var102, (float)this.player.z + var156, new ItemStack(Block.WOOD.id)));
                                  this.level.addEntity(new Item(this.level, (float)this.player.x + var900, (float)this.player.y + var102, (float)this.player.z + var156, new ItemStack(Block.WOOD.id)));
                                  --var10.stackSize;
                               } else if (var10.itemID == Block.WOOD.id) {
                                   this.level.addEntity(new Item(this.level, (float)this.player.x + var900, (float)this.player.y + var102, (float)this.player.z + var156, new ItemStack(Block.WORKBENCH.id)));
                                  var10.stackSize -= 4;
                               }
                               
                               if (var10.stackSize == 0) {
                                   this.player.inventory.Inventory[this.player.inventory.selected] = null;
                                }
                           }
                           
                            if (Keyboard.getEventKey() == 48 && this.gamemode.isCreative()) {
                        		ItemSelectScreen itemSelectScreen = new ItemSelectScreen();
                        		this.setCurrentScreen(itemSelectScreen);
                            }
                        }
                     }

                     for(var35 = 0; var35 < 9; ++var35) {
                        if (Keyboard.getEventKey() == var35 + 2) {
                           this.player.inventory.selected = var35;
                           
                           if(Mouse.isButtonDown(0)) {
                               this.gamemode.resetHits();
                           }
                        }
                     }
                  } while(Keyboard.getEventKey() != this.settings.toggleFogKey.key);

                  this.settings.toggleSetting(3, !Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) ? 1 : -1);
               }
            }

            var35 = Mouse.getEventDWheel();
            if (var35 != 0) {
               var33 = var35;
               Inventory var37 = this.player.inventory;
               if (var35 > 0) {
                  var33 = 1;
               }

               if (var33 < 0) {
                  var33 = -1;
               }

               for(var37.selected -= var33; var37.selected < 0; var37.selected += 9) {
               }

               while(var37.selected >= 9) {
                  var37.selected -= 9;
               }
               
               this.gamemode.resetHits();
            }

            if (currentScreen == null) {
               if (!this.hasMouse && Mouse.getEventButtonState()) {
                  this.grabMouse();
               } else {
                  if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                     
                	 if (this.settings.handleClick) {
                     if (this.editMode) {
                         this.onMouseClick(1);
                      } else {
                         this.onMouseClick(0);
                      }
                	 } else {
                		 this.onMouseClick(0);
                      this.editMode = false;        		 
                	 }
                     
                     this.lastClick = this.ticks;
                  }

                  if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                	 if(this.settings.handleClick) {
                         this.editMode = !this.editMode;
                	 } else {
                		 this.onMouseClick(1);
                	 }
                	 
                     this.lastClick = this.ticks;
                  }

                  if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.selected != null) {
                     if ((var33 = this.level.getTile(this.selected.x, this.selected.y, this.selected.z)) == Block.GRASS.id && this.networkManager != null) {
                        var33 = Block.DIRT.id;
                     }

                     if (this.networkManager != null && var33 == Block.DOUBLE_SLAB.id) {
                        var33 = Block.SLAB.id;
                     }

                     if (this.networkManager != null && var33 == Block.BEDROCK.id) {
                        var33 = Block.STONE.id;
                     }

                     if (var33 == Block.BIRCH_LEAVES.id) {
                        var33 = Block.LEAVES.id;
                     }
                     
                     if (var33 == Block.OTHER_BLACKBERRY_LEAVES.id) {
                        var33 = Block.LEAVES.id;
                     }

                     if (var33 == Block.OTHER_REDBERRY_LEAVES.id) {
                        var33 = Block.LEAVES.id;
                     }
                     
                     if (var33 == Block.SNOW_OTHER_REDBERRY_LEAVES.id) {
                         var33 = Block.SNOW_LEAVES.id;
                     }
                     
                     if (var33 == Block.SNOW_OTHER_BLACKBERRY_LEAVES.id) {
                        var33 = Block.SNOW_LEAVES.id;
                     }

                     if (var33 == Block.SNOW_BIRCH_LEAVES.id) {
                        var33 = Block.SNOW_LEAVES.id;
                     }
                     
                     if (var33 == Block.DUNGEON_CHEST.id) {
                         var33 = Block.WOOD.id;
                      }
                     
                     this.player.inventory.grabBlock(var33, this.gamemode.isCreative());
                  }
               }
            }

            if (currentScreen != null) {
               currentScreen.mouseEvent();
            }
         }
      }

      if (!this.waiting && currentScreen != null) {
         this.lastClick = this.ticks + 10000;
      }

      if (currentScreen != null) {
         currentScreen.doInput();
         if (currentScreen != null) {
            currentScreen.tick();
         }
      }

      if (!this.waiting && this.level != null) {
         Renderer var38 = this.renderer;
         ++this.renderer.levelTicks;
         ItemRenderer var44 = var38.itemRenderer;
         var38.itemRenderer.lastPos = var44.pos;
         if (var44.moving) {
            ++var44.offset;
            if (var44.offset == 7) {
               var44.offset = 0;
               var44.moving = false;
            }
         }

         Player var45 = var44.minecraft.player;
         ItemStack var46 = var44.minecraft.player.inventory.getSelected();
         float var47 = 0.4F;
         float var48 = (var46 == var44.itemToRenderer ? 1.0F : 0.0F) - var44.pos;
         if (var48 < -var47) {
            var48 = -var47;
         }

         if (var48 > var47) {
            var48 = var47;
         }

         var44.pos += var48;
         if (var44.pos < 0.1F) {
            var44.itemToRenderer = var46;
         }

         if (this.raining && !this.snowing && this.level.levelType == 2) {
            Renderer var49 = var38;
            var45 = var38.minecraft.player;
            Level var51 = var38.minecraft.level;
            var5 = (int)var45.x;
            var6 = (int)var45.y;
            var7 = (int)var45.z;

            for(var34 = 0; var34 < 50; ++var34) {
               int var56 = var5 + var49.random.nextInt(9) - 4;
               int var54 = var7 + var49.random.nextInt(9) - 4;
               int var57;
               if ((var57 = var51.getHighestTile(var56, var54)) <= var6 + 4 && var57 >= var6 - 4) {
                  float var59 = var49.random.nextFloat();
                  float var60 = var49.random.nextFloat();
                  if(this.settings.particles) {
                  var49.minecraft.particleManager.spawnParticle(new WaterDropParticle(var51, (float)var56 + var59, (float)var57 + 0.1F, (float)var54 + var60));
                  }
               }
            }
         }

         LevelRenderer var50 = this.levelRenderer;
         ++this.levelRenderer.ticks;
         this.level.tickEntities();
         if (!this.isOnline()) {
            this.level.tick();
         }

         this.level.randomUpdates((int)this.player.x, (int)this.player.y, (int)this.player.z);
         this.particleManager.tick();
      }

   }

   private void renderLoadingScreen() throws LWJGLException {
	   ScaledResolution var69 = new ScaledResolution(this.width, this.height);
	   int var1 = var69.getScaledWidth();
	   int var2 = var69.getScaledHeight();
      GL11.glClear(16640);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, (double)var1, (double)var2, 0.0D, 100.0D, 300.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      GL11.glViewport(0, 0, this.width, this.height);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      ShapeRenderer var3 = ShapeRenderer.instance;
      GL11.glDisable(2896);
      GL11.glEnable(3553);
      GL11.glDisable(2912);
      GL11.glBindTexture(3553, this.textureManager.load("/title/mojang.png"));
      var3.begin();
      var3.color(16777215);
      var3.vertexUV(0.0F, (float)this.height, 0.0F, 0.0F, 0.0F);
      var3.vertexUV((float)this.width, (float)this.height, 0.0F, 0.0F, 0.0F);
      var3.vertexUV((float)this.width, 0.0F, 0.0F, 0.0F, 0.0F);
      var3.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      var3.end();
      short var4 = 256;
      short var5 = 256;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var3.color(16777215);
      this.blit((this.width / 2 - var4) / 2, (this.height / 2 - var5) / 2, 0, 0, var4, var5);
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.1F);
      Display.swapBuffers();
   }

   public void blit(int var1, int var2, int var3, int var4, int var5, int var6) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      ShapeRenderer var9 = ShapeRenderer.instance;
      var9.begin();
      var9.vertexUV((float)(var1 + 0), (float)(var2 + var6), 0.0F, (float)(var3 + 0) * var7, (float)(var4 + var6) * var8);
      var9.vertexUV((float)(var1 + var5), (float)(var2 + var6), 0.0F, (float)(var3 + var5) * var7, (float)(var4 + var6) * var8);
      var9.vertexUV((float)(var1 + var5), (float)(var2 + 0), 0.0F, (float)(var3 + var5) * var7, (float)(var4 + 0) * var8);
      var9.vertexUV((float)(var1 + 0), (float)(var2 + 0), 0.0F, (float)(var3 + 0) * var7, (float)(var4 + 0) * var8);
      var9.end();
   }

   public final void toggleFullScreen() {
      try {
         if (this.fullscreen) {
            Display.setDisplayMode(Display.getDesktopDisplayMode());
            this.width = Display.getDisplayMode().getWidth();
            this.height = Display.getDisplayMode().getHeight();
         } else {
            if (this.canvas != null) {
               this.width = this.canvas.getWidth();
               this.height = this.canvas.getHeight();
            } else {
               this.width = this.tempWidth;
               this.height = this.tempHeight;
            }

            Display.setDisplayMode(new DisplayMode(this.tempWidth, this.tempHeight));
         }

         Display.setFullscreen(this.fullscreen);
         Display.update();
         Thread.sleep(1000L);
         if (this.fullscreen) {
            this.grabMouse();
         }

         if (currentScreen != null) {        	 
            this.resize(this.width, this.height);
         }
         
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void resize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      if (currentScreen != null) {
		   ScaledResolution var3 = new ScaledResolution(var1, var2);
	      var1 = var3.getScaledWidth();
	      var2 = var3.getScaledHeight();
         currentScreen.open(this, var1, var2);
      }

   }

   public final boolean isOnline() {
      return this.networkManager != null;
   }

   public final void generateLevel(int var1, int var2, int var3, int var4) {
      this.gamemode = null;
      
      if (this.selectedGameMode == 0) {
         this.gamemode = new CreativeGameMode(this);
      } else if (this.selectedGameMode == 1) {
         this.gamemode = new SurvivalGameMode(this);
      } else if (this.selectedGameMode == 2) {
         this.gamemode = new HardcoreGameMode(this);
      }

      String var5 = this.session != null ? this.session.username : "anonymous";
      levelgen.islandGen = var3 == 1;
      levelgen.floatingGen = var3 == 2;
      levelgen.flatGen = var3 == 3;
      levelgen.levelType = var4;
      var1 = 128 << var1;
      var3 = var1;
      short var7 = 64;
      if (var2 == 1) {
         var1 /= 2;
         var3 <<= 1;
      } else if (var2 == 2) {
         var1 /= 2;
         var3 = var1;
         var7 = 256;
      } else if (var2 == 3) {
         var1 /= 2;
         var3 = var1;
         var7 = 512;
      }

      Level var8 = levelgen.generate(var5, var1, var3, var7);
      var8.gamemode = this.selectedGameMode;
      this.gamemode.prepareLevel(var8);
      this.setLevel(var8);
      this.selectedGameMode = 0;
      this.raining = false;
      this.snowing = false;
   }
   
   public final boolean loadSlotLevel(String var1, int var2) {
	  Level var3;
	  if ((var3 = this.levelIo.loadSlot(this.host, var1, var2)) == null) {
	     return false;
	  } else {
	     this.setLevel(var3);
	     return true;
	  }
   }

   public final void setLevel(Level var1) {
      if (this.applet == null || !this.applet.getDocumentBase().getHost().equalsIgnoreCase("minecraft.net") && !this.applet.getDocumentBase().getHost().equalsIgnoreCase("www.minecraft.net") || !this.applet.getCodeBase().getHost().equalsIgnoreCase("minecraft.net") && !this.applet.getCodeBase().getHost().equalsIgnoreCase("www.minecraft.net")) {
         var1 = null;
      }

      this.level = var1;
      if (var1 == null) {
         this.gamemode = new CreativeGameMode(this);   
      } else {
         this.gamemode = new SurvivalGameMode(this);
      }

      if (var1 != null) {
         if (var1.gamemode == 0) {
            this.gamemode = new CreativeGameMode(this);
         } else if (var1.gamemode == 1) {
            this.gamemode = new SurvivalGameMode(this);
         } else if (var1.gamemode == 2) {
            this.gamemode = new HardcoreGameMode(this);
         }
         
         if(this.networkManager != null) {
        	this.gamemode.apply(this.player);
         }

         var1.initTransient();
         this.gamemode.apply(var1);
         var1.font = this.fontRenderer;
         var1.rendererContext$5cd64a7f = this;
         if (!this.isOnline()) {
            this.player = (Player)var1.findSubclassOf(Player.class);
         } else if (this.player != null) {
            this.player.resetPos();
            this.gamemode.preparePlayer(this.player);
            if (var1 != null) {
               var1.player = this.player;
               var1.addEntity(this.player);
            }
         }
      }

      if (this.player == null) {
         this.player = new Player(var1);
         this.player.resetPos();
         this.gamemode.preparePlayer(this.player);
         if (var1 != null) {
            var1.player = this.player;
         }
      }

      if (this.player != null) {
         this.player.input = new InputHandlerImpl(this.settings);
         this.gamemode.apply(this.player);
      }

      if (this.levelRenderer != null) {
         LevelRenderer var2 = this.levelRenderer;
         if (this.levelRenderer.level != null) {
            var2.level.removeListener(var2);
         }

         var2.level = var1;
         if (var1 != null) {
            var1.addListener(var2);
            var2.refresh();
         }
      }

      if (this.particleManager != null) {
         ParticleManager var4 = this.particleManager;
         if (var1 != null) {
            var1.particleEngine = var4;
         }

         for(int var3 = 0; var3 < 2; ++var3) {
            var4.particles[var3].clear();
         }
      }

      System.gc();
   }
   
   public void playTutorialLevel(Minecraft var1) {
   }
}
