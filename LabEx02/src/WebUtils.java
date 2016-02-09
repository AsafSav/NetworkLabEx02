import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;


public class WebUtils {
	
	private static final Pattern UrlPattern = Pattern.compile("(?<protocol>http[s]?):\\/\\/[.*\\@]?(www\\.|.*@)?(?<domain>[\\w\\-]+\\.[\\w\\-]+[\\.[\\w\\-]+]*)(?<port>\\:\\d+)?(?<uri>\\S*)");
	private static final String[] UrlGroups = new String[]{"protocol", "domain", "uri", "port"};
	
	public static HashMap<String, String> CutUrl(String url) {
		HashMap<String, String> toReturn = new HashMap<String, String>();
		Matcher m = UrlPattern.matcher(url);
		while (m.find()) {
            for (String name : UrlGroups) {
                String regPattern = m.group(name);
                if (regPattern != null && !regPattern.isEmpty()) {
                    toReturn.put(name, m.group(name));
                }
            }
        }
		
		return toReturn;
	}
			
	public static String GetIpByDomain(String domain) {
		try {
			// TODO: Change The Domain of the resolver.
			Resolver resolver = new SimpleResolver("10.233.104.38");
			Lookup lookup = new Lookup(domain, Type.A);
			lookup.setResolver(resolver);
			Record[] records = lookup.run();
			String toReturn = ((ARecord) records[0]).getAddress().toString().split("/")[0];
			return toReturn.substring(0, toReturn.length() - 1);
		} catch (UnknownHostException e) {
			// HANDLE WEIRD EXCEPTION
		} catch (TextParseException e) {
			// HANDLE WEIRD EXCEPTION
		}
		
		return null;
	}
	
}
