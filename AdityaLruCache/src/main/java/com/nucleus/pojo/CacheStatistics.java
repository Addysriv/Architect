package com.nucleus.pojo;

public class CacheStatistics {
	
	public int totalCacheSize;
	public int currentMemorySize;
	public int currentDiskSize;
	public int totalAccessCount=0;
	public double hitRatio=0;
	public double missRatio=0;
	public long  avgOptimisationTime;
	public long avgReplinishmentTime;
	
	@Override
	public String toString() {
		
		return "totalCacheSize:"+totalCacheSize+" currentMemorySize:"+currentMemorySize
				+" currentDiskSize:"+currentDiskSize+" totalAccessCount:"+totalAccessCount
				+" hitRatio:"+hitRatio+" missRatio:"+missRatio
				+" avgOptimisationTime:"+avgOptimisationTime+" avgReplinishmentTime:"+avgReplinishmentTime;
	}
}
