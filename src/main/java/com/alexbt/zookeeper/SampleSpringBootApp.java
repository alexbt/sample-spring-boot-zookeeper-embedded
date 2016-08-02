package com.alexbt.zookeeper;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

import com.alexbt.zookeeper.embedded.EmbeddedZookeeperUtil;

@SpringBootApplication
@EnableDiscoveryClient
public class SampleSpringBootApp {

	private static final String LOCALHOST = "localhost:";

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PreDestroy
	public void preDestroy() {
		EmbeddedZookeeperUtil.stop();
	}

	@Bean
	public ZooKeeperConnection zooKeeperConnection() {
		return new ZooKeeperConnection(LOCALHOST + embeddedZooKeeper().getClientPort());
	}

	@Bean
	public EmbeddedZooKeeper embeddedZooKeeper() {
		return new EmbeddedZooKeeper();
	}

	public static void main(String... args) {
		new SpringApplicationBuilder() //
				.sources(SampleSpringBootApp.class)//
				.run(args);
	}
}
