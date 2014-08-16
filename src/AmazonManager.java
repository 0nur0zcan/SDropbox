import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;


public class AmazonManager {

	private static AmazonManager instance = null;
	
	private AmazonS3 s3;
	private TransferManager transferManager;
	private String bucketName;
	//private String key = "MyObjectKey";
	private String directory;
	
	protected AmazonManager() {
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/0nur0zcan/.aws/credentials), and is in valid format.",
                    e);
        }
        
        s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        
        transferManager = new TransferManager(credentials);
	}
	
	public static AmazonManager getInstance() {
		if(instance == null)
			instance = new AmazonManager();
		return instance;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public void setDirectory(String dir) {
		directory = dir;
	}
	
	public void createBucket() {
		if(!s3.doesBucketExist(bucketName))
			s3.createBucket(bucketName);
	}
	
	public static String convertIntoKey(String path) {
		if(Definitions.isMac())
			return path.replaceFirst(Definitions.DEFAULT_DIRECTORY_MAC, "");
		else if(Definitions.isWindows())
			return path.replaceFirst(Definitions.DEFAULT_DIRECTORY_WIN, "");
		else
			return "";
	}
	
	public boolean uploadFileToCloud(File file) throws IOException {
		PutObjectRequest request = new PutObjectRequest(bucketName, convertIntoKey(file.getAbsolutePath()), file);
		
		s3.putObject(request);
		
		return true;
	}
	
	public boolean uploadFileToCloud(String content) throws IOException {
		return uploadFileToCloud(createFile(content));
	}
	
	public boolean uploadDirectory(final File dir) {
		MultipleFileUpload upload = transferManager.uploadDirectory(bucketName, convertIntoKey(dir.getAbsolutePath()), dir, true);
		
		//upload.addProgressListener(myProgressListener);
		
		try {
			upload.waitForCompletion();
		} catch (AmazonClientException
				| InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
		transferManager.shutdownNow();
		
		return true;
	}
	
	public void uploadFolder(final File dir) throws IOException {
		for (final File fileEntry : dir.listFiles()) {
			if (fileEntry.isDirectory()) {
				uploadFolder(fileEntry);
	        } else {
	        	uploadFileToCloud(fileEntry);
	        }
		}
	}
	
	public boolean downloadFileFromCloud(String key, String dest) throws IOException {
		System.out.println("Downloading an object");

        S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		
        InputStream objectData = object.getObjectContent();
		
        writeIntoFile(objectData, dest);
        
        objectData.close();
        
		return true;
	}
	
	public boolean deleteFromCloud(String key) {
		System.out.println("Deleting an object\n");
		
        s3.deleteObject(bucketName, key);
        
        return true;
	}
	
	private static File createFile(String content) throws IOException {
		File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(content);
        writer.close();

        return file;
	}
	
	private static boolean writeIntoFile(InputStream inputStream, String dest) {
		OutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream(new File(dest));
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
