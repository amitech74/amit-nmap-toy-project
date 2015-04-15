package amit.nmapscans.exec;

import java.io.IOException;

public interface INMapExecutor<T> {

	public T scanForOpenPorts(String host) throws IOException;

}
