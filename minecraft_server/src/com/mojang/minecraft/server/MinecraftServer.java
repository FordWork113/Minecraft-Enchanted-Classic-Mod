package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;
import com.mojang.comm.SocketServer;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.net.Packet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MinecraftServer implements Runnable {
	static Logger logger = Logger.getLogger("MinecraftServer");
	static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private SocketServer socketServer;
	private Map connections = new HashMap();
	private List clients = new ArrayList();
	private List connectionQueue = new ArrayList();
	private int maxPlayers;
	private Properties properties = new Properties();
	public Level level;
	private boolean isPublic = false;
	public String serverName;
	public String motd;
	private int port;
	public boolean adminSlot;
	private Client[] playerClients;
	public PlayerList admins = new PlayerList("Admins", new File("admins.txt"));
	public PlayerList banned = new PlayerList("Banned", new File("banned.txt"));
	private PlayerList bannedIP = new PlayerList("Banned (IP)", new File("banned-ip.txt"));
	public PlayerList players = new PlayerList("Players", new File("players.txt"));
	private List commandQueue = new ArrayList();
	private String salt = "" + (new Random()).nextLong();
	private String serverURL = "";
	public MD5Hash md5Hash = new MD5Hash(this.salt);
	public boolean verifyNames = false;
	private boolean growTrees = false;
	private int numConnections;

	public MinecraftServer() throws IOException {
		try {
			this.properties.load(new FileReader("server.properties"));
		} catch (Exception exception4) {
			logger.warning("Failed to load server.properties!");
		}

		try {
			this.serverName = this.properties.getProperty("server-name", "Minecraft Server");
			this.motd = this.properties.getProperty("motd", "Welcome to my Minecraft Server!");
			this.port = Integer.parseInt(this.properties.getProperty("port", "25565"));
			this.maxPlayers = Integer.parseInt(this.properties.getProperty("max-players", "16"));
			this.isPublic = Boolean.parseBoolean(this.properties.getProperty("public", "true"));
			this.verifyNames = Boolean.parseBoolean(this.properties.getProperty("verify-names", "true"));
			this.growTrees = Boolean.parseBoolean(this.properties.getProperty("grow-trees", "false"));
			this.adminSlot = Boolean.parseBoolean(this.properties.getProperty("admin-slot", "false"));
			if(this.maxPlayers < 1) {
				this.maxPlayers = 1;
			}

			if(this.maxPlayers > 32) {
				this.maxPlayers = 32;
			}

			this.numConnections = Integer.parseInt(this.properties.getProperty("max-connections", "3"));
			this.properties.setProperty("server-name", this.serverName);
			this.properties.setProperty("motd", this.motd);
			this.properties.setProperty("max-players", "" + this.maxPlayers);
			this.properties.setProperty("port", "" + this.port);
			this.properties.setProperty("public", "" + this.isPublic);
			this.properties.setProperty("verify-names", "" + this.verifyNames);
			this.properties.setProperty("max-connections", "3");
			this.properties.setProperty("grow-trees", "" + this.growTrees);
			this.properties.setProperty("admin-slot", "" + this.adminSlot);
		} catch (Exception exception3) {
			exception3.printStackTrace();
			logger.warning("server.properties is broken! Delete it or fix it!");
			System.exit(0);
		}

		if(!this.verifyNames) {
			logger.warning("######################### WARNING #########################");
			logger.warning("verify-names is set to false! This means that anyone who");
			logger.warning("connects to this server can choose any username he or she");
			logger.warning("wants! This includes impersonating an OP!");
			if(this.isPublic) {
				logger.warning("");
				logger.warning("AND SINCE THIS IS A PUBLIC SERVER, IT WILL HAPPEN TO YOU!");
				logger.warning("");
			}
			
			logger.warning("");
			logger.warning("THIS IS A BETA SERVER VERSION!!!");
			logger.warning("");
			logger.warning("If you wish to fix this, edit server.properties, and change");
			logger.warning("verify-names to true.");
			logger.warning("###########################################################");
		}

		try {
			this.properties.store(new FileWriter("server.properties"), "Minecraft server properties");
		} catch (Exception exception2) {
			logger.warning("Failed to save server.properties!");
		}

		this.playerClients = new Client[this.maxPlayers];
		this.socketServer = new SocketServer(this.port, this);
		(new ConsoleInput(this)).start();
	}

	public final void disconnect(SocketConnection sc) {
		Client sc1;
		if((sc1 = (Client)this.connections.get(sc)) != null) {
			this.players.remove(sc1.name);
			logger.info(sc1 + " disconnected");
			this.connections.remove(sc1.connection);
			this.clients.remove(sc1);
			if(sc1.port >= 0) {
				this.playerClients[sc1.port] = null;
			}

			this.sendPacket(Packet.PLAYER_DISCONNECT, new Object[]{sc1.port});
		}

	}

	private void connect(SocketConnection sc) {
		this.connectionQueue.add(new Connection(sc, 100));
	}

	public final void connect(Client c) {
		this.connectionQueue.add(new Connection(c.connection, 100));
	}

	public static void disconnect(Client c) {
		c.connection.disconnect();
	}

	public final void sendPacket(Packet packet, Object... data) {
		for(int i3 = 0; i3 < this.clients.size(); ++i3) {
			try {
				((Client)this.clients.get(i3)).sendPacket(packet, data);
			} catch (Exception exception5) {
				((Client)this.clients.get(i3)).handleException(exception5);
			}
		}

	}

	public final void sendPlayerPacket(Client pc, Packet packet, Object... data) {
		for(int i4 = 0; i4 < this.clients.size(); ++i4) {
			if(this.clients.get(i4) != pc) {
				try {
					((Client)this.clients.get(i4)).sendPacket(packet, data);
				} catch (Exception exception6) {
					((Client)this.clients.get(i4)).handleException(exception6);
				}
			}
		}

	}

	public void run() {
		logger.info("Now accepting input on " + this.port);
		int i1 = 50000000;
		int i2 = 500000000;

		try {
			long j3 = System.nanoTime();
			long j5 = System.nanoTime();
			int i7 = 0;

			while(true) {
				this.tick();

				for(; System.nanoTime() - j5 > (long)i1; ++i7) {
					j5 += (long)i1;
					this.tickLevel();
					if(i7 % 1200 == 0) {
						MinecraftServer minecraftServer8 = this;

						try {
							new LevelIO(minecraftServer8);
							LevelIO.save(minecraftServer8.level, new FileOutputStream("server_level.dat"));
						} catch (Exception exception10) {
							logger.severe("Failed to save the level! " + exception10);
						}

						logger.info("Level saved! Load: " + this.clients.size() + "/" + this.maxPlayers);
					}

					if(i7 % 900 == 0) {
						HashMap hashMap9;
						(hashMap9 = new HashMap()).put("name", this.serverName);
						hashMap9.put("users", this.clients.size());
						hashMap9.put("max", this.maxPlayers - (this.adminSlot ? 1 : 0));
						hashMap9.put("public", this.isPublic);
						hashMap9.put("port", this.port);
						hashMap9.put("salt", this.salt);
						hashMap9.put("admin-slot", this.adminSlot);
						hashMap9.put("version", (byte)7);
					}
				}

				while(System.nanoTime() - j3 > (long)i2) {
					j3 += (long)i2;
					this.sendPacket(Packet.KEEP_ALIVE, new Object[0]);
				}

				Thread.sleep(5L);
			}
		} catch (Exception exception11) {
			logger.log(java.util.logging.Level.SEVERE, "Error in main loop, server shutting down!", exception11);
			exception11.printStackTrace();
		}
	}

	private void tickLevel() {
		Iterator iterator1 = this.clients.iterator();

		while(iterator1.hasNext()) {
			Client client2 = (Client)iterator1.next();

			try {
				client2.tick();
			} catch (Exception exception8) {
				client2.handleException(exception8);
			}
		}

		this.level.tick();

		for(int i9 = 0; i9 < this.connectionQueue.size(); ++i9) {
			Connection connection10 = (Connection)this.connectionQueue.get(i9);
			this.disconnect(connection10.socketConnection);

			try {
				SocketConnection socketConnection3 = connection10.socketConnection;

				try {
					if(socketConnection3.writeBuffer.position() > 0) {
						socketConnection3.writeBuffer.flip();
						socketConnection3.socketChannel.write(socketConnection3.writeBuffer);
						socketConnection3.writeBuffer.compact();
					}
				} catch (IOException iOException6) {
				}

				if(connection10.time-- <= 0) {
					try {
						connection10.socketConnection.disconnect();
					} catch (Exception exception5) {
					}

					this.connectionQueue.remove(i9--);
				}
			} catch (Exception exception7) {
				try {
					connection10.socketConnection.disconnect();
				} catch (Exception exception4) {
				}
			}
		}

	}

	public final void beginLevelLoading(String str) {
		logger.info(str);
	}

	public final void levelLoadUpdate(String str) {
		logger.fine(str);
	}

	private void tick() {
		List list1 = this.commandQueue;
		synchronized(this.commandQueue) {
			while(this.commandQueue.size() > 0) {
				this.command((Client)null, (String)this.commandQueue.remove(0));
			}
		}

		try {
			SocketServer socketServer13 = this.socketServer;

			SocketChannel socketChannel14;
			while((socketChannel14 = socketServer13.ssc.accept()) != null) {
				try {
					socketChannel14.configureBlocking(false);
					SocketConnection socketConnection2 = new SocketConnection(socketChannel14);
					socketServer13.connections.add(socketConnection2);
					SocketConnection socketConnection4 = socketConnection2;
					MinecraftServer minecraftServer3 = socketServer13.server;
					if(socketServer13.server.bannedIP.contains(socketConnection2.ip)) {
						socketConnection2.sendPacket(Packet.KICK_PLAYER, new Object[]{"You\'re banned!"});
						logger.info(socketConnection2.ip + " tried to connect, but is banned.");
						minecraftServer3.connect(socketConnection2);
					} else {
						int i5 = 0;
						Iterator iterator6 = minecraftServer3.clients.iterator();

						while(iterator6.hasNext()) {
							if(((Client)iterator6.next()).connection.ip.equals(socketConnection4.ip)) {
								++i5;
							}
						}

						if(i5 >= minecraftServer3.numConnections) {
							socketConnection4.sendPacket(Packet.KICK_PLAYER, new Object[]{"Too many connection!"});
							logger.info(socketConnection4.ip + " tried to connect, but is already connected " + i5 + " times.");
							minecraftServer3.connect(socketConnection4);
						} else {
							int i22;
							if((i22 = minecraftServer3.getFreePlayerSlots()) < 0) {
								socketConnection4.sendPacket(Packet.KICK_PLAYER, new Object[]{"The server is full!"});
								logger.info(socketConnection4.ip + " tried to connect, but failed because the server was full.");
								minecraftServer3.connect(socketConnection4);
							} else {
								Client client16 = new Client(minecraftServer3, socketConnection4, i22);
								logger.info(client16 + " connected");
								minecraftServer3.connections.put(socketConnection4, client16);
								minecraftServer3.clients.add(client16);
								if(client16.port >= 0) {
									minecraftServer3.playerClients[client16.port] = client16;
								}
							}
						}
					}
				} catch (IOException iOException10) {
					socketChannel14.close();
					throw iOException10;
				}
			}

			for(int i17 = 0; i17 < socketServer13.connections.size(); ++i17) {
				SocketConnection socketConnection15 = (SocketConnection)socketServer13.connections.get(i17);

				try {
					SocketConnection socketConnection18 = socketConnection15;
					socketConnection15.socketChannel.read(socketConnection15.readBuffer);
					int i19 = 0;

					while(socketConnection18.readBuffer.position() > 0 && i19++ != 100) {
						socketConnection18.readBuffer.flip();
						byte b20 = socketConnection18.readBuffer.get(0);
						Packet packet24;
						if((packet24 = Packet.PACKETS[b20]) == null) {
							throw new IOException("Bad command: " + b20);
						}

						if(socketConnection18.readBuffer.remaining() < packet24.size + 1) {
							socketConnection18.readBuffer.compact();
							break;
						}

						socketConnection18.readBuffer.get();
						Object[] object21 = new Object[packet24.fields.length];

						for(int i7 = 0; i7 < object21.length; ++i7) {
							object21[i7] = socketConnection18.read(packet24.fields[i7]);
						}

						socketConnection18.client.readPacket(packet24, object21);
						if(!socketConnection18.connected) {
							break;
						}

						socketConnection18.readBuffer.compact();
					}

					if(socketConnection18.writeBuffer.position() > 0) {
						socketConnection18.writeBuffer.flip();
						socketConnection18.socketChannel.write(socketConnection18.writeBuffer);
						socketConnection18.writeBuffer.compact();
					}
				} catch (Exception exception9) {
					MinecraftServer minecraftServer10001 = socketServer13.server;
					Client client23;
					if((client23 = (Client)socketServer13.server.connections.get(socketConnection15)) != null) {
						client23.handleException(exception9);
					}
				}

				try {
					if(!socketConnection15.connected) {
						socketConnection15.disconnect();
						socketServer13.server.disconnect(socketConnection15);
						socketServer13.connections.remove(i17--);
					}
				} catch (Exception exception8) {
					exception8.printStackTrace();
				}
			}

		} catch (IOException iOException11) {
			throw new RuntimeException("IOException while ticking socketserver", iOException11);
		}
	}

	public final void command(Client conn, String cmd) {
		while(cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}

		logger.info((conn == null ? "[console]" : conn.name) + " admins: " + cmd);
		String[] string3;
		if((string3 = cmd.split(" "))[0].toLowerCase().equals("ban") && string3.length > 1) {
			this.ban(string3[1]);
		} else if(string3[0].toLowerCase().equals("kick") && string3.length > 1) {
			this.kick(string3[1]);
		} else if(string3[0].toLowerCase().equals("banip") && string3.length > 1) {
			this.banip(string3[1]);
		} else if(string3[0].toLowerCase().equals("unban") && string3.length > 1) {
			String conn1 = string3[1];
			this.banned.remove(conn1);
		} else if(string3[0].toLowerCase().equals("op") && string3.length > 1) {
			this.op(string3[1]);
		} else if(string3[0].toLowerCase().equals("deop") && string3.length > 1) {
			this.deop(string3[1]);
		} else if(string3[0].toLowerCase().equals("setspawn")) {
			if(conn != null) {
				this.level.setSpawnPos(conn.x / 32, conn.y / 32, conn.z / 32, (float)(conn.xRot * 320 / 256));
			} else {
				logger.info("Can\'t set spawn from console!");
			}
		} else {
			if(string3[0].toLowerCase().equals("solid")) {
				if(conn != null) {
					conn.placeUnbreakable = !conn.placeUnbreakable;
					if(conn.placeUnbreakable) {
						conn.sendChatMessage("Now placing unbreakable stone");
						return;
					}

					conn.sendChatMessage("Now placing normal stone");
					return;
				}
			} else {
				if(string3[0].toLowerCase().equals("broadcast") && string3.length > 1) {
					this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, cmd.substring("broadcast ".length()).trim()});
					return;
				}

				if(string3[0].toLowerCase().equals("say") && string3.length > 1) {
					this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, cmd.substring("say ".length()).trim()});
					return;
				}

				if((string3[0].toLowerCase().equals("teleport") || string3[0].toLowerCase().equals("tp")) && string3.length > 1) {
					if(conn == null) {
						logger.info("Can\'t teleport from console!");
						return;
					}

					Client client4;
					if((client4 = this.getClient(string3[1])) == null) {
						conn.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, "No such player"});
						return;
					}

					conn.connection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, client4.x, client4.y, client4.z, client4.xRot, client4.yRot});
				} else if(conn != null) {
					conn.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, "Unknown command!"});
				}
			}

		}
	}

	public final void netSetTile(int x, int y, int z) {
		this.sendPacket(Packet.SET_TILE, new Object[]{x, y, z, this.level.getTile(x, y, z)});
	}

	public final int getPlayerSlots() {
		int i1 = 0;

		for(int i2 = 0; i2 < this.maxPlayers; ++i2) {
			if(this.playerClients[i2] == null) {
				++i1;
			}
		}

		return i1;
	}

	private int getFreePlayerSlots() {
		for(int i1 = 0; i1 < this.maxPlayers; ++i1) {
			if(this.playerClients[i1] == null) {
				return i1;
			}
		}

		return -1;
	}

	public final List getClients() {
		return this.clients;
	}

	private void kick(String name) {
		boolean z2 = false;
		Iterator iterator3 = this.clients.iterator();

		while(iterator3.hasNext()) {
			Client client4;
			if((client4 = (Client)iterator3.next()).name.equalsIgnoreCase(name)) {
				z2 = true;
				client4.kickPlayer("You were kicked");
			}
		}

		if(z2) {
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, name + " got kicked from the server!"});
		}

	}

	private void ban(String name) {
		this.banned.add(name);
		boolean z2 = false;
		Iterator iterator3 = this.clients.iterator();

		while(iterator3.hasNext()) {
			Client client4;
			if((client4 = (Client)iterator3.next()).name.equalsIgnoreCase(name)) {
				z2 = true;
				client4.kickPlayer("You were banned");
			}
		}

		if(z2) {
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, name + " got banned!"});
		}

	}

	private void op(String name) {
		this.admins.add(name);
		Iterator iterator3 = this.clients.iterator();

		while(iterator3.hasNext()) {
			Client client2;
			if((client2 = (Client)iterator3.next()).name.equalsIgnoreCase(name)) {
				client2.sendChatMessage("You\'re now op!");
				client2.sendPacket(Packet.USER_TYPE, new Object[]{100});
			}
		}

	}

	private void deop(String name) {
		this.admins.remove(name);
		Iterator iterator3 = this.clients.iterator();

		while(iterator3.hasNext()) {
			Client client2;
			if((client2 = (Client)iterator3.next()).name.equalsIgnoreCase(name)) {
				client2.placeUnbreakable = false;
				client2.sendChatMessage("You\'re no longer op!");
				client2.sendPacket(Packet.USER_TYPE, new Object[]{0});
			}
		}

	}

	private void banip(String ip) {
		boolean z2 = false;
		String string3 = "";
		Iterator iterator4 = this.clients.iterator();

		while(true) {
			Client client5;
			SocketConnection socketConnection6;
			do {
				if(!iterator4.hasNext()) {
					if(z2) {
						this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, string3 + " got ip banned!"});
					}

					return;
				}

				if((client5 = (Client)iterator4.next()).name.equalsIgnoreCase(ip)) {
					break;
				}

				socketConnection6 = client5.connection;
				if(client5.connection.ip.equalsIgnoreCase(ip)) {
					break;
				}

				socketConnection6 = client5.connection;
			} while(!client5.connection.ip.equalsIgnoreCase("/" + ip));

			socketConnection6 = client5.connection;
			this.bannedIP.add(client5.connection.ip);
			client5.kickPlayer("You were banned");
			if(string3 == "") {
				string3 = string3 + ", ";
			}

			string3 = string3 + client5.name;
			z2 = true;
		}
	}

	public final Client getClient(String name) {
		Iterator iterator3 = this.clients.iterator();

		Client client2;
		do {
			if(!iterator3.hasNext()) {
				return null;
			}
		} while(!(client2 = (Client)iterator3.next()).name.equalsIgnoreCase(name));

		return client2;
	}

	public static void main(String[] args) {
		try {
			MinecraftServer args1;
			MinecraftServer minecraftServer1 = args1 = new MinecraftServer();
			logger.info("Setting up");
			File file2;
			if((file2 = new File("server_level.dat")).exists()) {
				try {
					minecraftServer1.level = (new LevelIO(minecraftServer1)).load(new FileInputStream(file2));
				} catch (Exception exception4) {
					logger.warning("Failed to load level. Generating a new level");
					exception4.printStackTrace();
				}
			} else {
				logger.warning("No level file found. Generating a new level");
			}

			if(minecraftServer1.level == null) {
				minecraftServer1.level = (new LevelGen(minecraftServer1)).generateLevel("--", 256, 256, 64);
			}

			try {
				new LevelIO(minecraftServer1);
				LevelIO.save(minecraftServer1.level, new FileOutputStream("server_level.dat"));
			} catch (Exception exception3) {
			}

			minecraftServer1.level.creativeMode = true;
			minecraftServer1.level.growTrees = minecraftServer1.growTrees;
			minecraftServer1.level.addListener(minecraftServer1);
			(new Thread(args1)).start();
		} catch (Exception exception5) {
			logger.severe("Failed to start the server!");
			exception5.printStackTrace();
		}
	}

	static List a(MinecraftServer minecraftServer0) {
		return minecraftServer0.commandQueue;
	}

	static String b(MinecraftServer minecraftServer0) {
		return minecraftServer0.serverURL;
	}

	static String a(MinecraftServer minecraftServer0, String string1) {
		return minecraftServer0.serverURL = string1;
	}

	static {
		ServerLogFormatter serverLogFormatter0 = new ServerLogFormatter();
		Handler[] handler1;
		int i2 = (handler1 = logger.getParent().getHandlers()).length;

		for(int i3 = 0; i3 < i2; ++i3) {
			Handler handler4 = handler1[i3];
			logger.getParent().removeHandler(handler4);
		}

		ConsoleHandler consoleHandler6;
		(consoleHandler6 = new ConsoleHandler()).setFormatter(serverLogFormatter0);
		logger.addHandler(consoleHandler6);

		try {
			ServerLog serverLog7;
			(serverLog7 = new ServerLog(new FileOutputStream("server.log"), serverLogFormatter0)).setFormatter(serverLogFormatter0);
			logger.addHandler(serverLog7);
		} catch (Exception exception5) {
			logger.warning("Failed to open file server.log for writing: " + exception5);
		}
	}
}