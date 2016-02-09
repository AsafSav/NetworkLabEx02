import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Config {
	
	//private String configPath = "config.ini";
	private static Config TheInstance = null;
	private static Object lockObj = new Object();
	
	public static Config GetInstance() {
		synchronized (lockObj) {
			if (TheInstance == null) {
				TheInstance = new Config();
			}
			
			return TheInstance;
		}
	}
	
	private Config() {
		ReadConfig();
	}
	
	private int serverPort;
	private String serverRoot;
	private String defaultPage;
	private int maxThreads;
	private int maxDownloaders;
	private int maxAnalyzers;
	private List<String> imageExtensions;
	private List<String> videoExtensions;
	private List<String> documentExtensions;
	
	public int GetPort() {
		return serverPort;
	}
	
	public String GetRoot() {
		return serverRoot;
	}
	
	public String GetDefaultPage() {
		return defaultPage;
	}
	
	public int GetMaxThreads() {
		return maxThreads;
	}

	public int GetMaxDownloaders() {
		return maxDownloaders;
	}
	
	public int GetMaxAnalyzer() {
		return maxAnalyzers;
	}
	
	public boolean isValidExtension(String ext) {
		return (imageExtensions.contains(ext) || videoExtensions.contains(ext) || documentExtensions.contains(ext));	
	}
	
	public boolean isImageExtension(String ext) {
		return imageExtensions.contains(ext);
	}
	
	public boolean isVideoExtension(String ext) {
		return videoExtensions.contains(ext);
	}
	
	public boolean isDocumentExtension(String ext) {
		return documentExtensions.contains(ext);
	}
	
	public void ReadConfig() {
		try {
			
			String PathToFind = System.getProperty("user.dir") + "\\Config.ini";
			InputStream streamer = new FileInputStream(PathToFind);
			BufferedReader reader = new BufferedReader(new InputStreamReader(streamer));
			HashMap<String, String> configMap = new HashMap<String, String>();
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					int eqIndex = line.indexOf("=");
					if (eqIndex != -1) {
						configMap.put(line.substring(0,eqIndex), line.substring(eqIndex + 1));
					}
				}
				
				serverPort = Integer.parseInt(configMap.get("port"));
				serverRoot = configMap.get("root");
				defaultPage = configMap.get("defaultPage");
				maxThreads = Integer.parseInt(configMap.get("maxThreads"));
				maxDownloaders = Integer.parseInt(configMap.get("maxDownloaders"));
				maxAnalyzers = Integer.parseInt(configMap.get("maxAnalyzers"));
				imageExtensions = Arrays.asList(configMap.get("imageextensions").split(","));
				videoExtensions = Arrays.asList(configMap.get("videoextensions").split(","));
				documentExtensions = Arrays.asList(configMap.get("documentextensions").split(","));
				
			} catch (IOException e) {
				// couldn't read from config file
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// couldn't parse the config numbers (port)
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Config File Was Not Found");
			e.printStackTrace();
		}
	}
	
}
