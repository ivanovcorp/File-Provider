# File Provider

## Short description
This project is meant to be used by both developers and QAs for testing or developing purposes.
It contains 4 different file provider implementations, which makes file-generation easy and fast.

### Dependency

The first step of using this API is to include the project as dependency in your pom.xml

	<dependency>
	    <groupId>com.ivanovcorp</groupId>
	    <artifactId>file-provider</artifactId>
	    <version>0.1.0-SNAPSHOT</version>
	</dependency>

### FileProvider

**Pre build File Providers**

* **QuickFileProvider** quickly generated dummy date with user-defined size
* **RandomFileProvider** generates files with random content by a given size.
* **StaticFileProvider** sends files from a list or based on a directory
* **TemplatableFileProvider** sends files from a list or directory but replaces a "{template}" placeholder before each request


### Example