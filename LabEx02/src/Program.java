public class Program {

	public static void main(String[] args) {
		TcpServer webServer = new TcpServer();
		webServer.RunServer();
		//StringBuilder a = new StringBuilder();
	}
	
	private static long GetContentLength(StringBuilder request) {
		long toReturn = -1;
		
		int lengthIndex = request.indexOf("content-length:");
		if (lengthIndex != -1) {
			String docLength = request.substring(lengthIndex + 15, Math.min(request.indexOf("\r", lengthIndex + 15), request.indexOf(" ", lengthIndex + 15)));
			toReturn = Long.parseLong(docLength);
		}
		
		return toReturn;
	}
}
