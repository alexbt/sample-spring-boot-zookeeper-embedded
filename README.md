What is this?
-------------------
This project is me playing around with Zookeeper. Keep in mind that I know very little about this product.
This sample project uses spring-boot, Apache's (Netflix's) Curator API to deal with Zookeeper.

Overview
-------------------
The main entry point isLauncherZookeeperWithServiceOne, which launches Spring-Boot using "zookeeper" profile.
This starts an Embedded Zookeeper server, and lunches the application with ("service-one") with automatic client discovery.
application-zookeeper.yml specifies where the application will look for Zookeeper.

Another entry point is available, simply to show how 2 services can see each other through Zookeeper.
LauncherServiceTwo.java starts the application using profile "two", which reigsters the service "service-two".

The projects contains:
* LauncherZookeeperWithServiceOne: starts an embedded zookeeper + launches service-one
* LauncherServiceTwo: launches service-two
* ZooClientController: a RestController allowing to interact with Zookeeper (read/write data)
* ZookeeperListener: a Zookeeper listener. service-one listens when service-two gets up.

Get the code
-------------------
Clone the repository:

    $ git clone git://github.com/alexturcot/sample-spring-boot-zookeeper-embedded


How to launch
-------------------
From the command line:

    $ cd sample-spring-boot-zookeeper-embedded
    $ mvn clean install
    
    $ mvn spring-boot:run -Dspring.profiles.active=zookeeper,one 
    $ mvn spring-boot:run -Dspring.profiles.active=two
    
    
Or in an IDE:
* Import sample-spring-boot-zookeeper-embedded as a maven project
* launch LauncherServiceOneWithZookeeper
* launch LauncherServiceTwo
    
Open a browser to http://localhost:8080/zookeeper

The simple operations available are displayed in the welcome page:
* Lists visible services
* Sets data in zookeeper
* Retrieves data from zookeeper
* Lists dependency listeners

