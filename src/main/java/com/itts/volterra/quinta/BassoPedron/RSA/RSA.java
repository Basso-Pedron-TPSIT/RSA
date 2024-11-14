package com.itts.volterra.quinta.BassoPedron.RSA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
	  private static final Logger logger = LogManager.getLogger(RSA.class);

	    // Funzione per generare un numero primo maggiore di una certa lunghezza in bit
	    public static BigInteger generatePrime(SecureRandom random, int minLength) {
	        BigInteger prime;
	        
	        // Genera numeri finché non troviamo un numero primo
	        do {
	            // Genera un numero casuale di almeno minLength bit
	            prime = new BigInteger(minLength, random);
	        } while (!prime.isProbablePrime(100)); 

	        return prime;
	    }

	    public static void main(String[] args) {
	        try {
	           
	            SecureRandom random = new SecureRandom();

	            // Genera due numeri primi casuali, entrambi maggiori di almeno 300 bit
	            BigInteger p = generatePrime(random, 300);
	            BigInteger q = generatePrime(random, 300);

	            // Calcola n 
	            BigInteger n = p.multiply(q);
	            
	            BigInteger P = p.subtract(BigInteger.ONE);
	            BigInteger Q = q.subtract(BigInteger.ONE);
	            
	            BigInteger z = P.multiply(Q);

	            // mostra i risultati nel log
	            logger.info("Numeri primi generati:");
	            logger.info("p: " + p);
	            logger.info("q: " + q);
	            logger.info("n = p * q: " + n);
	            logger.info("z = (p - 1) * (q - 1): " + z);
	        } catch (Exception e) {
	            logger.error("Errore durante la generazione dei numeri primi.", e);
	        }
	    }
}
