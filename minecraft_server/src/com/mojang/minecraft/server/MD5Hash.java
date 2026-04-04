package com.mojang.minecraft.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Hash {
	private String salt;

	public MD5Hash(String slt) {
		this.salt = slt;
	}

	public final String verify(String slt) {
		try {
			String string3 = this.salt + slt;
			MessageDigest slt1;
			(slt1 = MessageDigest.getInstance("MD5")).update(string3.getBytes(), 0, string3.length());
			return (new BigInteger(1, slt1.digest())).toString(16);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException2) {
			throw new RuntimeException(noSuchAlgorithmException2);
		}
	}
}