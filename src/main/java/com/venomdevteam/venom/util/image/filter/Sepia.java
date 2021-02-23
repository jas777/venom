package com.venomdevteam.venom.util.image.filter;

import java.awt.image.BufferedImage;

public class Sepia {

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

                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                if (tr > 255) {
                    r = 255;
                } else {
                    r = tr;
                }
                if (tg > 255) {
                    g = 255;
                } else {
                    g = tg;
                }
                if (tb > 255) {
                    b = 255;
                } else {
                    b = tb;
                }

                p = (a << 24) | (r << 16) | (g << 8) | b;

                original.setRGB(x, y, p);

            }
        }

        return original;

    }

}
