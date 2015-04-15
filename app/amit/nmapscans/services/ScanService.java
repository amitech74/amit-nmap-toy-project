package amit.nmapscans.services;


import amit.nmapscans.entities.Host;
import amit.nmapscans.entities.ScanStatus;
import amit.nmapscans.entities.Scans;
import amit.nmapscans.entities.builders.ScansBuilder;
import amit.nmapscans.exec.NMapExecutor;
import amit.nmapscans.models.*;
import amit.nmapscans.services.entity.HostsEntityService;
import amit.nmapscans.services.entity.ScansEntityService;
import amit.nmapscans.util.EvaluatePorts;
import amit.nmapscans.util.SpringUtils;
import amit.nmapscans.xml.NMAPXmlUtil;
import amit.nmapscans.xml.jaxb.Nmaprun;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service("ScansService")
public class ScanService {
	private ExecutionContext myExecutionContext;

	@Autowired
	private HostsEntityService hostsEntityService;

	@Autowired
	private ScansEntityService scansEntityService;

	@Autowired
	private ProcessScanResultService processScanResultService;

	public ScanService() {
		myExecutionContext = Akka.system().dispatchers().lookup("play.akka.actor.my-context");
	}

	public F.Promise<Result> initiateHostScan(final String hostName)  {

		return F.Promise.promise(
						new F.Function0<Result>() {
							public Result apply()  {

								//-- Validates the IP/Hostname.  Error should be recovered by the caller.
								try {
									InetAddress.getByName(hostName);
								} catch (UnknownHostException e) {
									return ModelsUtil.prepareBadRequestResult("400", "Invalid/Unknown IP/host=" + hostName , "");
								}

								Host host = hostsEntityService.addHost(hostName);

								Scans scanEntity = buildScanInitiationEntity(host);

								scanEntity = scansEntityService.save(scanEntity);

								//-- Initiate the scanning process here
								NMapExecutor nMapExecutor = SpringUtils.getBean(SpringUtils.NMapExecutor, NMapExecutor.class);
								DefaultExecuteResultHandler scanResultStatus = null;
								try {
									scanResultStatus = nMapExecutor.scanForOpenPorts(hostName);
								} catch (IOException e) {
									return ModelsUtil.prepareInternalServerErrorResult("500", "", e);
								}

								processScanResultService.processScanResult(scanResultStatus, nMapExecutor.getOutputStream(), scanEntity.getId());

								return ModelsUtil.processBusinessOutputIntoResult(buildScanIntiationModel(scanEntity));
							}
						},
						myExecutionContext
		);

	}

	/**
	 * Returns all available scan history for specified host
	 *
	 * @return
	 */
	public F.Promise<Result> retrieveScanHistoryForHost(final String name) {

		//-- retrieve history in a separate execution context, since it can take a while
		return F.Promise.promise(
						new F.Function0<Result>() {
							public Result apply() {
								final List<Scans> scanEntities = scansEntityService.findAllScansForHost(name);

								if(scanEntities==null || scanEntities.size()==0)
									return ModelsUtil.prepareBadRequestResult("400", String.format("Specified host [%s] not found", name) , "");


								return ModelsUtil.processBusinessOutputIntoResult(buildHostScanHistoryList(name, scanEntities));
							}
						},
						myExecutionContext
		);
	}


	public F.Promise<Result> retrieveScanResultForTransactionId(final int transactionId)  {

		return F.Promise.promise(
						new F.Function0<Result>() {
							public Result apply() {
								final List<Scans> scanEntities= scansEntityService.findLastTwoScansForTransactionId(transactionId);

								if(scanEntities==null || scanEntities.size()==0)
									return ModelsUtil.prepareBadRequestResult("400", String.format("Transaction Id %d not found", transactionId) , "");

								Scans newScanEntity = scanEntities.get(0);

								//-- Process the result directly if no history available to compare with
								if(scanEntities.size()==1){
									return ModelsUtil.processBusinessOutputIntoResult(copyOpenPortsDirectlyWithStatus(newScanEntity));
								}

								/*
								*	We have previous scan history available, so compare and prepare result
								* We have an assured guarantee here that there's 2 values in the collection, else runtime can barf.
								* */
								return ModelsUtil.processBusinessOutputIntoResult(compareAndCopyPortsWithStatus(newScanEntity, scanEntities.get(1)));

							}
						},
						myExecutionContext
		);


	}

	private HostScanStatus buildBasicScanStatus(Scans scanEntity){
		ScanStatus nss = scanEntity.getStatus();
		HostScanStatus hss = new HostScanStatus(nss.toString());
		HostScanResult hsr = buildBasicScanResult(scanEntity);
		hss.setHostScanResult(hsr);

		if(! nss.equals(ScanStatus.Complete) && !nss.equals(ScanStatus.InProgress))
			hsr.setErrorStatus(hss.getStatus().toString());

		return hss;
	}


	private HostScanResult buildBasicScanResult(Scans scanEntity){
		HostScanResult hsr = new HostScanResult();

		hsr.setHost(scanEntity.getHost().getName());
		hsr.setTransactionId(scanEntity.getId());
		hsr.setCreatedDate(scanEntity.getCreatedDate().getTime());
		hsr.setUpdatedDate(scanEntity.getUpdatedDate()!=null?scanEntity.getUpdatedDate().getTime():null);
		return hsr;
	}

	private HostScanStatus copyOpenPortsDirectlyWithStatus(Scans scanEntity){
		HostScanStatus hss= buildBasicScanStatus(scanEntity);
		if(! scanEntity.getStatus().equals(ScanStatus.Complete)){
			return hss;
		}

		copyOpenPortsDirectly(hss, scanEntity);

		return hss;
	}


	private void copyOpenPortsDirectly(HostScanStatus hss, Scans scanEntity){
		if(! scanEntity.getStatus().equals(ScanStatus.Complete)){
			return;
		}

		HostScanResult hsr = hss.getHostScanResult();

		try {
			Nmaprun nmaprun = NMAPXmlUtil.parserXMLData(scanEntity.getData());
			if(NMAPXmlUtil.isHostUnreachable(nmaprun) ){
				hsr.setErrorStatus(Errors.HOST_UNREACHABLE);
				return;
			}

			Set<Port> ports = ModelsUtil.copyPorts(NMAPXmlUtil.extractPorts(nmaprun));
			hsr.setPorts(ports);

		} catch (JAXBException e) {
			e.printStackTrace();
			hsr.setErrorStatus(Errors.RESULT_PARSING_ERROR);
		}

		return;
	}

	private HostScanStatus compareAndCopyPortsWithStatus(Scans newScan, Scans prevScan){
		HostScanStatus hss = buildBasicScanStatus(newScan);
		ScanStatus nss = newScan.getStatus();
		if(! nss.equals(ScanStatus.Complete)){
			return hss;
		}

		compareAndCopyPorts(hss, newScan, prevScan);
		return hss;
	}


	private void compareAndCopyPorts(HostScanStatus hss, Scans newScan, Scans prevScan){

		HostScanResult hsr = hss.getHostScanResult();

		try {

			Nmaprun nmaprun = NMAPXmlUtil.parserXMLData(newScan.getData());

			if(NMAPXmlUtil.isHostUnreachable(nmaprun)) {
				hsr.setErrorStatus(Errors.HOST_UNREACHABLE);
				return;
			}

			Set<Port> newPorts = ModelsUtil.copyPorts(NMAPXmlUtil.extractPorts(nmaprun));

			Set<Port> prevPorts = extractPortsIfAvailable(prevScan);

			if(prevPorts ==null) {
				Set<Port> ports = ModelsUtil.copyPorts(NMAPXmlUtil.extractPorts(nmaprun));
				hsr.setPorts(ports);
				return;
			}


			//-- Evaluate ports now
			evaluateAndAddPorts(newPorts, prevPorts, hsr);

		} catch (JAXBException e) {
			e.printStackTrace();
			hsr.setErrorStatus(Errors.RESULT_PARSING_ERROR);
		}

		return;
	}

	private void evaluateAndAddPorts(Set<Port> newPorts, Set<Port> prevPorts, HostScanResult hsr){

		EvaluatePorts.EvaluatedPorts evaluatedPorts = EvaluatePorts.evaluate(newPorts, prevPorts);
		hsr.setPorts(evaluatedPorts.getIntersectionPorts());
		hsr.setAddedPorts(evaluatedPorts.getAddedPorts());
		hsr.setRemovedPorts(evaluatedPorts.getRemovedPorts());

	}

	private Set<Port> extractPortsIfAvailable(Scans scanEntity){
		if(scanEntity.getStatus().equals(ScanStatus.Complete)) {
			Nmaprun nmaprun = null;
			try {
				nmaprun = NMAPXmlUtil.parserXMLData(scanEntity.getData());
			} catch (JAXBException e) {
				e.printStackTrace();
				return null;
			}

			if (NMAPXmlUtil.isHostUnreachable(nmaprun) == false) {
				return ModelsUtil.copyPorts(NMAPXmlUtil.extractPorts(nmaprun));
			}
		}

		return null;
	}


	/**
	 * Compares and builds historic information of all scans for specified host
	 * This method should be called, when there's atleast 1 value in the collection.
	 *
	 * @param hostName
	 * @param scanEntities
	 * @return
	 */
	private HostScanHistory buildHostScanHistoryList(final String hostName, final List<Scans> scanEntities){

		HostScanHistory hsh = new HostScanHistory(hostName);

		//-- queue complete scan entities, to process when we have something to compare with.
		LinkedList<Scans> scansLinkedList = new LinkedList<>();
		//-- lookup reference to get HostScanStatus added already
		Map<Integer, HostScanStatus> hssMap = new HashMap();

		for(Scans currentScan : scanEntities){

			HostScanStatus hss = buildBasicScanStatus(currentScan);
			hsh.add(hss);

			//-- if scan is not complete, then move on to the next one
			if(! currentScan.getStatus().equals(ScanStatus.Complete)){
				continue;
			}

			if(scansLinkedList.size()>0){
				Scans lastScan = scansLinkedList.remove();
				HostScanStatus lastHss = hssMap.get(lastScan.getId());
				//-- Now we are ready to compare and copy
				compareAndCopyPorts(lastHss, lastScan, currentScan);
			}

			//-- queue up this completed one, to compare with the next one
			scansLinkedList.add(currentScan);
			//-- holds reference to scan status in a lookup
			hssMap.put(currentScan.getId(), hss);
		}

		//-- Here we should have only 1 item left in queue, if at all
		if(scansLinkedList.size()>0){
			Scans lastScan = scansLinkedList.remove();
			HostScanStatus lastHss = hssMap.get(lastScan.getId());
			copyOpenPortsDirectly(lastHss, lastScan);
		}

		return hsh;
	}

	private Scans buildScanInitiationEntity(Host host){
		return ScansBuilder.newInstance().host(host).scanStatus(ScanStatus.InProgress).createdDate().build();
	}

	private HostScanInitiated buildScanIntiationModel(Scans scanEntity){
		return ModelsUtil.createHostScanIntiated(scanEntity.getId(), scanEntity.getStatus().toString());
	}
}
