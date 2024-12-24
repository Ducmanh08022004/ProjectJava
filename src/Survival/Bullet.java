/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Survival;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private final double velocityX;
    private final double velocityY;
    private static final int SPEED = 10;

    public Bullet(int startX, int startY, int targetX, int targetY) {
        this.x = startX;
        this.y = startY;

        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        this.velocityX = (deltaX / distance) * SPEED;
        this.velocityY = (deltaY / distance) * SPEED;
    }

    public void update() {
        x += velocityX;
        y += velocityY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Thêm phương thức getBounds
    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 10); // Kích thước đạn là 10x10
    }
}



