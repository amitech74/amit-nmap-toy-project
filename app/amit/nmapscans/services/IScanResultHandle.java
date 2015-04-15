package amit.nmapscans.services;

public interface IScanResultHandle<T, S> {

	public void processScanResult(T scanResultStatus, S resultStream, int transactionId);
}
