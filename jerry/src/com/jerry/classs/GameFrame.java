package com.jerry.classs;

import java.awt.Toolkit;

import javax.swing.JFrame;

import com.sun.awt.AWTUtilities;

/**
 * Java中表示窗体的类：JFrame 步骤： 1.写一个类，继承JFrame 2.写一个构造方法，确定窗体的形状
 */
public class GameFrame extends JFrame {
	//获取系统当前分辨率的宽度
	static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	//获取系统当前分辨率的高度
	static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	/**
	 * 构造方法：类似于模具 给窗体定型
	 */
	public GameFrame() {
		//设置标题
		setTitle("爱心");
		//设置尺寸（大小）
		//setSize(宽度, 高度);
		setSize(width, height);
		//设置默认的关闭选项(每次制作窗体必须的加)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//隐藏标题栏
		setUndecorated(true);
		//让特效一直显示在屏幕的顶端
		setAlwaysOnTop(true);
		//让窗体不遮盖桌面（程序运行时可以点击桌面的图标）
		AWTUtilities.setWindowOpaque(this, false);//固定的套路
	}

	public static void main(String[] args) {
		//创建窗体
		GameFrame frame = new GameFrame();
		//创建面板
		GamePanel panel = new GamePanel();
		//将面板加入窗体
		frame.add(panel);
		//让窗体显示
		//true表示显示，false表示隐藏
		frame.setVisible(true);
	}
}
