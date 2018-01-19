package provider.example;

import java.io.File;

import provider.FileProviderFactory;
import provider.exception.FileProviderException;
import provider.properties.FileProviderProperties;
import provider.runner.FileProviderRunner;

public class QuickProviderExample {

	private static final File PROPERTIES_FILE = new File(
			"src/test/resources/example-properties/quick-provider.properties");

	public static void main(String[] args) throws InterruptedException {

		FileProviderProperties properties = new FileProviderProperties(PROPERTIES_FILE); // init properties

		FileProviderRunner fileProviderRunner = FileProviderFactory.initializeFileProvider(properties, null); // init runner																											

		System.out.println("Generating files");
		fileProviderRunner.start(); // start the provider.

		Thread.sleep(10 * 1000); // provider runs for 10 seconds
		try {
			fileProviderRunner.stop();
		} catch (FileProviderException e) {
			System.out.println("Failed: " + e.getMessage());
		}

		System.out.println("Generated files: " + fileProviderRunner.getProcessedFiles());
		fileProviderRunner.getResults(); // collect results
	}
}
