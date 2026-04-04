package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;

public final class Connection {
	public SocketConnection socketConnection;
	public int time;

	public Connection(SocketConnection connection, int time) {
		this.socketConnection = connection;
		this.time = 100;
	}
}