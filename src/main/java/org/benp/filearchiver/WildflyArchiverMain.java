package org.benp.filearchiver;

import java.io.File;

import javax.ejb.Schedule;

import org.benp.file.FileArchiverBean;



public class WildflyArchiverMain {
	
//	@Schedule(hour = "*", minute = "*", second = "*/20", persistent=false)
//	@Schedule(dayOfWeek="Mon", hour="2", persistent=false)
	@Schedule(dayOfMonth = "7,14", hour="2", persistent=false)
//	@Schedule(hour = "*", minute = "*", second = "*/61", persistent=false)
	public void run() {
		int daysOlderThanToArchive = 14;
		File jbossLogDir = new File("C:/app/servers/wildfly-8.1.0.Final/standalone/log");
		File jbossArchiveLogDir = new File(jbossLogDir.getAbsolutePath() + "/archive");
		FileArchiverBean archiverBean = new FileArchiverBean(daysOlderThanToArchive, jbossArchiveLogDir);
		try {
			archiverBean.archiveFiles(jbossLogDir, jbossArchiveLogDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
//		archiveMailFiles(); // For my specific company
	}

