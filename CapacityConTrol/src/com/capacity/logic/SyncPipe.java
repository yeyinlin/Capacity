package com.capacity.logic;

import java.io.InputStream;
import java.io.OutputStream;

class  SyncPipe implements Runnable {
	private  OutputStream ostrm_;
	private  InputStream istrm_;
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
		istrm_ = istrm;
		ostrm_ = ostrm;
	}

	public void run() {
		try {
			final byte[] buffer = new byte[1024];
			for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
				ostrm_.write(buffer, 0, length);
			}
		} catch (Exception e) {
			throw new RuntimeException("处理命令出现错误：" + e.getMessage());
		}
	}

	
}
