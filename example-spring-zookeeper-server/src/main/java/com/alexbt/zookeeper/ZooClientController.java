package com.alexbt.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zooclient")
@PreAuthorize("permitAll()")
public class ZooClientController {
	
	@Autowired
	private DiscoveryClient discovery;

	@RequestMapping(method = RequestMethod.GET)
	public String test() throws IOException {
		String name = discovery.getServices().get(0);
		List<String> list = discovery.getInstances(name).stream().map(d -> d.getUri().getPath()).collect(Collectors.toList());
		return list.toString();
	}
}
