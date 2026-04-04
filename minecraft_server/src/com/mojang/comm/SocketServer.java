package com.mojang.comm;

import com.mojang.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.List;

public final class SocketServer {
	public ServerSocketChannel ssc;
	public MinecraftServer server;
	public List connections = new LinkedList();

	public SocketServer(int port, MinecraftServer server) throws IOException {
		this.server = server;
		this.ssc = ServerSocketChannel.open();
		this.ssc.socket().bind(new InetSocketAddress(port));
		this.ssc.configureBlocking(false);
	}
}
