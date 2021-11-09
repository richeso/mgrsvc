package com.mapr.mgrsvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.mgrsvc.config.VolumeParm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;


@RestController
public class MapRController {

    private final String URI_VOLUME_INFO = "/volume/info";
    private final String URI_VOLUME_CREATE = "/volume/create";
    private final String URI_VOLUME_REMOVE = "/volume/remove";
    private final String dareIsFalse ='"'+"dare"+'"'+":false";
    private static final Logger log = LoggerFactory.getLogger(MapRController.class);
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VolumeParm volumeParm;

    @Value("${mapr.cluster.name}")
    private String clusterName;

    @Value("${api.host.baseurl}")
    private String apiHost;

    @Value("${server.port}")
    private String serverPort;

    @Value("${local.host.baseurl}")
    private String localhost;

    @Value("${api.host.user}")
    private String apiUser;

    @Value("${api.host.passwd}")
    private String apiPasswd;

    @Value("${dareCheck}")
    private boolean dareCheck;

    //@RequestMapping(value = "/api/c8vol", method = RequestMethod.POST,consumes="application/json")
    //public ResponseEntity<String> c8vol(@RequestBody Map<String, Object> payload) throws Exception {

    @RequestMapping(value = "/api/c8vol", method = RequestMethod.POST)
    //@GetMapping("/api/c8vol")
    public ResponseEntity<String> c8vol(@RequestParam Map<String, String> payload) {
        log.debug(String.valueOf(payload));
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        String volumePath = (String) payload.get("path");
        String quota = (String) payload.get("quota");
        String advisoryquota = (String) payload.get("advisoryquota");

        //http[s]://<host>:<port>/rest/volume/info?<parameters>
        //curl -k -X GET 'https://abc.sj.us:8443/rest/volume/info?name=test_vol' --user mapr:mapr
        //curl -k -X POST 'https://<hostname>:8443/rest/volume/create?name=<volName>&path=<mountPath>' --user mapr:mapr
        try {
            //Map<String, String> params = new HashMap<>();
            //params.put("name", volume);
            //params.put("path", path);
            // create request
            // HttpEntity request = new HttpEntity(params, headers);
            // Ensure user is authenticated
            Map<String, Object> userData = PamUser.getUserData(userid, password);
            System.out.println("User Authenticated via PAM: " + userData.toString());

            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + URI_VOLUME_CREATE)
                    .queryParam("name", volume)
                    .queryParam("path", volumePath);
            if (quota != null && !quota.equals(""))
                builder.queryParam("quota",quota);
            if (advisoryquota != null && !advisoryquota.equals(""))
                builder.queryParam("advisoryquota",advisoryquota);

            // add default parameters
            // using for-each loop for iteration over Map.entrySet()

            for (Map.Entry<String, String> entry : volumeParm.getParm().entrySet()) {
                String key = entry.getKey();
                String value = volumeParm.getParmValue(key, userid);
                System.out.println("Key = " + key + ", Value = " + value);
                if (key.equals("dare") && value.equals("true") && dareCheck) {
                    if (!isClusterDareEnabled(userid, password))
                        System.out.println("dare override to: false");
                        value = "false";
                }
                builder.queryParam(key, value);
            }
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.POST, request, Map.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("{error:" + '"' + e.getMessage().replace('"', '-') + '"' + "}");
        }
    }

    //@RequestMapping(value = "/api/deletevol", method = RequestMethod.POST,consumes="application/json")
    //public ResponseEntity<String> deletevol(@RequestBody Map<String, Object> payload) throws Exception {

    //@GetMapping("/api/deletevol")
    @RequestMapping(value = "/api/deletevol", method = RequestMethod.POST)
    public ResponseEntity<String> deletevol(@RequestParam Map<String, String> payload) {
        log.debug(String.valueOf(payload));
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        try {

            Map<String, Object> userData = PamUser.getUserData(userid, password);
            System.out.println("User Authenticated via PAM: " + userData.toString());

            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + URI_VOLUME_REMOVE)
                    .queryParam("name", volume);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.POST, request, Map.class);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("{error:" + '"' + e.getMessage().replace('"', '-') + '"' + "}");
        }
    }

    //@RequestMapping(value = "/api/volinfo", method = RequestMethod.POST,consumes="application/json")
    //public ResponseEntity<String> volinfo(@RequestBody Map<String, Object> payload) throws Exception {
    //@GetMapping("/api/volinfo")
    @RequestMapping(value = "/api/volinfo", method = RequestMethod.POST)
    public ResponseEntity<String> volinfo(@RequestParam Map<String, String> payload) {
        log.debug(String.valueOf(payload));
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String volume = (String) payload.get("volume");
        try {

            Map<String, Object> userData = PamUser.getUserData(userid, password);
            System.out.println("User Authenticated via PAM: " + userData.toString());
            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + URI_VOLUME_INFO)
                    .queryParam("name", volume);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.POST, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("{error:" + '"' + e.getMessage().replace('"', '-') + '"' + "}");
        }
    }

    //@RequestMapping(value = "/api/volinfo", method = RequestMethod.POST,consumes="application/json")
    //public ResponseEntity<String> volinfo(@RequestBody Map<String, Object> payload) throws Exception {
    //@GetMapping("/api/mapr")
    @RequestMapping(value = "/api/mapr", method = RequestMethod.POST)
    public ResponseEntity<String> mapr(@RequestParam Map<String, String> payload) {
        log.debug(String.valueOf(payload));
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        String uri = (String) payload.get("uri");
        try {

            Map<String, Object> userData = PamUser.getUserData(userid, password);
            System.out.println("User Authenticated via PAM: " + userData.toString());
            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + uri);
            payload.forEach((k, v) -> {
                if (k.equals("userid") || k.equals("password") || k.equals("uri")) ;
                else {
                    log.debug("Request Parm key: " + k + ", Request Parm value: " + v);
                    builder.queryParam(k, v);
                }
            });
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.POST, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("{error:" + '"' + e.getMessage().replace('"', '-') + '"' + "}");
        }
    }

    @GetMapping("/api/pamauthenticate")
    public ResponseEntity<String> pamauthenticate(@RequestParam Map<String, String> payload) {
        System.out.println(payload);
        String userid = (String) payload.get("userid");
        String password = (String) payload.get("password");
        try {
            HttpHeaders headers = createAuthHeader(userid, password, false);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(localhost + ":" + serverPort + "/api/paminfo")
                    .queryParam("userid", userid)
                    .queryParam("password", password);
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            return ResponseEntity.ok(responseString);
        } catch (Exception e) {
            log.debug("Error Encountered: " + e.getMessage());
            return ResponseEntity.ok("{error:" + '"' + e.getMessage().replace('"', '-') + '"' + "}");
        }
    }

    private HttpHeaders createAuthHeader(String userid, String password) {
        return createAuthHeader(userid, password, true);
    }

    private HttpHeaders createAuthHeader(String userid, String password, boolean override) {
        // create auth credentials
        // override parameters if necessary
        String useUserid = userid;
        String usePassword = password;
        if (override && apiUser != null && !apiUser.equals("") & apiPasswd != null && !apiPasswd.equals("")) {
            useUserid = apiUser;
            usePassword = apiPasswd;
        }
        String authStr = useUserid + ":" + usePassword;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    private boolean isClusterDareEnabled(String userid, String password) throws Exception {

        boolean isDare = true;
        try {
            HttpHeaders headers = createAuthHeader(userid, password);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiHost + "/dashboard/info");
            // make a request
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.POST, request, Map.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseString = objectMapper.writeValueAsString(response.getBody());
            if (responseString.indexOf(dareIsFalse) >=0)
                isDare=false;
            /*
            List<Object> clusterData = (List<Object>) response.getBody().get("data");
            for (Object item : clusterData) {
                Map<String, Object> clusterMap = (Map<String, Object>) item;
                Map<String, Object> singleCluster = (Map<String, Object>) clusterMap.get("cluster");
                String cname = (String) singleCluster.get("name");
                if (cname.equals(clusterName)) {
                    isDare = singleCluster.get("dare").toString().equals("true");
                    break;
                }
            }
             */
            return isDare;
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug("Error Encountered querying dashboard info: " + e.getMessage());
            throw (e);
        }

    }
}
