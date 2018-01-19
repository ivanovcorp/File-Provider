package provider.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import provider.FileProvider;
import provider.data.FileProviderData;

/**
 * Implementation of {@link FileProvider}.
 * Uses pre-created files, used by the API by the properties file.
 * 
 * @author Ivan Ivanov
 */
public class StaticFileProvider implements FileProvider {

	private static final Logger log = Logger.getLogger(StaticFileProvider.class);

	private File[] files;
	private byte[][] fileDataBytes;
	private String[] fileNames;
	private int fileDataRingIndex;

	/**
	 * @param files
	 */
	public StaticFileProvider(File... files) {
		fileDataRingIndex = 0;
		if (files[0].isDirectory()) {
			this.files = files[0].listFiles();
		} else {
			this.files = files;
		}
		fileDataBytes = new byte[this.files.length][];
		fileNames = new String[this.files.length];
	}

	/**
	 * @param files
	 */
	public void setFiles(File[] files) {
		if (files[0].isDirectory()) {
			this.files = files[0].listFiles();
		} else {
			this.files = files;
		}
	}

	/**
	 * @see provider.FileProvider#getFileData()
	 */
	public FileProviderData getFileData() {
		fileDataRingIndex %= fileDataBytes.length;
		byte[] currentFileData = fileDataBytes[fileDataRingIndex];
		String currentFileName = fileNames[fileDataRingIndex];
		fileDataRingIndex++;
		return new FileProviderData(currentFileData, currentFileName);
	}

	/**
	 * @see provider.FileProvider#prepareFileData()
	 */
	public void prepareFileData() throws IOException {
		log.info("Prepare file data.");
		for (int i = 0; i < files.length; i++) {
			Path filePath = files[i].toPath();
			log.info("Read in " + filePath);
			fileDataBytes[i] = Files.readAllBytes(filePath);
			fileNames[i] = files[i].getName();
		}
	}
}
