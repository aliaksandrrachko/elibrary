package by.it.academy.grodno.elibrary.service.utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomPasswordGenerator {

    private static Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private RandomPasswordGenerator() {
    }

    public static String generateRandomPassword() {
        int randomNumber = (random.nextInt(1000000000));
        return Integer.toString(randomNumber, 20);
    }
}
