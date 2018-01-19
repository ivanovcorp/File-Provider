package provider.example;

import java.io.File;

import provider.FileProviderFactory;
import provider.properties.FileProviderProperties;
import provider.runner.FileProviderRunner;

public class TemplateProviderExample {

	private static final File PROPERTIES_FILE = new File(
			"src/test/resources/example-properties/template-provider.properties");

	public static void main(String[] args) {

		FileProviderProperties properties = new FileProviderProperties(PROPERTIES_FILE); // init properties

		File testFile = new File("src/test/resources/test1.tpl");

		SimpleTemplatingEngine simpleTemplateEngine = SimpleTemplatingEngine.getInstance();

		FileProviderRunner fileProviderRunner = FileProviderFactory.initializeFileProvider(properties,
				simpleTemplateEngine, testFile); // init runner

		fileProviderRunner.start(); // start the provider.
		System.out.println("Generating files.");

		System.out.println("Generated files: " + fileProviderRunner.getProcessedFiles());
		fileProviderRunner.getResults(); // collect results
	}

}
