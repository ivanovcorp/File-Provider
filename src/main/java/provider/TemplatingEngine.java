package provider;

/**
 * Defines what a templating engine provider needs to implement.
 * 
 * @author Ivan Ivanov
 */
public interface TemplatingEngine {

	/**
	 * Provides custom replacement data for templates. The method is called every
	 * time a file is requested from the provider.
	 * 
	 * @return string that replaces the first occurrence of "{template}" in the
	 *         requested file
	 */
	String getReplacement();

	/**
	 * Provides a custom file name. The method is called every time a file is
	 * requested from the provider
	 * 
	 * @param initialFileName
	 *            the initial name of the file
	 * @return string used in the rqeust instead of the initial file name
	 */
	String getFileName(String initialFileName);

	/**
	 * Initializes the templating engine for the current iteration. Each time a file
	 * is requested this is called.
	 */
	void initCurrentIteration();
}
