package coop.tecso.examen.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class Util {

    /**
     * Returns true if a value is true. Useful for method references
     */
    public static Boolean isTrue(Boolean value) {
        return value;
    }

    /**
     * Generate a random UUID
     */
    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }


    /**
     * It works that generates a random number
     *
     * @param length
     * @param useLetters
     * @param useNumbers
     * @return
     */
    public static String generateRandomAccount(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    /**
     * Function to validate BigDecimal values
     *
     * @param firstValue
     * @param secondValue
     * @return
     */
    public static Boolean validateAmount(BigDecimal firstValue, BigDecimal secondValue) {
        int res;
        res = firstValue.compareTo(secondValue);

        if (res == 0) {
            return true;
        } else if (res == 1) {
            return true;
        } else if (res == -1) {
            return false;
        }

        return false;
    }
}
