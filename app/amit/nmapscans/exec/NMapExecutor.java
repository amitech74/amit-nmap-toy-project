package amit.nmapscans.exec;

import org.apache.commons.exec.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service("NMapExecutor")
@Scope("prototype")
public class NMapExecutor implements INMapExecutor<DefaultExecuteResultHandler> {

	private static final ProcessDestroyer SHUTDOWN_HOOK_PROCESS_DESTROYER = new ShutdownHookProcessDestroyer();
	private Executor defaultExecutor;
	private CommandLine cmdLine;
	private DefaultExecuteResultHandler resultHandler;
	private PumpStreamHandler pumpStreamHandler;
	private OutputStream outputStream;

	public NMapExecutor() {

		initNmapCommandLine();

		initDefaultExecutor();

		initResultHandler();
	}

	private void initResultHandler(){
		resultHandler = new DefaultExecuteResultHandler();
	}

	private void initDefaultExecutor(){
		//-- Used to kill process if timedout - currently waiting forever
		ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);

		//-- Prepare stream to read complete output as String later
		outputStream = new ByteArrayOutputStream();
		pumpStreamHandler = new PumpStreamHandler(outputStream);

		//-- initialize the default executor along with shutdown hook, to kill all processes, when jvm exits
		defaultExecutor = new DefaultExecutor();
		defaultExecutor.setExitValue(1);
		defaultExecutor.setWatchdog(watchdog);
		defaultExecutor.setProcessDestroyer(SHUTDOWN_HOOK_PROCESS_DESTROYER);
		defaultExecutor.setStreamHandler(pumpStreamHandler);

	}

	private void initNmapCommandLine(){
		cmdLine = new CommandLine("nmap");
		//-- scan 0 to 1000 ports
		cmdLine.addArgument("-p");

		cmdLine.addArgument("0-1000");

		//-- interested only in open ports
		cmdLine.addArgument("--open");

		//-- request xml output
		cmdLine.addArgument("-oX");
		cmdLine.addArgument("-");
	}

	@Override
	public DefaultExecuteResultHandler scanForOpenPorts(String host) throws IOException {

		//-- Add host to the command line
		cmdLine.addArgument(host);

		defaultExecutor.execute(cmdLine, resultHandler);

		return resultHandler;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
}
