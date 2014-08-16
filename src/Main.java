import java.io.IOException;
import java.util.UUID;

public class Main {

	public static void main(String[] args) throws IOException {
		
		
		
		ProgramManager.getInstance("onur", "onur-bckt-009"); //"my-first-s3-bucket-" + UUID.randomUUID();
		
		Synchronizer synchronizer = new Synchronizer(ProgramManager.getInstance().getSelfFileManager(), 
				ProgramManager.getInstance().getUserSelfDirPath());
		
//		synchronizer.testDB();
		
//		synchronizer.synchronize();
	}

}
