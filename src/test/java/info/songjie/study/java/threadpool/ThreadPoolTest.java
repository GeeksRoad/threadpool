package info.songjie.study.java.threadpool;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class ThreadPoolTest extends TestCase {
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);
	
	@Test
	public void testThreadPool() throws IOException{
		ThreadPool threadPool = new ThreadPool();
		for(int i=0; i<100; i++){
			threadPool.execute(new Task());
		}
		System.in.read();
	}
	
	
	private static class Task implements Runnable {  
        private volatile static  int i = 1;  
  
        public void run() {// 执行任务  
        	try {
				Thread.sleep((long) (10));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	logger.debug("任务{}完成", i);
        	i++;
        }  
    }  
}
