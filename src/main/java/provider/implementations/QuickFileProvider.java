package provider.implementations;

import java.io.IOException;
import java.util.UUID;

import provider.FileProvider;
import provider.data.FileProviderData;

/**
 * Implementation of {@link FileProvider}. 
 * Generates dummy files by given size, without any data.
 * 
 * @author Ivan Ivanov
 */
public class QuickFileProvider implements FileProvider {

	private byte[] fileData;

	/**
	 * @param dataSizeByte
	 */
	public QuickFileProvider(int dataSizeByte) {
		fileData = new byte[dataSizeByte];
	}

	/**
	 * @see provider.FileProvider#getFileData()
	 */
	public FileProviderData getFileData() {
		String randomName = UUID.randomUUID().toString();
		return new FileProviderData(fileData, randomName);
	}

	/**
	 * No external data is used in the quick file provider -> ignored
	 */
	public void prepareFileData() throws IOException {
		// ignored
	}
}
