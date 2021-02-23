package com.venomdevteam.venom.util.image.noise;

import com.venomdevteam.venom.util.image.PVector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PerlinFlowField {


    private double time = 0;

    private final int scale;
    private final int height;
    private final int width;
    private final int option;

    private final double increment = 0.1;

    private double xoff = 0;
    private double yoff = 0;
    private double zoff = 0;

    private final int noOfPoints = 600;

    private final Particle[] particles = new Particle[noOfPoints];
    private final PVector[] flowField;

    private final BufferedImage image;

    public PerlinFlowField(int height, int width, int option) {
        this.height = height;
        this.width = width;
        this.option = option;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.scale = 75;

        int rows = (int) Math.floor(this.height / this.scale);
        int cols = (int) Math.floor(this.width / this.scale);

        flowField = new PVector[rows * cols];

        for (int i = 0; i < noOfPoints; i++) {
            particles[i] = new Particle(scale, image, this);
        }

    }

    public BufferedImage getNoiseImage() {

        for (int i = 0; i < 512; i++) p[i] = new Random().nextInt(256);

        this.time += 0.01;

        Color color = null;

        switch (this.option) {
            case 1:
                color = new Color(255, 255, 255);
                break;
            case 2:
            case 4:
            case 3:
            case 5:
            case 6:
            case 7:
                color = Color.BLACK;
                break;
        }

//        System.out.println(color.getRGB());

        Graphics2D graphics2D = image.createGraphics();

        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics2D.dispose();

        int rows = (int) Math.floor(this.height / this.scale);
        int cols = (int) Math.floor(this.width / this.scale);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                int index = (x + y * cols);

                float angle = (float) (noise(xoff, yoff, zoff) * (Math.PI * 2) * 4);

                PVector vector = PVector.fromAngle(angle);
                vector.setMag(0.01F);

                flowField[index] = vector;

                this.xoff += this.increment;
            }
            this.yoff += this.increment;
            this.zoff += 0.003;
        }

        for (int i = 0; i < 1600; i++) {
            for (Particle particle : particles) {
                particle.follow(flowField);
                particle.update();
                particle.edges();
                particle.show();
            }
        }

        return this.image;
    }

    public float noise(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
                Y = (int) Math.floor(y) & 255,                  // CONTAINS POINT.
                Z = (int) Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x),                                // COMPUTE FADE CURVES
                v = fade(y),                                // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z,      // HASH COORDINATES OF
                B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;      // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z),  // AND ADD
                grad(p[BA], x - 1, y, z)), // BLENDED
                lerp(u, grad(p[AB], x, y - 1, z),  // RESULTS
                        grad(p[BB], x - 1, y - 1, z))),// FROM  8
                lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1),  // CORNERS
                        grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
                        lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
                                grad(p[BB + 1], x - 1, y - 1, z - 1))));
    }

    public float fade(double t) {
        return (float) (t * t * t * (t * (t * 6 - 15) + 10));
    }

    public float lerp(double t, double a, double b) {
        return (float) (a + t * (b - a));
    }

    public float grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        float u = (float) (h < 8 ? x : y),                 // INTO 12 GRADIENT DIRECTIONS.
                v = (float) (h < 4 ? y : h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    final int[] p = new int[512];
    final int[] permutation = {151, 160, 137, 91, 90, 15,
            131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
            190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
            88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
            102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
            135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
            5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
            251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
            49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
            138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    };

//    static {
//
//    }


    public int getOption() {
        return option;
    }

    public Particle[] getParticles() {
        return particles;
    }
}
