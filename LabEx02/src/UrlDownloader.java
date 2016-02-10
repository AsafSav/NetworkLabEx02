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
		try {
			Download();
		} catch (CrawlerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void Download() throws CrawlerException{
		StringBuilder htmlPage = new StringBuilder("");
		StringBuilder headers = new StringBuilder("");
		HashMap<String, String> url = WebUtils.CutUrl(urlToDownload);
		String toFetch = "";
		if (url.containsKey("uri")) {
			toFetch = url.get("uri");
		} else if (urlToDownload.equals(CrawlerHandler.GetDomain())) {
			toFetch = "/";
		} else {
			toFetch = urlToDownload;
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
			int i = 0;

//			while ((line = reader.readLine()) != null) {
//				htmlPage.append(line.toLowerCase());
//				System.out.println(line);
//			}
			
			while (((line = reader.readLine()) != null) && (!line.isEmpty())) {
				headers.append(line.toLowerCase() + "\n");
				System.out.println(line);
			}				

			while ((i = reader.read()) != -1) {
				htmlPage.append((char)i);
			}
			
			if (!htmlPage.toString().isEmpty()) {
				HtmlAnalyzer analyzer = new HtmlAnalyzer(htmlPage);
				CrawlerHandler.InsertToAnalyzers(analyzer);
			} else if (!checkAlternateLocation(headers)) {
				if (CrawlerHandler.isCrawlDone()) {
					CrawlerHandler.doWhenFinished();
				}
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

	private boolean checkAlternateLocation(StringBuilder headers) throws CrawlerException {
		int responseCode =  Integer.parseInt(headers.substring(9, 12));
		if (responseCode == 301) {
			int locationIndex = headers.indexOf("location:");
			if (locationIndex != -1) {
				String path = headers.substring(locationIndex + 10, Math.min(headers.indexOf("\n", locationIndex + 11), headers.indexOf(" ", locationIndex + 11)));
				HashMap<String, String> url = WebUtils.CutUrl(path);
				CrawlerHandler.SetDomain(url.get("domain"));
				System.out.println(CrawlerHandler.GetDomain());
				CrawlerHandler.InsertToDownladers(new UrlDownloader(Port, path));
				return true;
			}
		} else if (responseCode != 200) {
			throw new CrawlerException("Bad Response. Expected response code 200/301. Given: " + responseCode);
		}
		
		return false;
	}

}
