import java.io.* ;
import java.net.* ;

public class HttpRequestHandler implements Runnable {

	private Socket socket;
	private HttpRequestParser request;
	private String root;
	private ResponseFactory responseBuilder;
	private final static String CRLF = "\r\n";
	protected final static String PARAMS_PAGE = "params_info.html";
	private int ResponseCode;
	private String ResponseReason;
	
	public HttpRequestHandler(Socket socket, String root, String defaultPage) {
		this.socket = socket;
		this.root = root;
		request = new HttpRequestParser(defaultPage);
		ResponseCode = 200;
	}
	
	public String GetDomainFromRequest() {
		String toReturn = "";
		if (request != null && request.GetParams() != null && !request.GetParams().get("domain").isEmpty()) {
			toReturn = request.GetParams().get("domain").replace("%3a", ":").replace("%2f", "/");
		}
		
		return toReturn;
	}

	@Override
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void processRequest() throws IOException {		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
		
		try {
			request.parseRequest(reader);
		} catch (HttpRequestException e) {
			System.out.println(e.getMessage());
			ResponseCode = 400;
			ResponseReason = "Bad Request";
		}
		
		if (isCrawlingRequest()) {
			CrawlerHandler crawler = new CrawlerHandler(GetDomainFromRequest());
			crawler.StartCrawling();
		}
		
		checkTypeOfResponse();
		
		writer.write(createHTTPResponseHeaders().getBytes());
		writer.write(createHTTPResponseBody(writer));
		writer.write(CRLF.getBytes());
		
		writer.flush();
		writer.close();
		reader.close();
	}
	
	private boolean isCrawlingRequest() {
		return (request.GetRequestType() == eRequestType.POST && request.GetRequestPath().contains("execResults.html"));		
	}

	private void checkTypeOfResponse() {
		responseBuilder = new ResponseFactory(request, root);
		if (ResponseCode != 400) {
			try {
				File temp = new File(root + request.GetRequestPath());
				System.out.println(request.GetMessageBody());
				System.out.println(request.GetRequestPath());
				if (temp.isFile() || request.GetRequestPath().equals(PARAMS_PAGE)) {
					switch (request.GetRequestType()) {
					case GET:
						responseBuilder = new GETResponse(request, root);
						ResponseCode = 200;
						ResponseReason = "OK";
						break;
					case POST:
						responseBuilder = new GETResponse(request, root);
						ResponseCode = 200;
						ResponseReason = "OK";
						break;
					case TRACE:
						responseBuilder = new TRACEResponse(request, root);
						ResponseCode = 200;
						ResponseReason = "OK";
						break;
					case HEAD:
						responseBuilder = new HEADRespones(request, root);
						ResponseCode = 200;
						ResponseReason = "OK";
						break;
					default:
						ResponseCode = 501;
						ResponseReason = "Unimplemented Method";
						break;
					}
				} else {
					ResponseCode = 404;
					ResponseReason = "File Not Found";
				}
			} catch (Exception e) {
				ResponseCode = 500;
				ResponseReason = "Internal Server Error";
			}
		}
	}

	private byte[] createHTTPResponseBody(DataOutputStream writer)  throws IOException {
		if (ResponseCode != 200) {
			return new byte[0];
		}
		byte[] toReturn = responseBuilder.MakeResponseBody(writer);
		return toReturn;
	}
	
	private String createHTTPResponseHeaders() {
		String toReturn =  "";
		try {
			toReturn = responseBuilder.MakeResponseHeaders(ResponseCode, ResponseReason);
		} catch (Exception e) {
			toReturn = responseBuilder.MakeResponseHeaders(500, "Internal Server Error");
		}
		
		return toReturn;
	}

}
