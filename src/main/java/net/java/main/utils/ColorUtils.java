package net.java.main.utils;

import java.awt.*;

public class ColorUtils {

    public static Color skyRainbow(int var2, float st, float bright) {
        double v1 = Math.ceil(System.currentTimeMillis() + (var2 * 109L)) / 5;
        return Color.getHSBColor((double) ((float) ((v1 %= 360.0) / 360.0)) < 0.5 ? -((float) (v1 / 360.0)) : (float) (v1 / 360.0), st, bright);
    }
    public static int getHealthColor(float health, float maxHealth) {
        float clampedHealth = health;
        if (health > 20) {
            clampedHealth = 20f;
        }

        float[] fractions = {0f, 0.5f, 1f};
        Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN};

        float progress = clampedHealth * 5 * 0.01f;
        Color customColor = blendColors(fractions, colors, progress).brighter();

        return customColor.getRGB();
    }
    public static Color getHealthColor2(float health, float maxHealth) {
        float clampedHealth = health;
        if (health > 20) {
            clampedHealth = 20f;
        }

        float[] fractions = {0f, 0.5f, 1f};
        Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN};

        float progress = clampedHealth * 5 * 0.01f;
        Color customColor = blendColors(fractions, colors, progress).brighter();

        return customColor;
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = getFractionIndices(fractions, progress);
            float[] range = {fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = {colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color blendedColor = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return blendedColor;
        } else {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
    }

    public static int[] getFractionIndices(float[] fractions, float progress) {
        int[] range = new int[2];
        int startPoint = 0;
        while (startPoint < fractions.length && fractions[startPoint] <= progress) {
            ++startPoint;
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, float ratio) {
        float r = ratio;
        float ir = 1.0f - r;

        float[] rgb1 = color1.getColorComponents(null);
        float[] rgb2 = color2.getColorComponents(null);

        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;

        red = Math.min(255.0f, Math.max(0.0f, red));
        green = Math.min(255.0f, Math.max(0.0f, green));
        blue = Math.min(255.0f, Math.max(0.0f, blue));

        return new Color(red, green, blue);
    }
}
