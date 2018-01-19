package provider.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import provider.FileProvider;
import provider.FileProviderModes;
import provider.data.FileProviderData;
import provider.exception.FileProviderException;
import provider.exception.FileProviderRuntimeException;
import provider.properties.FileProviderProperties;


/**
 * Defines the main File Provider component. 
 * It manages the state, the generation of files and overall controll over the API.
 * 
 * @author Ivan Ivanov
 */
public class FileProviderRunner {
	
	private static final Logger log = Logger.getLogger(FileProviderRunner.class);

	private static final long FIRST_EMITTED = 0L;
	
	private static long startTime = 0;
	private static long endTime = 0;
	
	
	/**
	 * Represents the state of the File Provider.
	 * 
	 * @author Ivan Ivanov	 
	 */
	public enum STATE {
		RUNNING, STOPPED
	}
	
	private FileProvider fileProvider;
	private Disposable controllerSubscription;
    private Disposable driverSubscription;
    private int processedFilesCount;
    private Map<String, Long> processedFiles;
    private FileProviderProperties properties;
    
    private STATE state;
	
	/**
	 * @param fileProvider
	 * @param properties
	 */
	public FileProviderRunner(FileProvider fileProvider, FileProviderProperties properties) {
		this.fileProvider = fileProvider;
		this.properties = properties;
		this.processedFilesCount = 0;
		this.processedFiles = new HashMap<>();
	}
	
	/**
	 * Used to start processing of the {@link FileProvider}
	 */
	public void start() {
		log.setLevel(Level.DEBUG);
		this.state = STATE.RUNNING;
		log.info("Starting file provider");
		startTime = System.currentTimeMillis();
		try {
			this.fileProvider.prepareFileData();
		} catch (IOException e1) {
			log.error("Failed to prepare the file data.");
		}
		
		ConnectableFlowable<Long> controllingObs = Flowable.interval(this.properties.getFileCreationalInterval(), TimeUnit.MILLISECONDS, Schedulers.newThread())
                .startWith(FIRST_EMITTED)                              
                .onBackpressureDrop()
                .publish();
		Flowable<FileProviderData> fileProvidingObs = Flowable.just(1)
				.map(tick -> fileProvider.getFileData())
				.subscribeOn(Schedulers.computation())
				.repeatWhen(flowable -> controllingObs);
		
		this.controllerSubscription = controllingObs.connect();
				
		if (this.properties.getProviderMode().equals(FileProviderModes.STATIC.name()) ) {
			long startT = System.currentTimeMillis();			
			int expectedCount = this.properties.getStaticModeFileCount();
			while (this.processedFilesCount < expectedCount) {
				this.saveFile(this.fileProvider.getFileData());		
				try {
					Thread.sleep(this.properties.getFileCreationalInterval());
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			}
			this.state = STATE.STOPPED;
			log.debug("Static File Provider runned for: " + (System.currentTimeMillis() - startT) / 1000.0 + " seconds.");
			
		} else {			
			this.driverSubscription = fileProvidingObs.subscribe(this::saveFile);
		}
	}
	
	/**
	 * Stops the File Provider API.
	 * 
	 * @return result of stop invokation
	 * @throws FileProviderException
	 */
	public boolean stop() throws FileProviderException {
		if (state == STATE.STOPPED) {
            log.warn("Cannot stop! File Provider is already stopped");
            return true;
        }
        log.info("Stoping File Provider.");
        controllerSubscription.dispose();
        int retries = 1;
        while (!controllerSubscription.isDisposed() && retries <= 5) {
        	log.info("Provider still running. Retry stopping: " + retries);
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
        	retries++;
        }
			
        state = STATE.STOPPED;

        if(state != STATE.STOPPED) 
        	throw new FileProviderException(FileProviderException.COULD_NOT_STOP_EXCEPTION);
        
        endTime = System.currentTimeMillis();
		log.debug("Provider runned for: " + (endTime - startTime) / 1000.0 + " seconds.");
        return true;
	}
	
	private void saveFile(FileProviderData providerData) {
		this.processedFilesCount++;
		String fileName = System.currentTimeMillis() + providerData.getFileName();
		Path pth = null;
		if (!this.properties.getStoreDirectory().isEmpty() && this.properties.getStoreDirectory() != null) {			
			pth = Paths.get(this.properties.getStoreDirectory() + "/" + fileName);			
		} else {
			pth = Paths.get("target/output/" + fileName);			
		}
		
		try {
			Files.write(pth, providerData.getFileContent(), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			log.error(e);
		}
		log.debug("File " + fileName + " saved.");		
		this.processedFiles.put(fileName, System.currentTimeMillis());
	}
	
	/**
	 * Prints results of the current {@link FileProvider} processing.
	 */
	public void getResults() {
		int retries = 1;
		log.info("Results are being generated...");
		while (this.processedFilesCount > this.processedFiles.size() && retries <= 5) {
			log.warn("Results not ready. Sleeping for 1 second. Retry " + retries + "/5.");
			try {
				
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage());
			}
			if (retries == 5 && this.processedFilesCount > this.processedFilesCount) {
				throw new FileProviderRuntimeException(FileProviderRuntimeException.FAILED_TO_PARSE_RESULTS_EXCEPTION);
			}
		}
		log.info("#####################################################");
		for (Map.Entry<String, Long> e : this.processedFiles.entrySet()) {
			log.info("------------------------------------------------------------");
			log.info("| File: " + e.getKey() + " | Processed at: " + convertToNormalDateTime(e.getValue()) + " |");
			log.info("------------------------------------------------------------");
		}
		log.info("#####################################################");
	}
	
	/**
	 * @return count of processed files
	 */
	public int getProcessedFiles() {
		return this.processedFilesCount;
	}
	
	private String convertToNormalDateTime(long time) {
		DateFormat formatter = new SimpleDateFormat("[dd-MM-yyyy-HH:mm:ss]");
		
		Date date = new Date(time);
		return formatter.format(date);
	}
}
