package com.alexbt.zookeeper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.stereotype.Service;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;


@Service
public class ZookeeperListener implements DependencyWatcherListener {
	
	@Autowired
	private ZooKeeperConnection zooKeeperConnection;
	
	private Set<String> dependencies = new HashSet<>();

	@Override
	public void stateChanged(String dependencyName, DependencyState newState) {
		dependencies.add(dependencyName);
	}

	public Set<String> getDependencies() {
		return dependencies;
	}
}