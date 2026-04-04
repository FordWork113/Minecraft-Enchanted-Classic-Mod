package com.mojang.minecraft.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.zip.GZIPOutputStream;

final class LevelCopyThread extends Thread {
	private byte[] blocks;
	private Client client;

	LevelCopyThread(Client client, byte[] blocks) {
		this.client = client;
		this.blocks = blocks;
	}

	public final void run() {
		try {
			ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
			Thread.sleep(500L);
			ByteArrayOutputStream byteArrayOutputStream3 = byteArrayOutputStream1;
			byte[] b2 = this.blocks;

			try {
				DataOutputStream dataOutputStream6;
				(dataOutputStream6 = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream3))).writeInt(b2.length);
				dataOutputStream6.write(b2);
				dataOutputStream6.close();
			} catch (Exception exception4) {
				throw new RuntimeException(exception4);
			}

			Thread.sleep(500L);
			this.client.setBlocks(byteArrayOutputStream1.toByteArray());
		} catch (InterruptedException interruptedException5) {
		}
	}
}