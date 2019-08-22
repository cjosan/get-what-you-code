package com.cjosan.getwhatyoucode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	@Autowired
	Environment environment;

	public String getProperty(String name) {
		return environment.getProperty(name);
	}

	public String getToken() {
		return environment.getProperty("TOKEN_PREFIX");
	}
}
