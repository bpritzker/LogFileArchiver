package org.benp.du.file.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.benp.du.conf.DuArchiveFileConfig;
import org.benp.du.util.DuFileUtils;

public class DuFileArchiver {
	
	private static Logger logger = LogManager.getLogger(DuFileArchiver.class.getName());
	
	private List<DuArchiveFileConfig> configs;
	
	
	public DuFileArchiver(Properties props) {
		configs = parseArchiveProperties(props);
	}
	
	
	
	
	/**
	 * This will eventually take in more than one 
	 * @param props
	 * @return
	 */
	private List<DuArchiveFileConfig> parseArchiveProperties(Properties props) {
		
		String archiveDaysFromNowToArchive = props.getProperty("archive.1.daysFromNowToArchive").trim();
		String archiveFileFromDir = props.getProperty("archive.1.archiveFileFromDir").trim();
		String archiveFileDestPrefixFileName = props.getProperty("archive.1.fileDestArchivePrefixName").trim();
		String archiveFilesDestArchiveDir = props.getProperty("archive.1.filesDestArchiveDir").trim();

		
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
		DuArchiveFileConfig tempConfig = new DuArchiveFileConfig();
		tempConfig.setDaysFromNowToArchive(Integer.parseInt(archiveDaysFromNowToArchive));
		tempConfig.setFileFromDir(new File(archiveFileFromDir));
		tempConfig.setFileDestPrefixFileName(archiveFileDestPrefixFileName);
		tempConfig.setFilesDestArchiveDir(new File(archiveFilesDestArchiveDir));
		
		List<DuArchiveFileConfig> resultArchiveFileConfig = new ArrayList<>();
		resultArchiveFileConfig.add(tempConfig);
		return resultArchiveFileConfig;
	}











	public void run() throws Exception {
		for (DuArchiveFileConfig currArchiveFileConfig : configs) {
			List<File> filesToArchive = 
					DuFileUtils.getFilesOlderThanDaysFromNow(
							currArchiveFileConfig.getDaysFromNowToArchive(), 
							currArchiveFileConfig.getFileFromDir());
			File zipArchiveFile = new File(
					currArchiveFileConfig.getFilesDestArchiveDir() 
					+ "/"
					+ currArchiveFileConfig.getFileDestPrefixFileName() 
					+ "--" + DuFileUtils.nowDateStamp()
					+ ".zip");
			File archiveFile = DuFileUtils.zipFiles(filesToArchive, zipArchiveFile);
			logger.info("Created Zip file: " + archiveFile.getAbsolutePath());
			DuFileUtils.deleteFiles(filesToArchive);
		}
	}

}
