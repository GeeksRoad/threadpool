package info.songjie.study.java.threadpool;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ThreadPool {
	private Logger logger = LoggerFactory.getLogger(ThreadPool.class);
	// 线程池的初始大小
	private int initSize = 5;
	
	// 工作线程
	private WorkThread[] workThreads;
	
	// 是否继续接受新的任务
	private boolean canReceiveNewTask = true;
	
	// 任务列表。非线程安全的，对其进行操作要注意同步。
	List<Runnable> taskQuene = new LinkedList<Runnable>();
	
	// 执行完成的任务数
	private long finishedTask = 0;
	
	public ThreadPool(){
		this(5);
	}
	
	public ThreadPool(int initSize){
		workThreads = new WorkThread[initSize];
		for(int i=0; i<initSize; i++){
			workThreads[i] = new WorkThread();
			new Thread(workThreads[i]).start();
		}
	}
	
	public void execute(Runnable task){
		synchronized(taskQuene){
			taskQuene.add(task);
			taskQuene.notify();
		}
	}
	
	// 工作线程被定义为私有内部类
	private class WorkThread implements Runnable{
		// 该线程是否能够工作，将其置为false则会停止该线程
		private boolean canWork  = true;
		public void run() {
			while(canWork){
				Runnable task = null;
				synchronized(taskQuene){
					logger.debug("共有{}个任务", taskQuene.size());
					if(!taskQuene.isEmpty()){
						if(!taskQuene.isEmpty()){
							task = taskQuene.remove(0);
						}
					}else{
						try {
							taskQuene.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(task != null){
					task.run();
					finishedTask++;
				}
			}
		}
	}
}
