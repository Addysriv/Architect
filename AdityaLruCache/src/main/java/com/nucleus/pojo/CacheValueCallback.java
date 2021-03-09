package com.nucleus.pojo;

@FunctionalInterface
public interface CacheValueCallback<T> {

	public T getValue(String key);
	
}
