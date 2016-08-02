package com.alexbt.zookeeper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.stereotype.Service;


@Service
public class ZookeeperListener implements DependencyWatcherListener {
	
	private Set<String> dependencies = new HashSet<>();

	@Override
	public void stateChanged(String dependencyName, DependencyState newState) {
		dependencies.add(dependencyName);
	}

	public Set<String> getDependencies() {
		return dependencies;
	}
}