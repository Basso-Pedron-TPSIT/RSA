package com.itts.volterra.quinta.BassoPedron.RSA;

import java.util.ArrayList;

/**
 * Classe Tools
 * Contiene metodi utili
 * 
 * @author Matteo Basso, Lorenzo Pedron
 */
public class Tools {
	/**
	 * Divide la stringa in sottostringhe della lunghezza specificata
	 * 
	 * @param string la stringa da dividere
	 * @param numberOfChars il massimo numero di caratteri che deve avere avere la sottostringa
	 * @return l'ArrayList contenente le sottostringhe
	 */
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
