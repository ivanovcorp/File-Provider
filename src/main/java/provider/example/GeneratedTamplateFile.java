package provider.example;

/**
 * Simple data structure, used by the example templating engine.
 * 
 * @author Ivan Ivanov
 */
public class GeneratedTamplateFile {
	
	private String fileName;
    private String timeStamp;
    private String uuid;
    private boolean isFound;
    
    public GeneratedTamplateFile(String filename, String timestamp, String uuid){
        this.fileName = filename;
        this.timeStamp = timestamp;
        this.uuid = uuid;
        this.isFound = false;
    }

    /**
     * @return file name
     */
    public String getFileName(){
        return this.fileName;
    }

    /**
     * @return timestamp 
     */
    public String getTimestamp(){
        return this.timeStamp;
    }

    /**
     * @return UUID
     */
    public String getUUID(){
        return this.uuid;
    }

    /**
     * @return boolean is the file found
     */
    public boolean getFound(){
        return this.isFound;
    }

    /**
     * setting isFound to true
     */
    public void foundFile(){
        this.isFound = true;
    }
}
