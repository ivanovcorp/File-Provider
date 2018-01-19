package provider.exception;

/**
 * Custom Interruption exception implementation
 * 
 * @author Ivan Ivanov
 */
public class FileProviderException extends InterruptedException {

	/** field <code>serialVersionUID</code> */
	private static final long serialVersionUID = 1L;
	
	public static final String COULD_NOT_STOP_EXCEPTION = "File Provider could not be stopped.";

	public FileProviderException() {
		super();
	}
	
	public FileProviderException(String message) {
		super(message);
	}
}
