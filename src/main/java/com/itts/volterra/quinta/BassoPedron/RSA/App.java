package com.itts.volterra.quinta.BassoPedron.RSA;

import java.math.BigInteger;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
	private static final Logger logger = LogManager.getLogger(App.class);
	public static void main(String[] args) {
		RSA rsa = new RSA(2048);
		Scanner input = new Scanner (System.in);
		String message = "";
		System.out.println ("Inserisci la Stringa da criptare: ");
		message = input.nextLine();
		System.out.println(message.length());
		
		String encrypted = rsa.encrypt(message);
		System.out.println("Stringa criptata: " + encrypted);
		String decrypted = rsa.decrypt(encrypted);
		System.out.println("Stringa decriptata: " + decrypted);
	
	}
}
