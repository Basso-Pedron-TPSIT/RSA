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
	
	private final int l; // lunghezza del blocco di caratteri da criptare
	private BigInteger n, d, e, z; // (modulo n, esponente privato, esponente pubblico, funzione di eulero)
	public RSA(int bitLength) {
		// Genera due numeri primi casuali, entrambi maggiori di almeno bitLength bit
        BigInteger p = generatePrime(bitLength);
        BigInteger q = generatePrime(bitLength);

        // Calcolo n 
        n = p.multiply(q);
        
        l = n.bitLength() / 8;
        
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
    	ArrayList<String> parts = Tools.splitStringInSubStringOfNchars(messageToCrypt, l);
    	String[] cryptedParts = new String[parts.size()];
    	Thread[] threads = new Thread[parts.size()];
    	int partNum = 0;
    	for(String part : parts) {
    		final int threadNum = partNum;
    		threads[threadNum] = new Thread(new Runnable() {
    			@Override
    			public void run() {
    				BigInteger crypted = new BigInteger(part.getBytes()).modPow(e, n);
    				cryptedParts[threadNum]  = crypted.toString() + ";";
    			}
    		});
    		threads[threadNum].start();
    		partNum++;
    	}
    	
    	for(int i = 0; i < threads.length; i++) {
    		try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		sb.append(cryptedParts[i]);
    	}
    	
    	logger.info("Encrypted");
    	return sb.toString();
    }
    
    public String decrypt(String cryptedMessage) {
    	StringBuilder sb = new StringBuilder();
    	String[] parts = cryptedMessage.split(";");
    	String[] decryptedParts = new String[parts.length];
    	Thread[] threads = new Thread[parts.length];
    	
    	int partNum = 0;
    	for(String part : parts) {
    		if(!part.isEmpty()) {
    			final int threadNum = partNum;
        		threads[threadNum] = new Thread(new Runnable() {
        			@Override
        			public void run() {
        				BigInteger decrypted = new BigInteger(part).modPow(d, n);
                		decryptedParts[threadNum] = new String(decrypted.toByteArray(), StandardCharsets.UTF_8);
        			}
        		});
        		threads[threadNum].start();
        		partNum++;
    		}
    	}
    	
    	for(int i = 0; i < threads.length; i++) {
    		if(threads[i] != null) {
    			try {
    				threads[i].join();
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		sb.append(decryptedParts[i]);
    		}
    	}
    	/*
    	for(String part : parts) {
    		if(!part.isEmpty()) {
    			BigInteger decrypted = new BigInteger(part).modPow(d, n);
        		sb.append(new String(decrypted.toByteArray(), StandardCharsets.UTF_8));
    		}
    	}*/
    	
    	return sb.toString();
    }
}
