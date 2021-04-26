package com.redhat.developers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple demo rest controller that calls GitHub API to perform some simple operations which will require authorizations
 * The idea of this controller is to demonstrate how to use the Kubernetes Secrets mounted as file
 * and use the token and username to call GitHub
 *
 * @author kameshsampath
 */
@RestController
public class GitHubController {
	
	private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    /**
     * Get the list of GitHub repositories of the user
     */
    @GetMapping("/mygithubrepos")
    public List<Repository> listMyRepositories() {
    	return gitHubService.listRepositories();
    }
}