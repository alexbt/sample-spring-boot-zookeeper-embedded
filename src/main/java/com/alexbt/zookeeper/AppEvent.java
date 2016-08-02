package com.alexbt.zookeeper;

import java.lang.reflect.Field;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class AppEvent implements ApplicationListener<ApplicationStartedEvent>{

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		try {
			Field field = event.getSpringApplication().getClass().getDeclaredField("additionalProfiles");
			field.setAccessible(true);
			Set<String> profiles = (Set<String>)field.get(event.getSpringApplication());
			if(profiles.remove("zookeeper")){
				ZookeeperUtil.start();
				profiles.add("zookeeper-started");
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
