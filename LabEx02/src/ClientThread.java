import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class ClientThread extends Thread {
	private LinkedBlockingQueue<Runnable> queue;
	AtomicInteger activeThreadCount;

	public ClientThread (LinkedBlockingQueue<Runnable> queue, AtomicInteger activeThreads) {
		super();
		this.queue = queue;
		activeThreadCount = activeThreads;
	}
	
	@Override
	public void run() {
			try {
				while (true) {
					Runnable toRun = queue.take();
					activeThreadCount.incrementAndGet();
					toRun.run();
					activeThreadCount.decrementAndGet();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
