package rolling.redisspringdatademo.utils;

import java.util.Random;

public class RandomUtils {
    public static String randomNumber(int length) {
        String set = "0123456789";
        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i<length; i++){
            sb.append(set.charAt(random.nextInt(10)));
        }

        return sb.toString();
    }
}
