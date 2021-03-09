package com.nucleus.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;


import com.nucleus.pojo.CacheConfig;
import com.nucleus.pojo.CacheStatistics;
import com.nucleus.pojo.CacheValueCallback;
import com.nucleus.pojo.RemoveCallbackListener;


/*
 * 
 * @author Aditya Srivastava
 * 
 * 
 */

public class GenericCacheImpl<T> implements GenericCache<T> {

	private int 					capacity;
	private int 					size;         // To track number of elements present in Cache + Disk
	private int 					thresholdSize;
	private CacheValueCallback<T> 	valueCallback;
	private RemoveCallbackListener	removeValueListener;
	private long 					expiryTime;
	private Map<String, ValueWrapper<T>> cache;  // HashMap is used to achieve search operations in O(1) complexity.										  
	private ValueWrapper<T> 		start;       // Doubly Linked List concept is used to achieve remove operation in O(1) complexity.
	private ValueWrapper<T> 		lastNode;
	private int 					accessCount;
	private int						hitRatioCount;
	private int						missRatioCount;
	private long					totalOptimizationTime;
	private long					totalReplinishmentTime;
	
	
	public GenericCacheImpl(CacheConfig config, CacheValueCallback<T> valueCallback,RemoveCallbackListener removeValueListener) {
		this.capacity = config.getCapacity();
		this.expiryTime = config.getExpiryTime();
		this.thresholdSize=config.getThresholdSize();
		this.valueCallback = valueCallback;   
		this.cache = new HashMap<>();            //Since write operations will be handled by Doubly Linked List thus HashMap is used instead of ConcurrentHM for performance purpose.
		if(removeValueListener!=null)
			this.removeValueListener=removeValueListener;

		size=0;
		accessCount=0;
		totalReplinishmentTime=0;
		totalOptimizationTime=0;
		removeExpiredEntriesFromDisk();
		
	}



	@Override
	public T getValue(String key) {
		long totalTimeForGet=getCurrentTime();
		T value=null;
		ValueWrapper<T> wrapper=null;
		accessCount++;
		if(!cache.containsKey(key)) {       // New record need to be added in Maps
			long timeStartLog=getCurrentTime();
			missRatioCount++;
			size++;
			value=valueCallback.getValue(key);
			wrapper=new ValueWrapper<T>(value);
			wrapper.key=key;
			if(size>=capacity) {                     // cache full
				if(removeValueListener!=null)
					CompletableFuture.supplyAsync(() -> removeLruEntriesOnCacheFull())
									.thenAcceptAsync(b -> removeValueListener.removeListener());       // removing 10% of capacity entries from disk
				else
				{
					CompletableFuture.supplyAsync(() -> removeLruEntriesOnCacheFull())
											.thenAccept(b -> System.out.println(key+" entry removed from cache."));
					
				}
			}

			if(size>thresholdSize)      // threshold reached LRU entry getting saved in disk
			{
				addEntriesToDisk(wrapper);
				cache.put(key,null);
			}
			else {                        //  Threshold limit not yet reached
				cache.put(key,wrapper);
			}

			addAtTop(wrapper);
			
			totalReplinishmentTime+=getCurrentTime()-timeStartLog;

		}
		else if(cache.get(key)==null)      // Record is not in HashMap but present in disk 
		{
			long timeStartLog=getCurrentTime();
			
			ReentrantLock lock = new ReentrantLock();

			lock.lock();

			wrapper=readCacheDataFromDisk(key);       // Reading entry from disk if manually deleted then adding again in disk
			if(wrapper==null) {
				value=valueCallback.getValue(key);
				wrapper=new ValueWrapper<T>(value);
				if(size>=capacity) {                     // cache full

					CompletableFuture.supplyAsync(() -> removeLruEntriesOnCacheFull());       // removing 10% of capacity entries from disk
				}
				missRatioCount++;
				totalReplinishmentTime+=getCurrentTime()-timeStartLog;
			}
			else {
				hitRatioCount++;
			}
				
				
			wrapper.key=key;
			if(wrapper.getAccessTime()<(getCurrentTime() - expiryTime))          // entry in disk expired
			{
				missRatioCount++;
				value=valueCallback.getValue(key);
				wrapper=new ValueWrapper<T>(value);
				final String removeKey=key;
				CompletableFuture.supplyAsync(() -> removeLastEntriesFromDisk(removeKey));           // updating the updated entry from valuecallback in disk
			}
			
			cache.put(key,wrapper);
			removeNode(wrapper);
			addAtTop(wrapper);
			CompletableFuture.supplyAsync(() -> removeLruFromEntityMapAndAddTodisk());             // Move updated entity to map and remove from disk
			lock.unlock();
			totalOptimizationTime+=getCurrentTime()-timeStartLog;
		}
		else if((wrapper=cache.get(key)).getAccessTime()< (getCurrentTime() - expiryTime))    // entry in cache expired
		{
			missRatioCount++;
			value=valueCallback.getValue(key);
			wrapper=new ValueWrapper<T>(value);
			wrapper.key=key;
			removeNode(cache.get(key));
			cache.put(key, wrapper);
			addAtTop(wrapper);
		}
		else
		{
			hitRatioCount++;
			(wrapper=cache.get(key)).setAccessTime(getCurrentTime());
			wrapper.key=key;
			value=wrapper.getValue();
			removeNode(wrapper);
			addAtTop(wrapper);

		}

		System.out.println("Total time taken in msec to get value "+(getCurrentTime()-totalTimeForGet));
		return value;
	}


	private Boolean removeLruFromEntityMapAndAddTodisk() {
		

		ValueWrapper<T> eldestEntry = null;
		
		for(Entry<String, ValueWrapper<T>> entry: cache.entrySet()) {
			if(eldestEntry==null) {
				eldestEntry=entry.getValue();
				continue;
			}
			
			if(entry.getValue()!=null && eldestEntry.accessTime>entry.getValue().accessTime)
			{
				eldestEntry=entry.getValue();
			}
			
			if(eldestEntry.accessTime>(getCurrentTime()-expiryTime))             //if eldest entity is expired directly remove the value
			{
				removeNode(eldestEntry);
				cache.remove(eldestEntry.key);
				if(removeValueListener!=null)
					CompletableFuture.supplyAsync(() -> removeValueListener.removeListener());
									
				else
				{
					String key=eldestEntry.key;
					System.out.println(key+" entry removed from cache.");
					
				}
				return true;
			}
			
		}
		
		addEntriesToDisk(eldestEntry);               // if not expired oldest entry moved to disk
		cache.put(eldestEntry.key,null);
		
		return true;
	}
	
	private synchronized String removeNodeFromLast() {
		String key="";
		if(lastNode!=null && lastNode.previous!=null)
		{
			key=lastNode.key;
			lastNode=lastNode.previous;
			lastNode.next=null;
		}


		return key;
	}

	private Boolean updateValueInDisk(String key) {
		T value=valueCallback.getValue(key);
		ValueWrapper<T> wrapper=new ValueWrapper(value);
		File file=new File(wrapper.key+".txt");
		try {
			if(!file.createNewFile())
			{
				file.delete();
				file=new File(wrapper.key+".txt");
			}
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(wrapper);
			o.close();
			f.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	private Boolean addEntriesToDisk(ValueWrapper<T> valueWrapper) {


		try {
			FileOutputStream f = new FileOutputStream(valueWrapper.key+".txt");
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(valueWrapper);

			o.close();
			f.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		} 

		return true;
	}

	@SuppressWarnings("unchecked")
	private ValueWrapper<T> readCacheDataFromDisk(String key){

		ValueWrapper<T> wrapper =null;

		FileInputStream fi;
		try {
			fi = new FileInputStream(new File(key+".txt"));

			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			wrapper = (ValueWrapper<T>) oi.readObject();

			fi.close();
			oi.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		return wrapper;

	}


	private void addAtTop(ValueWrapper<T> node) {
		node.next = start;
		node.previous = null;
		if (start != null)
			start.previous = node;
		start = node;
		if (lastNode == null)
			lastNode = start;
	}

	private Boolean removeNode(ValueWrapper<T> node) {

		if (node.previous != null) {
			node.previous.next = node.next;
		} else {
			start = node.next;
		}

		if (node.next != null) {
			node.next.previous = node.previous;
		} else {
			lastNode = node.previous;
		}
		
		return true;
	}



	private Boolean removeLruEntriesOnCacheFull() {

		int removeCounter=(int) (capacity*0.10);
		synchronized (this) {                     					 // synchronized block to maintain concurrency

			for(int i=0;i<removeCounter;i++) {

				Thread t=new Thread(() -> {
					String oldKey=removeNodeFromLast();      			 // removing last data from linked list as it will be the oldest    
					removeLastEntriesFromDisk(oldKey);
					cache.remove(oldKey);
					System.out.println("Cache entry "+oldKey+" is evicted.");
					size--;

				});  

				t.setPriority(Thread.MIN_PRIORITY);     // removing data from disk with low priority thread asynchronously
				t.start();

			}
		}

		return true;
	}

	private synchronized Boolean removeLastEntriesFromDisk(String key) {
		try {
			File file=new File(key+".txt");
			file.delete();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private void removeExpiredEntriesFromDisk() {            // A thread will run every 1 minute to check if LRU entry is expired or not if yes then it will be deleted.
		
		Thread removeExpiredEntries=new Thread(() -> {                
			try {
				Thread.sleep(100000);
				if(lastNode!=null && lastNode.accessTime< (getCurrentTime() - expiryTime) && cache.containsKey(lastNode.key) && cache.get(lastNode.key)==null)
				{
					String oldKey=removeNodeFromLast();      			 // removing last data from linked list as it will be the oldest    
					removeLastEntriesFromDisk(oldKey);
					cache.remove(oldKey);
					System.out.println("Cache entry "+oldKey+" expired and is evicted.");
					size--;
				}
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			
		});
		removeExpiredEntries.setPriority(Thread.NORM_PRIORITY);
		removeExpiredEntries.start();
		
	}

	@Override
	public CacheStatistics getStatistics() {

		CacheStatistics stats=new CacheStatistics();
		stats.totalCacheSize=size;
		if(size>thresholdSize)
		{
			stats.currentMemorySize=thresholdSize;
			stats.currentDiskSize=size-thresholdSize;
		}
		else
		{
			stats.currentMemorySize=size;
			stats.currentDiskSize=0;	
		}
		stats.totalAccessCount=accessCount;
		if(hitRatioCount!=0 && accessCount!=0) {
			double hitRatio=((double)hitRatioCount/(double)accessCount)*100;
			stats.hitRatio=hitRatio;
		}
		if(missRatioCount!=0 && accessCount!=0) {
			double missRatio=((double)missRatioCount/(double)accessCount)*100;
			stats.missRatio=missRatio;
		}
		
		if(totalOptimizationTime!=0 && accessCount!=0) {
			stats.avgOptimisationTime=totalOptimizationTime/accessCount;
		}
		
		if(totalReplinishmentTime!=0 && accessCount!=0) {
			stats.avgReplinishmentTime=totalReplinishmentTime/accessCount;
		}
		return stats;
	}


	protected long getCurrentTime() {			//protected method so that it can be overridden in test class
		return System.currentTimeMillis();
	}

}


class ValueWrapper<V> implements Serializable{

	private static final long serialVersionUID = 1L;

	V value;
	long accessTime;
	ValueWrapper<V> previous;
	ValueWrapper<V> next;
	String key;

	ValueWrapper(V value) {
		this.value = value;
		this.accessTime = getCurrentTime();
	}

	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public long getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}
	

	protected long getCurrentTime() {			//protected method so that it can be overridden in test class
		return System.currentTimeMillis();
	}


}



