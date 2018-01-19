package provider;

import java.io.File;

import org.apache.log4j.Logger;

import provider.properties.FileProviderProperties;
import provider.runner.FileProviderRunner;

/**
 * Defines creation of the File Provider.
 * 
 * @author Ivan Ivanov
 */
public final class FileProviderFactory {

	private static final Logger log = Logger.getLogger(FileProviderFactory.class);

	private FileProviderFactory() {
		// stateless
	}

	/**
	 * Initializes File Provider Runner component.
	 * 
	 * @param properties
	 * @param tpEngine
	 * @param files
	 * @return {@link FileProviderRunner}
	 */
	public static FileProviderRunner initializeFileProvider(FileProviderProperties properties, TemplatingEngine tpEngine, File...files) {

		FileProviderRunner fpr = null;
		log.info("Validating properties.");
		properties.validateProperties();

		FileProvider fp = null;
		switch (FileProviderTypes.valueOf(properties.getProviderType())) {
			case QUICK_FILE_PROVIDER:
				fp = FileProviderTypeFactory.createQuickFileProvider(Integer.valueOf(properties.getFileSize()));
				break;
			case RANDOM_FILE_PROVIDER:
				fp = FileProviderTypeFactory.createRandomFileProvider(Integer.valueOf(properties.getFileSize()));
				break;
			case STATIC_FILE_PROVIDER:
				fp = FileProviderTypeFactory.createStaticFileProvider(new File(properties.getFileDirectory()));
				break;				
			case TEMPLATE_FILE_PROVIDER:
				fp = FileProviderTypeFactory.createTemplatableFileProvider(tpEngine, files);
				break;
		}

		fpr = new FileProviderRunner(fp, properties);
		
		return fpr;
	}
}
