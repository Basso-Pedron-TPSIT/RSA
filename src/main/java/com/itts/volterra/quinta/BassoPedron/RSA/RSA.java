package com.itts.volterra.quinta.BassoPedron.RSA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

public class RSA {
	private static final Logger logger = LogManager.getLogger(RSA.class);
	private final int MAX_STRING_BYTES;
	private BigInteger n, d, e, z; // (modulo n, esponente privato, esponente pubblico, funzione di eulero)
	public RSA(int bitLength) {
		int byteOfRSA = (bitLength / 8);
		MAX_STRING_BYTES = byteOfRSA;
		System.err.println(MAX_STRING_BYTES);
		// Genera due numeri primi casuali, entrambi maggiori di almeno bitLength bit
        BigInteger p = generatePrime(bitLength);
        BigInteger q = generatePrime(bitLength);

        // Calcolo n 
        n = p.multiply(q);
        
        BigInteger P = p.subtract(BigInteger.ONE);
        BigInteger Q = q.subtract(BigInteger.ONE);
        // Calcolo funzione di eulero
        z = P.multiply(Q);
        
        // Calcolo l'esponente pubblico
        e = generateCoprime(z);
        
        d = calculatePrivateExponent(e, z);
        
        // mostra i risultati nel log
        logger.info("Numeri primi generati:");
        logger.info("p: " + p);
        logger.info("q: " + q);
        logger.info("n = p * q: " + n);
        logger.info("z = (p - 1) * (q - 1): " + z);
        logger.info("ESPONENTE PUBBLICO");
        logger.info("Numero coprimo generato (e): " + e);
        logger.info("ESPONENTE PRIVATO");
        logger.info("Numero d tale che il suo prodotto con e sia congruo a 1 mod (z) : " + d);
        
		
	}
    // Funzione per generare un numero primo maggiore di almeno bitLength bit 
    public BigInteger generatePrime(int bitLength) {
    	SecureRandom random = new SecureRandom();
        BigInteger prime;
        
        do {
            prime = new BigInteger(bitLength, random);
        } while (!prime.isProbablePrime(100)); // finché non troviamo un numero primo

        return prime;
    }
	    
    // Funzione per generare un numero coprimo e con z
    public BigInteger generateCoprime(BigInteger z) {
        SecureRandom random = new SecureRandom();
        BigInteger e;

        do {
            e = new BigInteger(z.bitLength(), random);
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(z) >= 0 || e.gcd(z).compareTo(BigInteger.ONE) != 0);

        return e; // Restituisce il numero coprimo
    }
    
    public BigInteger calculatePrivateExponent(BigInteger e, BigInteger eulerFunction) {
    	return e.modInverse(eulerFunction);
    }
    
    public String encrypt(String messageToCrypt) {
    	StringBuilder sb = new StringBuilder();
    	for(byte part : messageToCrypt.getBytes()) {
    		BigInteger crypted = BigInteger.valueOf(part).modPow(e, n);
    		sb.append(crypted.toString());
    		sb.append(";");
    	}
    	
    	return sb.toString();
    }
    
    public String decrypt(String cryptedMessage) {
    	StringBuilder sb = new StringBuilder();
    	String[] parts = cryptedMessage.split(";");
    	for(String part : parts) {
    		if(!part.isEmpty()) {
    			BigInteger decrypted = new BigInteger(part).modPow(d, n);
        		sb.append(new String(decrypted.toByteArray(), StandardCharsets.UTF_8));
    		}
    	}
    	
    	return sb.toString();
    }
}
