package Survival;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tank {
    private int x, y;
    private final Color color;
    private static final int SPEED = 4;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private Image tankImage;

    public Tank(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        // Load hình ảnh của xe tăng
        try {
            tankImage = ImageIO.read(getClass().getResource("/tank.JFIF")); // Đường dẫn tới file hình ảnh của xe tăng
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveLeft() {
        if (x - SPEED >= 0) {
            x -= SPEED;
        }
    }

    public void moveRight(int screenWidth) {
        if (x + SPEED + WIDTH <= screenWidth) {
            x += SPEED;
        }
    }

    public void moveUp() {
        if (y - SPEED >= 0) {
            y -= SPEED;
        }
    }

    public void moveDown(int screenHeight) {
        if (y + SPEED + HEIGHT <= screenHeight) {
            y += SPEED;
        }
    }

    public void draw(Graphics g) {
        if (tankImage != null) {
            g.drawImage(tankImage, x, y, WIDTH, HEIGHT, null); // Vẽ hình ảnh của xe tăng
        } else {
            // Nếu không load được hình ảnh, vẽ hình chữ nhật như cũ
            g.setColor(color);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    public int getX() {
        return x + WIDTH / 2;
    }

    public int getY() {
        return y;
    }

    // Phương thức getBounds để kiểm tra va chạm
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
