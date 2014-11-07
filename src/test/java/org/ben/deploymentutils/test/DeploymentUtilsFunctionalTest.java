package org.ben.deploymentutils.test;

import org.benp.filearchiver.ArchiverCronMain;
import org.benp.filearchiver.ArchiverCronMainTest;

public class DeploymentUtilsFunctionalTest {

	public static void main(String[] args) {
		DeploymentUtilsFunctionalTest deploymentUtilsFunctionalTest = new DeploymentUtilsFunctionalTest();
		try {
			deploymentUtilsFunctionalTest.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() throws Exception {
		String dirToCreateFilesIn =  
				DeploymentUtilsGlobal.TESTING_ROOT_DIR 
				+ "/" + DeploymentUtilsGlobal.TESTING_ROOT_DIR_FILES_TO_ARCHIVE;
		DeploymentUtilsTestingUtils.createTestFiles(dirToCreateFilesIn);
		
		ArchiverCronMain archiver = new ArchiverCronMainTest();
		archiver.run(null);
		
		
	}

}
