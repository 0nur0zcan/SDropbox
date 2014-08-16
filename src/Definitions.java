
public class Definitions {
	
	public final static String DEFAULT_DIRECTORY_MAC = "/Users/0nur0zcan/SDropbox/";
	public final static String DEFAULT_DIRECTORY_WIN = "C:\\SDropbox\\";
	public final static String SELF_EXTENSION = "/self/";
	public final static String SHARED_EXTENSION = "/shared/";
	public final static String IMAGE_FILES_PATH = "/image/";
	
	
	public final static String IMAGE_FILE_NAME = "localImage.img";
	public final static String SHARED_IMAGE_FILE_NAME = "localSharedImage.img";
	
	public final static String CLOUD_IMAGE_FILE_NAME = "cloudImage.img";
	public final static String CLOUD_SHARED_IMAGE_FILE_NAME = "cloudSharedImage.img";
	
	
	public final static String LOCAL_OK = "LOCAL_OK";
	public final static String LOCAL_DIFF_EXISTS = "LOCAL_DIFF_EXISTS";
	public final static String LOCAL_ERASED = "LOCAL_ERASED";
	public final static String LOCAL_NEWLY_CREATED = "LOCAL_NEWLY_CREATED";
	
	public final static String CLOUD_OK = "CLOUD_OK";
	public final static String CLOUD_DIFF_EXISTS = "CLOUD_DIFF_EXISTS";
	public final static String CLOUD_ERASED = "CLOUD_ERASED";
	public final static String CLOUD_NEWLY_CREATED = "CLOUD_NEWLY_CREATED";
	
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
	
}
