package com.itts.volterra.quinta.BassoPedron.RSA;

import java.util.ArrayList;

public class Tools {
	public static ArrayList<String> splitStringInSubStringOfNchars(String string, int numberOfChars) {
		ArrayList<String> splitted = new ArrayList<String>();
		int startIndex = 0;
		while(startIndex < string.length()) {
			int endIndex = Math.min(startIndex + numberOfChars, string.length());
			splitted.add(string.substring(startIndex, endIndex));
			startIndex = endIndex;
		}
		return splitted;
	}
}
