package com.mojang.minecraft.render;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.GuiScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Shaders {
   public Minecraft minecraft;
   public static final Shaders instance = new Shaders();
   public int activeProgram = 0;
   public int baseProgram = 0;
   private int baseVShader = 0;
   private int baseFShader = 0;
   public int finalProgram = 0;
   private int finalVShader = 0;
   private int finalFShader = 0;
   private int baseTextureId = 0;
   private int baseTextureWidth = 0;
   private int baseTextureHeight = 0;
   private int depthTextureId = 0;
   private int depthTextureWidth = 0;
   private int depthTextureHeight = 0;
   private int depthTexture2Id = 0;
   private int depthTexture2Width = 0;
   private int depthTexture2Height = 0;

   public Shaders() {
      this.initShaders();
   }

   public void initShaders() {
       String mcDir = System.getProperty("user.dir");

       String path = "/shaders";
       File file = new File(mcDir, path.startsWith("/") ? path.substring(1) : path);
       
       if (!file.exists()) {
           System.out.println("ERROR: Shaders files not found.");
       }       
      
      if(file.exists()) {
      this.baseProgram = ARBShaderObjects.glCreateProgramObjectARB();
      if (this.baseProgram != 0) {
         this.baseVShader = this.createVertShader(path + "/base.vsh");
         this.baseFShader = this.createFragShader(path + "/base.fsh");
      }

      if (this.baseVShader != 0 && this.baseFShader != 0) {
         ARBShaderObjects.glAttachObjectARB(this.baseProgram, this.baseVShader);
         ARBShaderObjects.glAttachObjectARB(this.baseProgram, this.baseFShader);
         ARBShaderObjects.glLinkProgramARB(this.baseProgram);
         ARBShaderObjects.glValidateProgramARB(this.baseProgram);
         if (!printLogInfo(this.baseProgram)) {
            this.baseProgram = 0;
         }
      }

      this.finalProgram = ARBShaderObjects.glCreateProgramObjectARB();
      if (this.finalProgram != 0) {
         this.finalVShader = this.createVertShader(path + "/final.vsh");
         this.finalFShader = this.createFragShader(path +  "/final.fsh");
      }

      if (this.finalVShader != 0 && this.finalFShader != 0) {
         ARBShaderObjects.glAttachObjectARB(this.finalProgram, this.finalVShader);
         ARBShaderObjects.glAttachObjectARB(this.finalProgram, this.finalFShader);
         ARBShaderObjects.glLinkProgramARB(this.finalProgram);
         ARBShaderObjects.glValidateProgramARB(this.finalProgram);
         if (!printLogInfo(this.finalProgram)) {
            this.finalProgram = 0;
         }
      }
     }

   }

   public void useProgram(int var1) {
      ARBShaderObjects.glUseProgramObjectARB(var1);
      this.activeProgram = var1;
      if (var1 != 0) {
         int var2 = ARBShaderObjects.glGetUniformLocationARB(var1, "sampler0");
         ARBShaderObjects.glUniform1iARB(var2, 0);
      }

   }

  private int createVertShader(String path) {
    int shader = ARBShaderObjects.glCreateShaderObjectARB(35633);
    if (shader == 0)  {
       return 0;
    }

    String source = loadShader(path);
    if (source == null) {
       return 0;
    }

    ARBShaderObjects.glShaderSourceARB(shader, source);
    ARBShaderObjects.glCompileShaderARB(shader);

    if (!printLogInfo(shader)) {
        shader = 0;
    }

    return shader;
  }

  private int createFragShader(String path) {
    int shader = ARBShaderObjects.glCreateShaderObjectARB(35632);
    if (shader == 0)  {
       return 0;
    }

    String source = loadShader(path);
    if (source == null) {
       return 0;
    }

    ARBShaderObjects.glShaderSourceARB(shader, source);
    ARBShaderObjects.glCompileShaderARB(shader);

    if (!printLogInfo(shader)) {
        shader = 0;
    }

    return shader;
 }

   private static boolean printLogInfo(int var0) {
      IntBuffer var1 = BufferUtils.createIntBuffer(1);
      ARBShaderObjects.glGetObjectParameterARB(var0, 35716, var1);
      int var2 = var1.get();
      if (var2 > 1) {
         ByteBuffer var3 = BufferUtils.createByteBuffer(var2);
         var1.flip();
         ARBShaderObjects.glGetInfoLogARB(var0, var1, var3);
         byte[] var4 = new byte[var2];
         var3.get(var4);
         String var5 = new String(var4);
         System.out.println("Info log:\n" + var5);
         return false;
      } else {
         return true;
      }
   }

   private int createTexture(int var1, int var2, boolean var3) {
      int var4 = GL11.glGenTextures();
      GL11.glBindTexture(3553, var4);
      ByteBuffer var5;
      if (var3) {
         var5 = ByteBuffer.allocateDirect(var1 * var2 * 4 * 4);
         GL11.glTexImage2D(3553, 0, 6402, var1, var2, 0, 6402, 5126, var5);
      } else {
         var5 = ByteBuffer.allocateDirect(var1 * var2 * 4);
         GL11.glTexImage2D(3553, 0, 6408, var1, var2, 0, 6408, 5121, var5);
      }

      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      return var4;
   }

   private void a(int var1) {
      GL11.glDeleteTextures(var1);
   }

   public int baseTextureId(Minecraft var1) {
      if (this.baseTextureId == 0 || this.baseTextureWidth != var1.width || this.baseTextureHeight != var1.height) {
         int var2 = this.createTexture(var1.width, var1.height, false);
         this.baseTextureId = var2;
         this.baseTextureWidth = var1.width;
         this.baseTextureHeight = var1.height;
      }

      return this.baseTextureId;
   }

   public int depthTextureId(Minecraft var1) {
      if (this.depthTextureId == 0 || this.depthTextureWidth != var1.width || this.depthTextureHeight != var1.height) {
         int var2 = this.createTexture(var1.width, var1.height, true);
         this.depthTextureId = var2;
         this.depthTextureWidth = var1.width;
         this.depthTextureHeight = var1.height;
      }

      return this.depthTextureId;
   }

   public int depthTexture2Id(Minecraft var1) {
      if (this.depthTexture2Id == 0 || this.depthTexture2Width != var1.width || this.depthTexture2Height != var1.height) {
         int var2 = this.createTexture(var1.width, var1.height, true);
         this.depthTexture2Id = var2;
         this.depthTexture2Width = var1.width;
         this.depthTexture2Height = var1.height;
      }

      return this.depthTexture2Id;
   }

   public void processScene(Minecraft var1, float var2, float var3, float var4, float var5) {
      if (var1.settings.shaders && this.finalProgram != 0) {
         GL11.glBindTexture(3553, this.baseTextureId(var1));
         GL11.glCopyTexImage2D(3553, 0, 6408, 0, 0, var1.width, var1.height, 0);
         GL13.glActiveTexture(33985);
         GL11.glBindTexture(3553, this.depthTextureId(var1));
         GL13.glActiveTexture(33986);
         GL11.glBindTexture(3553, this.depthTexture2Id(var1));
         GL13.glActiveTexture(33984);
         this.useProgram(this.finalProgram);
         int var6 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "sampler0");
         ARBShaderObjects.glUniform1iARB(var6, 0);
         int var7 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "sampler1");
         ARBShaderObjects.glUniform1iARB(var7, 1);
         int var8 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "sampler2");
         ARBShaderObjects.glUniform1iARB(var8, 2);
         int var9 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "aspectRatio");
         ARBShaderObjects.glUniform1fARB(var9, (float)var1.width / (float)var1.height);
         int var10 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "near");
         ARBShaderObjects.glUniform1fARB(var10, 0.05F);
         int var11 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "far");
         ARBShaderObjects.glUniform1fARB(var11, var2);
         int var12 = GL11.glGetInteger(2917);
         int var13 = ARBShaderObjects.glGetUniformLocationARB(this.finalProgram, "fogMode");
         ARBShaderObjects.glUniform1iARB(var13, var12);
         GL11.glClearColor(var3, var4, var5, 0.0F);
         GL11.glClear(16384);
         GL11.glDisable(2929);
         GL11.glDisable(3042);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0D, (double)var1.width, (double)var1.height, 0.0D, -1.0D, 1.0D);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glBegin(7);
         GL11.glTexCoord2f(0.0F, 1.0F);
         GL11.glVertex3f(0.0F, 0.0F, 0.0F);
         GL11.glTexCoord2f(0.0F, 0.0F);
         GL11.glVertex3f(0.0F, (float)var1.height, 0.0F);
         GL11.glTexCoord2f(1.0F, 0.0F);
         GL11.glVertex3f((float)var1.width, (float)var1.height, 0.0F);
         GL11.glTexCoord2f(1.0F, 1.0F);
         GL11.glVertex3f((float)var1.width, 0.0F, 0.0F);
         GL11.glEnd();
         GL11.glEnable(2929);
         this.useProgram(0);
      }

   }

   public static void copyDepthTexture(int var0, Minecraft var1) {
      GL11.glBindTexture(3553, var0);
      GL11.glCopyTexImage2D(3553, 0, 6402, 0, 0, var1.width, var1.height, 0);
   }

   private String loadShader(String path) {
    try {
        String mcDir = System.getProperty("user.dir");

        File file = new File(mcDir, path.startsWith("/") ? path.substring(1) : path);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder source = new StringBuilder();

        String line;
        String versionLine = null;

        while ((line = reader.readLine()) != null) {

            if (line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            String trimmed = line.trim();

            if (trimmed.startsWith("#version")) {
                if (versionLine == null) {
                    versionLine = trimmed;
                }
                continue;
            }

            if (trimmed.startsWith("#include")) {
                String includePath = trimmed.split("\"")[1];
                source.append(loadShader(includePath)).append("\n");
                continue;
            }

            source.append(line).append("\n");
        }

        reader.close();

        if (versionLine == null) {
            versionLine = "#version 120";
        }

        return versionLine + "\n" + source.toString();

    } catch (Exception e) {
        System.out.println("Error loading shader: " + path);
        e.printStackTrace();
        return null;
    }
}
  
}
