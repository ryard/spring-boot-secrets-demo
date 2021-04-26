package com.redhat.developers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class GitHubControllerTestConfig {
	
	@Bean
	RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder();
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) { 
        return restTemplateBuilder.build();
    }
	
	@Bean
	public GitHubService gitHubService(RestTemplate restTemplate) {
		return new GitHubService(restTemplate);
	}

}
