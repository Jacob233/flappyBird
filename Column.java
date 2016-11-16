package com.play.flappybird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 柱子
 * 
 * @author xieji
 *
 */
public class Column {
	/** 柱子图片. */
	private BufferedImage columnImage;

	/** 柱子出现的位置. */
	private int x, y;

	/** 柱子中间空白高度. */
	private int gap = 109;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getGap() {
		return gap;
	}

	public int getWidth() {
		return width;
	}

	/** 柱子的宽高. */
	private int width, height;

	/** 随机数对象,用于生成柱子出现的纵坐标. */
	private Random random;

	/**
	 * 构造方法初始化图片
	 */
	public Column(int x) {
		this.x = x;
		try {
			this.columnImage = ImageIO.read(this.getClass().getResource("img\\column.png"));
			this.width = this.columnImage.getWidth();
			this.height = this.columnImage.getHeight();
			this.random = new Random();
			// 让柱子的高度一半作为y
			this.y = 150 + this.random.nextInt(100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 让柱子滚动
	 */
	public void step() {
		this.x--;
		if (this.x <= -this.width) {
			this.x = 320;
			this.y = 150 + this.random.nextInt(100);
		}
	}

	/**
	 * 绘制柱子的方法
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		g.drawImage(this.columnImage, this.x - this.width / 2, this.y - this.height / 2, null);

	}

}
