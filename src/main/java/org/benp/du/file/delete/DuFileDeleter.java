package org.benp.du.file.delete;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.benp.du.conf.DuDeleteFileConfig;
import org.benp.du.util.DuFileUtils;

public class DuFileDeleter {
	
	private static Logger logger = LogManager.getLogger(DuFileDeleter.class.getName());
	
	List<DuDeleteFileConfig> configs;
	
	public DuFileDeleter(Properties props) {
		configs = parseDeleteProperties(props);
	}
	
	
	/**
	 * This will eventually take in more than one 
	 * @param props
	 * @return
	 */
	private List<DuDeleteFileConfig> parseDeleteProperties(Properties props) {
		
		String daysFromNowToDelete = props.getProperty("delete.1.daysFromNowToDelete").trim();
		String deleteFileDir = props.getProperty("delete.1.deleteFileDir").trim();

		
		// If all the parameters are null then it means we don't have anything to archive
		if (daysFromNowToDelete == null 
				&& deleteFileDir == null) { 
			logger.info("No Archive Parameters.");
			return new ArrayList<>();
		}
		
		// TODO check for -1 days from now...
		if (daysFromNowToDelete == null 
				|| deleteFileDir == null) {
			throw new RuntimeException(String.format(
					"Bad parameters for delete \n" 
					+ "daysFromNowToDelete <%s>\n"
					+ "deleteFileDir <%s>\n"
					,daysFromNowToDelete,
					deleteFileDir));
		}
		DuDeleteFileConfig tempConfig = new DuDeleteFileConfig();
		tempConfig.setDaysFromNowToDelete(Integer.parseInt(daysFromNowToDelete));
		tempConfig.setDeleteFileDir(new File(deleteFileDir));
		
		List<DuDeleteFileConfig> resultConfig = new ArrayList<>();
		resultConfig.add(tempConfig);
		return resultConfig;
	}


	public void run() {
		for (DuDeleteFileConfig currConfig : configs) {
			List<File> filesToDelete = 
					DuFileUtils.getFilesOlderThanDaysFromNow(
					currConfig.getDaysFromNowToDelete(), 
					currConfig.getDeleteFileDir());
			DuFileUtils.deleteFiles(filesToDelete);
		}
		
	}

}
