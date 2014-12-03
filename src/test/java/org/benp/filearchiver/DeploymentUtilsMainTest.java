package org.benp.filearchiver;

import java.io.File;

import org.benp.du.DeploymentUtilsMain;
import org.benp.du.test.DeploymentUtilsGlobal;

public class DeploymentUtilsMainTest extends DeploymentUtilsMain {

	@Override
	protected File getPropertiesFile(File file) {
		return new File(
				DeploymentUtilsGlobal.PROJECT_ROOT_DIR 
				+ "/src/main/resources/DeploymentUtils.properties");
	}
	
}
