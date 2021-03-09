package com.nucleus.service;

import com.nucleus.pojo.CacheStatistics;

public interface GenericCache<T> {

	
	public T getValue(String key);

	public CacheStatistics getStatistics();
	
}
