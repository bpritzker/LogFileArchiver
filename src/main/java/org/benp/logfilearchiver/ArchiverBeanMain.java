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

package org.benp.logfilearchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;



/**
 * This class is used to archive files. It is designed to run in a JBoss server but could
 * also be run as a stand alone app. 
 */
@Stateless
public class ArchiverBeanMain {
	
	private static Logger log = Logger.getLogger(ArchiverBeanMain.class.getName());
	
	private boolean IN_TEST_MODE = true;
	
//	private File mailRootDir = new File("C:/Temp/TEMP_DATA/MailDir");
	
	// archive any file older than this number of days
	private int daysOlderThanToArchive = 0;

	/**
	 * Use this method for testing and scheduling as cron job.
	 */
	public static void main(String[] args) {
		
		ArchiverBeanMain archiverBeanMain = new ArchiverBeanMain();
		archiverBeanMain.run();
	}
	
	
//	@Schedule(hour = "*", minute = "*", second = "*/20", persistent=false)
//	@Schedule(dayOfWeek="Mon", hour="2", persistent=false)
	@Schedule(dayOfMonth = "7,14", hour="2", persistent=false)
//	@Schedule(hour = "*", minute = "*", second = "*/61", persistent=false)
	public void run() {
		archiveJbossLogs();
//		archiveMailFiles(); // For my specific company
	}

	
	private void archiveJbossLogs() {
//		File jbossLogDir = new File(System.getProperty("jboss.server.log.dir"));
		File jbossLogDir = new File("C:/app/servers/wildfly-8.1.0.Final/standalone/log");
		File jbossArchiveLogDir = new File(jbossLogDir.getAbsolutePath() + "/archive");
		try {
			archiveFiles(jbossLogDir, jbossArchiveLogDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public void archiveFiles(File fromDir, File toDir) throws Exception {
		List<File> filesForArchive = getFilesForArchive(fromDir);
		if (filesForArchive.size() == 0) {
			log.info("Nothing to archive");
		} else {
			createAndCleanUpArchiveFile(filesForArchive, toDir);
		}
	}


	private void createAndCleanUpArchiveFile(List<File> filesForArchive, File toDir) throws Exception {

//		File tempDirToArchiveFiles = getTempArchiveDir(toDir);
		
//		prepareFilesForArchive(filesForArchive, tempDirToArchiveFiles);
		
//		File zipFile = zipFiles(tempDirToArchiveFiles);
		
		File tempZipFile = getArchiveFileName(toDir);

		zipFiles(filesForArchive, tempZipFile);

//		cleanup(tempDirToArchiveFiles, zipFile);
		cleanup(filesForArchive);
		
	}


	private void cleanup(List<File> filesForArchive) {
		for (File currFile : filesForArchive) {
			FileUtils.deleteQuietly(currFile);
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
		if (IN_TEST_MODE) {
			// if testing, we can run this ever minute with no issues
			df = new SimpleDateFormat("yyyy-MM-dd-mm");
		}
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
	
	
	private File zipFiles(List<File> files, File zipFile) throws Exception {
			   byte[] buffer = new byte[4096];
//		        File[] files = tempDirToArchiveFilesIn.listFiles();
//		        UUID uniqueFileName = UUID.randomUUID();
//		        File zipFile = new File(tempDirToArchiveFilesIn.getParent() + "/" + uniqueFileName.toString() + ".zip");
		        FileOutputStream fout = new FileOutputStream(zipFile);
		        ZipOutputStream zout = new ZipOutputStream(fout);	        
		        for (File file : files) {
		                FileInputStream fin = new FileInputStream(file);
		                zout.putNextEntry(new ZipEntry(file.getName()));
		                int length;
		                while ((length = fin.read(buffer)) > 0) {
		                    zout.write(buffer, 0, length);
		                }
		                zout.closeEntry();
		                fin.close();
		        }
		        IOUtils.closeQuietly(zout);
		        log.info("Created zip file: " + zipFile.getAbsolutePath());
		        return zipFile;	
	}

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
		Path path = Paths.get(file.getAbsolutePath());
		BasicFileAttributes view = getView(path);
		FileTime lastModifiedFileTime = view.lastModifiedTime();
		Date lastModifiedDate = new Date(lastModifiedFileTime.toMillis());
		Date currentDate = new Date();
		
		int daysFromToday = Days.daysBetween(
				new DateTime(lastModifiedDate), 
				new DateTime(currentDate)).getDays(); 
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		log.info(file.getName() + " --  " + df.format(lastModifiedDate) + "  -- Days: " + daysFromToday);
		if (daysFromToday >= daysOlderThanToArchive) {
			return true;
		} else {
			return false;
		}
	}
	

	private BasicFileAttributes getView(Path path) {
		BasicFileAttributes view = null;
		try {
			view
			   = Files.getFileAttributeView(path, BasicFileAttributeView.class)
			          .readAttributes();
		} catch (IOException ioe) {
			throw new RuntimeException("Could not get view!", ioe);
		}
		return view;
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
