package provider;

import java.io.IOException;

import provider.data.FileProviderData;

/**
 * Defines what each provider implementation must have. The provided file data
 * must be in a binary format.
 *
 * @author Ivan Ivanov
 */
public interface FileProvider {

	/**
	 * Returns the data of the file. (See {@link FileProviderData})
	 *
	 * @return {@link FileProviderData}
	 */
	FileProviderData getFileData();

	/**
	 * Prepares the data. E.g. reading files in, files processing...
	 *
	 * @throws IOException
	 *             when problems occur during the file data preparations.
	 */
	void prepareFileData() throws IOException;
}
