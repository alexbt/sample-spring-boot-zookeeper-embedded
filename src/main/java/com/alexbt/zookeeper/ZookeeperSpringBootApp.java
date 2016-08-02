package com.alexbt.zookeeper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

@SpringBootApplication
@EnableDiscoveryClient
public class ZookeeperSpringBootApp {

	@Autowired
	public void setEnvironment(Environment env) {
		if (env.getActiveProfiles().toString().contains("zookeeper")) {
			ZookeeperUtil.start();
		}
	}

	public ZookeeperSpringBootApp() {
	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PreDestroy
	public void preDestroy() {
		ZookeeperUtil.stop();
	}

	@Bean
	public ZooKeeperConnection zooKeeperConnection() {

		ZooKeeperConnection zooKeeperConnection = new ZooKeeperConnection(
				"localhost:" + embeddedZooKeeper().getClientPort());
		return zooKeeperConnection;
	}

	@Bean
	public EmbeddedZooKeeper embeddedZooKeeper() {
		return new EmbeddedZooKeeper();
	}

	public static void main(String... args) {
		new SpringApplicationBuilder() //
		.sources(ZookeeperSpringBootApp.class)//
		.run(args);
	}
}
