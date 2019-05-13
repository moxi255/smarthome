package com.smarthome.app.queue;

import java.util.concurrent.BlockingQueue;

/**
 * 执行命令线程
 * @author smmh
 *
 */
public class Consumer implements Runnable{

	private BlockingQueue<?> queue;
	
	public Consumer(BlockingQueue<?> queue) {
		this.queue = queue;
	}
	
	
	@Override
	public void run() {
		
	}

	
	
}
