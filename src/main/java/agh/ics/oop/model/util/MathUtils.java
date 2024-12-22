package agh.ics.oop.model.util;

import java.util.Random;

public class MathUtils {
    public static int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
        {
            result += y;
        }
        return result;
    }

    public static String nonClassicalProbabilityRandom(String value1, double prob1, String value2, double prob2){
        if (prob1 + prob2 != 1){
            throw new IllegalArgumentException();
        }
        int convertedProb1 = (int) Math.round(100*prob1);
        int convertedProb2 = (int) Math.round(100*prob2);

        if (convertedProb1 + convertedProb2 != 100){
            throw new IllegalArgumentException("given probabilities are unmappable to 0-100 integers");
        }
        Random random = new Random();
        int randomNum = random.nextInt(100);
        return randomNum < convertedProb1 ? value1 : value2;
    }
}
