package provider.implementations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import provider.FileProvider;
import provider.TemplatingEngine;
import provider.data.FileProviderData;

/**
 * Implementation of {@link FileProvider}
 * Uses {@link TemplatingEngine}
 * 
 * @author Ivan Ivanov
 */
public class TemplatableFileProvider implements FileProvider {

	private static final Logger log = Logger.getLogger(TemplatableFileProvider.class);
	private static final String TEMPLATE_PLACEHOLDER = "{template}";

	private TemplatingEngine templatingEngine;
	private File[] files;
	private int fileDataRingIndex;
	private String[][] filesInMemory;
	private int[] lineIndex;

	/**
	 * @param templatingEngine
	 * @param files
	 */
	public TemplatableFileProvider(TemplatingEngine templatingEngine, File... files) {
		if (files[0].isDirectory()) {
			this.files = files[0].listFiles();
		} else {
			this.files = files;
		}
		this.templatingEngine = templatingEngine;
		this.filesInMemory = new String[this.files.length][];
		this.lineIndex = new int[this.files.length];
	}

	/**
	 * @see provider.FileProvider#getFileData()
	 */
	public FileProviderData getFileData() {
		// init templating engine for the current iteration
		templatingEngine.initCurrentIteration();

		// get current file data
		fileDataRingIndex %= filesInMemory.length;
		String[] currentFileData = filesInMemory[fileDataRingIndex];

		FileProviderData providerData = null;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				PrintWriter outputWriter = new PrintWriter(outputStream, true)) {

			for (int i = 0; i < currentFileData.length; i++) {
				if (i == lineIndex[fileDataRingIndex]) {
					outputWriter.append(
							currentFileData[i].replace(TEMPLATE_PLACEHOLDER, templatingEngine.getReplacement()) + "\n");
				} else {
					outputWriter.append(currentFileData[i] + "\n");
				}
			}
			outputWriter.flush();
			byte[] result = outputStream.toByteArray();
			providerData = new FileProviderData(result,
					templatingEngine.getFileName(files[fileDataRingIndex].getName()));
			fileDataRingIndex++;
		} catch (IOException e) {
			log.error("Cannot write content.", e);
		}

		// should not happen
		return providerData;
	}

	/**
	 * @see provider.FileProvider#prepareFileData()
	 */
	@Override
	public void prepareFileData() throws IOException {
		for (int i = 0; i < files.length; i++) {
			ArrayList<String> lines = new ArrayList();

			try (BufferedReader reader = new BufferedReader(new FileReader(files[i]))) {
				String oneLine;
				int index = 0;
				while ((oneLine = reader.readLine()) != null) {
					if (oneLine.trim().length() > 0) {
						if (oneLine.contains(TEMPLATE_PLACEHOLDER)) {
							lineIndex[i] = index;
						}
						index++;
						lines.add(oneLine);
					}
				}
			}
			filesInMemory[i] = lines.toArray(new String[lines.size()]);
		}
	}
}
