package com.nucleus.AdityaLruCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.nucleus.pojo.*;
import com.nucleus.service.*;


/**
 * Hello world!
 *
 */
public class App 
{

	private GenericCache<Car> cache;
	private int clock = 0;
	Map<String, AtomicInteger> instanceCounter = new HashMap<>();

	public static void main( String[] args )
	{
		new App().test();
	}


	public void test() {


		System.out.println( "Hello World!" );


		CacheConfig config = new CacheConfig();		//creating cache config
		config.setCapacity(4);
		config.setExpiryTime(1000000000000000000l);
		config.setThresholdSize(2);

		CacheValueCallback<Car> valueCallback = new CacheValueCallback<Car>() {
			@Override
			public Car getValue(String key) {
				Car c = new Car(key, "BLACK");

				return c;
			}
		};

		cache = new GenericCacheImpl<Car>(config, valueCallback,null);
		System.out.println(cache.getStatistics());
		
		String key="CAR1";
		cache.getValue(key);
		System.out.println();
		System.out.println("Car1 "+cache.getStatistics());
		cache.getValue(key);
		System.out.println();
		System.out.println("Car2 "+cache.getStatistics());
		cache.getValue("Car2");
		System.out.println();
		System.out.println("Car3 "+cache.getStatistics());
		cache.getValue("Car3");
		System.out.println();
		System.out.println("Car4 "+cache.getStatistics());
		cache.getValue("Car4");
		System.out.println();
		System.out.println("Car5 "+cache.getStatistics());
		cache.getValue("Car5");
		System.out.println();
		System.out.println("Car6 "+cache.getStatistics());
		
	}

}

class Car implements Serializable{


	private static final long serialVersionUID = 1L;
	private String registrationNumber;
	private String color;

	public Car(String registrationNumber, String color) {
		this.registrationNumber = registrationNumber;
		this.color = color;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public String getColor() {
		return color;
	}


}