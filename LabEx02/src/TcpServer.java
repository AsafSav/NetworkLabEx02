import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	
	private Config config;
	private ThreadPool threadPool;
	private ServerSocket serverSocket;
	private Socket clientSocket;

	
	public TcpServer() {
		config = Config.GetInstance();
		
		
		threadPool = new ThreadPool(config.GetMaxThreads());
		try {
			serverSocket = new ServerSocket(config.GetPort());
			System.out.println("Listening on port: " + config.GetPort());
		} catch (IOException e) {
			System.out.println("Not A Valid Port! Sorry, goodbye!");
			System.exit(0);
		}
	}
	
	public void RunServer() {
		try {
			while (true) {
				clientSocket = serverSocket.accept();
				threadPool.InsertTask(new HttpRequestHandler(clientSocket, config.GetRoot(), config.GetDefaultPage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
