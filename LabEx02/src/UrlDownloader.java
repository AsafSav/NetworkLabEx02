import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class UrlDownloader implements Runnable {
	
	private String urlToDownload;
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
		HashMap<String, String> url = WebUtils.CutUrl(urlToDownload);
		String toFetch = "";
		if (url.containsKey("uri")) {
			toFetch = url.get("uri");
		} else {
			toFetch = "/";
		}
		HttpRequest request = new HttpRequest(toFetch);
		try {
			S = new Socket();
			S.connect(new InetSocketAddress(CrawlerHandler.GetDomain(), Port), 1000);
			DataOutputStream writer = new DataOutputStream(S.getOutputStream());
			writer.write(request.CreateRequest(eRequestType.GET).getBytes());
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(S.getInputStream()));
			String line;
			if (reader.ready()) {
				while ((line = reader.readLine()) != null) {
					htmlPage.append(line.toLowerCase() + "\n");
					System.out.println(line);
				}
			}
			if (!htmlPage.toString().isEmpty()) {
				HtmlAnalyzer analyzer = new HtmlAnalyzer(htmlPage);
				CrawlerHandler.InsertToAnalyzers(analyzer);
			}

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
