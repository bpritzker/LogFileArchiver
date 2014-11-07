package org.benp.filearchiver;

import java.io.File;

import org.ben.deploymentutils.test.DeploymentUtilsGlobal;

public class ArchiverCronMainTest extends ArchiverCronMain {

	@Override
	protected File getPropertiesFile() {
		return new File(
				DeploymentUtilsGlobal.PROJECT_ROOT_DIR 
				+ "/src/main/resources/DeploymentUtils.properties");
	}
	
}
