package provider.data;

import provider.FileProvider;

/**
 * Encapsulates the file data provided by {@link FileProvider}
 * 
 * @author Ivan Ivanov
 */
public class FileProviderData {

	private byte[] fileContent;
	private String fileName;

	/**
	 * @param fileContent
	 * @param fileName
	 */
	public FileProviderData(byte[] fileContent, String fileName) {
		this.fileContent = fileContent;
		this.fileName = fileName;
	}

	/**
	 * @return file content
	 */
	public byte[] getFileContent() {
		return fileContent;
	}

	/**
	 * @return file name
	 */
	public String getFileName() {
		return fileName;
	}
}
