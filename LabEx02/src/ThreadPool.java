import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

	private ClientThread[] threads;
	private LinkedBlockingQueue<Runnable> taskQueue;
	
	public ThreadPool(int numOfThreads) {
		threads = new ClientThread[numOfThreads];
		taskQueue = new LinkedBlockingQueue<Runnable>();
		for (int i = 0; i < numOfThreads; i++) {
			threads[i] = new ClientThread(taskQueue);
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
		return taskQueue.isEmpty();
	}
}
