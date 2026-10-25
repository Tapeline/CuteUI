package me.tapeline.cuteui;

public class EMath {

    public static int clamp(int x, int min, int max) {
        return x < min
            ? min
            : (x > max
                ? max
                : x);
    }

}
