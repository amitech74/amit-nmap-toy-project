package amit.nmapscans.services;

import amit.nmapscans.entities.ScanStatus;
import amit.nmapscans.entities.Scans;
import amit.nmapscans.entities.builders.ScansBuilder;
import amit.nmapscans.services.entity.HostsEntityService;
import amit.nmapscans.services.entity.ScansEntityService;
import amit.nmapscans.xml.NMAPXmlUtil;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import play.libs.Akka;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.xml.bind.JAXBException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Service("ProcessScanResultService")
public class ProcessScanResultService implements IScanResultHandle<DefaultExecuteResultHandler, OutputStream>{

	private ExecutionContext myExecutionContext;

	public final static int WAIT_FOR_RESULTS_SECS = 5;

	@Autowired
	private ScansEntityService scansEntityService;

	public ProcessScanResultService() {
		myExecutionContext = Akka.system().dispatchers().lookup("play.akka.actor.my-context");
	}


	@Override
	public void processScanResult(final DefaultExecuteResultHandler scanResultStatus, final OutputStream resultStream, final int transactionId) {

		//-- Schedule to check for results after 10 seconds
		Akka.system().scheduler().scheduleOnce(
						Duration.create(WAIT_FOR_RESULTS_SECS, TimeUnit.SECONDS),
						new CheckForResults(scanResultStatus, resultStream, transactionId),
						myExecutionContext);
	}

	/**
	 * Checks if results are available to be processed.
	 */
	public class CheckForResults implements Runnable {

		private DefaultExecuteResultHandler scanResultStatus;
		private OutputStream resultStream;
		private int transactionId;

		public CheckForResults(DefaultExecuteResultHandler scanResultStatus, OutputStream resultStream, int transactionId) {
			this.scanResultStatus = scanResultStatus;
			this.resultStream = resultStream;
			this.transactionId = transactionId;
		}

		@Override
		public void run() {

			//-- Check back after 10 seconds if results are not available yet.
			if(!scanResultStatus.hasResult()){
				Akka.system().scheduler().scheduleOnce(
								Duration.create(WAIT_FOR_RESULTS_SECS, TimeUnit.SECONDS),
								new CheckForResults(scanResultStatus, resultStream, transactionId),
								myExecutionContext);
				return;
			}

			//-- Success, so save the results now.
			Scans scanEntity = scansEntityService.find(transactionId);

			//-- Default Scan Status
			ScanStatus scanStatus = ScanStatus.ERROR;
			String xmlResult="";
			if(scanResultStatus.hasResult()){
				xmlResult = resultStream.toString();
				try {
					scanStatus=NMAPXmlUtil.isHostUnreachable(NMAPXmlUtil.parserXMLData(xmlResult))?ScanStatus.HOST_UNREACHABLE:ScanStatus.Complete;
				} catch (JAXBException e) {
					e.printStackTrace();
					scanStatus = ScanStatus.ERROR_XML_PARSING;
				}
			}

			scanEntity = ScansBuilder.updateInstance(scanEntity).scanStatus(scanStatus).updatedDate().data(xmlResult).build();
			scansEntityService.save(scanEntity);

		}

	}
}

