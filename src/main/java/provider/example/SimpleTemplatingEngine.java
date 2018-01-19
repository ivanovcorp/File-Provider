package provider.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import provider.TemplatingEngine;

/**
 * Simple {@link TemplatingEngine} implementation.
 * 
 * @author Ivan Ivanov
 */
public class SimpleTemplatingEngine implements TemplatingEngine {

	private static final Logger log = Logger.getLogger(SimpleTemplatingEngine.class);

	private int counter = 0;
	private List<GeneratedTamplateFile> generatedFileList;
	private String currentUUID = "";
	private static SimpleTemplatingEngine tpengine = null;

	private SimpleTemplatingEngine() {
		this.generatedFileList = Collections.synchronizedList(new ArrayList<GeneratedTamplateFile>());
	}

	/**
	 * @return {@link SimpleTemplatingEngine}
	 */
	public static synchronized SimpleTemplatingEngine getInstance() {
		if (tpengine == null) {
			tpengine = new SimpleTemplatingEngine();
		}

		return tpengine;
	}

	/* (non-Javadoc)
	 * @see provider.TemplatingEngine#getReplacement()
	 */
	@Override
	public String getReplacement() {
		String uuid = currentUUID;
		String xmlTag = "<uuid>" + uuid + "</uuid>";
		return xmlTag;
	}

	/* (non-Javadoc)
	 * @see provider.TemplatingEngine#getFileName(java.lang.String)
	 */
	@Override
	public synchronized String getFileName(String initialFileName) {
		String generatedFileName = this.counter + "-" + initialFileName;
		String timestamp = LocalDateTime.now().toString();
		GeneratedTamplateFile gf = new GeneratedTamplateFile(generatedFileName, timestamp, currentUUID);
		this.generatedFileList.add(gf);
		return generatedFileName;
	}

	/* (non-Javadoc)
	 * @see provider.TemplatingEngine#initCurrentIteration()
	 */
	@Override
	public synchronized void initCurrentIteration() {
		counter++;
		currentUUID = UUID.randomUUID().toString();
	}

	/**
	 * @return list of generated files.
	 */
	public List<GeneratedTamplateFile> getFileList() {
		return this.generatedFileList;
	}

	/**
	 * @param filename
	 * @return {@link GeneratedTamplateFile}
	 */
	public GeneratedTamplateFile findFileByOriginalName(String filename) {
		for (GeneratedTamplateFile file : this.generatedFileList) {
			if (file.getFileName().equals(filename)) {
				return file;
			}
		}
		return null;
	}

	/**
	 * @return boolean true if all files are found
	 */
	public boolean allFileFound() {
		for (GeneratedTamplateFile file : this.generatedFileList) {
			if (!file.getFound()) {
				log.error("File " + file.getFileName() + " wasn't found.");
				return false;
			}
		}
		return true;
	}
}
