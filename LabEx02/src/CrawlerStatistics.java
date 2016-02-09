import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


public class CrawlerStatistics {

	public String domain;
	public Date date;
	public static int numImages;
	public static long sizeImages;
	public static int numVideos;
	public static long sizeVideos;
	public static int numDocuments;
	public static long sizeDocuments;
	public static int numFiles;
	public static long sizeFiles;
	public static int internalLinks;
	public static int externalLinks;
	public static int numConnectedDomains;
	public static double averageRTT;
	
	// If u have time - do get/set synchronized for each field
	
	public void initStatistics(String domain) {
		this.domain = domain;
		date = new Date();
		numImages = 0;
		sizeImages = 0;
		numVideos = 0;
		sizeVideos = 0;
		numDocuments = 0;
		sizeDocuments = 0;
		numFiles = 0;
		sizeFiles = 0;
		internalLinks = 0;
		externalLinks = 0;
		numConnectedDomains = 0;
		averageRTT = 0;
	}
	
	public JSONObject getStatisticsJson() throws JSONException {
		HashMap<String, Double> stats = new HashMap<String, Double>();
		stats.put("numImages", (double)numImages);
		stats.put("sizeImages", (double)sizeImages);
		stats.put("numVideos", (double)numVideos);
		stats.put("sizeVideos", (double)sizeVideos);
		stats.put("numDocuments", (double)numDocuments);
		stats.put("sizeDocuments", (double)sizeDocuments);
		stats.put("numFiles", (double)numFiles);
		stats.put("sizeFiles", (double)sizeFiles);
		stats.put("internalLinks", (double)internalLinks);
		stats.put("externalLinks", (double)externalLinks);
		stats.put("numConnectedDomains", (double)numConnectedDomains);
		stats.put("averageRTT", averageRTT);
		JSONObject crawlStat = new JSONObject();
		crawlStat.put("Domain", domain);
		crawlStat.put("Date", DateFormat.getInstance().format(date));
		crawlStat.put("Statistics", stats);
		
		return crawlStat;
	}	
}
