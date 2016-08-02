package com.alexbt.zookeeper.launchers;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.alexbt.zookeeper.SampleSpringBootApp;

public class LauncherServiceOneWithZookeeper {
	public static void main(String[] args) throws InterruptedException {
		new SpringApplicationBuilder() //
				.sources(SampleSpringBootApp.class)//
				.profiles("zookeeper", "one")//
				.run(args);
	}
}
