package com.mapr.mgrsvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpClientConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
public class MapRController {

    private final String URI_VOLUME_INFO = "/volume/info";
    private final String URI_VOLUME_CREATE = "/volume/create";
    private static final Logger log = LoggerFactory.getLogger(MapRController.class);
    @Autowired
    RestTemplate restTemplate;

    @Value("${api.host.baseurl}")
    private String apiHost;

    @RequestMapping(
            value = "/api/c8vol",
            method = RequestMethod.POST,
            consumes="application/json")

    public ResponseEntity<String> c8vol(@RequestBody Map<String, Object> payload)
            throws Exception {
        System.out.println(payload);
        String username = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String requestType = (String) payload.get("requestType");
        String volume = (String) payload.get("volume");
        String volumePath = (String) payload.get("path");
        //http[s]://<host>:<port>/rest/volume/info?<parameters>
        //curl -k -X GET 'https://abc.sj.us:8443/rest/volume/info?name=test_vol' --user mapr:mapr
        //curl -k -X POST 'https://<hostname>:8443/rest/volume/create?name=<volName>&path=<mountPath>' --user mapr:mapr
        try {
            //Map<String, String> params = new HashMap<>();
            //params.put("name", volume);
            //params.put("path", path);
            // create request
            // HttpEntity request = new HttpEntity(params, headers);
            HttpHeaders headers = createAuthHeader(username, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost+URI_VOLUME_CREATE)
                    .queryParam("name", volume)
                    .queryParam("path", volumePath);

            // make a request
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, String.class);
            String results = response.toString();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Error Encountered: "+e.getMessage());
            return ResponseEntity.ok("error encountered: "+e.getMessage());
        }
    }
    public HttpHeaders createAuthHeader(String username, String password) {
        // create auth credentials
        String authStr = username+":"+password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

}
