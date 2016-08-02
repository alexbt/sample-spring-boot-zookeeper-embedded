package com.alexbt.zookeeper;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class LauncherServiceOneWithZookeeper {
	public static void main(String[] args) throws InterruptedException {
		new SpringApplicationBuilder() //
				.sources(ZookeeperSpringBootApp.class)//
				.profiles("zookeeper", "one")//
				.run(args);
	}
}
