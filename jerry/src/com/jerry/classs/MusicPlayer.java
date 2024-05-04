package com.jerry.classs;

import java.io.BufferedInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * 播放音乐的
 */
public class MusicPlayer {
	//音乐的播放组件
	Player player;

	/**
	 * 设置音乐播放器
	 */
	public MusicPlayer(String path) {
		//使用IO流将背景音乐加载到程序中
		BufferedInputStream ins = new BufferedInputStream(this.getClass().getResourceAsStream(path));
		//将音乐流输入到播放器组件中
		try {
			player = new Player(ins);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始播放音乐
	 */
	public void startMusic() {
		//播放音乐
		try {
			player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

}
