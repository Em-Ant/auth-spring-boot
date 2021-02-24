package it.emant.auth.util;

import java.util.Random;

public class KeyGenerator {
    static int leftLimit = 48; // numeral '0'
    static int rightLimit = 122; // letter 'z'
    static Random random = new Random();

    public static String generate(int lenght) {
      return random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(lenght)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
    }
  
    public static String generate() {
      return generate(32);
    }
}
