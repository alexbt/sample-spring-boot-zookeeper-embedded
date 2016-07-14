package com.alexbt.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zooclient")
public class ZooClientController {
	
	@Autowired
	private DiscoveryClient discovery;

	@RequestMapping(method = RequestMethod.GET)
	public String test() throws IOException {
		String result = "";
		for(String registered: discovery.getServices()){
			List<String> list = discovery.getInstances(registered).stream().map(d -> d.getServiceId()+":"+d.getUri().toASCIIString()+"\n").collect(Collectors.toList());
			result+= list.toString();
		}
		return result;
	}
}
