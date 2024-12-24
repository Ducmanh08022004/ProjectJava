package Survival;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final Timer timer;
    private Tank playerTank;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemyTanks;
    private final Random random;
    private boolean gameOver;
    private int score = 0;

    // Các biến trạng thái để theo dõi bắn và di chuyển
    private boolean isShooting = false;   // Trạng thái bắn
    private boolean isMovingUp = false;   // Trạng thái di chuyển lên
    private boolean isMovingDown = false; // Trạng thái di chuyển xuống
    private boolean isMovingLeft = false; // Trạng thái di chuyển trái
    private boolean isMovingRight = false;
    private boolean isdead = true;// Trạng thái di chuyển phải
    private int shootCooldown = 0;        // Thời gian chờ giữa các lần bắn
    private int ShieldCooldown = 0;
    private final int COOLDOWN_TIME = 15;
    private final int COOLDOWN_E = 150;
    private int COOLDOWN = 0;
    public Game() {
        random = new Random();
        initGame();
        timer = new Timer(3, this);  //
        timer.start();

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
    }

    // Khởi tạo lại trò chơi
    private void initGame() {
        playerTank = new Tank(100, 400, Color.GREEN);
        bullets = new ArrayList<>();
        enemyTanks = new ArrayList<>();
        gameOver = false;
        score = 0;

        // Tạo một số tank địch ban đầu
        spawnEnemy();
        spawnEnemy();
        spawnEnemy();
    }

    private void spawnEnemy() {
        int screenWidth = getWidth() > 0 ? getWidth() : 800;
        int screenHeight = getHeight() > 0 ? getHeight() : 600;
        int x = random.nextInt(screenWidth - 40) + 20;
        int y = random.nextInt(screenHeight - 40) + 20;


        enemyTanks.add(new Enemy(x, y, Color.RED));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Hiển thị thông báo "Game Over" khi thua
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2 - 20);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            g.drawString("Press R to Restart", getWidth() / 2 - 80, getHeight() / 2 + 40);
            return; // Dừng vẽ các đối tượng khác khi game kết thúc
        }

        playerTank.draw(g);

        // Vẽ vòng tròn xung quanh player khi Shield đang hoạt động
        if (ShieldCooldown > 0) {
            g.setColor(new Color(0, 255, 255, 128));  // Màu xanh nhạt với độ trong suốt
            int radius = 40; // Bán kính của vòng tròn bảo vệ
            g.drawOval(playerTank.getX() - 20, playerTank.getY() - 10, radius, radius);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Enemy enemyTank : enemyTanks) {
            enemyTank.draw(g);
        }

        // Vẽ điểm số
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        // Hiển thị trạng thái của phím E (Khiên bảo vệ)
        if (COOLDOWN == 0) {
            g.setColor(Color.GREEN);
            g.drawString("Shield Ready (Press E)", 10, 50);
        } else {
            g.setColor(Color.RED);
            g.drawString("Shield Cooldown: " + (COOLDOWN / 60) + "s", 10, 50);  // Hiển thị thời gian chờ tính bằng giây
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Kiểm tra nếu game đã kết thúc
        if (gameOver) {
            return;
        }
        // Giảm thời gian chờ giữa các lần bắn nếu còn đang đếm ngược
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (COOLDOWN >0)
        {
            COOLDOWN--;
        }
        if (ShieldCooldown > 0) {
            ShieldCooldown--;
        }
        // Xử lý bắn đạn nếu đang bắn và đã hết thời gian chờ
        if (isShooting && shootCooldown == 0) {
            if (getMousePosition() != null) {
                int mouseX = getMousePosition().x;
                int mouseY = getMousePosition().y;
                bullets.add(new Bullet(playerTank.getX(), playerTank.getY(), mouseX, mouseY));
                shootCooldown = COOLDOWN_TIME;  // Đặt lại thời gian chờ sau khi bắn
            }
        }
        if (ShieldCooldown==0)
        {
            isdead=true;
        }
        // Xử lý di chuyển dựa trên trạng thái phím
        if (isMovingUp) {
            playerTank.moveUp();
        }
        if (isMovingDown) {
            playerTank.moveDown(getHeight());
        }
        if (isMovingLeft) {
            playerTank.moveLeft();
        }
        if (isMovingRight) {
            playerTank.moveRight(getWidth());
        }

        // Cập nhật đạn
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.getX() > getWidth() || bullet.getY() > getHeight()) {
                bullets.remove(i);
                i--;
            }
        }

        // Cập nhật tank địch
        for (int i = 0; i < enemyTanks.size(); i++) {
            Enemy enemyTank = enemyTanks.get(i);
            enemyTank.moveTowards(playerTank.getX(), playerTank.getY());

            // Kiểm tra va chạm giữa đạn và địch
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if (enemyTank.getBounds().intersects(bullet.getBounds())) {
                    enemyTanks.remove(i);
                    bullets.remove(j);
                    i--;
                    spawnEnemy();
                    score += 10;
                    break;
                }
            }

            // Kiểm tra va chạm giữa địch và người chơi
            if (enemyTank.getBounds().intersects(playerTank.getBounds()) && isdead==true) {
                gameOver = true; // Người chơi thua
                repaint(); // Vẽ lại ngay lập tức để hiển thị thông báo "Game Over"
                return;
            }

            // Nếu tank địch ra khỏi màn hình thì loại bỏ nó
            if (enemyTank.getX() < 0 || enemyTank.getX() > getWidth() ||
                    enemyTank.getY() < 0 || enemyTank.getY() > getHeight()) {
                enemyTanks.remove(i);
                i--;
                spawnEnemy();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!gameOver) {
            // Kiểm tra trạng thái di chuyển
            if (key == KeyEvent.VK_W) {
                isMovingUp = true;
            }
            if (key == KeyEvent.VK_S) {
                isMovingDown = true;
            }
            if (key == KeyEvent.VK_A) {
                isMovingLeft = true;
            }
            if (key == KeyEvent.VK_D) {
                isMovingRight = true;
            }
            if (key == KeyEvent.VK_E && COOLDOWN==0) {
                isdead = false;
                ShieldCooldown=COOLDOWN_E;
                COOLDOWN=300;
            }
        }
        // Khi trò chơi kết thúc, kiểm tra phím 'R' để khởi động lại
        if (gameOver && key == KeyEvent.VK_R) {
            initGame();  // Khởi tạo lại trò chơi
            repaint();   // Vẽ lại màn hình ngay lập tức
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Khi thả phím, cập nhật trạng thái di chuyển
        if (key == KeyEvent.VK_W) {
            isMovingUp = false;
        }
        if (key == KeyEvent.VK_S) {
            isMovingDown = false;
        }
        if (key == KeyEvent.VK_A) {
            isMovingLeft = false;
        }
        if (key == KeyEvent.VK_D) {
            isMovingRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        isShooting = true; // Bắt đầu bắn khi nhấn chuột
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isShooting = false; // Dừng bắn khi thả chuột
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Survival Tank");
        Game game = new Game();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}