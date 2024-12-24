package Survival;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tanbt
 */
public class Enemy {
    private int x, y;
    private final Color color;
    private static final int SPEED = 2;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 20;
    private Image enemyImage;

    public Enemy(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        // Load the enemy image
        try {
            enemyImage = ImageIO.read(getClass().getResource("/quai.JFIF")); // Đường dẫn tới file hình ảnh
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveTowards(int targetX, int targetY) {
        if (x < targetX) {
            x += SPEED;
        } else {
            x -= SPEED;
        }

        if (y < targetY) {
            y += SPEED;
        } else {
            y -= SPEED;
        }
    }

    public void draw(Graphics g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, x, y, WIDTH, HEIGHT, null); // Vẽ hình ảnh địch
        } else {
            // Nếu không load được hình ảnh thì dùng fillRect như cũ
            g.setColor(color);
            g.fillRect(x, y, WIDTH, HEIGHT);
            g.fillRect(x + 15, y - 10, 10, 10);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
