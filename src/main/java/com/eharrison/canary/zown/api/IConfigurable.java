package com.eharrison.canary.zown.api;

public interface IConfigurable {
	boolean overridesConfiguration();
	
	IConfiguration getConfiguration();
	
	boolean setOverridesConfiguration(boolean overridesConfiguration);
}
