package com.jerry.classs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/***
 * Java中的面板：Panel 步骤： 1.写一个类，继承JPanel 2.写一个构造方法，确定面板的特点
 */
public class GamePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int num = 300;//代表屏幕中文字的数量
	//定义数组，用来存放所有文字的横坐标
	int[] xs = new int[num];
	//定义数组，用来存放所有文字的纵坐标
	int[] ys = new int[num];
	//定义颜色数组，用来存放所有文字的颜色
	Color[] cs = new Color[num];
	//定义数组，用来存放所有文字的大小
	int[] fs = new int[num];
	//定义数组，用来存放所有文字的移动熟读
	int[] sp = new int[num];
	//用来生成随机数的工具
	Random rd = new Random();
	//定义图片
	BufferedImage img;

	/**
	 * 让文字活动
	 */
	public void action() {

		//Java中创建并启动线程的固定格式
		//创建并启动一个线程去播放音乐
		new Thread() {
			public void run() {
				//重复播放音乐
				while (true) {
					//创建音乐播放器
					MusicPlayer player = new MusicPlayer("/music/bgm.mp3");
					//开始播放音乐
					player.startMusic();
				}
			}
		}.start();
		//创建并启动线程，控制文字移动
		new Thread() {
			public void run() {
				//while,死循环控制文字一直移动
				while (true) {
					//遍历所有的文字，让文字移动起来
					for (int i = 0; i < num; i++) {
						//让文字的向左移动
						xs[i] -= sp[i];
						//如果横坐标小于等于0，移动到左边界
						if (xs[i] <= 0) {
							//让文字的横坐标等于屏幕宽度（文字出现在最右边）
							xs[i] = GameFrame.width;
						}
					}
					//让线程休眠
					try {
						//每移动一次休眠的毫秒数
						Thread.sleep(15);
						//刷新界面，将文字画在新的位置上
						repaint();//重新调用paint方法，绘制界面
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 面板设置
	 */
	public GamePanel() {
		//加载图片
		try {
			img = ImageIO.read(this.getClass().getResource("/img/img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//启动文字移动
		action();
		//设置面板的背景颜色
		//setBackground(Color.black);
		//循环，随机出所有文字的横纵坐标
		for (int i = 0; i < num; i++) {
			//随机生成所有的横坐标，并放入横坐标的数组中
			//rd.nextInt(num);表示在表示在[0,num)区间随机生成整数
			xs[i] = rd.nextInt(GameFrame.width);
			//随机生成所有的横坐标，并放入纵坐标的数组中
			//rd.nextInt(num);表示在表示在[0,num)区间随机生成整数
			ys[i] = rd.nextInt(GameFrame.height);
			//随机颜色
			cs[i] = new Color(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255));
			//随机字体大小
			fs[i] = rd.nextInt(30) + 1;
			//随机文字速度
			sp[i] = rd.nextInt(10) + 1;
		}
		//让背景透明
		setOpaque(false);
	}

	/**
	 * 画图专用方法
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < num; i++) {
			//设置画笔颜色(从颜色数组中获取)
			g.setColor(cs[i]);
			//设置画笔字体
			//new Font("字体名称",字体样式,字体大小)
			g.setFont(new Font("华文新魏", Font.BOLD, fs[i]));
			//画图片g.drawImage(图片, 横坐标,纵坐标, 宽度，高度，null);
			g.drawImage(img, xs[i], ys[i], fs[i] * 2, fs[i] * 2, null);
			//画文字g.drawString("文字内容",横坐标,纵坐标)
			//g.drawString("★I LOVE YOU", xs[i], ys[i]);
		}
	}
}
