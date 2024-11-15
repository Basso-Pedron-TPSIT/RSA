package com.itts.volterra.quinta.BassoPedron.RSA;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Matteo Basso, Lorenzo Pedron
 */
public class App {
	private static final Logger logger = LogManager.getLogger(App.class);
	public static void main(String[] args) {
		RSA rsa = new RSA(2048);
		Scanner input = new Scanner (System.in);
		String message = "";
		System.out.print("Inserisci la Stringa da criptare: ");
		message = input.nextLine();
		
		String encrypted = rsa.encrypt(message);
		logger.info("Stringa criptata: " + encrypted);
		String decrypted = rsa.decrypt(encrypted);
		logger.info("Stringa decriptata: " + decrypted);
	
	}
}
