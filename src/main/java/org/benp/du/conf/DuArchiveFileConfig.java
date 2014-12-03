package org.benp.du.conf;

import java.io.File;

public class DuArchiveFileConfig extends DuConfigBase {
	
	private int daysFromNowToArchive;
	private File fileFromDir;
	private String fileDestPrefixFileName;
	private File filesDestArchiveDir;
	
	public int getDaysFromNowToArchive() {
		return daysFromNowToArchive;
	}
	public void setDaysFromNowToArchive(int daysFromNowToArchive) {
		this.daysFromNowToArchive = daysFromNowToArchive;
	}
	public File getFileFromDir() {
		return fileFromDir;
	}
	public void setFileFromDir(File fileFromDir) {
		this.fileFromDir = fileFromDir;
	}
	public String getFileDestPrefixFileName() {
		return fileDestPrefixFileName;
	}
	public void setFileDestPrefixFileName(String fileDestPrefixFileName) {
		this.fileDestPrefixFileName = fileDestPrefixFileName;
	}
	public File getFilesDestArchiveDir() {
		return filesDestArchiveDir;
	}
	public void setFilesDestArchiveDir(File filesDestArchiveDir) {
		this.filesDestArchiveDir = filesDestArchiveDir;
	}
	
	@Override
	public String getName() {
		return "Archive File Config";
	}
	
}
