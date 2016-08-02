package com.alexbt.zookeeper;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class LauncherServiceTwo {
	public static void main(String[] args) throws InterruptedException {
		new SpringApplicationBuilder() //
		.sources(ZookeeperSpringBootApp.class)//
		.profiles("two")//
		.run(args);
	}
}
