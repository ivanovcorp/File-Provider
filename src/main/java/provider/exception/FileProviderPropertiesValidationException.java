package provider.exception;

/**
 * Simple Runtime exception implementation, used in the properties handling.
 * 
 * @author Ivan Ivanov
 */
public class FileProviderPropertiesValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -4238425344297813012L;
	
	/* Exception messages */
	public static final String EXCEPTION_WRONG_PROVIDER = "Provider type property is either wrong or missing. Providers: QUICK_FILE_PROVIDER, STATIC_FILE_PROVIDER, QUICK_FILE_PROVIDER and TEMPLATE_FILE_PROVIDER.";
	
	public static final String EXCEPTION_WRONG_FILE_SIZE = "File size property is wrong. It must be a numeric value";
	public static final String EXCEPTION_FILE_SIZE_MISSING = "File size property is empty.";
	
	public static final String EXCEPTION_DIRECTORY_MISSING = "Directory property is empty.";
	public static final String EXCEPTION_DIRECTORY_DOES_NOT_EXIST = "Directory does not exist.";

	public static final String WRONG_INTERVAL_EXCEPTION = "Interval must have numeric value.";

	public static final String EXCEPTION_STORE_DIRECTORY_MISSING = "Store directory property is empty.";

	public static final String EXCEPTION_STORE_DIRECTORY_DOES_NOT_EXIST = "Store directory does not exist.";

	public static final String WRONG_STATIC_FILE_COUNT = "Static file count must have numeric value.";

	public static final String EXCEPTION_WRONG_MODE = "Provider mode can be either: STATIC or DYNAMIC.";

	public static final String EXCEPTION_MODE_MISSING = "Provider mode property is missing.";
	
	public FileProviderPropertiesValidationException() {
		super();
	}
	
	public FileProviderPropertiesValidationException(String message) {
		super(message);
	}
}
