package provider.example;

import java.io.File;
import java.io.IOException;

import provider.FileProviderFactory;
import provider.properties.FileProviderProperties;
import provider.runner.FileProviderRunner;

public class StaticProviderExample {

	private static final File PROPERTIES_FILE = new File(
			"src/test/resources/example-properties/static-provider.properties");

	public static void main(String[] args) throws IOException, InterruptedException {

		FileProviderProperties properties = new FileProviderProperties(PROPERTIES_FILE); // init properties

		FileProviderRunner fileProviderRunner = FileProviderFactory.initializeFileProvider(properties, null); // init runner																												

		System.out.println("Generating files.");
		fileProviderRunner.start(); // start the provider.

		System.out.println("Generated files: " + fileProviderRunner.getProcessedFiles());
		fileProviderRunner.getResults(); // collect results

	}
}
