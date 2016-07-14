package com.alexbt.zookeeper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;

@SpringBootApplication
@EnableDiscoveryClient
public class Launcher {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) throws InterruptedException {

		EmbeddedZooKeeper embeddedZooKeeper = new EmbeddedZooKeeper(2181);
		embeddedZooKeeper.start();

		// new EmbeddedZooKeeper(2181).start();
		// ZooKeeperConnection zooKeeperConnection = new ZooKeeperConnection("localhost:2181");
		// zooKeeperConnection.setAutoStartup(true);
		// // ZooKeeperConnection zooKeeperConnection = new ZooKeeperConnection("localhost:2181");
		// zooKeeperConnection.start();
		// Thread.sleep(3000);

		new SpringApplicationBuilder() //
				.sources(Launcher.class)//
				.profiles("remote").run(args);
	}
}
