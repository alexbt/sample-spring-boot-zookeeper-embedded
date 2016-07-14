package com.alexbt.zookeeper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.xd.dirt.zookeeper.EmbeddedZooKeeper;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

@RestController
@RequestMapping("/zookeeper")
public class CloudController {

	@Autowired
	private EmbeddedZooKeeper embeddedZooKeeper;
	
	@Autowired
	private ZooKeeperConnection zooKeeperConnection;

	@RequestMapping(method = RequestMethod.GET)
	public String findByRepo() throws IOException {
		return String.valueOf(embeddedZooKeeper);
	}
}
