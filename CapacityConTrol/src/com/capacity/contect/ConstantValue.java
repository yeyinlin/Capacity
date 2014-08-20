package com.capacity.contect;

public interface ConstantValue {
	/**
	 * 打开记事本 
	 */
	String NOTEPAD="notepad.exe";
	/**
	 * 计算机
	 */
	String CALC="calc.exe";
	/**
	 * 15秒关机
	 */
	String RONONCE="rononce -p";
	/**
	 * 60秒倒计时关机命令 
	 */
	String TSSHUTDN="tsshutdn";
	
	/**
	 * 打开音乐
	 */
	String MUSIC="F:\\千千静听\\TTPlayer.exe";
	
	/**
	 * 关闭音乐
	 */
	String COLSEMUSIC = "taskkill /f /t /im TTPlayer.exe";
	
	
	//	taskkill  /im iexplore.exe
	//关闭某个程序
	//"taskkill  /f /t /im BaiduMusic.exe"
	//关闭电脑
	//shutdown -s -t 00
	
	
	
}
