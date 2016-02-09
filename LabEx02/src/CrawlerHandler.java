import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public class CrawlerHandler {
	private static ThreadPool Downloaders;
	private static ThreadPool Analyzers;
	private static List<String> CheckedUrls;
	private String DomainIP;
	public static CrawlerStatistics stats;	
	
	public CrawlerHandler(String i_Domain) {
		DomainIP = WebUtils.GetIpByDomain(i_Domain);
		initCrawler();
	}
	
	private void initCrawler() {
		Config config = Config.GetInstance();
		Downloaders = new ThreadPool(config.GetMaxDownloaders());
		Analyzers = new ThreadPool(config.GetMaxAnalyzer());	
		stats = new CrawlerStatistics();
		CheckedUrls = new ArrayList<String>();
	}
	
	public void StartCrawling() {
		stats.initStatistics(DomainIP);
		InsertToDownladers(new UrlDownloader(80, DomainIP));
		// Insert the first url from user to downloaders
	}
	
	public static void doWhenFinished() {
		try {
			JSONObject crawlResult = stats.getStatisticsJson();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO - Handle exceptions
		// Add CrawlStat to the crawls.json file
	}
	
	public static boolean isCrawlDone() {
		if (Downloaders.isFree() && Analyzers.isFree()) {
			try {
				Thread.sleep(3000);
				if (Downloaders.isFree() && Analyzers.isFree()) {
					return true;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public String GetDomainIp() {
		return DomainIP;
	}
	
	// If the url has been crawled we return true, otherwise we return false and add it to the "checked" list;
	public static boolean CheckIfUrlBeenCrawled(String url) {
		boolean toReturn = false;
		if (CheckedUrls.contains(url)) {
			toReturn = true;
		} else {
			CheckedUrls.add(url);
		}
		
		return toReturn;
	}

	public static void InsertToAnalyzers(HtmlAnalyzer htmlToAnalyze) {
		Analyzers.InsertTask(htmlToAnalyze);
	}
	
	public static void InsertToDownladers(UrlDownloader downloader) {
		Downloaders.InsertTask(downloader);
	}
}
