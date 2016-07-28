package com.alexbt.zookeeper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

@Configuration
public class ZookeeperConfiguration {
	@Bean
	public ZooKeeperConnection zooKeeperConnection() {
		return new ZooKeeperConnection("localhost:" + embeddedZooKeeper().getClientPort());
	}

	@Bean
	public EmbeddedZooKeeper embeddedZooKeeper() {
		return new EmbeddedZooKeeper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
