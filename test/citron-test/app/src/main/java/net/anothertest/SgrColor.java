package net.anothertest;

import android.graphics.Color;

public final class SgrColor {
    /**
     * An array of normal intensity colors.
     */
    public static final int[] COLOR_NORMAL = new int[] {
            Color.argb(255, 0, 0, 0),
            Color.argb(255, 128, 0, 0),
            Color.argb(255, 0, 128, 0),
            Color.argb(255, 128, 128, 0),
            Color.argb(255, 0, 0, 128),
            Color.argb(255, 128, 0, 128),
            Color.argb(255, 0, 128, 128),
            Color.argb(255, 192, 192, 192),
            Color.CYAN,
            Color.MAGENTA,
            Color.argb(0, 0, 0, 0) // Transparent
    };

    /**
     * An array of bright intensity colors.
     */
    public static final int[] COLOR_BRIGHT = new int[] {
            Color.argb(255, 128, 128, 128),
            Color.argb(255, 255, 0, 0),
            Color.argb(255, 0, 255, 0),
            Color.argb(255, 255, 255, 0),
            Color.argb(255, 0, 0, 255),
            Color.argb(255, 0, 0, 255),
            Color.argb(255, 255, 0, 255),
            Color.argb(255, 0, 255, 255),
            Color.argb(255, 255, 255, 255),
            Color.CYAN,
            Color.MAGENTA,
            Color.argb(0, 0, 0, 0) // Transparent
    };

    /**
     * Default private constructor to prevent instantiation.
     */
    private SgrColor() {

    }
}
