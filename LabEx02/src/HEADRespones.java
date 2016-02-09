import java.io.File;

public class HEADRespones extends ResponseFactory {

	public HEADRespones(HttpRequestParser request, String root) {
		super(request, root);
	}
	
	@Override
	public String MakeResponseHeaders(int segmentCode, String reason) {
		StringBuilder toReturn = new StringBuilder(super.MakeResponseHeaders(segmentCode, reason));
		if (segmentCode == 200) {
			toReturn.append("content-type: " + request.GetContentType().GetType() + CRLF);
			
			File toReadLength = new File(rootDirectory + request.GetRequestPath());
			toReturn.append("content-length: " + toReadLength.length() + CRLF + CRLF);
		}
		
		System.out.println(toReturn);
		return toReturn.toString();
	}
}
