package com.redhat.developers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GitHubService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GitHubService.class);
	
    @Value("${demo.secretsPath}")
    private String secretsPath; 

    private final RestTemplate restTemplate;
    
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * A simple demo rest service that calls GitHub API to perform some simple operations which will require authorizations
     * The idea of this service is to demonstrate how to use the Kubernetes Secrets mounted as file
     * and use the token and username to call GitHub
     *
     * @author kameshsampath
     */
    public List<Repository> listRepositories() {
        try {

            final URI githubUserSecretsURI = ResourceUtils.getURL(secretsPath + "/github/user").toURI(); 
            final URI githubUserTokenSecretsURI = ResourceUtils.getURL(secretsPath + "/github/token").toURI(); 

            final byte[] encodedGithubUser = Files.readAllBytes(Paths.get(githubUserSecretsURI));
            final byte[] encodedGithubToken = Files.readAllBytes(Paths.get(githubUserTokenSecretsURI));

            String githubUser = sanitize(encodedGithubUser);

            String githubUserToken = sanitize(encodedGithubToken);

            String authHeader = String.format("%s:%s", githubUser, githubUserToken);

            log.info("Listing Repositories of user :{}", githubUser);

            String basicAutheader = Base64.getEncoder().encodeToString(authHeader.getBytes());

            log.info("Auth Header : {}", basicAutheader);

            ResponseEntity<String> response =
                restTemplate.exchange(buildHttpEntity("user/repos", basicAutheader), String.class);
            
            return parseJSON(response.getBody());

        } catch (URISyntaxException e) {
            log.error("Error querying github", e);
            return new ArrayList<>();

        } catch (IOException e) {
            log.error("Error querying github", e);
            return new ArrayList<>();
        } catch (ParseException e) {
        	log.error("Received invalid JSON", e);
        	return new ArrayList<>();
        }
    }
	
    /**
     * Parse a JSON string
     *
     * @param json - the json string to be parsed
     * @return List of Repository objects
     */
	private List<Repository> parseJSON(String json) throws ParseException {
		List<Repository> repoList = new ArrayList<>();
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray)parser.parse(json);
		
		for (Object item : jsonArray) {
			JSONObject object = (JSONObject) item;
			Repository repository = new Repository();
			repository.setFullName((String)object.get("full_name"));
			repository.setGitUrl((String)object.get("git_url"));
			repository.setPrivate(((Boolean) object.get("private")).booleanValue());
			repository.setCreatedAt((String)object.get("created_at"));
			repository.setUpdatedAt((String)object.get("updated_at"));
			repository.setPushedAt((String)object.get("pushed_at"));
			repoList.add(repository);
		}
		
		return repoList;
	}
	
    /**
     * A method to build {@link RequestEntity} by adding needed basic authentication headers
     *
     * @param path           - the github api path to call with out leading &quot;/&quot;
     * @param basicAutheader - the Basic Authorization Base64 string representation header value
     * @return {@link RequestEntity}
     */
    private RequestEntity<Void> buildHttpEntity(String path, String basicAutheader) {
        URI githubApiUri = new UriTemplate("https://api.github.com/{path}").expand(path);
        log.info("Calling API:{}", githubApiUri.toASCIIString());
        RequestEntity<Void> requestEntity =
            RequestEntity.get(githubApiUri)
                .header("Authorization", String.format(" Basic %s", basicAutheader))
                .accept(MediaType.parseMediaType("application/vnd.github.v3+json"))
                .build();

        return requestEntity;
    }
	
    /**
     * remove all new lines from the String
     *
     * @param strBytes - the string bytes where newline to be removed
     * @return sanitized string without newlines
     */
    private String sanitize(byte[] strBytes) {
        return new String(strBytes)
            .replace("\r", "")
            .replace("\n", "");
    }
}
