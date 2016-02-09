import java.io.DataOutputStream;
import java.io.IOException;


public class TRACEResponse extends ResponseFactory {

	public TRACEResponse(HttpRequestParser request, String root) {
		super(request, root);
	}
	
	@Override
	public String MakeResponseHeaders(int segmentCode, String reason) {
		StringBuilder toReturn = new StringBuilder(super.MakeResponseHeaders(segmentCode, reason));
		String origRequest = request.GetOriginalRequest();
		if (segmentCode == 200) {
			toReturn.append("content-type: " + request.GetContentType().GetType() + CRLF);
			toReturn.append("content-length: " + origRequest.length() + CRLF + CRLF);
		}
		
		System.out.println(toReturn + CRLF);
		return toReturn.toString();
	}
	
	@Override
	public byte[] MakeResponseBody(DataOutputStream writer)  throws IOException {
		return request.GetOriginalRequest().getBytes();
	}

}
