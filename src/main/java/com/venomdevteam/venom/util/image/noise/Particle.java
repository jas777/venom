package com.venomdevteam.venom.util.image.noise;

import com.venomdevteam.venom.util.image.PVector;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class Particle {

    private final BufferedImage image;

    private final PerlinFlowField perlin;

    private final int width;
    private final int height;
    private final int scale;

    private double cycle;
    private boolean increment;

    private Color color = null;

    final PVector pos;
    final PVector vel = new PVector(0, 0);
    final PVector acc = new PVector(0, 0);

    final float maxSpeed = 1;

    final PVector prevPos;

    public Particle(int scale, BufferedImage image, PerlinFlowField perlin) {

        this.image = image;

        this.perlin = perlin;

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.scale = scale;

        this.pos = new PVector(new Random().nextInt(width), new Random().nextInt(height));

        prevPos = pos.copy();
    }

    public void update() {
        vel.add(acc);
        vel.limit(maxSpeed);
        pos.add(vel);
        acc.mult(0);
    }

    public void follow(PVector[] vectors) {

        int cols = (int) Math.floor(this.width / this.scale);

        int x = (int) Math.floor(pos.x / scale);
        int y = (int) Math.floor(pos.y / scale);
        int index = (x - 1) + ((y - 1) * cols);
        index = index - 1;
        if (index > vectors.length || index < 0) {
            index = vectors.length - 1;
        }
        PVector force = vectors[index];
        applyForce(force);
    }

    void applyForce(PVector force) {
        acc.add(force);
    }

    public void show() {

        switch (perlin.getOption()) {
            case 1:
                color = new Color(0, 0, 0, 10);
                break;
            case 2:
                color = new Color(255, 255, 255, 10);
                break;
            case 3:
                color = new Color(255, 0, 0, 10);
                break;
            case 4:
                color = new Color(0, 0, 255, 10);
                break;
            case 5:
                color = (Arrays.asList(perlin.getParticles()).indexOf(this) & 2) == 0 ? new Color(255, 0, 0, 10) : new Color(0, 0, 255, 10);
                break;
            case 6:
                int blue = (int) Math.floor(map(this.vel.x + this.vel.y, this.maxSpeed * 2, 255, 0));
                int red = (int) Math.floor(map(this.vel.x + this.vel.y, this.maxSpeed * 2, 0, 255));
                color = new Color(Math.min(255, Math.max(0, red)), 0, Math.min(255, Math.max(0, blue)), 30);
                break;
            case 7:

                double frequency = 2 * Math.PI / 1400;

                double r = Math.sin(0.01666 * cycle + 3) * 127 + 128;
                double g = Math.sin(0.01000 * cycle + 8) * 127 + 128;
                double b = Math.sin(0.00999 * cycle + 6) * 127 + 128;

                b = Math.min(200, b);

                color = new Color((int) Math.round(r), (int) Math.round(g), (int) Math.round(b), 70);

//                if (cycle == 32 && increment) {
//                    increment = false;
//                    cycle--;
//                } else if (cycle == 0 && !increment) {
//                    increment = true;
//                    cycle++;
//                } else {
//                    if (increment) {
//                        cycle++;
//                    } else {
//                        cycle--;
//                    }
//                }

                cycle += 1.2;

                break;
        }

//        pos.x -= 1;
//        pos.y -= 1;

        int x = (int) Math.floor(pos.x);
        int y = (int) Math.floor(pos.y);

        Graphics2D graphics2D = image.createGraphics();

        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(1));

        graphics2D.draw(new Line2D.Double(x, y, x, y));
        graphics2D.dispose();
        //        stroke(255, 50);
//        strokeWeight(2);
//        point(pos.x, pos.y);
    }

    public void updatePrev() {
        prevPos.x = pos.x;
        prevPos.y = pos.y;
    }

    public void edges() {
        if (pos.x > width) {
            pos.x = 0;
            updatePrev();
        }
        if (pos.x < 0) {
            pos.x = width;
            updatePrev();
        }

        if (pos.y > height) {
            pos.y = 0;
            updatePrev();
        }
        if (pos.y < 0) {
            pos.y = height;
            updatePrev();
        }
    }

    private float map(float n, float stop1, float start2, float stop2) {

        return (n - (float) 0) / (stop1 - (float) 0) * (stop2 - start2) + start2;

//        if (start2 < stop2) {
//            return Math.max(Math.min(newval, start2), stop2);
//        } else {
//            return Math.max(Math.min(newval, stop2), start2);
//        }
    }
}