package com.goodformentertainment.canary.zown.api;

/**
 * Classes which implement IConfigurable contain an IConfiguration.
 * 
 * @author Todd Harrison
 */
public interface IConfigurable {
	/**
	 * Get the configuration for this IConfigurable object.
	 * 
	 * @return The IConfiguration for this IConfigurable object.
	 */
	IConfiguration getConfiguration();
}
