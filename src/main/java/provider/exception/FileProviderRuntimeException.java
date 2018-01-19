package provider.exception;

/**
 * Simple Runtime exception implementation.
 * 
 * @author Ivan Ivanov
 */
public class FileProviderRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4025612973587951877L;

	public static final String FAILED_TO_PARSE_RESULTS_EXCEPTION = "Failed to get results of the provider in 5 retries.";
	
	public FileProviderRuntimeException() {
		super();
	}
	
	public FileProviderRuntimeException(String message) {
		super(message);
	}
}
