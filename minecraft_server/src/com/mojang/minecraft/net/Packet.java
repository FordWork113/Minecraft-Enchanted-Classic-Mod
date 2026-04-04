package com.mojang.minecraft.net;

public final class Packet {
	public static final Packet[] PACKETS = new Packet[256];
	public static final Packet LOGIN = new Packet(new Class[]{Byte.TYPE, String.class, String.class, Byte.TYPE});
	public static final Packet KEEP_ALIVE = new Packet(new Class[0]);
	public static final Packet LEVEL_INITIALIZE = new Packet(new Class[0]);
	public static final Packet LEVEL_DATA = new Packet(new Class[]{Short.TYPE, byte[].class, Byte.TYPE});
	public static final Packet LEVEL_FINALIZE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE});
	public static final Packet PLACE_OR_REMOVE_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet SET_TILE = new Packet(new Class[]{Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE});
	public static final Packet PLAYER_JOIN = new Packet(new Class[]{Byte.TYPE, String.class, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_TELEPORT = new Packet(new Class[]{Byte.TYPE, Short.TYPE, Short.TYPE, Short.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE_AND_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_MOVE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_ROTATE = new Packet(new Class[]{Byte.TYPE, Byte.TYPE, Byte.TYPE});
	public static final Packet PLAYER_DISCONNECT = new Packet(new Class[]{Byte.TYPE});
	public static final Packet CHAT_MESSAGE = new Packet(new Class[]{Byte.TYPE, String.class});
	public static final Packet KICK_PLAYER = new Packet(new Class[]{String.class});
	public static final Packet USER_TYPE = new Packet(new Class[]{Byte.TYPE});
	public final int size;
	private static int nextId = 0;
	public final byte id = (byte)(nextId++);
	public Class[] fields;

	private Packet(Class... data) {
		PACKETS[this.id] = this;
		this.fields = new Class[data.length];
		int i2 = 0;

		for(int i3 = 0; i3 < data.length; ++i3) {
			Class class4 = data[i3];
			this.fields[i3] = class4;
			if(class4 == Long.TYPE) {
				i2 += 8;
			} else if(class4 == Integer.TYPE) {
				i2 += 4;
			} else if(class4 == Short.TYPE) {
				i2 += 2;
			} else if(class4 == Byte.TYPE) {
				++i2;
			} else if(class4 == Float.TYPE) {
				i2 += 4;
			} else if(class4 == Double.TYPE) {
				i2 += 8;
			} else if(class4 == byte[].class) {
				i2 += 1024;
			} else if(class4 == String.class) {
				i2 += 64;
			}
		}

		this.size = i2;
	}
}