package com.mapr.mgrsvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TempController {

	private final String URI_USERS = "/users";
	private final String URI_USERS_ID = "/users/{id}";

	@Autowired
	RestTemplate restTemplate;
/*
	// 1. getForObject(url, classType)
	@PostMapping("/api/volumeinfo")
	public ResponseEntity<String> getVolumeInfo() {
		String url = "https://jsonplaceholder.typicode.com/posts";

		// create auth credentials
		String authStr = "username:password";
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		// create request
		HttpEntity request = new HttpEntity(headers);

		// make a request
		ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);

		// get JSON response
		String json = response.getBody();
		User[] usersArray = restTemplate.getForObject(URI_USERS, User[].class);
		return new ResponseEntity<>(Arrays.asList(usersArray), HttpStatus.OK);
	}

	@GetMapping("api/delete/volume/{id}")
	public ResponseEntity<User> getById_v1(@PathVariable final long id) {
		Map<String, String> params = new HashMap<>();
		params.put("id", "1");

		User user = restTemplate.getForObject(URI_USERS_ID, User.class, params);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// 2. getForEntity(url, responseType)
	@GetMapping("all-users_v2")
	public ResponseEntity<User[]> getAll_v2() {
		ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(URI_USERS, User[].class);
		return responseEntity;
	}

	@GetMapping("all-users_v2/{id}")
	public ResponseEntity<User> getById_v2(@PathVariable final long id) {
		Map<String, String> params = new HashMap<>();
		params.put("id", "1");

		ResponseEntity<User> responseEntity 
			= restTemplate.getForEntity(URI_USERS_ID, User.class, params);
		return responseEntity;
	}

	// 3. exchange(url, method, requestEntity, responseType)
	@GetMapping("all-users_v3")
	public ResponseEntity<User[]> getAll_v3() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("X-HEADER_NAME", "XYZ");

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<User[]> responseEntity = restTemplate
				.exchange("/users", HttpMethod.GET, entity, User[].class);
		return responseEntity;
	}

	@GetMapping("all-users_v3/{id}")
	public ResponseEntity<User> getById_v3(@PathVariable final long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("X-HEADER_NAME", "XYZ");

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<User> responseEntity = restTemplate
				.exchange("/users/" + id, HttpMethod.GET, entity, User.class);
		return responseEntity;
	}

	@PostMapping("users")
	public User create(@RequestBody final User newUser) {

		User createdUser = restTemplate.postForObject(URI_USERS, newUser, User.class);
		return createdUser;
	}
	*/
}
