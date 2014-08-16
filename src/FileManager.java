import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class FileManager {
	
	private String owner = null;
	
	private FileMetaData localFileMetaData = null;
	private FileMetaData localDirFileMetaData = null;
	private FileMetaData cloudFileMetaData = null;
	
	public FileManager(String givenOwner) {
		owner = givenOwner;
	}
	
	public void tagLocalDir() {
		resetTags(localFileMetaData, true);
		resetTags(localDirFileMetaData, true);
		
		tagLocalDir(localFileMetaData, localDirFileMetaData);
	}
	
	public void tagLocalDir(FileMetaData localfmd, FileMetaData localDir) {
		// compare each image element with local directory element
		for(int localImageElementIndex = 0; localImageElementIndex < localfmd.getFileList().size(); localImageElementIndex++) {
			FileMetaData localImageElement = localfmd.getFileList().get(localImageElementIndex);
			
			for(int localDirElementIndex = 0; localDirElementIndex < localDir.getFileList().size(); localDirElementIndex++) {
				FileMetaData localDirElement = localDir.getFileList().get(localDirElementIndex);

				if(localImageElement.equalsByName(localDirElement)) {
					if(localImageElement.hasSameLastModified(localDirElement)) {
						localImageElement.setSynchTag(Definitions.LOCAL_OK);
						localDirElement.setSynchTag(Definitions.LOCAL_OK);
					}
					else {
						localImageElement.setSynchTag(Definitions.LOCAL_DIFF_EXISTS);
						localDirElement.setSynchTag(Definitions.LOCAL_DIFF_EXISTS);
					}

					// If directory, continue recursively
					tagLocalDir(localImageElement, localDirElement);
				}
			}

			// if it is not tagged before, than it means it is deleted
			if(localImageElement.getSynchTag() == null) {
				tagLocal(localImageElement, Definitions.LOCAL_ERASED);
			}
		}

		// for each element in local directory, if it is not tagged before, it means it is a new file
		for(int localDirElementIndex = 0; localDirElementIndex < localDir.getFileList().size(); localDirElementIndex++) {
			FileMetaData localDirElement = localDir.getFileList().get(localDirElementIndex);

			if(localDirElement.getSynchTag() == null) {
				tagLocal(localDirElement, Definitions.LOCAL_NEWLY_CREATED);
			}
		}
	}
	
	public void tagCloudDir() {
		resetTags(localFileMetaData, false);
		resetTags(cloudFileMetaData, false);
		
		tagCloudDir(localFileMetaData, cloudFileMetaData);
	}
	
	public void tagCloudDir(FileMetaData localfmd, FileMetaData cloudfmd) {
		// compare each image element with local directory element
		for(int localImageElementIndex = 0; localImageElementIndex < localfmd.getFileList().size(); localImageElementIndex++) {
			FileMetaData localImageElement = localfmd.getFileList().get(localImageElementIndex);
			
			for(int cloudImageElementIndex = 0; cloudImageElementIndex < cloudfmd.getFileList().size(); cloudImageElementIndex++) {
				FileMetaData cloudImageElement = cloudfmd.getFileList().get(cloudImageElementIndex);

				if(localImageElement.equalsByName(cloudImageElement)) {
					if(localImageElement.hasSameLastModified(cloudImageElement)) {
						localImageElement.setSynchTagCloud(Definitions.CLOUD_OK);
						cloudImageElement.setSynchTagCloud(Definitions.CLOUD_OK);
					}
					else {
						localImageElement.setSynchTagCloud(Definitions.CLOUD_DIFF_EXISTS);
						cloudImageElement.setSynchTagCloud(Definitions.CLOUD_DIFF_EXISTS);
					}

					// If directory, continue recursively
					tagCloudDir(localImageElement, cloudImageElement);
				}
			}

			// if it is not tagged before, than it means it is deleted
			if(localImageElement.getSynchTagCloud() == null) {
				tagCloud(localImageElement, Definitions.CLOUD_ERASED);
			}
		}

		// for each element in local directory, if it is not tagged before, it means it is a new file
		for(int cloudImageElementIndex = 0; cloudImageElementIndex < cloudfmd.getFileList().size(); cloudImageElementIndex++) {
			FileMetaData cloudImageElement = cloudfmd.getFileList().get(cloudImageElementIndex);

			if(cloudImageElement.getSynchTag() == null) {
				tagLocal(cloudImageElement, Definitions.CLOUD_NEWLY_CREATED);
			}
		}
	}
	
	public void resetTags(FileMetaData fmd, boolean isFirst) {
		if(isFirst) {
			tagLocal(fmd, null);
		}
		else {
			tagCloud(fmd, null);
		}
	}
	
	// posts local tag to the given file meta data instance recursively with the selected tag
	public void tagLocal(FileMetaData fmd, String tag) {
		List<FileMetaData> fileList = fmd.getFileList();
		
		fmd.setSynchTag(tag);
		
		for(FileMetaData ifmd : fileList) {
			tagLocal(ifmd, tag);
		}
	}
	
	public void tagCloud(FileMetaData fmd, String tag) {
		List<FileMetaData> fileList = fmd.getFileList();
		
		fmd.setSynchTagCloud(tag);
		
		for(FileMetaData ifmd : fileList) {
			tagCloud(ifmd, tag);
		}
	}
	
	public FileMetaData getLocalFileMetaData() {
		return localFileMetaData;
	}
	
	public void setLocalFileMetaData(FileMetaData fmd) {
		localFileMetaData = fmd;
	}
	
	public FileMetaData getLocalDirFileMetaData() {
		return localDirFileMetaData;
	}
	
	public void setLocalDirFileMetaData(FileMetaData fmd) {
		localDirFileMetaData = fmd;
	}
	
	public FileMetaData getCloudFileMetaData() {
		return cloudFileMetaData;
	}
	
	public void setCloudFileMetaData(FileMetaData fmd) {
		cloudFileMetaData = fmd;
	}
	
	public static void writeFileMetaDataToFile(FileMetaData fmd, String path) {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		
		try {
			new File(path).createNewFile();
			
			fout = new FileOutputStream(path);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(fmd);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FileMetaData loadFileMetaDataFromFile(String path) {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		FileMetaData fmd = null;
		
		try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
	        fmd = (FileMetaData) ois.readObject();
	        ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        return fmd;
	}
}
