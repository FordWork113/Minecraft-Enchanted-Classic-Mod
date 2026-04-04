package com.mojang.minecraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AcceptableChars {
	public static final String acceptableLetters = readAcceptableChars();

	   private static String readAcceptableChars() {
	      String var0 = "";

	      try {
	         BufferedReader var1 = new BufferedReader(new InputStreamReader(AcceptableChars.class.getResourceAsStream("/font.txt"), "UTF-8"));
	         String var2 = "";

	         while((var2 = var1.readLine()) != null) {
	            if (!var2.startsWith("#")) {
	               var0 = var0 + var2;
	            }
	         }

	         var1.close();
	      } catch (Exception var3) {
	      }

	      return var0;
	   }
}
