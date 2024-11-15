package com.itts.volterra.quinta.BassoPedron.RSA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Classe RSA
 * Cripta e decripta una stringa
 * 
 * @author Matteo Basso, Lorenzo Pedron
 * @version 1.0.0
 */
public class RSA {
	private static final Logger logger = LogManager.getLogger(RSA.class);
	
	private final int l; // lunghezza del blocco di caratteri da criptare
	private BigInteger n, d, e, z; // (modulo n, esponente privato, esponente pubblico, funzione di eulero)
	
	/**
	 * Costruttore che genera i due numeri primi, chiave primaria e chiave pubblica
	 * 
	 * @param bitLength il numero di bit dei numeri primi da generare
	 */
	public RSA(int bitLength) {
		logger.info("Inizializzazione in corso");
		// Genera due numeri primi casuali, entrambi maggiori di almeno bitLength bit
        BigInteger p = generatePrime(bitLength);
        BigInteger q = generatePrime(bitLength);

        // Calcolo n 
        n = p.multiply(q);
        
        l = n.bitLength() / 8; // da bit a byte
        
        // Calcolo funzione di eulero
        BigInteger P = p.subtract(BigInteger.ONE);
        BigInteger Q = q.subtract(BigInteger.ONE);
        z = P.multiply(Q);
        
        // Calcolo l'esponente pubblico
        e = generateCoprime(z);
        
        d = calculatePrivateExponent(e, z);
        
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
	
	/**
	 * Genera un numero primo maggiore di almeno bitLength bit
	 * 
	 * @param bitLength il numero di bit
	 * @return il numero primo generato come BigInteger
	 */
    public BigInteger generatePrime(int bitLength) {
    	SecureRandom random = new SecureRandom();
        BigInteger prime;
        
        do {
            prime = new BigInteger(bitLength, random);
        } while (!prime.isProbablePrime(100));

        return prime;
    }
    
    /**
     * Genera un numero coprimo e con z
     * 
     * @param z il BigInteger per il quale verrà generato il numero coprimo
     * @return il numero coprimo generato
     */
    public BigInteger generateCoprime(BigInteger z) {
        SecureRandom random = new SecureRandom();
        BigInteger e;

        do {
            e = new BigInteger(z.bitLength(), random);
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(z) >= 0 || e.gcd(z).compareTo(BigInteger.ONE) != 0);

        return e;
    }
    
    /**
     * Calcola l'esponente privato per la crittografia RSA
     * 
     * @param e l'esponente pubblico BigInteger utilizzato nella coppia di chiavi RSA
     * @param eulerFunction la funzione di eulero BigInteger
     * @return l'esponete privato
     */
    public BigInteger calculatePrivateExponent(BigInteger e, BigInteger eulerFunction) {
    	return e.modInverse(eulerFunction);
    }
    
    /**
     * Cripta il messaggio usando l'algoritmo RSA
     * 
     * @param messageToCrypt la stringa contenete il messaggio da criptare
     * @return il messaggio criptato
     */
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
				logger.error("Thread di criptazione interrotto!", e);
			}
    		sb.append(cryptedParts[i]);
    	}
    	
    	logger.info("Messaggio criptato correttamente");
    	return sb.toString();
    }
    
    /**
     * Decripta il messaggio criptato con RSA
     * 
     * @param cryptedMessage la stringa contenente il messaggio da decriptare
     * @return il messaggio originale
     */
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
    				logger.error("Thread di decriptazione interrotto!", e);
    			}
        		sb.append(decryptedParts[i]);
    		}
    	}
    	
    	logger.info("Messaggio decriptato correttamente");	
    	return sb.toString();
    }
}
