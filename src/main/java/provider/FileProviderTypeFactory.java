package provider;

import java.io.File;

import provider.implementations.QuickFileProvider;
import provider.implementations.RandomFileProvider;
import provider.implementations.StaticFileProvider;
import provider.implementations.TemplatableFileProvider;

import provider.TemplatingEngine;

/**
 * Provides the ready to use implementations of {@link FileProvider}
 *
 * @author Ivan Ivanov
 */
public final class FileProviderTypeFactory {

	private FileProviderTypeFactory() {
		// stateless
	}

	/**
	 * Create new templatable file provider. On each request the provider gets a
	 * file from the list, replaces the first occurrence of "{template}" with the
	 * data from the templating engine and provides it back.
	 * {@link TemplatingEngine#getReplacement()} is called for each request. It can
	 * be used to generate timestamps, unique ids or other data. The concrete
	 * implementation is left to the developer.
	 *
	 * @param templatingEngine
	 *            provides the replacement for each request
	 * @param files
	 *            the files to be processed and send to the test driver on each
	 *            request.
	 * @return {@link FileProvider}
	 */
	public static FileProvider createTemplatableFileProvider(TemplatingEngine templatingEngine, File... files) {
		return new TemplatableFileProvider(templatingEngine, files);
	}

	/**
	 * Create a new static provider. It is used to manage a list of file. On each
	 * request a file from the list is provided back.
	 *
	 * @param files
	 *            the files to be send to the test driver on each request.
	 * @return {@link FileProvider}
	 */
	public static FileProvider createStaticFileProvider(File... files) {
		return new StaticFileProvider(files);
	}

	/**
	 * Create a quick file provider, which generates empty data with predefined size
	 * 
	 * @param dataSize
	 *            data size in byte
	 * @return {@link FileProvider}
	 */
	public static FileProvider createQuickFileProvider(int dataSize) {
		return new QuickFileProvider(dataSize);
	}

	/**
	 * Create a random data file provider. It generates file with with predefined
	 * size and filled with random data
	 * 
	 * @param dataSize
	 *            data size in byte
	 * @return {@link FileProvider}
	 */
	public static FileProvider createRandomFileProvider(int dataSize) {
		return new RandomFileProvider(dataSize);
	}

}
