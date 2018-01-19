package provider.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import provider.FileProviderModes;
import provider.FileProviderTypes;
import provider.exception.FileProviderException;
import provider.exception.FileProviderPropertiesValidationException;

/**
 * Defines all properties needed to build and use FileProvider
 * 
 * @author Ivan Ivanov
 *
 */
public class FileProviderProperties {

	private static final Logger log = Logger.getLogger(FileProviderProperties.class);

	/* Property keys */
	public static final String KEY_PROVIDER_TYPE = "provider.type";
	public static final String KEY_FILE_SIZE = "provider.file.size";
	public static final String KEY_FILE_DIRECTORY = "provider.file.read.directory";
	public static final String KEY_FILE_DIRECTORY_STORE = "provider.file.write.directory";
	public static final String KEY_FILE_INTERVAL_BETWEEN_FILES = "provider.file.creation.interval";
	public static final String KEY_PROVIDER_MODE = "provider.mode";
	public static final String KEY_STATIC_FILES_COUNT = "provider.mode.static.files.count";

	private Properties properties;

	public FileProviderProperties() {
		this.properties = new Properties();
	}

	/**
	 * @param propertyFile
	 */
	public FileProviderProperties(File propertyFile) {
		try {
			this.setProperties(loadPropertiesFromFile(propertyFile));
		} catch (FileProviderException e) {
			log.error("Could not load properties from file. File not found!");
		}
	}

	/**
	 * Reads properties from a file.
	 * 
	 * @param file
	 * @return
	 * @throws FileProviderException
	 */
	public static Properties loadPropertiesFromFile(File file) throws FileProviderException {
		Properties properties = new Properties();
		InputStream is = null;
		if (!file.exists())
			throw new FileProviderException("File '" + file.getAbsolutePath() + "' does not exist!");
		try {
			is = FileUtils.openInputStream(file);
			properties.load(is);
		} catch (IOException e) {
			log.error("Could not load TestDriverProperties from File '" + file + "'!", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return properties;
	}

	/**
	 * @param properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return
	 */
	public Properties getProperties() {
		return this.properties;
	}
	
	/**
	 * @return interval between file creation
	 */
	public long getFileCreationalInterval() {
		long interval = 0;
		try {
			interval = Long.parseLong(this.getProperties().getProperty(KEY_FILE_INTERVAL_BETWEEN_FILES));
		} catch (NumberFormatException ex) {
			throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.WRONG_INTERVAL_EXCEPTION);
		}
		return interval;
	}
	
	/**
	 * @param creationalInterval
	 */
	public void setFileCreationalInterval(int creationalInterval) {
		this.getProperties().put(KEY_FILE_INTERVAL_BETWEEN_FILES, creationalInterval);
	}

	/**
	 * @return directory used to store files
	 */
	public String getStoreDirectory() {
		return this.getProperties().getProperty(KEY_FILE_DIRECTORY_STORE);
	}
	
	/**
	 * @param storeDirectory
	 */
	public void setStoreDirectory(String storeDirectory) {
		this.getProperties().put(KEY_FILE_DIRECTORY_STORE, storeDirectory.trim());
	}
	
	/**
	 * @return {@link FileProviderTypes}
	 */
	public String getProviderType() {
		return this.getProperties().getProperty(KEY_PROVIDER_TYPE).toUpperCase().trim();
	}

	/**
	 * @param providerType
	 */
	public void setProviderType(String providerType) {
		this.getProperties().put(KEY_PROVIDER_TYPE, providerType.trim());
	}

	/**
	 * @return size used to create files
	 */
	public String getFileSize() {
		return this.getProperties().getProperty(KEY_FILE_SIZE).trim();
	}

	/**
	 * @param fileSize
	 */
	public void setFileSize(int fileSize) {
		this.getProperties().put(KEY_FILE_SIZE, fileSize);
	}

	/**
	 * @return directory to read the files from
	 */
	public String getFileDirectory() {
		return this.getProperties().getProperty(KEY_FILE_DIRECTORY).trim();
	}

	/**
	 * @param fileDirectory
	 */
	public void setFileDirectory(String fileDirectory) {
		this.getProperties().put(KEY_FILE_SIZE, fileDirectory.trim());
	}
	
	/**
	 * @param mode -> use {@link FileProviderModes}
	 */
	public void setProviderMode(FileProviderModes mode) {
		this.getProperties().put(KEY_PROVIDER_MODE, mode);
	}
	
	/**
	 * @return provider mode
	 */
	public String getProviderMode() {
		return this.getProperties().getProperty(KEY_PROVIDER_MODE).toUpperCase();
	}
	
	/**
	 * @param fileCount
	 */
	public void setStaticModeFileCount(int fileCount) {
		this.getProperties().put(KEY_STATIC_FILES_COUNT, fileCount);		
	}
	
	/**
	 * @return count of the files which will be generated
	 */
	public int getStaticModeFileCount() {
		int fileC = 0;
		try {
			fileC = Integer.parseInt(this.getProperties().getProperty(KEY_STATIC_FILES_COUNT));
		} catch (NumberFormatException ex) {
			throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.WRONG_STATIC_FILE_COUNT);
		}
		return fileC;
	}

	/**
	 * Validates currently provided properties.
	 */
	public void validateProperties() {

		if (this.getProviderType().isEmpty() || (!this.getProviderType().contentEquals(FileProviderTypes.QUICK_FILE_PROVIDER.name())
				&& !this.getProviderType().contentEquals(FileProviderTypes.STATIC_FILE_PROVIDER.name())
				&& !this.getProviderType().contentEquals(FileProviderTypes.RANDOM_FILE_PROVIDER.name())
				&& !this.getProviderType().contentEquals(FileProviderTypes.TEMPLATE_FILE_PROVIDER.name()))) {
			throw new FileProviderPropertiesValidationException(
					FileProviderPropertiesValidationException.EXCEPTION_WRONG_PROVIDER);
		}

		if ((this.getProviderType().equals(FileProviderTypes.QUICK_FILE_PROVIDER.name()) || this.getProviderType().equals(FileProviderTypes.RANDOM_FILE_PROVIDER.name())) && this.getFileSize().isEmpty()) {
			throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_FILE_SIZE_MISSING);
		} else if (this.getProviderType().contentEquals(FileProviderTypes.QUICK_FILE_PROVIDER.name())
				|| this.getProviderType().contentEquals(FileProviderTypes.RANDOM_FILE_PROVIDER.name())) {
			try {
				Integer.parseInt(this.getFileSize());
			} catch (NumberFormatException nfe) {
				throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_WRONG_FILE_SIZE);
			}			
			
		}

		if (this.getFileDirectory().isEmpty() && this.getProviderType().equals(FileProviderTypes.STATIC_FILE_PROVIDER.name())) {
			throw new FileProviderPropertiesValidationException(
					FileProviderPropertiesValidationException.EXCEPTION_DIRECTORY_MISSING);
		} else if (this.getProviderType().equals(FileProviderTypes.STATIC_FILE_PROVIDER.name())) {
			if (!new File(this.getFileDirectory()).exists()) {
				throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_DIRECTORY_DOES_NOT_EXIST);
			}
		}
		
		if (!new File(this.getStoreDirectory()).exists() && !this.getStoreDirectory().isEmpty()) {
			throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_STORE_DIRECTORY_DOES_NOT_EXIST);
		}
		
		this.getFileCreationalInterval();
		
		if (!this.getProviderMode().isEmpty() && this.getProviderMode() != null ) {
			if (!this.getProviderMode().toUpperCase().contentEquals(FileProviderModes.DYNAMIC.name()) 
					&& !this.getProviderMode().toUpperCase().contentEquals(FileProviderModes.STATIC.name())) {
				throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_WRONG_MODE);
			}
		} else {
			throw new FileProviderPropertiesValidationException(FileProviderPropertiesValidationException.EXCEPTION_MODE_MISSING);
		}
		
		if (this.getProviderMode().contentEquals(FileProviderModes.STATIC.name())) {
			this.getStaticModeFileCount();
		}
	}
}
