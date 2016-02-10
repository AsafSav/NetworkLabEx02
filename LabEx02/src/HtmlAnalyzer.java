import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HtmlAnalyzer implements Runnable {

	private Config config;
	private StringBuilder Html;
	private List<String> Suspectedlinks;
	private Pattern hrefPattern = Pattern.compile("href=\"(.*?)\"");
	private int Port;
	
	public HtmlAnalyzer(StringBuilder Html) {
		this.Html = Html;
		config = Config.GetInstance();
		Port = 80;
	}
	
	@Override
	public void run() {
		try {
			Analyze();
		} catch (CrawlerException e) {
			System.out.println(e.getMessage());
		}
	}

	private void Analyze() throws CrawlerException {
		int responseCode =  Integer.parseInt(Html.substring(9, 12));
		if (responseCode == 301) {
			int locationIndex = Html.indexOf("Location:");
			if (locationIndex != -1) {
				String path = Html.substring(locationIndex + 10, Math.min(Html.indexOf("\n", locationIndex + 10), Html.indexOf(" ", locationIndex + 10)));
				CrawlerHandler.InsertToDownladers(new UrlDownloader(Port, path));
				return;
			}
		} else if (responseCode != 200) {
			throw new CrawlerException("Not a valid response. Expected 200, Given " + responseCode);
		}
		
		findHrefs();
		handleLinks();
		if (CrawlerHandler.isCrawlDone()) {
			CrawlerHandler.doWhenFinished();
		}
		
	}
	
	private void handleLinks() {
		for(String link : Suspectedlinks) {
			int dotIndex = link.indexOf(".");
			if (dotIndex != -1) {
				String ext = link.substring(dotIndex + 1);
				if (config.isImageExtension(ext)) {
					// Create head request and handle statistics
				} 
				else if (config.isVideoExtension(ext)) {
					// Create head request and handle statistics
				}
				else if (config.isDocumentExtension(ext)) {
					// Create head request and handle statistics
				}
				else {
					
					CrawlerHandler.InsertToDownladers(new UrlDownloader(Port, link)); // TODO - CHECK HOW WE GET THE ACTUAL PORT
					// Update statistics
				}
			}
		}
		
	}

	private void findHrefs() {
		Matcher matcher = hrefPattern.matcher(Html);
		String link = "";
		while (matcher.find()) {
			link = matcher.group();
			if (!Suspectedlinks.contains(link) && CrawlerHandler.CheckIfUrlBeenCrawled(link) && isValidDomain(link)) {
				Suspectedlinks.add(link);
			}
		}
 	}
	
	private boolean isValidDomain(String link) {
		boolean toReturn = true;
		// CHECK IF VALID - CURRENT DOMAIN
		return toReturn;
	}
	
	private String HttpHeadRequest(String link) {
		// ASK JONATHAN HOW TO SEND THE HEAD REQUEST
		HttpRequest headRequset = new HttpRequest(link);
		return headRequset.CreateRequest(eRequestType.HEAD);
	}
	
	// NOT SURE IF WE NEED THIS
	private String DetrmineType(String ext) {
		eContentType type = eContentType.icon;
		switch (ext) {
		case "html":
			type = eContentType.text;
			break;
		case "bmp":
			type = eContentType.text;
			break;
		case "jpg":
			type = eContentType.text;
			break;
		case "png":
			type = eContentType.text;
			break;
		case "gif":
			type = eContentType.text;
			break;
		case "ico":
			type = eContentType.text;
			break;
		case "avi":
			type = eContentType.text;
			break;
		case "mpg":
			type = eContentType.text;
			break;
		case "mp4":
			type = eContentType.text;
			break;
		case "wmv":
			type = eContentType.text;
			break;
		case "mov":
			type = eContentType.text;
			break;
		case "flv":
			type = eContentType.text;
			break;
		case "swf":
			type = eContentType.text;
			break;
		case "mkv":
			type = eContentType.text;
			break;
		case "pdf":
			type = eContentType.text;
			break;
		case "doc":
			type = eContentType.text;
			break;
		case "docx":
			type = eContentType.text;
			break;
		case "xls":
			type = eContentType.text;
			break;
		case "xlsx":
			type = eContentType.text;
			break;
		case "ppt":
			type = eContentType.text;
			break;
		case "pptx":
			type = eContentType.text;
			break;
		default:
			break;
		}
		
		return type.GetType();
	}
	
	
	
	

}
