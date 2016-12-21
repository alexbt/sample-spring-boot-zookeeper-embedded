[test](#intg-test)
What's up with this repo ?
-------------------
This project is me playing around with Zookeeper. This sample project uses :
* spring-boot;
* Apache's Zookeeper;
* Apache's (Netflix's) Curator API on top of Zookeeper;
* XD Dirt's Embedded Zookeeper (so no need to install anything).

The scope is very simple, it allows to:
* register services to zookeeper;
* list visible services (from a service's point of view);
* read/write data from Zookeeper;
* have a service notified when another service registers.


Walkthrough
-------------------
**Embedded Zookeeper**

This project relies on an embedded Zookeeper server, which can be launch simply by using **spring-xd-dirt** :

```
<dependency>
	<groupId>org.springframework.xd</groupId>
	<artifactId>spring-xd-dirt</artifactId>
<dependency>
```

It's as simple as this: ```new EmbeddedZooKeeper(2181).start()``` without having to install anything.

However, since I'm using ```@EnableDiscoveryClient```, the client registration happens as soon as Spring starts up. For this reason, I added an ApplicationListener to launch Zookeeper before any client tries to register: 
```
public class EmbeddedZookeeperLauncher implements ApplicationListener<ApplicationStartedEvent>{
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
	   ...
		embeddedZooKeeper.start();
	   ...
    }
}
```

To wire the ApplicationListener, it is defined in 
> META-INF/spring.factories:

```
org.springframework.context.ApplicationListener=com.alexbt.zookeeper.embedded.EmbeddedZookeeperLauncher
```


**Register services**

I created 2 spring profile configuration, each representing a service. 
Both profiles are defined in yaml:

> application-one-yml:
```
server.port: 8080
spring.application.name: service-one
spring.cloud.zookeeper.dependencies:
  service-two-alias:
    path: service-two
    required: false
```

The service register itself thanks to EnableDiscoveryClient:
```
@SpringBootApplication
@EnableDiscoveryClient
public class SampleSpringBootApp {
```

> application-two-yml:
```
server.port: 8081
spring.application.name: service-two
```

* Profile *one* registers the service *service-one*, while *two* registers *service-two*
* Service-one launches on port 8081, while service-two launches on port 8081.
* Service-one *listens* to service-two registration.


**Zookeeper Listener**

Because service define ```spring.cloud.zookeeper.dependencies```, Zookeeper will look for DependencyWatcherListener's implementation:

```
@Service
public class ZookeeperListener implements DependencyWatcherListener {
	@Override
	public void stateChanged(String dependencyName, DependencyState newState) {
		...
	}
}
```	

service-one will is notified of service-two's change of state (connected or not to zookeeper). 

**Zookeeper operations**

Few URI are made to trigger operations on Zookeeper, here's the interesting code:

Listing visible services using Curator:

```
@Autowired
private DiscoveryClient discovery;

List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
for (String name : discovery.getServices()) {
	instances.addAll(discovery.getInstances(name));
}
System.out.println(instances);
```

This prints the visible ServiceInstances:
```
[{"serviceId":"service-one","host":"192.168.1.103","port":8080,"secure":false,"metadata":{},"uri":"http://192.168.1.103:8080"},
{"serviceId":"service-two","host":"192.168.1.103","port":8081,"secure":false,"metadata":{},"uri":"http://192.168.1.103:8081"}]
```


create an empty node using Curator:

```
@Autowired
private ZooKeeperConnection zooKeeperConnection;
	
CuratorFramework testCuratorFw = zooKeeperConnection.getClient().usingNamespace("test");
testCuratorFw.createContainers("/mynode");
```

create a node with data using Curator:

```
testCuratorFw.create().withACL(Ids.OPEN_ACL_UNSAFE).forPath("/node", "data".getBytes());
```

create a node with data using Zookeeper's API:
```
final int ZOOKEEPER_IGORE_VERSIONS_FLAG = -1
testCuratorFw.getZookeeperClient().getZooKeeper().setData("/test/node", "data".getBytes(), ZOOKEEPER_IGORE_VERSIONS_FLAG);
```

read the data using Curator:

```	
Stat stat = testCuratorFw.checkExists().forPath("/node");
if (stat != null) {
	String data = new String(testCuratorFw.getData().forPath("/node"));
}
```



Get the code - do it
-------------------
Clone the repository:

    $ git clone git://github.com/alexturcot/sample-spring-boot-zookeeper-embedded



How to launch
-------------------
From the command line:

    $ cd sample-spring-boot-zookeeper-embedded
    $ mvn clean install
    
    $ mvn spring-boot:run -Dspring.profiles.active=zookeeper,one &
    $ mvn spring-boot:run -Dspring.profiles.active=two
    
The first spring-boot:run is launched in the background, when you are done, you may kill it:

    $ sudo pkill -f sample-spring-boot-zookeeper-embedded
    
To launch inside an IDE:
* Import sample-spring-boot-zookeeper-embedded as a maven project;
* launch LauncherServiceOneWithZookeeper;
* launch LauncherServiceTwo.

It's running - so what?
-------------------
Launching the project with profiles "zookeeper" and **one** will start an embedded Zookeeper server, as well as registering the spring-boot application named **service-one** to it.

At this point, you may :
* type [http://localhost:8080/zookeeper](http://localhost:8080/zookeeper) to view the welcome page;
* list the registered services (only service-one);
* add sample data to zookeeper;
* read the data from zookeeper.

<a name="intg-test"/>
test
-----

Also, service-one is listening for service-two, meanining it will be notified when service-two registers to zookeeper.

If you launch the spring-boot application with profile **two**, it will start another instance named **service-two** and register it to zookeeper. **service-one** will then be notified. From the welcome page, you may now :

* list the dependency listeners (you could have done it before service-two registers, but it would have been empty).
