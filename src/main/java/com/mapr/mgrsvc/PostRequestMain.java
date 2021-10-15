package com.mapr.mgrsvc;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//import com.ituple.common.dto.ServiceResponse;

public class PostRequestMain {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");

        headers.setAll(map);

        Map req_payload = new HashMap();
        req_payload.put("name", "piyush");

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        String url = "http://localhost:8080/xxx/xxx/";

        ResponseEntity<?> response = new RestTemplate().postForEntity(url, request, String.class);
        //      ServiceResponse entityResponse = (ServiceResponse) response.getBody();
        // System.out.println(entityResponse.getData());
    }

}