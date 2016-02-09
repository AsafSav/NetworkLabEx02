import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class UrlDownloader implements Runnable {

	private String urlToDownload;
	private String Domain;
	private int Port;
	Socket S;
	
	public UrlDownloader(int port, String urlToDownload) {
		this.urlToDownload = urlToDownload;
		this.Port = port;
	}
	
	@Override
	public void run() {
		Download();
	}
	
	private void Download() {
		StringBuilder htmlPage = new StringBuilder("");
		HttpRequest request = new HttpRequest(urlToDownload);
		try {
			S = new Socket();
			S.connect(new InetSocketAddress(urlToDownload, Port), 1000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(S.getInputStream()));
			DataOutputStream writer = new DataOutputStream(S.getOutputStream());
			writer.write(request.CreateRequest(eRequestType.GET).getBytes());
			writer.flush();
			String line;
			char[] msg = new char[99999];
			while ((reader.read(msg)) != -1) {
				//htmlPage.append(line + "\n");
				htmlPage.append(msg);
				System.out.println(msg.toString());
			}
			
			HtmlAnalyzer analyzer = new HtmlAnalyzer(htmlPage, true);
			CrawlerHandler.InsertToAnalyzers(analyzer);
			writer.close();
			reader.close();
			S.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
