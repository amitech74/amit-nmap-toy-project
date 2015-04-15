package amit.nmapscans.xml;

import amit.nmapscans.xml.jaxb.Nmaprun;
import org.apache.commons.jxpath.JXPathContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class NMAPXmlUtil {
	private static final String CONFIG_PACKAGE_PATH="amit.nmapscans.xml.jaxb";


	public static Nmaprun parserXMLData(String xml) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(CONFIG_PACKAGE_PATH);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (Nmaprun)unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
	}


	public static List<Nmaprun.Host.Ports.Port> extractPorts(Nmaprun nmaprunObject){

		List<Nmaprun.Host> hostList = (List) JXPathContext.newContext(nmaprunObject).getValue("host", List.class);
		if(hostList.size()==0)
			return new ArrayList<>();


		List<Nmaprun.Host.Ports.Port> ports = (List) JXPathContext.newContext(nmaprunObject).getValue("host[1]/ports/port", List.class);
		if (ports == null){
			return new ArrayList<>();
		}

		return ports;
	}

	public static Boolean isHostUnreachable(Nmaprun nmaprunObject){
		Nmaprun.Runstats.Hosts hosts = (Nmaprun.Runstats.Hosts) JXPathContext.newContext(nmaprunObject).getValue("runstats/hosts[1]", Nmaprun.Runstats.Hosts.class);
		if(hosts!=null) {
			return !hosts.getUp().equals("1");
		}

		return true;
	}

}
