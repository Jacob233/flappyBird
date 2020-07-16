package com.play.flappybird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 下方滚动类，包含了地面的图片
 * @author xieji
 *
 */
public class Ground {
	
	private BufferedImage groundImage;
	
	private int x, y;
	
	private int width;

	int getY() {
		return y;
	}

	public Ground(int y){
		this.y = y;
		try {
			this.groundImage = ImageIO.read(this.getClass().getResource("img\\ground.png"));
			this.width = this.groundImage.getWidth();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载滚动图片错误");
		}
	}
	
	/**
	 * 改变X坐标,实现滚动效果
	 */
	void step(){
		this.x--;
		if(this.x <= -(this.width - 320)){
			this.x = 0;
		}
		
	}

	void paint(Graphics g){
		g.drawImage(this.groundImage, this.x, this.y, null);
	}
}
