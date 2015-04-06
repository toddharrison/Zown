package com.eharrison.canary.zown.api;

import java.util.Collection;

public interface ITemplate extends IConfigurable {
	String getName();
	
	Collection<? extends IZown> getZowns();
}
