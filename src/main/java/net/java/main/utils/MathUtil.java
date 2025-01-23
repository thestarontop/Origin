package net.java.main.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author xiatian244
 * @since  4/5/2024
 */
public class MathUtil {
    public static Random random = new Random();
    public static Double clamp(final double num, final double min, final double max) {
        return Math.min(Math.max(num, min), max);
    }

    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }
    public static float lerp(float tickDelta, float old, float _new) {
        return old + (_new - old) * tickDelta;
    }


    public static double interpolate2(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static float getRandomFloat(float max, float min) {
        SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }

    public static int getRandom(final int min, final int max) {
        if (max < min) {
            return 0;
        }
        return min + random.nextInt((max - min) + 1);
    }

    public static long getRandom(long min, long max) {

        long range = max - min;
        long scaled = random.nextLong() * range;
        if (scaled > max) {
            scaled = max;
        }
        long shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }

    public static double getRandom(double min, double max) {

        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < 1.0E-5F;
    }

    public static boolean equals(double a, double b) {
        return Math.abs(a - b) < 1.0E-5;
    }
}
