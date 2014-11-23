package org.benp.du.test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class DuTestingUtils {
	
	public static void main(String[] args) {
		System.out.println("running.");
		DuTestingUtils deploymentUtilsTestingUtils = new DuTestingUtils();
		try {
			String archiveDir = DeploymentUtilsGlobal.TESTING_ROOT_DIR 
					+ "/" + DeploymentUtilsGlobal.TESTING_ROOT_DIR_FILES_TO_ARCHIVE;
				deploymentUtilsTestingUtils.createTestFiles(archiveDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
	}
	
	public void createTestFiles(String dirToCreateFilesIn) throws Exception {
		System.out.println("dir: " + dirToCreateFilesIn);
		File testingFileDir = new File(dirToCreateFilesIn);
		testingFileDir.mkdirs();
		
		createTestFile(new File(dirToCreateFilesIn + "/nowFile1.txt"), 0);

		createTestFile(new File(dirToCreateFilesIn + "/twoDaysOldFile.txt"), 2);
		
		createTestFile(new File(dirToCreateFilesIn + "/tenDaysOldFile.txt"), 10);
		
		for (int i=0; i < 10; i++) {
			createTestFile(new File(dirToCreateFilesIn + "/twoDaysOldFile-" + i + ".txt"), 2);
		}

		for (int i=0; i < 10; i++) {
			createTestFile(new File(dirToCreateFilesIn + "/twoDaysOldFile-" + i + ".txt"), 4);
		}

		
	}

	public static void createTestFile(File fileToCreate, int daysOld) throws Exception {
		System.out.println("Creating File: " + fileToCreate.getAbsolutePath() + " - Days Old: " + daysOld);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -daysOld);  // number of days to add
		fileToCreate.setLastModified(c.getTimeInMillis());
		fileToCreate.createNewFile();
	}
	
	
}
