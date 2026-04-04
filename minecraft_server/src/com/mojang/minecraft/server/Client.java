package com.mojang.minecraft.server;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.User;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import util.Mth;

public final class Client {
	private static Logger logger = MinecraftServer.logger;
	public final SocketConnection connection;
	private final MinecraftServer server;
	private boolean onlyIP = false;
	private boolean sendingPackets = false;
	public String name = "";
	public final int port;
	private ArrayList packets = new ArrayList();
	private long currentTime;
	private List packetData = new ArrayList();
	private int messageCounter = 0;
	public int x;
	public int y;
	public int z;
	public int yRot;
	public int xRot;
	private boolean ignorePackets = false;
	private int tickCounter = 0;
	private int ticks = 0;
	private volatile byte[] blocks = null;
	public boolean placeUnbreakable = false;

	public Client(MinecraftServer server, SocketConnection connection, int port) {
		this.server = server;
		this.connection = connection;
		this.port = port;
		this.currentTime = System.currentTimeMillis();
		connection.client = this;
		Level server1 = server.level;
		this.x = (server1.xSpawn << 5) + 16;
		this.y = (server1.ySpawn << 5) + 16;
		this.z = (server1.zSpawn << 5) + 16;
		this.xRot = (int)(server1.rotSpawn * 256.0F / 360.0F);
		this.yRot = 0;
	}

	public final String toString() {
		SocketConnection socketConnection1;
		return !this.onlyIP ? (socketConnection1 = this.connection).ip : this.name + " (" + (socketConnection1 = this.connection).ip + ")";
	}

	public final void readPacket(Packet packet, Object[] data) {
		if(!this.ignorePackets) {
			if(packet != Packet.LOGIN) {
				if(packet != Packet.KEEP_ALIVE) {
					if(this.onlyIP && this.sendingPackets) {
						if(packet == Packet.PLACE_OR_REMOVE_TILE) {
							if(this.packetData.size() > 1200) {
								this.kickCheat("Too much lag");
							} else {
								this.packetData.add(data);
							}
						} else if(packet == Packet.CHAT_MESSAGE) {
							String string7;
							if((string7 = data[1].toString().trim()).length() > 0) {
								this.receiveChatMessage(string7);
							}

						} else {
							if(packet == Packet.PLAYER_TELEPORT) {
								if(this.packetData.size() > 1200) {
									this.kickCheat("Too much lag");
									return;
								}

								this.packetData.add(data);
							}

						}
					}
				}
			} else {
				byte b6 = ((Byte)data[0]).byteValue();
				String string3 = ((String)data[1]).trim();
				String string8 = (String)data[2];
				char[] c4 = string3.toCharArray();

				for(int i5 = 0; i5 < c4.length; ++i5) {
					if(c4[i5] < 32 || c4[i5] > 127) {
						this.kickCheat("Bad name!");
						return;
					}
				}

				if(string3.length() < 2 || string3.length() > 16) {
					this.kickPlayer("Illegal name.");
				}

				if(this.server.verifyNames && !string8.equals(this.server.md5Hash.verify(string3))) {
					this.kickPlayer("The name wasn\'t verified by minecraft.net!");
				} else if(b6 != 7) {
					this.kickPlayer("Wrong protocol version.");
				} else if(this.server.banned.contains(string3)) {
					this.kickPlayer("You\'re banned!");
				} else if(this.server.adminSlot && !this.server.admins.contains(string3) && this.server.getPlayerSlots() < 1) {
					this.connection.sendPacket(Packet.KICK_PLAYER, new Object[]{"The server is full!"});
					logger.info(string3 + " connected, but got kicked because the server was almost full and there are reserved admin slots.");
					this.server.connect(this);
					this.ignorePackets = true;
				} else {
					Client client11;
					if((client11 = this.server.getClient(string3)) != null) {
						client11.kickPlayer("You logged in from another computer.");
					}

					logger.info(this + " logged in as " + string3);
					this.onlyIP = true;
					this.name = string3;
					this.connection.sendPacket(Packet.LOGIN, new Object[]{(byte)7, this.server.serverName, this.server.motd, this.server.admins.contains(string3) ? 100 : 0});
					Level level9 = this.server.level;
					byte[] b10 = level9.copyBlocks();
					(new LevelCopyThread(this, b10)).start();
					this.server.players.add(string3);
				}
			}
		}
	}

	private void receiveChatMessage(String msg) {
		msg = msg.trim();
		this.messageCounter += msg.length() + 15 << 2;
		if(this.messageCounter > 600) {
			this.messageCounter = 760;
			this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, "Too much chatter! Muted for eight seconds."});
			logger.info("Muting " + this.name + " for chatting too much");
		} else {
			char[] c2 = msg.toCharArray();

			for(int i3 = 0; i3 < c2.length; ++i3) {
				if(c2[i3] < 32 || c2[i3] > 127) {
					this.kickCheat("Bad chat message!");
					return;
				}
			}

			if(msg.startsWith("/")) {
				if(this.server.admins.contains(this.name)) {
					this.server.command(this, msg.substring(1));
				} else {
					this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, "You\'re not a server admin!"});
				}
			} else {
				logger.info(this.name + " says: " + msg);
				this.server.sendPacket(Packet.CHAT_MESSAGE, new Object[]{this.port, this.name + ": " + msg});
			}
		}
	}

	public final void kickPlayer(String str) {
		this.connection.sendPacket(Packet.KICK_PLAYER, new Object[]{str});
		logger.info("Kicking " + this + ": " + str);
		this.server.connect(this);
		this.ignorePackets = true;
	}

	private void kickCheat(String str) {
		this.kickPlayer("Cheat detected: " + str);
	}

	public final void sendChatMessage(String str) {
		this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, str});
	}

	public final void setBlocks(byte[] blocks) {
		this.blocks = blocks;
	}

	public final void tick() {
		if(this.tickCounter >= 2) {
			this.tickCounter -= 2;
		}

		if(this.messageCounter > 0) {
			--this.messageCounter;
			if(this.messageCounter == 600) {
				this.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, "You can now talk again."});
				this.messageCounter = 300;
			}
		}

		Object[] object2;
		boolean z25;
		if(this.packetData.size() > 0) {
			for(boolean z1 = true; this.packetData.size() > 0 && z1; z1 = z25) {
				short s3;
				short s4;
				byte b5;
				byte b6;
				short s13;
				short s10001;
				short s10002;
				short s10003;
				byte b10004;
				if((object2 = (Object[])this.packetData.remove(0))[0] instanceof Short) {
					s10001 = ((Short)object2[0]).shortValue();
					s10002 = ((Short)object2[1]).shortValue();
					s10003 = ((Short)object2[2]).shortValue();
					b10004 = ((Byte)object2[3]).byteValue();
					b6 = ((Byte)object2[4]).byteValue();
					b5 = b10004;
					s4 = s10003;
					s3 = s10002;
					s13 = s10001;
					++this.tickCounter;
					if(this.tickCounter == 100) {
						this.kickCheat("Too much clicking!");
					} else {
						Level level21 = this.server.level;
						float f22 = (float)s13 - (float)this.x / 32.0F;
						float f23 = (float)s3 - ((float)this.y / 32.0F - 1.62F);
						float f24 = (float)s4 - (float)this.z / 32.0F;
						f22 = f22 * f22 + f23 * f23 + f24 * f24;
						f23 = 8.0F;
						if(f22 >= f23 * f23) {
							System.out.println("Distance: " + Mth.sqrt(f22));
							this.kickCheat("Distance");
						} else if(!User.creativeTiles.contains(Tile.tiles[b6])) {
							this.kickCheat("Tile type");
						} else if(s13 >= 0 && s3 >= 0 && s4 >= 0 && s13 < level21.width && s3 < level21.depth && s4 < level21.height) {
							if(b5 == 0) {
								if(level21.getTile(s13, s3, s4) != Tile.unbreakable.id || this.server.admins.contains(this.name)) {
									level21.setTile(s13, s3, s4, 0);
								}
							} else {
								Tile tile18;
								if((tile18 = Tile.tiles[level21.getTile(s13, s3, s4)]) == null || tile18 == Tile.water || tile18 == Tile.calmWater || tile18 == Tile.lava || tile18 == Tile.calmLava) {
									if(this.placeUnbreakable && b6 == Tile.rock.id) {
										level21.setTile(s13, s3, s4, Tile.unbreakable.id);
									} else {
										level21.setTile(s13, s3, s4, b6);
									}

									Tile.tiles[b6].onPlace(level21, s13, s3, s4);
								}
							}
						}
					}

					z25 = true;
				} else {
					((Byte)object2[0]).byteValue();
					s10001 = ((Short)object2[1]).shortValue();
					s10002 = ((Short)object2[2]).shortValue();
					s10003 = ((Short)object2[3]).shortValue();
					b10004 = ((Byte)object2[4]).byteValue();
					b6 = ((Byte)object2[5]).byteValue();
					b5 = b10004;
					s4 = s10003;
					s3 = s10002;
					s13 = s10001;
					if(s13 == this.x && s3 == this.y && s4 == this.z && b5 == this.xRot && b6 == this.yRot) {
						z25 = true;
					} else {
						boolean z7 = s13 == this.x && s3 == this.y && s4 == this.z;
						if(this.ticks++ % 2 == 0) {
							int i8 = s13 - this.x;
							int i9 = s3 - this.y;
							int i10 = s4 - this.z;
							if(i8 >= 128 || i8 < -128 || i9 >= 128 || i9 < -128 || i10 >= 128 || i10 < -128 || this.ticks % 20 <= 1) {
								this.x = s13;
								this.y = s3;
								this.z = s4;
								this.xRot = b5;
								this.yRot = b6;
								this.server.sendPlayerPacket(this, Packet.PLAYER_TELEPORT, new Object[]{this.port, s13, s3, s4, b5, b6});
								z25 = false;
								continue;
							}

							if(s13 == this.x && s3 == this.y && s4 == this.z) {
								this.xRot = b5;
								this.yRot = b6;
								this.server.sendPlayerPacket(this, Packet.PLAYER_ROTATE, new Object[]{this.port, b5, b6});
							} else if(b5 == this.xRot && b6 == this.yRot) {
								this.x = s13;
								this.y = s3;
								this.z = s4;
								this.server.sendPlayerPacket(this, Packet.PLAYER_MOVE, new Object[]{this.port, i8, i9, i10});
							} else {
								this.x = s13;
								this.y = s3;
								this.z = s4;
								this.xRot = b5;
								this.yRot = b6;
								this.server.sendPlayerPacket(this, Packet.PLAYER_MOVE_AND_ROTATE, new Object[]{this.port, i8, i9, i10, b5, b6});
							}
						}

						z25 = z7;
					}
				}
			}
		}

		if(!this.onlyIP && System.currentTimeMillis() - this.currentTime > 5000L) {
			this.kickPlayer("You need to log in!");
		} else if(this.blocks != null) {
			Level level11 = this.server.level;
			byte[] b15 = new byte[1024];
			int i16 = 0;
			int i17 = this.blocks.length;
			this.connection.sendPacket(Packet.LEVEL_INITIALIZE, new Object[0]);

			int i19;
			while(i17 > 0) {
				i19 = i17;
				if(i17 > b15.length) {
					i19 = b15.length;
				}

				System.arraycopy(this.blocks, i16, b15, 0, i19);
				this.connection.sendPacket(Packet.LEVEL_DATA, new Object[]{i19, b15, (i16 + i19) * 100 / this.blocks.length});
				i17 -= i19;
				i16 += i19;
			}

			this.connection.sendPacket(Packet.LEVEL_FINALIZE, new Object[]{level11.width, level11.depth, level11.height});
			this.connection.sendPacket(Packet.PLAYER_JOIN, new Object[]{-1, this.name, this.x, this.y, this.z, this.xRot, this.yRot});
			this.server.sendPlayerPacket(this, Packet.PLAYER_JOIN, new Object[]{this.port, this.name, (level11.xSpawn << 5) + 16, (level11.ySpawn << 5) + 16, (level11.zSpawn << 5) + 16, (int)(level11.rotSpawn * 256.0F / 360.0F), 0});
			this.server.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, this.name + " joined the game"});
			Iterator iterator20 = this.server.getClients().iterator();

			while(iterator20.hasNext()) {
				Client client12;
				if((client12 = (Client)iterator20.next()) != null && client12 != this && client12.onlyIP) {
					this.connection.sendPacket(Packet.PLAYER_JOIN, new Object[]{client12.port, client12.name, client12.x, client12.y, client12.z, client12.xRot, client12.yRot});
				}
			}

			this.sendingPackets = true;
			i19 = 0;

			while(i19 < this.packets.size()) {
				Packet packet14 = (Packet)this.packets.get(i19++);
				object2 = (Object[])((Object[])this.packets.get(i19++));
				this.sendPacket(packet14, object2);
			}

			this.packets = null;
			this.blocks = null;
		}
	}

	public final void sendPacket(Packet packet, Object... data) {
		if(!this.sendingPackets) {
			this.packets.add(packet);
			this.packets.add(data);
		} else {
			this.connection.sendPacket(packet, data);
		}
	}

	public final void handleException(Exception e) {
		if(e instanceof IOException) {
			logger.info(this + " lost connection suddenly. (" + e + ")");
		} else {
			logger.warning(this + ":" + e);
			logger.log(java.util.logging.Level.WARNING, "Exception handling " + this + "!", e);
			e.printStackTrace();
		}

		this.server.sendPlayerPacket(this, Packet.CHAT_MESSAGE, new Object[]{-1, this.name + " left the game"});
		MinecraftServer.disconnect(this);
	}
}