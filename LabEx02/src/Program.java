import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import org.omg.CORBA.DATA_CONVERSION;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;



public class Program {

	public static void main(String[] args) {
		TcpServer webServer = new TcpServer();
		webServer.RunServer();
	}
}
