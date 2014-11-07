/**
 * Copyright 2014 Ben Pritzker
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.benp.file;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * This class is used to archive files. It is designed to run in a JBoss server but could
 * also be run as a stand alone app. 
 */
@Stateless
public class FileArchiverBean {
	
	private static final Logger logger = LogManager.getLogger(FileArchiverBean.class.getName());
	
//	private boolean IN_TEST_MODE = false;
	
//	private File mailRootDir = new File("C:/Temp/TEMP_DATA/MailDir");
	
	// archive any file older than this number of days
	private int daysOlderThanToArchive;
	private File directoryToArchiveTo;


	public FileArchiverBean(int daysOlderThanToArchive, File directoryToArchiveTo) {
		this.daysOlderThanToArchive = daysOlderThanToArchive;
		this.directoryToArchiveTo = directoryToArchiveTo;
	}
	

	public void archiveFiles(File fromDir, File toDir) throws Exception {
		List<File> filesForArchive = getFilesForArchive(fromDir);
		if (filesForArchive.size() == 0) {
			logger.info("Nothing to archive");
		} else {
			createAndCleanUpArchiveFile(filesForArchive, toDir);
		}
	}


	private void createAndCleanUpArchiveFile(List<File> filesForArchive, File toDir) throws Exception {

//		File tempDirToArchiveFiles = getTempArchiveDir(toDir);
		
//		prepareFilesForArchive(filesForArchive, tempDirToArchiveFiles);
		
//		File zipFile = zipFiles(tempDirToArchiveFiles);
		
		File tempZipFile = getArchiveFileName(toDir);

		
		File zipFile = DeploymentFileUtils.zipFiles(filesForArchive, tempZipFile);

//		cleanup(tempDirToArchiveFiles, zipFile);
		if (zipFile != null) {
			DeploymentFileUtils.deleteFiles(filesForArchive);
		}
	}



//	private void cleanup(File tempDirToArchiveFiles, File zipFile) {
//		try {
//			FileUtils.deleteDirectory(tempDirToArchiveFiles);
//			File archiveFileName = getArchiveFileName(zipFile);
//			FileUtils.moveFile(zipFile, archiveFileName);
//		} catch (IOException e) {
//			throw new RuntimeException("Could not clean up old temp files. Dir: <" 
//					+  getAbsolutePathNonNull(tempDirToArchiveFiles) + ">", e);
//		}
//	}




	private File getArchiveFileName(File toDir) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		if (IN_TEST_MODE) {
//			// if testing, we can run this ever minute with no issues
//			df = new SimpleDateFormat("yyyy-MM-dd-mm");
//		}
		String fileName = "archive--" + df.format(new Date()) + ".zip";
		File resultFile = new File (toDir.getAbsolutePath() + "/" + fileName);
		return resultFile;
	}

//
//	private File zipDir(File tempDirToArchiveFiles) {
//		File zipFile = null;
//		try {
//			zipFile = zipDirectory(tempDirToArchiveFiles);
//		} catch (Exception e) {
//			throw new RuntimeException("Could not zip directory.", e);
//		}
//		return zipFile;
//	}
//
//
//	private void prepareFilesForArchive(List<File> filesForArchive,
//			File tempDirToArchiveFiles) {
//		for (File currFile : filesForArchive) {
//			
//			File destFile = new File(tempDirToArchiveFiles.getAbsolutePath() + "/" + currFile.getName());
//			
//			try {
//				if (IN_TEST_MODE) {
//					FileUtils.copyFile(currFile, destFile, true);
//				} else {
//					FileUtils.moveFile(currFile, destFile);
//				}
//			}
//			catch (IOException ioe) {
//				throw new RuntimeException("Error, could not copy/move files to archive Dir.", ioe);
//			}
////			File destFile = new File(tempDirToArchiveFiles.getAbsolutePath() + "/" + currFile.getName());
////			try {
////				FileUtils.copyFile(currFile, destFile);
////			} catch (IOException ioe) {
////				log.log(Level.SEVERE, "Could not move file for archive!! " + currFile.getAbsolutePath(), ioe);
////			}
//			
//		}
//		
//	}
//
//
//	private File getTempArchiveDir(File toDir) {
//		UUID uniqueDirName = UUID.randomUUID();
//		File tempDirToArchiveFilesIn = new File(toDir.getAbsolutePath() + "/" + uniqueDirName.toString());
//		
//		log.info("unique dir name: " + uniqueDirName.toString());
//		boolean success = tempDirToArchiveFilesIn.mkdirs();
//		if (!success) {
//			throw new RuntimeException(
//					"Creat Directory failed :( -- Dir <" 
//					+ tempDirToArchiveFilesIn.getAbsolutePath() + ">" );
//		}
//		return tempDirToArchiveFilesIn;
//	}
//	
	
	

//	private File zipDirectory(File tempDirToArchiveFilesIn) throws Exception {
//		   byte[] buffer = new byte[4096];
//	        File[] files = tempDirToArchiveFilesIn.listFiles();
//	        UUID uniqueFileName = UUID.randomUUID();
//	        File zipFile = new File(tempDirToArchiveFilesIn.getParent() + "/" + uniqueFileName.toString() + ".zip");
//	        FileOutputStream fout = new FileOutputStream(zipFile);
//	        ZipOutputStream zout = new ZipOutputStream(fout);	        
//	        for (File file : files) {
//
//	                FileInputStream fin = new FileInputStream(file);
//	                zout.putNextEntry(new ZipEntry(file.getName()));
//	                int length;
//	                while ((length = fin.read(buffer)) > 0) {
//	                    zout.write(buffer, 0, length);
//	                }
//	                zout.closeEntry();
//	                fin.close();
//	        }
//	        IOUtils.closeQuietly(zout);
//	        return zipFile;
//	}


	private List<File> getFilesForArchive(File fromDir) {

		List<File> resultFilesForArchive = new ArrayList<>();
		
		File[] listOfFiles = fromDir.listFiles();
		for (File currFile : listOfFiles) {
			
			if (currFile.isFile() && isReadyToArchive(currFile)) {
				resultFilesForArchive.add(currFile);
			}
		}		
		return resultFilesForArchive;
	}


	private boolean isReadyToArchive(File file) {
		
		int daysFromToday = org.benp.file.DeploymentFileUtils.getDaysFromToday(file);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		logger.info(file.getName() + " --  " + df.format(daysFromToday) + "  -- Days: " + daysFromToday);
		if (daysFromToday >= daysOlderThanToArchive) {
			return true;
		} else {
			return false;
		}
	}
	


//
//	private String getAbsolutePathNonNull(File file) {
//		if (file == null) {
//			return "FILE WAS NULL!";
//		} else {
//			return file.getAbsolutePath();
//		}
//	}	

}
