package org.benp.du.conf;

public abstract class DuConfigBase {
	private boolean inLiveMode;
	
	public abstract String getName();
	
	public boolean isInLiveMode() {
		return inLiveMode;
	}
	public void setInLiveMode(boolean inLiveMode) {
		this.inLiveMode = inLiveMode;
	}

}
