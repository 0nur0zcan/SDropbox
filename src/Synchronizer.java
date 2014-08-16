import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Synchronizer {
	private FileManager fileManager;
	
	public Synchronizer(FileManager fileManager, String dir) {
		this.fileManager = fileManager;
		
		AmazonManager.getInstance().setDirectory(dir);
	}
	
	public void synchronize() throws IOException {
		
		fileManager.tagLocalDir();
		
		FileMetaData localDirMD = fileManager.getLocalDirFileMetaData();
		FileMetaData localImageMD = fileManager.getLocalFileMetaData();
		FileMetaData cloudImageMD = fileManager.getCloudFileMetaData();
		
		synchronizeLocalDir(localDirMD, localImageMD);
		
		fileManager.tagCloudDir();
		
		synchronizeCloudDir(localImageMD, cloudImageMD);
	}
	
	public void synchronizeLocalDir(FileMetaData localDir, FileMetaData localImage) {
		
	}
	
	public void synchronizeCloudDir(FileMetaData localImage, FileMetaData cloudImage) {
		
	}
	
	public void testDB() {
		System.out.println("-------- MySQL JDBC Connection Testing ------------");
	    String DNS = "MYDNS";
	            String myDBname = "sdropbox";
	            String MYSQLUSER = "root";
	            String MYSQLPW = "";
	    try {

	        Class.forName("com.mysql.jdbc.Driver");

	    } catch (ClassNotFoundException e) {

	        System.out.println("Where is your MySQL JDBC Driver?");
	        e.printStackTrace();
	        return;

	    }

	    System.out.println("MySQL JDBC Driver Registered!");
	    Connection connection = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://administrator@ec2-54-76-188-109.eu-west-1.compute.amazonaws.com/sdropbox", MYSQLUSER, MYSQLPW);


	    } catch (SQLException e) {
	        System.out.println("Connection Failed! Check output console");
	        //currentDir = "broke";
	    }

	    if (connection != null) {
	        System.out.println("You made it, take control your database now!");
	    } else {
	        System.out.println("Failed to make connection!");
	    }
	}
}
