package com.alexbt.zookeeper.embedded;

import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;

public class EmbeddedZookeeperUtil {
	
	private static final EmbeddedZooKeeper embeddedZooKeeper = new EmbeddedZooKeeper(2181);

	public static void start(){
		embeddedZooKeeper.start();
	}
	
	public static void stop(){
		embeddedZooKeeper.stop();
	}
}
