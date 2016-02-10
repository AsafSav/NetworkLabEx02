import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {

	private ClientThread[] threads;
	private LinkedBlockingQueue<Runnable> taskQueue;
	public AtomicInteger activeThreadsCount;
	
	public ThreadPool(int numOfThreads) {
		threads = new ClientThread[numOfThreads];
		taskQueue = new LinkedBlockingQueue<Runnable>();
		activeThreadsCount.set(0);
		for (int i = 0; i < numOfThreads; i++) {
			threads[i] = new ClientThread(taskQueue, activeThreadsCount);
			threads[i].start();
		}
	}
	
	
	public void InsertTask(Runnable toInsert) {
		try {
			taskQueue.put(toInsert);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isFree() {
		return (activeThreadsCount.get() == 0 && taskQueue.isEmpty());
	}
}
