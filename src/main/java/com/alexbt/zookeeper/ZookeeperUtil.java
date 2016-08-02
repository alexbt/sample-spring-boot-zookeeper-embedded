package com.alexbt.zookeeper;

import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;

public class ZookeeperUtil {
	
	private final static EmbeddedZooKeeper embeddedZooKeeper = new EmbeddedZooKeeper(2181);

	public static void start(){
		embeddedZooKeeper.start();
	}
	
	public static void stop(){
		embeddedZooKeeper.stop();
	}
}
