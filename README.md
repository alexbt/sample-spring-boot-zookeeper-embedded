What's up with this repo ?
-------------------
This project is me playing around with Zookeeper. Keep in mind that I'm not an export on this product. This sample project uses :
* spring-boot
* Apache's (Netflix's) Curator API to deal with Zookeeper
* XD Dirt's Embedded Zookeeper (so no need to install anything)

The scope is very simple, it allows to:
* read/write data from Zookeeper
* list visible services
* have one service be notified when another service registers

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
* Import sample-spring-boot-zookeeper-embedded as a maven project
* launch LauncherServiceOneWithZookeeper
* launch LauncherServiceTwo

It's running - so what?
-------------------
Launching the project with profiles "zookeeper" and **one** will start an embedded Zookeeper server, as well as registering the spring-boot application named **service-one** to it.

At this point, you may :
* type [http://localhost:8080/zookeeper](http://localhost:8080/zookeeper) to view the welcome page
* list the registered services (only service-one)
* add sample data to zookeeper
* read the data from zookeeper

Also, service-one is listening for service-two, meanining it will be notified when service-two registers to zookeeper.

If you launch the spring-boot application with profile **two**, it will start another instance named **service-two** and register it to zookeeper. **service-one** will then be notified. From the welcome page, you may now :

* list the dependency listeners (you could have done it before service-two registers, but it would have been empty).
