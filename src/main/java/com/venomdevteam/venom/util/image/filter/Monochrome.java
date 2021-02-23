package com.venomdevteam.venom.util.image.filter;

import java.awt.image.BufferedImage;

public class Monochrome {

    public static BufferedImage apply(BufferedImage original) {

        int width = original.getWidth();
        int height = original.getHeight();

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int p = original.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int avg = (r + g + b) / 3;

                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                original.setRGB(x, y, p);

            }
        }

        return original;

    }

}
