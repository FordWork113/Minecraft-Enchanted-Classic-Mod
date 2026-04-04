package com.mojang.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

final class ConsoleInput extends Thread {
	private MinecraftServer server;

	ConsoleInput(MinecraftServer server) {
		this.server = server;
	}

	public final void run() {
		try {
			BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
			String string2 = null;

			while((string2 = bufferedReader1.readLine()) != null) {
				synchronized(MinecraftServer.a(this.server)) {
					MinecraftServer.a(this.server).add(string2);
				}
			}

			MinecraftServer.logger.warning("stdin: end of file! No more direct console input is possible.");
		} catch (IOException iOException5) {
			MinecraftServer.logger.warning("stdin: ioexception! No more direct console input is possible.");
			iOException5.printStackTrace();
		}
	}
}