package org.benp.du;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.benp.du.file.archive.DuFileArchiver;
import org.benp.du.file.delete.DuFileDeleter;
import org.benp.du.util.DuFileUtils;


/**
 * This program will do the setup and then execute the archiver and file deleter.
 * @author Ben Pritzker
 *
 */
public class DeploymentUtilsMain {
	
	private static Logger logger = LogManager.getLogger(DeploymentUtilsMain.class.getName());

	public static void main(String[] args) {
		logger.info("Starting DeploymentUtilsMain...");
		DeploymentUtilsMain deploymentUtilsMain = new DeploymentUtilsMain();
		deploymentUtilsMain.run(args);
		logger.info("Done DeploymentUtilsMain");
	}

	public void run(String[] args) {
		try {
			File propFile = getPropFile(args);
			Properties props = loadProperties(propFile);
			archive(props);
			delete(props);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	
	private File getPropFile(String[] args) {
		if (args == null || args.length == 0) {
			return null;
		} 
		 
		File resultFile = new File(args[0]);
		if (! resultFile.exists()) {
			throw new RuntimeException("Error: Could not find File: <" + resultFile.getAbsoluteFile() + ">");
		}
		return resultFile;
	}

	/**
	 * Override this method to send notifications or whateva
	 * @param e
	 */
	private void handleException(Exception e) {
		e.printStackTrace();
	}

	private void delete(Properties props) {
		DuFileDeleter deleter = new DuFileDeleter(props);
		deleter.run();
		
	}

	private void archive(Properties props) throws Exception {
		DuFileArchiver archiver = new DuFileArchiver(props);
		archiver.run();
	}


	public void alert(Properties props) {
		throw new RuntimeException("Not Implemented.");
	}

	
	
	
	
	
	
	
	
	
	
	
	private Properties loadProperties(File propFile) {
		File propsFile = getPropertiesFile(propFile);
		
		InputStream input = null;
		Properties properties = new Properties();
		try {
			input = new FileInputStream(propsFile);
			properties.load(input);
		} catch (FileNotFoundException fnfe) {
			logger.fatal("COULD NOT FIND PROPERTIES FILE! <" 
					+ DuFileUtils.getAbsoluteFileName(propsFile)+ ">");
			throw new RuntimeException("COULD NOT FIND PROPERTIES FILE! <" 
					+ DuFileUtils.getAbsoluteFileName(propsFile)+ ">");
		} catch (IOException ioe) {
			logger.fatal("COULD NOT LOAD PROPERTIES FILE! <" 
					+ DuFileUtils.getAbsoluteFileName(propsFile)+ ">");
			throw new RuntimeException("COULD NOT LOAD PROPERTIES FILE! <" 
					+ DuFileUtils.getAbsoluteFileName(propsFile)+ ">");			
		}
		return properties;
	}

	/**
	 * This method is used to override the props file for testing.
	 * @param propFile
	 * @return
	 */
	protected File getPropertiesFile(File propFile) {
		if (propFile != null) {
			return propFile;
		}
		return new File("DeploymentUtilsArchiver.properties");
	}	
	

}
