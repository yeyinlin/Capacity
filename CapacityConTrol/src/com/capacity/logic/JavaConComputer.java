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
			/** ���¿��������Լ��������cmd���� */
			stdin.println(propath); // ��λ��D�̸�Ŀ¼
			stdin.close();
		} catch (Exception e) {
			throw new RuntimeException("������ִ���" + e.getMessage());
		}

	
	}
	
}
