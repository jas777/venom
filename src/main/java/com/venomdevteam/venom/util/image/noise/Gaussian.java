package com.venomdevteam.venom.util.image.noise;

import com.venomdevteam.venom.util.image.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gaussian {

    private final double mean;
    private final double sigma;

    public Gaussian(double mean, double sigma) {
        this.mean = mean;
        this.sigma = sigma;
    }

    public BufferedImage processImage(BufferedImage image) {

        double variance = sigma * sigma;

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage processed = new BufferedImage(width, height, image.getType());
        double a = 0.0;
        double b;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                while (a == 0.0) {
                    a = Math.random();
                }

                b = Math.random();

                double d = Math.sqrt(-2 * Math.log(a)) * Math.cos(2 * Math.PI * b);
                double noise = mean + Math.sqrt(variance) * d;

                int gray = new Color(image.getRGB(x, y)).getRed();
                int alpha = new Color(image.getRGB(x, y)).getAlpha();

                double color = gray + noise;
                if (color > 255)
                    color = 255;
                if (color < 0)
                    color = 0;

                int newColor = (int) Math.round(color);

                int rgb = ImageUtil.colorToRGB(alpha, newColor, newColor, newColor);

                processed.setRGB(x, y, rgb);
            }
        }

        return processed;

    }

}
