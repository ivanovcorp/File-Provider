package provider.implementations;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import provider.FileProvider;
import provider.data.FileProviderData;

/**
 * Implementation of {@link FileProvider}. 
 * Generates files by given size, with random data.
 * 
 * @author Ivan Ivanov
 */
public class RandomFileProvider implements FileProvider {

	private byte[] fileData;

	/**
	 * @param dataSizeByte
	 */
	public RandomFileProvider(int dataSizeByte) {
		fileData = new byte[dataSizeByte];
	}

	/**
	 * @see provider.FileProvider#getFileData()
	 */
	public FileProviderData getFileData() {
		String randomName = UUID.randomUUID().toString();
		Random random = new Random();
		random.nextBytes(fileData);

		return new FileProviderData(fileData, randomName);
	}

	/**
	 * No external data is used in the complex file provider -> ignored
	 */
	public void prepareFileData() throws IOException {
		// ignored
	}
}
