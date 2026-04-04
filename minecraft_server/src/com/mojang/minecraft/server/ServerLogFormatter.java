package com.mojang.minecraft.server;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * prob some $1 class or whatever coz its not public
 */
final class ServerLogFormatter extends Formatter {
	public final String format(LogRecord lr) {
		Level level3 = lr.getLevel();
		String string2 = "   ";
		if(level3 == Level.WARNING) {
			string2 = "  !";
		}

		if(level3 == Level.SEVERE) {
			string2 = "***";
		}

		return string2 + "  " + MinecraftServer.dateFormat.format(lr.getMillis()) + "  " + lr.getMessage() + "\n";
	}
}