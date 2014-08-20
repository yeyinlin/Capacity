package com.capacity.logic;

import java.io.PrintWriter;

public class JavaConComputer {

	public void SetCmdContent(String propath){

		String[] command = {"cmd"};
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getErrorStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			/** 以下可以输入自己想输入的cmd命令 */
			stdin.println(propath); // 定位到D盘根目录
			stdin.close();
		} catch (Exception e) {
			throw new RuntimeException("编译出现错误：" + e.getMessage());
		}

	
	}
	
}
