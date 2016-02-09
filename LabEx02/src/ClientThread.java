import java.util.concurrent.LinkedBlockingQueue;


public class ClientThread extends Thread {
	private LinkedBlockingQueue<Runnable> queue;

	public ClientThread (LinkedBlockingQueue<Runnable> queue) {
		super();
		this.queue = queue;
	}
	
	@Override
	public void run() {
			try {
				while (true) {
					queue.take().run();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
