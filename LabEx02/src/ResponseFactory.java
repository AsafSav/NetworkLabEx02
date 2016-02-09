import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;


// A factory of HTTP responses.
public class ResponseFactory {
	
	protected final static String CRLF = "\r\n";
	protected final static int CHUNK_SIZE = 256;
	protected final static String PARAMS_PAGE = "params_info.html";
	
	protected HttpRequestParser request;
	protected String rootDirectory;
	
	public ResponseFactory(HttpRequestParser request, String root) {
		this.request = request;
		rootDirectory = root;
	}
	
	// Returns response with the given code and reason.
	public String MakeResponseHeaders(int segmentCode, String reason) {
		String toReturn = request.GetHttpVersion() + " " + segmentCode + " " + reason + CRLF;
		if (segmentCode != 200) {
			toReturn += CRLF;
		}
		return toReturn;
	}
	
	public byte[] MakeResponseBody(DataOutputStream writer)  throws IOException {
		return new byte[0];
	}
	
	protected byte[] readFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] bFile = new byte[(int)file.length()];
			
			while (fis.available() != 0) {
				fis.read(bFile, 0 , bFile.length);	
			}
			
			fis.close();
			return bFile;
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, file not found to read from");
		} catch (IOException e) {
			System.out.println("Couldn't read properly from file");
		}
		
		return null;
	}
	
	public String paramsHeader() {
		StringBuilder response = new StringBuilder("");
		response.append("Content-Type: " + request.GetContentType().GetType() + CRLF);
		if (request.isChunked()) {
			response.append("Transfer-Encoding: chunked" + CRLF + CRLF);
		} else {
			response.append("Content-Length: " + paramsBody().length + CRLF + CRLF);
		}
		
		return response.toString();
	}
	
	public byte[] paramsBody() {
		StringBuilder sb = new StringBuilder("");
		sb.append("<html><head><title>Params</title></head><body> Params:  <br/>");
		Hashtable<String, String> paramsHash = request.GetParams();
		for (String key : paramsHash.keySet()) {
			sb.append(key + " = " +  paramsHash.get(key) + "<br/>");
		}
		sb.append("</body></html>" + CRLF + CRLF);
		
		return sb.toString().getBytes();
	}
}
