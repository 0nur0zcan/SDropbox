import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileMetaData implements Serializable {
	
	private String owner;

	private String name;
	private String key;
	private boolean isDirectory;
	private String lastModificationDate;
	private long size;
	private String synchTag, synchTagCloud;
	
	private List<FileMetaData> fileList = new ArrayList<FileMetaData>();
	
	public FileMetaData() {
		
	}
	
	public FileMetaData(String owner) {
		this.owner = owner;
	}
	
	public FileMetaData(String filePath, String owner) {
		this.owner = owner;
		
		File file = new File(filePath);
		
		name = file.getAbsolutePath();
		//lastModified = 
		//key = 
		
		if(file.isDirectory()) {
			isDirectory = true;
			size = -1;
			
			deriveAllDir(file, owner);
		}
		else {
			isDirectory = false;
			size = file.length();
		}
	}
	
	public void deriveAllDir(File dir, String owner) {
		for(File file : dir.listFiles()) {
			FileMetaData fmd = new FileMetaData(file.getAbsolutePath(), owner);
			fileList.add(fmd);
		}
	}

	public List<FileMetaData> getFileList() {
		return fileList;
	}
	
	public boolean equalsByName(FileMetaData fmd) {
		return this.name.equals(fmd.getName());
	}
	
	public boolean hasSameLastModified(FileMetaData fmd) {
		return lastModificationDate.equals(fmd.getLastModificationDate());
	}

	public String getName() {
		return name;
	}
	
	public String getLastModificationDate() {
		return lastModificationDate;
	}
	
	public String getSynchTag() {
		return synchTag;
	}
	
	public String getSynchTagCloud() {
		return synchTagCloud;
	}
	
	public void setSynchTag(String synchTag) {
		this.synchTag = synchTag;
	}
	
	public void setSynchTagCloud(String synchTag) {
		this.synchTagCloud = synchTag;
	}
}
