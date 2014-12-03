package org.benp.du.test;

import org.benp.filearchiver.DeploymentUtilsMainTest;

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
		DuTestingUtils deploymentUtilsTestingUtils = new DuTestingUtils();
		deploymentUtilsTestingUtils.createTestFiles(dirToCreateFilesIn);
		
		DeploymentUtilsMainTest archiver = new DeploymentUtilsMainTest();
		archiver.run(null);
		
		
	}

}
