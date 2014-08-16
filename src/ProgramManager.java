import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;


public class ProgramManager {
	
	private static ProgramManager instance = null;
	
	private String userName;
	
	private FileManager selfFileManager = null;
	
	public FileManager getSelfFileManager() {
		return selfFileManager;
	}

	protected ProgramManager(String userName, String bucketName) {
		this.userName = userName;
		
		selfFileManager = new FileManager(userName);
		
		deriveUserSelfDir();
		//deriveUserSharedDir();
		deriveLocalImage();
		//deriveSharedImage();
		
		AmazonManager.getInstance().setBucketName(bucketName);
	}
	
	public static ProgramManager getInstance(String userName, String bucketName) {
		if(instance == null)
			instance = new ProgramManager(userName, bucketName);
		return instance;
	}
	
	public static ProgramManager getInstance() {
		return instance;
	}
	
	public void deriveUserSelfDir() {
		File dir = new File(getUserSelfDirPath());
		if(!dir.exists()) { 
			dir.mkdir();
		}
		
		FileMetaData localDir = new FileMetaData(getUserSelfDirPath(), userName);
		selfFileManager.setLocalDirFileMetaData(localDir);
	}
	
	public void deriveLocalImage() {
		if(!new File(getUserLocalImagePath()).exists()) { 
			if(!new File(getImageFilePath()).exists()) {
				new File(getImageFilePath()).mkdirs();
			}
			
			FileMetaData localImage = new FileMetaData(getUserSelfDirPath(), userName);
			selfFileManager.setLocalFileMetaData(localImage);
			
			FileManager.writeFileMetaDataToFile(localImage, getUserLocalImagePath());
		}
		else {
			selfFileManager.setLocalFileMetaData(FileManager.loadFileMetaDataFromFile(getUserLocalImagePath()));
		}
	}
	
	public String getUserSelfDirPath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.SELF_EXTENSION;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.SELF_EXTENSION;
		else
			return "";
	}
	
	public String getUserSharedDirPath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.SHARED_EXTENSION;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.SHARED_EXTENSION;
		else
			return "";
	}
	
	public String getImageFilePath() {
		if(Definitions.isMac()) {
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.IMAGE_FILES_PATH;
		}
		else if(Definitions.isWindows()) {
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.IMAGE_FILES_PATH; 
		}
		else
			return "";
	}
	
	public String getUserLocalImagePath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.IMAGE_FILES_PATH + Definitions.IMAGE_FILE_NAME;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.IMAGE_FILES_PATH + Definitions.IMAGE_FILE_NAME;
		else
			return "";
	}
	
	public String getUserLocalSharedImagePath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.IMAGE_FILES_PATH + Definitions.SHARED_IMAGE_FILE_NAME;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.IMAGE_FILES_PATH + Definitions.SHARED_IMAGE_FILE_NAME;
		else
			return "";
	}
	
	public String getUserCloudImagePath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.IMAGE_FILES_PATH + Definitions.CLOUD_IMAGE_FILE_NAME;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.IMAGE_FILES_PATH + Definitions.CLOUD_IMAGE_FILE_NAME;
		else
			return "";
	}
	
	public String getUserCloudSharedImagePath() {
		if(Definitions.isMac())
			return Definitions.DEFAULT_DIRECTORY_MAC + userName + Definitions.IMAGE_FILES_PATH + Definitions.CLOUD_SHARED_IMAGE_FILE_NAME;
		else if(Definitions.isWindows())
			return Definitions.DEFAULT_DIRECTORY_WIN + userName + Definitions.IMAGE_FILES_PATH + Definitions.CLOUD_SHARED_IMAGE_FILE_NAME;
		else
			return "";
	}
	
	public void register() {
		AmazonManager.getInstance().createBucket();
	}
	
}
