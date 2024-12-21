package agh.ics.oop.model.util;

public class MathMod {
    public static int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
        {
            result += y;
        }
        return result;
    }
}
