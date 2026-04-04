package com.mojang.minecraft.level;

import com.mojang.minecraft.server.MinecraftServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class LevelIO {
	private MinecraftServer levelLoaderListener;

	public LevelIO(MinecraftServer server) {
		this.levelLoaderListener = server;
	}

	public final Level load(InputStream in) {
		if(this.levelLoaderListener != null) {
			this.levelLoaderListener.beginLevelLoading("Loading level");
		}

		if(this.levelLoaderListener != null) {
			this.levelLoaderListener.levelLoadUpdate("Reading..");
		}

		try {
			DataInputStream dataInputStream10;
			if((dataInputStream10 = new DataInputStream(new GZIPInputStream(in))).readInt() != 656127880) {
				return null;
			} else {
				byte in1;
				if((in1 = dataInputStream10.readByte()) > 2) {
					return null;
				} else if(in1 <= 1) {
					String in3 = dataInputStream10.readUTF();
					String string15 = dataInputStream10.readUTF();
					long j7 = dataInputStream10.readLong();
					short s3 = dataInputStream10.readShort();
					short s4 = dataInputStream10.readShort();
					short s5 = dataInputStream10.readShort();
					byte[] b6 = new byte[s3 * s4 * s5];
					dataInputStream10.readFully(b6);
					dataInputStream10.close();
					Level level11;
					(level11 = new Level()).setData(s3, s5, s4, b6);
					level11.name = in3;
					level11.creator = string15;
					level11.createTime = j7;
					return level11;
				} else {
					Level level2;
					LevelInputStream in2;
					(level2 = (Level)(in2 = new LevelInputStream(dataInputStream10)).readObject()).initTransient();
					in2.close();
					return level2;
				}
			}
		} catch (Exception exception9) {
			exception9.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(exception9.toString()).toString();
			return null;
		}
	}

	public static void save(Level level, OutputStream out) {
		try {
			DataOutputStream out1;
			(out1 = new DataOutputStream(new GZIPOutputStream(out))).writeInt(656127880);
			out1.writeByte(2);
			ObjectOutputStream out2;
			(out2 = new ObjectOutputStream(out1)).writeObject(level);
			out2.close();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}
}