package org.benp.du;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.benp.du.conf.ArchiveFileConfig;
import org.benp.du.util.DeploymentUtilsFileUtils;


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
		deploymentUtilsMain.run();
		logger.info("Done DeploymentUtilsMain");
	}

	public void run() {
		try {
			Properties props = loadProperties();
			archive(props);
			delete(props);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	
	/**
	 * Override this method to send notifications or whateva
	 * @param e
	 */
	private void handleException(Exception e) {
		e.printStackTrace();
		
	}

	private void delete(Properties props) {
		throw new RuntimeException("Not Implemented.");
		
	}

	private void archive(Properties props) throws Exception {
		
		List<ArchiveFileConfig> archiveFileConfigs = loadArchiveFileConfigs(props);
	
		for (ArchiveFileConfig currArchiveFileConfig : archiveFileConfigs) {
			List<File> filesToArchive = 
					DeploymentUtilsFileUtils.getFilesToArchive(
							currArchiveFileConfig.getDaysFromNowToArchive(), 
							currArchiveFileConfig.getFileFromDir());
			File zipArchiveFile = new File(
					currArchiveFileConfig.getFilesDestArchiveDir() 
					+ "/"
					+ currArchiveFileConfig.getFileDestPrefixFileName() 
					+ "--" + DeploymentUtilsFileUtils.nowDateStamp()
					+ ".zip");
			File archiveFile = DeploymentUtilsFileUtils.zipFiles(filesToArchive, zipArchiveFile);
			logger.info("Created Zip file: " + archiveFile.getAbsolutePath());
			DeploymentUtilsFileUtils.deleteFiles(filesToArchive);
			
		}
		
		
	}

	/**
	 * This will eventually take in more than one 
	 * @param props
	 * @return
	 */
	private List<ArchiveFileConfig> loadArchiveFileConfigs(Properties props) {
		
		String archiveDaysFromNowToArchive = props.getProperty("archive.1.daysFromNowToArchive");
		String archiveFileFromDir = props.getProperty("archive.1.archiveFileFromDir");
		String archiveFileDestPrefixFileName = props.getProperty("archive.1.fileDestArchivePrefixName");
		String archiveFilesDestArchiveDir = props.getProperty("archive.1.filesDestArchiveDir");

		
		// If all the parameters are null then it means we don't have anything to archive
		if (archiveDaysFromNowToArchive == null 
				&& archiveFileFromDir == null
				&& archiveFileDestPrefixFileName == null
				&& archiveFilesDestArchiveDir == null) { 
			logger.info("No Archive Parameters.");
			return new ArrayList<>();
		}
		
		// TODO check for -1 days from now...
		if (archiveDaysFromNowToArchive == null 
				|| archiveFileFromDir == null
				|| archiveFileDestPrefixFileName == null
				|| archiveFilesDestArchiveDir == null) {
			throw new RuntimeException(String.format(
					"Bad parameters for archive \n" 
					+ "archiveDaysFromNowToArchive <%s>\n"
					+ "archiveFileFromDir <%s>\n"
					+ "archiveFileDestPrefixFileName <%s>"
					+ "archiveFilesDestArchiveDir <%s>"
					,archiveDaysFromNowToArchive,
					archiveFileFromDir,
					archiveFileDestPrefixFileName,
					archiveFilesDestArchiveDir));
		}
		ArchiveFileConfig tempConfig = new ArchiveFileConfig();
		tempConfig.setDaysFromNowToArchive(Integer.parseInt(archiveDaysFromNowToArchive));
		tempConfig.setFileFromDir(new File(archiveFileFromDir));
		tempConfig.setFileDestPrefixFileName(archiveFileDestPrefixFileName);
		tempConfig.setFilesDestArchiveDir(new File(archiveFilesDestArchiveDir));
		
		List<ArchiveFileConfig> resultArchiveFileConfig = new ArrayList<>();
		resultArchiveFileConfig.add(tempConfig);
		return resultArchiveFileConfig;
		
	}

	public void alert(Properties props) {
		throw new RuntimeException("Not Implemented.");
	}

	
	
	
	
	
	
	
	
	
	
	
	private Properties loadProperties() {
		File propsFile = getPropertiesFile();
		
		InputStream input = null;
		Properties properties = new Properties();
		try {
			input = new FileInputStream(propsFile);
			properties.load(input);
		} catch (FileNotFoundException fnfe) {
			logger.fatal("COULD NOT FIND PROPERTIES FILE! <" 
					+ DeploymentUtilsFileUtils.getAbsoluteFileName(propsFile)+ ">");
			throw new RuntimeException("COULD NOT FIND PROPERTIES FILE! <" 
					+ DeploymentUtilsFileUtils.getAbsoluteFileName(propsFile)+ ">");
		} catch (IOException ioe) {
			logger.fatal("COULD NOT LOAD PROPERTIES FILE! <" 
					+ DeploymentUtilsFileUtils.getAbsoluteFileName(propsFile)+ ">");
			throw new RuntimeException("COULD NOT LOAD PROPERTIES FILE! <" 
					+ DeploymentUtilsFileUtils.getAbsoluteFileName(propsFile)+ ">");			
		}
		return properties;
	}

	protected File getPropertiesFile() {
		return new File("DeploymentUtilsArchiver.properties");
	}	
	

}
