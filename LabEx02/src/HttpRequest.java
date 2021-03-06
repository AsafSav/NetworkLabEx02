
public class HttpRequest {
	
	private final static String CRLF = "\r\n";
	
	private String Address;

	public HttpRequest(String i_Address) {
		this.Address = i_Address;
	}
	
	public String CreateRequest(eRequestType requestType) {
		StringBuilder toReturn = new StringBuilder("");
		toReturn.append(requestType.toString() + " ");
		toReturn.append(Address);
		toReturn.append(" HTTP/1.0");
		toReturn.append(CRLF);
		toReturn.append("Host: " + CrawlerHandler.GetDomain());
		toReturn.append(CRLF + CRLF);
		
		System.out.println("The Request  =  " + toReturn.toString());
		return toReturn.toString();
	}
}
