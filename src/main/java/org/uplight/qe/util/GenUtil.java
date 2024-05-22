package org.uplight.qe.util;
import org.apache.commons.text.RandomStringGenerator;

public class GenUtil {


    // Utility method to generate a random string of a specified length
    public static String genRandomStringWithNLength(int N) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z') // Specify the range of characters
                .filteredBy(Character::isLetterOrDigit) // Filter to include only letters and digits
                .build();
        return generator.generate(N);
    }

    public static void main(String[] args) {
        System.out.println(genRandomStringWithNLength(151));
    }

}
