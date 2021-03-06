package com.play.flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 游戏主界面,定义窗口大小
 *
 * @author xieji
 */
public class World extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * 背景图片.
     */
    private BufferedImage background;

    /**
     * 开始图片.
     */
    private BufferedImage startImage;

    /**
     * 结束图片.
     */
    private BufferedImage endImage;

    /**
     * 开始鸟图
     */
    private BufferedImage birdImage;

    /**
     * 底部滚动图片.
     */
    private Ground ground;

    /**
     * 面板上的柱子.
     */
    private Column column1;

    /**
     * 柱子2
     */
    private Column column2;

    /**
     * 飞翔的鸟
     */
    private Bird bird;

    /**
     * 标记游戏是否开始.
     */
    private boolean start;

    /**
     * 游戏是否结束.
     */
    private boolean gameOver;

    /**
     * 当前分数.
     */
    private int score;

    public World() {
        try {
            this.background = ImageIO.read(this.getClass().getResource(
                    "img\\bg.png"));
            this.startImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "img\\start.png"));
            this.birdImage = ImageIO.read(this.getClass().getResource(
                    "img\\0.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("启动失败啦,加载图片时发生错误。");
        }
        this.init();
    }

    /**
     * 初始化方法,加载界面的其他组件(图片)
     */
    private void init() {
        this.ground = new Ground(400);
        this.column1 = new Column(320 + 100);
        this.column2 = new Column(320 + 100 + 200);
        this.bird = new Bird(160, 240);

        // 初始游戏状态

        this.start = false;
        this.gameOver = false;
        this.score = 0;
    }

    /**
     * 实现界面底部滚动
     */
    private void action() {
        // 添加鼠标的点击事件,标记游戏开始
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) {
                    init();
                    return;
                }
                start = true;
                bird.flappy();
            }
        });

        while (true) {
            // 当游戏为开始状态的时候才调用柱子的移动
            if (this.start) {
                this.column1.step();
                this.column2.step();
                this.bird.step();
                if (this.bird.hit(this.column1, this.column2, this.ground)) {
                    // 添加结束音效
                    AudioPlayWave audioPlayWave = new AudioPlayWave(
                            "audio\\de.wav");
                    audioPlayWave.start();
                    this.gameOver = true;
                    this.start = false;
                }
                // 5
                // 判断通过柱子后计分
                if (this.bird.pass(this.column1, this.column2)) {
                    this.score++;
                }
            }
            this.ground.step();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }

    /**
     * 重写了父类的绘制方法
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.background, 0, 0, null);
        this.column1.paint(g);
        this.column2.paint(g);
        this.ground.paint(g);
        // 绘制小鸟
        this.bird.paint(g);
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 32);
        g.setFont(font);
        g.setColor(new Color(255, 236, 139));
        g.drawString("SCORE:" + this.score, 30, 50);
        // 如果游戏结束绘制结束界面
        if (this.gameOver) {
            try {
                this.endImage = ImageIO.read(getClass().getResource(
                        "img\\gameover.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(this.endImage, 0, 0, null);
            return;
        }
        // 游戏未开始绘制开始图片
        if (!this.start) {
            // 绘制开始图片
            g.drawImage(this.startImage, 0, 0, null);
            g.drawImage(this.birdImage, 141, 225, null);
        }
    }

    public static void main(String[] args) {
        // 创建一个java swing窗体
        JFrame window = new JFrame();
        // 创建一个面板
        World world = new World();
        window.add(world);
        window.setTitle("FlappyBird");
        // 设置窗体的大小
        window.setSize(320, 480);
        // 设置窗体不可改变大小
        window.setResizable(false);
        // 设置关闭时的处理事件
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体的居屏幕的显示位置
        window.setLocationRelativeTo(null);
        // 设置显示窗口
        window.setVisible(true);
        world.action();
    }
}
