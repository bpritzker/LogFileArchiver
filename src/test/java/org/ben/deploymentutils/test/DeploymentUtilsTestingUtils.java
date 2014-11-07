package org.ben.deploymentutils.test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class DeploymentUtilsTestingUtils {
	
	public static void main(String[] args) {
		System.out.println("running.");
		try {
			createTestFiles(DeploymentUtilsGlobal.TESTING_ROOT_DIR 
					+ "/" + DeploymentUtilsGlobal.TESTING_ROOT_DIR_FILES_TO_ARCHIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createTestFiles(String dirToCreateFilesIn) throws Exception {
		
		File nowFile1 = new File(dirToCreateFilesIn + "/nowFile1.txt");
		nowFile1.createNewFile();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -2);  // number of days to add
		File twoDaysOldFile = new File(dirToCreateFilesIn + "/twoDaysOldFile.txt");
		twoDaysOldFile.setLastModified(c.getTimeInMillis());
		twoDaysOldFile.createNewFile();

		c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -10);  // number of days to add
		File tenDaysOldFile = new File(dirToCreateFilesIn + "/tenDaysOldFile.txt");
		tenDaysOldFile.setLastModified(c.getTimeInMillis());
		tenDaysOldFile.createNewFile();
		
		
	}

}
