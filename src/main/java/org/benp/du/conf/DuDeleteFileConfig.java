package org.benp.du.conf;

import java.io.File;

public class DuDeleteFileConfig extends DuConfigBase {
	
	private int daysFromNowToDelete;
	private File deleteFileDir;

	@Override
	public String getName() {
		return "DeleteFileConfig";
	}

	public int getDaysFromNowToDelete() {
		return daysFromNowToDelete;
	}

	public void setDaysFromNowToDelete(int daysFromNowToDelete) {
		this.daysFromNowToDelete = daysFromNowToDelete;
	}

	public File getDeleteFileDir() {
		return deleteFileDir;
	}

	public void setDeleteFileDir(File deleteFileDir) {
		this.deleteFileDir = deleteFileDir;
	}

}
