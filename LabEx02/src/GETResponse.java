import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GETResponse extends ResponseFactory {

	public GETResponse(HttpRequestParser request, String root) {
		super(request, root);
	}

	@Override
	public String MakeResponseHeaders(int segmentCode, String reason) {
		StringBuilder toReturn = new StringBuilder(super.MakeResponseHeaders(segmentCode, reason));
		if (segmentCode == 200) {
			if (request.GetRequestPath().equals(PARAMS_PAGE)) {
				toReturn.append(paramsHeader());
			} else {
				toReturn.append("Content-Type: " + request.GetContentType().GetType() + CRLF);
				File toRead = new File(rootDirectory + request.GetRequestPath());
				if (!request.isChunked()) {
					toReturn.append("Content-Length: " + (int)toRead.length() + CRLF + CRLF);
				} else {
					toReturn.append("Transfer-Encoding: chunked" + CRLF + CRLF);
				}
			}
		}
		
		System.out.println(toReturn);
		return toReturn.toString();
	}
	
	@Override
	public byte[] MakeResponseBody(DataOutputStream writer) throws IOException {
		boolean isHtml = false;
		StringBuilder body = new StringBuilder("");
		byte[] toReturn = {};
		switch (request.GetContentType()) {
		case text:
			isHtml = true;
			if (request.GetRequestPath().equals(PARAMS_PAGE)) {
				break;
			}
			BufferedReader fileReader;
			try {
				fileReader = new BufferedReader(new FileReader(rootDirectory + request.GetRequestPath()));
				String line = fileReader.readLine();
					
				while (line != null && !line.isEmpty()) {
					body.append(line + CRLF);
					line = fileReader.readLine();
				}
				
				fileReader.close();
			} catch (IOException e) {
				System.out.println("Sorry, couldn't read from given file");
			}	
			break;
		case image_bmp:
			toReturn = readFile(new File(rootDirectory + request.GetRequestPath()));
			break;
		case image_gif:
			toReturn = readFile(new File(rootDirectory + request.GetRequestPath()));
			break;
		case image_jpg:
			toReturn = readFile(new File(rootDirectory + request.GetRequestPath()));
			break;
		case image_png:
			toReturn = readFile(new File(rootDirectory + request.GetRequestPath()));
			break;
		case icon:
			toReturn = readFile(new File(rootDirectory + request.GetRequestPath()));
			break;
		case application:
			break;
		default:
			break;
		}
		
		if (isHtml) {
			if (request.GetRequestPath().equals(PARAMS_PAGE)) {
				toReturn = paramsBody();
			} else {
				toReturn = body.toString().getBytes();
			}
		}
		
		if (request.isChunked()) {
			int index = 0;
			int countChars = 0;
			int lengthOfBytes = toReturn.length;
			byte[] line = new byte[CHUNK_SIZE];
			byte[] CRLF2Bytes = CRLF.getBytes();
			while (index != lengthOfBytes) {
				line[countChars] = toReturn[index];
				countChars++;
				if (countChars == CHUNK_SIZE) {
					writer.write(Integer.toHexString(CHUNK_SIZE).getBytes());
					writer.write(CRLF2Bytes);
					writer.flush();
					writer.write(line);
					writer.write(CRLF2Bytes);
					writer.flush();
										
					countChars = 0;
					line = new byte[CHUNK_SIZE];
				}
				index++;
			}
			
			if (countChars != 0) {
				writer.write(Integer.toHexString(countChars).getBytes());
				writer.write(CRLF2Bytes);
				writer.flush();
				for (int i = 0; i < countChars; i++) {
					writer.write(line[i]);
					
				}
				writer.write(CRLF2Bytes);
				writer.flush();
			}
			
			writer.write(Integer.toHexString(0).getBytes());
			writer.write(CRLF2Bytes);
			writer.flush();			
			toReturn = new byte[0];
		}
		
		return toReturn;
	}
}
