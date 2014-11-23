package org.benp.du.util;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class DeploymentUtilsFileUtils {
	
	private static Logger logger = LogManager.getLogger(DeploymentUtilsFileUtils.class.getName());
	
	
	public static List<File> getFilesToArchive(int daysFromNowToArchive, File archiveFileDir) {
		List<File> resultFiles = new ArrayList<>();
		File[] listOfFiles = archiveFileDir.listFiles();
		for (File currFile : listOfFiles) {
			if (getDaysFromToday(currFile) >= daysFromNowToArchive) {
				resultFiles.add(currFile);
			}
		}
		return resultFiles;
	}
	
	/**
	 * This method will return how many days ago the file was last modified.
	 * 
	 * @param file
	 * @return - Number of days from today since the file was last modified.
	 */
	public static int getDaysFromToday(File file) {
		Path path = Paths.get(file.getAbsolutePath());
		BasicFileAttributes view = getView(path);
		FileTime lastModifiedFileTime = view.lastModifiedTime();
		Date lastModifiedDate = new Date(lastModifiedFileTime.toMillis());
		Date currentDate = new Date();
		
		int daysFromToday = Days.daysBetween(
				new DateTime(lastModifiedDate), 
				new DateTime(currentDate)).getDays();
		logger.info("Days from Today-File: " + file.getAbsolutePath() + "  --  " + daysFromToday);
		return daysFromToday;
	}

	/**
	 * This will create zip file with all the files contained in the list
	 * @param files - All the files to add to the zip archive
	 * @param zipFile
	 * @return
	 * @throws Exception
	 */
	public static File zipFiles(List<File> files, File zipFile) throws Exception {
		   byte[] buffer = new byte[4096];
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
	        logger.info("Created zip file: " + zipFile.getAbsolutePath());
	        return zipFile;	
	}	
	
	/**
	 * This will attempt to delete all files in the list. If there were any errors it 
	 * will continue to delete but return an error.
	 * @param files - List of files to delete
	 * @return
	 */
	public static boolean deleteFiles(List<File> files) {
		boolean resultSuccessfulDelete = true;
			for (File currFile : files) {
				boolean tempDelete = currFile.delete();
				if (tempDelete == false) {
					resultSuccessfulDelete = false;
				}
			}
			return resultSuccessfulDelete;
	}
	
	
	/**
	 * private helper method
	 * @param path
	 * @return
	 */
	private static BasicFileAttributes getView(Path path) {
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
	
	
	public static String getAbsoluteFileName(File file) {
		if (file == null) {
			return "null";
		} else {
			return file.getAbsolutePath();
		}
	}
	
	
	public static String nowDateStamp() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(now);
	}

}
