package com.redhat.developers;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
//@SpringBootTest
@ContextConfiguration(classes = {GitHubControllerTestConfig.class})
@WebMvcTest(value = GitHubController.class)
@WithMockUser
@Import(GitHubController.class)
@ActiveProfiles("test")
public class GitHubControllerTest {
	
	@Value("${expected.response}")
	private String expected;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GitHubService gitHubService;
	
	
	@Test
	@WithMockUser
	public void listMyRepositoriesTest() throws Exception {
		//String expected = "[{\"fullName\":\"ryard/awesome-compose\",\"gitUrl\":\"git://github.com/ryard/awesome-compose.git\",\"createdAt\":\"2021-03-16T05:27:17Z\",\"updatedAt\":\"2021-03-16T05:27:18Z\",\"pushedAt\":\"2021-03-15T21:42:14Z\",\"private\":false},{\"fullName\":\"ryard/cicd-rest-app-ocp\",\"gitUrl\":\"git://github.com/ryard/cicd-rest-app-ocp.git\",\"createdAt\":\"2021-04-21T16:23:35Z\",\"updatedAt\":\"2021-04-21T16:23:36Z\",\"pushedAt\":\"2021-04-23T19:35:58Z\",\"private\":false}]";
		//String expected = "[{\"fullName\":\"ryard/awesome-compose\",\"gitUrl\":\"git://github.com/ryard/awesome-compose.git\",\"createdAt\":\"2021-03-16T05:27:17Z\",\"updatedAt\":\"2021-03-16T05:27:18Z\",\"pushedAt\":\"2021-03-15T21:42:14Z\",\"private\":false}]";
		List<Repository> mockRepositories = new ArrayList<>();
		Repository repo = new Repository();
		repo.setFullName("ryard/awesome-compose");
		repo.setGitUrl("git://github.com/ryard/awesome-compose.git");
		repo.setPrivate(false);
		repo.setCreatedAt("2021-03-16T05:27:17Z");
		repo.setUpdatedAt("2021-03-16T05:27:18Z");
		repo.setPushedAt("2021-03-15T21:42:14Z");
		mockRepositories.add(repo);
		repo = new Repository();
		repo.setFullName("ryard/cicd-rest-app-ocp");
		repo.setGitUrl("git://github.com/ryard/cicd-rest-app-ocp.git");
		repo.setPrivate(false);
		repo.setCreatedAt("2021-04-21T16:23:35Z");
		repo.setUpdatedAt("2021-04-21T16:23:36Z");
		repo.setPushedAt("2021-04-23T19:35:58Z");
		mockRepositories.add(repo);
		
		Mockito.when(gitHubService.listRepositories()).thenReturn(mockRepositories);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mygithubrepos").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

}
