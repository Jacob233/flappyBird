package com.play.flappybird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * 包含3张鸟的图片
 *
 * @author xieji
 */
public class Bird {

    /**
     * 所有小鸟图片.
     */
    private BufferedImage[] birds = new BufferedImage[3];

    /**
     * 当前鸟图片.
     */
    private BufferedImage bird;

    /**
     * 鸟出现的坐标.
     */
    private int x, y;

    /**
     * 图片序号.
     */
    private int index;

    /**
     * 重力加速度.
     */
    private int g;

    /**
     * 计算间隔时间（秒.
     */
    private double t;

    /**
     * 初始速度（像素/秒）.
     */
    private double v0;

    /**
     * 当前时刻速度
     */
    private double vt;

    /**
     * 运动距离.
     */
    private double s;

    /**
     * 飞行的角度.
     */
    private double angle;

    /**
     * 鸟可见实际大小
     */
    private int size = 26;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        // 循环加载鸟图片
        try {
            for (int i = 0; i < this.birds.length; i++) {
                this.birds[i] = ImageIO.read(this.getClass().getResource("img\\" + i + ".png"));
            }
        } catch (Exception ignored) {
        }
        // 设置当前鸟图片
        this.bird = this.birds[0];

        this.g = 4; // 重力加速度
        this.t = 0.25; // 每次计算的间隔时间
        this.v0 = 20; // 初始上抛速度
    }

    /**
     * 绘制小鸟
     *
     * @param g
     */
    synchronized void paint(Graphics g) {
        // 旋转坐标系
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(this.angle, this.x, this.y);
        // 以x,y 为中心绘制图片
        int x = this.x - bird.getWidth() / 2;
        int y = this.y - bird.getHeight() / 2;
        g.drawImage(this.bird, x, y, null);
        // 旋转回来
        g2d.rotate(-this.angle, this.x, this.y);
    }

    /**
     * 改变小鸟姿态
     */
    void step() {
        // Vt1 是本次
        double vt1 = this.vt;
        // 计算垂直上抛运动, 经过时间t以后的速度,
        double v = vt1 - g * t; // 当前速度-重力加速度*经过的时间
        // 作为下次计算的初始速度
        this.vt = v;
        // 计算垂直上抛运动的运行距离
        this.s = vt1 * t - 0.5 * g * t * t; // 当前速度*经过时间 -重力加速度*经过时间^2/2
        // 将计算的距离 换算为 y坐标的变化
        this.y = this.y - (int) s;
        // 计算仰角 横坐标移动速度60像素/秒，t=0.25秒，60*0.25=15
        this.angle = -Math.atan(s / 15);

        // 更换小鸟图片
        this.index++;
        this.bird = this.birds[this.index % 3];
        if (this.index == 3) {
            this.index = 0;
        }
    }

    /**
     * 鸟上抛动作
     */
    void flappy() {
        // 每次点击的时候添加音效
        AudioPlayWave audioPlayWave = new AudioPlayWave("audio\\fei.wav");
        audioPlayWave.start();
        // 每次小鸟上抛跳跃, 就是将小鸟在当前点重新以初始速度 V0 向上抛
        vt = v0;
    }

    /**
     * 判断鸟是否与柱子和地发生碰撞
     */
    boolean hit(Column column1, Column column2, Ground ground) {
        // 碰到地面
        if (y - size / 2 >= ground.getY() || y - size / 2 <= 0) {
            return true;
        }
        // 碰到柱子
        return hit(column1) || hit(column2);
    }

    /**
     * 检查当前鸟是否碰到柱子
     */
    private boolean hit(Column col) {
        // 如果鸟碰到柱子: 鸟的中心点x坐标在 柱子宽度 + 鸟的一半
        if (x > col.getX() - col.getWidth() / 2 - size / 2 && x < col.getX() + col.getWidth() / 2 + size / 2) {
            return y <= col.getY() - col.getGap() / 2 + size / 2 || y >= col.getY() + col.getGap() / 2 - size / 2;
        }
        return false;
    }

    /**
     * 判断鸟是否通过柱子
     */
    boolean pass(Column col1, Column col2) {
        return col1.getX() + this.size == x || col2.getX() + this.size == x;
    }
}
