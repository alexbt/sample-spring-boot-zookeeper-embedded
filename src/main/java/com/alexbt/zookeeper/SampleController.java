package com.alexbt.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

@RestController
public class SampleController {

	private static final String NAMESPACE = "test";
	private static final String PROPERTY_NAME = "/data";
	private static final String PROPERTY_VALUE = "test-";
	private static final String FORWARD_SLASH = "/";
	private static final int ZOOKEEPER_IGORE_VERSIONS_FLAG = -1;

	@Value("${spring.application.name}")
	private String serviceName;

	@Autowired
	private DiscoveryClient discovery;

	@Autowired
	private ZooKeeperConnection zooKeeperConnection;

	@Autowired
	private ZookeeperListener zookeeperListener;

	@RequestMapping(value = "listeners", method = RequestMethod.GET)
	public String listeners() throws Exception {
		return "This service listens for : " + zookeeperListener.getDependencies();
	}

	@RequestMapping(value = "services", method = RequestMethod.GET)
	public List<ServiceInstance> test() throws Exception {
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
		for (String name : discovery.getServices()) {
			instances.addAll(discovery.getInstances(name));
		}

		CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace(NAMESPACE);
		testCuratorFw.createContainers(PROPERTY_NAME);
		testCuratorFw.getZookeeperClient().getZooKeeper()//
				.setData(FORWARD_SLASH + NAMESPACE + PROPERTY_NAME, //
						(PROPERTY_VALUE + System.currentTimeMillis()).getBytes(), ZOOKEEPER_IGORE_VERSIONS_FLAG);

		return instances;
	}

	@RequestMapping(value = "properties/{propertyName}/{propertyValue}", method = RequestMethod.GET)
	public String setProperty(@PathVariable("propertyName") String propertyName,
			@PathVariable("propertyValue") String propertyValue) throws Exception {
		CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace(NAMESPACE);
		testCuratorFw.createContainers(FORWARD_SLASH + propertyName);
		testCuratorFw.getZookeeperClient().getZooKeeper()//
				.setData(FORWARD_SLASH + NAMESPACE + FORWARD_SLASH + propertyName, propertyValue.getBytes(),
						ZOOKEEPER_IGORE_VERSIONS_FLAG);

		return "set property: " + viewProperty(propertyName);
	}

	@RequestMapping(value = "properties/{propertyName}", method = RequestMethod.GET)
	public String viewProperty(@PathVariable("propertyName") String propertyName) throws Exception {
		CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace(NAMESPACE);
		Stat stat = testCuratorFw.checkExists().forPath(FORWARD_SLASH + propertyName);

		if (stat != null) {
			return propertyName + " = " + new String(testCuratorFw.getData().forPath(FORWARD_SLASH + propertyName));
		} else {
			return "Property " + propertyName + " does not exist";
		}
	}
}
