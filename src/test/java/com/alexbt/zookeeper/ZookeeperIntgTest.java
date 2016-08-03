package com.alexbt.zookeeper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xd.dirt.zookeeper.ZooKeeperConnection;

import com.alexbt.zookeeper.SampleController;
import com.alexbt.zookeeper.SampleSpringBootApp;
import com.alexbt.zookeeper.embedded.EmbeddedZookeeperUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleSpringBootApp.class)
@WebIntegrationTest
@ActiveProfiles({"zookeeper", "one"})
public class ZookeeperIntgTest {

	private static final String FORWARD_SLASH = "/";

	@Autowired
	private ZooKeeperConnection zooKeeperConnection;

	private static CuratorFramework curatorFramework;

	@BeforeClass
	public static void startZookeeper() {
		EmbeddedZookeeperUtil.start();
	}

	@AfterClass
	public static void after() {
		curatorFramework.close();
		EmbeddedZookeeperUtil.stop();
	}

	@Autowired
	public void setCuratorFramework(CuratorFramework curatorFramework) {
		ZookeeperIntgTest.curatorFramework = curatorFramework;
	}

	@Autowired
	private SampleController zooClientController;

	private String ns = "test";

	@Test
	public void test() throws Exception {
		List<ServiceInstance> test = zooClientController.test();
		assertEquals("service-one", test.get(0).getServiceId());
	}

	@Test
	public void testSetProperty() throws Exception {
		String propertyName = "testvalueSet";
		String propertyValue = "testdataSet";
		zooClientController.setProperty(propertyName, propertyValue);
		CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace(ns);

		assertEquals(propertyValue, new String(testCuratorFw.getData().forPath(FORWARD_SLASH + propertyName)));
	}
	
	@Test
	public void testGetProperty() throws Exception {
		String propertyName = "testvalueGet";
		String propertyValue = "testdataGet";

		CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace(ns);
		testCuratorFw.createContainers(FORWARD_SLASH + propertyName);
		testCuratorFw.getZookeeperClient().getZooKeeper()//
				.setData(FORWARD_SLASH + ns + FORWARD_SLASH + propertyName, propertyValue.getBytes(), -1);

		String msg = zooClientController.viewProperty(propertyName);
		String expectedResult = propertyName + " = " + propertyValue;
		assertTrue("\"" + msg + "\"  does not contain " + expectedResult, msg.contains(expectedResult));
	}

	@Test
	public void testIntgSetGetProperty() throws Exception {
		String propertyName = "testvalueGetSet";
		String propertyValue = "tesdataGetSet";
		zooClientController.setProperty(propertyName, propertyValue);
		String msg = zooClientController.viewProperty(propertyName);
		String expectedResult = propertyName + " = " + propertyValue;
		assertTrue("\"" + msg + "\"  does not contain " + expectedResult, msg.contains(expectedResult));
	}
}
