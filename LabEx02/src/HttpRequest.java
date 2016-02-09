
public class HttpRequest {
	
	private String Address;

	public HttpRequest(String i_Address) {
		this.Address = i_Address;
	}
	
	public String CreateRequest(eRequestType requestType) {
		StringBuilder toReturn = new StringBuilder("");
		toReturn.append(requestType.toString());
		toReturn.append(Address);
		toReturn.append(" HTTP/1.1");
			
		return toReturn.toString();
	}
}
