package com.mojang.minecraft.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public final class PlayerList {
	private static Logger logger = MinecraftServer.logger;
	private String desc;
	private File listFile;
	private Set content = new HashSet();

	public PlayerList(String desc, File file) {
		this.desc = desc;
		this.listFile = file;
		this.load();
	}

	public final void add(String name) {
		name = name.toLowerCase();
		this.content.add(name);
		this.save();
	}

	public final void remove(String name) {
		name = name.toLowerCase();
		this.content.remove(name);
		this.save();
	}

	public final boolean contains(String name) {
		name = name.toLowerCase();
		return this.content.contains(name);
	}

	private void load() {
		try {
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.listFile));
			String string2 = null;

			while((string2 = bufferedReader1.readLine()) != null) {
				string2 = string2.toLowerCase();
				this.content.add(string2);
			}

			bufferedReader1.close();
		} catch (IOException iOException4) {
			try {
				this.listFile.createNewFile();
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}

			logger.warning("Failed to load player list \"" + this.desc + "\". (" + iOException4 + ")");
		}
	}

	private void save() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.listFile));
			Iterator iterator2 = this.content.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (IOException iOException4) {
			logger.warning("Failed to save player list \"" + this.desc + "\". (" + iOException4 + ")");
		}
	}
}