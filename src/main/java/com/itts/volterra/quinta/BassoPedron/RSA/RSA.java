package com.itts.volterra.quinta.BassoPedron.RSA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
	private static final Logger logger = LogManager.getLogger(RSA.class);
	
	public RSA(int bitLength) {
		SecureRandom random = new SecureRandom();
		// Genera due numeri primi casuali, entrambi maggiori di almeno 300 bit
        BigInteger p = generatePrime(random, 300);
        BigInteger q = generatePrime(random, 300);

        // Calcolo n 
        BigInteger n = p.multiply(q);
        
        BigInteger P = p.subtract(BigInteger.ONE);
        BigInteger Q = q.subtract(BigInteger.ONE);
        // Calcolo z
        BigInteger z = P.multiply(Q);
        
        // Calcolo la chiave pubblica
        BigInteger e = generateCoprime(z);
        
        // mostra i risultati nel log
        logger.info("Numeri primi generati:");
        logger.info("p: " + p);
        logger.info("q: " + q);
        logger.info("n = p * q: " + n);
        logger.info("z = (p - 1) * (q - 1): " + z);
        logger.info("ESPONENTE PUBBLICO");
        logger.info("Numero coprimo generato (e): " + e);
		
	}
    // Funzione per generare un numero primo maggiore di almeno bitLength bit 
    public BigInteger generatePrime(SecureRandom random, int bitLength) {
        BigInteger prime;
        
        do {
            prime = new BigInteger(bitLength, random);
        } while (!prime.isProbablePrime(100)); // finché non troviamo un numero primo

        return prime;
    }
	    
	    
    // Funzione per calcolare il massimo comun divisore (gcd)
    public BigInteger gcd(BigInteger p, BigInteger q) {
        return p.gcd(q); // metodo della classe BigInteger
    }
	    
    // Funzione per generare un numero coprimo e con z
    public BigInteger generateCoprime(BigInteger z) {
        SecureRandom random = new SecureRandom();
        BigInteger e;

        do {
            e = new BigInteger(z.bitLength(), random);
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(z) >= 0 || gcd(e, z).compareTo(BigInteger.ONE) != 0);

        return e; // Restituisce il numero coprimo
    }

	    public static void main(String[] args) {
	        try {
	           
	            

	        } catch (Exception e) {
	            logger.error("Errore durante la generazione dei numeri primi!", e);
	        }
	    }
}
