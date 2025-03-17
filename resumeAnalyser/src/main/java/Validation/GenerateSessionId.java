package Validation;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateSessionId {
	private static SecureRandom random = new SecureRandom();
	public static String generateUniqueId() {
        return new BigInteger(130, random).toString(32); 
    }
}
