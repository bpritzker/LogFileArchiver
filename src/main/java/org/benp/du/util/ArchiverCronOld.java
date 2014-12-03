package org.benp.du.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This class is designed to be run on a scheduled basis
 * @author Ben Pritzker
 *
 */
@Deprecated
public class ArchiverCronOld {
	
	private static Logger logger = LogManager.getLogger(ArchiverCronOld.class.getName());
	
	private String filesToArchiveDir;
	private String archiveFileDir;
	private int daysFromNowToArchive;

	public static void main(String[] args) {
		logger.info("Staring ArchiverCronMain");
		ArchiverCronOld archiverCronMain = new ArchiverCronOld();
		archiverCronMain.run(args);
		logger.info("Done ArchiverCronMain");
	}

	public void run(String[] args) {
		init(args);
		List<File> filesToArchive = DuFileUtils.getFilesOlderThanDaysFromNow(
				daysFromNowToArchive, new File(filesToArchiveDir));
		File zipArchiveFile = getZipArchiveFile();
		try {
			DuFileUtils.zipFiles(filesToArchive, zipArchiveFile);
			// delete the files..
			for (File currFile : filesToArchive) {
				FileUtils.deleteQuietly(currFile);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private void init(String[] args) {
		if (args == null || args.length == 0) {
			initFromProperties();
		} else {
			logger.fatal("COMMAND LINE INITALIZATION NOT IMPLEMENTED YET!!!");
			System.out.println("COMMAND LINE INITALIZATION NOT IMPLEMENTED YET!!!");
			throw new RuntimeException("COMMAND LINE INITALIZATION NOT IMPLEMENTED YET!!!");
		}
	}

	private void initFromProperties() {
		File propsFile = getPropertiesFile();
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
		filesToArchiveDir = properties.getProperty("archive.filesToArchiveDir");
		archiveFileDir = properties.getProperty("archive.archiveFileDir");
		daysFromNowToArchive = Integer.parseInt(
				properties.getProperty("archive.daysFromNowToArchive"));
		
		logger.info("filesToArchiveDir <"+filesToArchiveDir+">");
		logger.info("archiveFileDir <"+archiveFileDir+">");
		logger.info("daysFromNowToArchive <"+daysFromNowToArchive+">");
		
	}

	
	
	protected File getZipArchiveFile() {
		return new File(archiveFileDir + "/Archive.zip");
	}
	
	
	/**
	 * Override this for testing.
	 * @return
	 */
	protected File getPropertiesFile() {
		return new File("DeploymentUtils.properties");
	}

}
