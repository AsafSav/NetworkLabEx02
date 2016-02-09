import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;

public class HttpRequestParser {
	
	private final static String CRLF = "\r\n";
	
	private String defualtPage;
	
	private String originalRequest;
	private eRequestType requestType;
	private String requestPath;
	private String httpVersion;
	private eContentType contentType;
    private Hashtable<String, String> requestHeaders;
    private Hashtable<String, String> requestParams;
    private StringBuffer messageBody;
	private int bodyLength;
	private String typeExtension;
	private boolean chunked;

    public HttpRequestParser(String defualtPage) {
    	this.defualtPage = defualtPage;
        requestHeaders = new Hashtable<String, String>();
        requestParams = new Hashtable<String, String>();
        messageBody = new StringBuffer();
        chunked = false;
    }
    
    public boolean isChunked() {
    	return chunked;
    }
    
    public String GetTypeExtension() {
    	return typeExtension;
    }
    
    public String GetOriginalRequest() {
    	return originalRequest;
    }
    
    public String GetHttpVersion() {
    	return httpVersion;
    }
    
    public eRequestType GetRequestType () {
    	return requestType;
    }
    
    public String GetRequestPath() {
    	return requestPath;
    }
    
    public Hashtable<String, String> GetParams() {
    	return requestParams;
    }
    
    public String GetHeaderParam(String headerName){
        return requestHeaders.get(headerName);
    }
    
    public String GetMessageBody() {
        return messageBody.toString();
    }
    
    public int GetBodyLength() {
    	return bodyLength;
    }

    public void parseRequest(BufferedReader requestReader) throws IOException, HttpRequestException {
    	
    	StringBuilder origRequest = new StringBuilder("");
    	String givenRequest = requestReader.readLine();
        String[] requestLine;
        
        if (givenRequest == null || givenRequest.length() == 0) {
            throw new HttpRequestException("Invalid Request: " + givenRequest);
        } else {
        	origRequest.append(givenRequest + CRLF);
        	System.out.println(givenRequest);
            requestLine = givenRequest.split(" ");
        }
        
        String header = requestReader.readLine();
        while (!header.equals("") && header != null) {
        	origRequest.append(header + CRLF);
            System.out.println(header);
            appendHeader(header);
            header = requestReader.readLine();
        }
        
        System.out.println();
        origRequest.append(CRLF);
        originalRequest = origRequest.toString();

        if (requestHeaders.containsKey("content-length") || requestHeaders.containsKey("transfer-encoding")) {
        	int contentLength = Integer.parseInt(requestHeaders.get("content-length"));
        	char[] bodyMessage = new char[contentLength];
        	requestReader.read(bodyMessage);
        	StringBuilder params = new StringBuilder("");
        	for (char c : bodyMessage) {
				params.append(c);
			}

        	appendMessageBody(params.toString());
        	bodyLength = contentLength;
        }
        
        appendRequestParams(requestLine);
        checkChunked();
    }

	private void checkChunked() {
		if (requestHeaders.containsKey("chunked") && requestHeaders.get("chunked").equals("yes")) {
			chunked = true;
		}		
	}

	private void appendHeader(String header) throws HttpRequestException {
        int headerIndex = header.indexOf(":");
        if (headerIndex != -1) {
       	 requestHeaders.put(
    			 header.substring(0, headerIndex).toLowerCase(), 
    			 header.substring(headerIndex + 1, header.length()).toLowerCase().trim());
        } else {
            throw new HttpRequestException("Invalid Header Parameter: " + header);
        }
    }
	
	private void appendParams(String param) throws HttpRequestException {
        int paramsIndex = param.indexOf("=");
        if (paramsIndex != -1) {
       	 requestParams.put(
    			 param.substring(0, paramsIndex).toLowerCase(), 
    			 param.substring(paramsIndex + 1, param.length()).toLowerCase());
        } else {
            throw new HttpRequestException("Invalid Parameter: " + param);
        }
    }
    
    private void appendMessageBody(String bodyLine) {
        messageBody.append(bodyLine).append(CRLF);
    }
    
    private void appendRequestParams(String[] requestLine) throws HttpRequestException {
        String[] params;
        String tempPath = null;
    	
    	switch (requestLine[0]) {
		case "GET":
			requestType = eRequestType.GET; 
			break;
		case "POST":
			requestType = eRequestType.POST;
			break;
		case "HEAD":
			requestType = eRequestType.HEAD;
			break;
		case "TRACE":
			requestType = eRequestType.TRACE;
			break;
		default:
			requestType = eRequestType.OTHER;
			break;
		}
    	
		if (requestLine[2].toUpperCase().contains("HTTP/")) {
			httpVersion = requestLine[2];
		} else {
			throw new HttpRequestException("Invalid HTTP Version");
		}
		
        if (requestLine[1].contains("?")) {
        	params = requestLine[1].split("\\?");
        	tempPath = params[0];
        	params = params[1].split("&");
        	for (int i = 0; i < params.length; i++) {
                appendParams(params[i]);
			}
		} else {
			tempPath = requestLine[1];
		}
		
        if (!GetMessageBody().isEmpty()) {
        	params = GetMessageBody().trim().split("&");
        	for (int i = 0; i < params.length; i++) {
                appendParams(params[i]);
			}
		}
        
		if (tempPath.equals("/")) {
			requestPath = defualtPage;
		} else {
			requestPath = tempPath.replace("/", "\\");
		}
		
		if (requestPath.charAt(0) != '\\'); {
			requestPath = "\\" + requestPath;
		}
		
		if (requestPath.indexOf("..\\") == 0) {
			requestPath = requestPath.substring(3);
		}
		
		int dotIndex = requestPath.indexOf(".") + 1;
		if (dotIndex != 0) {
			typeExtension = requestPath.substring(dotIndex).toLowerCase();
		} else {
			throw new HttpRequestException("Bad HTTP Request Path");
		}
		
		switch (typeExtension) {
		case "html":
			contentType = eContentType.text;
			break;
		case "bmp":
			contentType = eContentType.image_bmp;
			break;
		case "gif":
			contentType = eContentType.image_gif;
			break;
		case "png":
			contentType = eContentType.image_png;
			break;
		case "jpg":
			contentType = eContentType.image_jpg;
			break;	
		case "ico":
			contentType = eContentType.icon;
			break;
		default:
			contentType = eContentType.application;
			break;
		}
    }

	public eContentType GetContentType() {
		return contentType;
	}
    
}