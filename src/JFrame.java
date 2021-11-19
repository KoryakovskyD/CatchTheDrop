import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Дмитрий on 24.06.2021.
 */


public class JFrame extends javax.swing.JFrame {

    private static JFrame jFrame;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image drop;
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float dropV = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException{
        background = ImageIO.read(JFrame.class.getResourceAsStream("background.png"));
        gameOver = ImageIO.read(JFrame.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(JFrame.class.getResourceAsStream("drop.png"));
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setSize(906,478);
        jFrame.setResizable(false);
        lastFrameTime = System.nanoTime();
        GameField gameField = new GameField();
        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = drop_left + drop.getWidth(null);
                float dropBottom = drop_top + drop.getHeight(null);
                boolean isDrop = x >= drop_left && x <= dropRight && y >= drop_top && y <= dropBottom;
                if (isDrop) {
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (gameField.getWidth() - drop.getWidth(null)));
                    dropV = dropV + 20;
                    score++;
                    jFrame.setTitle("Score: " + score);
                }
            }
        });
        jFrame.add(gameField);
        jFrame.setVisible(true);
    }

    private  static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;
        drop_top = drop_top + dropV * deltaTime;
        g.drawImage(background,0,0,null);
        g.drawImage(drop,(int) drop_left,(int) drop_top,null);
        if(drop_top > jFrame.getHeight()) g.drawImage(gameOver, 280,120,null);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
