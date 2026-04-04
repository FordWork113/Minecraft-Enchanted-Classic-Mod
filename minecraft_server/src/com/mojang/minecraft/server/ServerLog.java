package com.mojang.minecraft.server;

import java.io.OutputStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

final class ServerLog extends StreamHandler {
	ServerLog(OutputStream out, Formatter fmt) {
		super(out, fmt);
	}

	public final synchronized void publish(LogRecord lr) {
		super.publish(lr);
		this.flush();
	}
}